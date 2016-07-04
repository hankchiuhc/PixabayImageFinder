package com.hankhc.pixabayimagefinder;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int STATE_LIST = 1;
    private static final int STATE_GRID = 2;
    private int mCurrentState = STATE_LIST;

    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 3;

    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mEditSearch;
    private Button mBtnSearch;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Request window feature action bar
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.RED));
        }

        mContext = this;

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_image_list);

        mLayoutManager = new StaggeredGridLayoutManager(LIST_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ListAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new ListScrollListener(mContext));

        mEditSearch = (EditText) findViewById(R.id.edit_search);
        mBtnSearch = (Button) findViewById(R.id.btn_search);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideIME(v);

                if (mEditSearch != null && mEditSearch.getText() != null) {
                    String searchText = generatePixabaySearchUrl(mEditSearch.getText().toString());
                    if (!TextUtils.isEmpty(searchText)) {
                        PixabayPhotoSearchTask photoSearchTask = new PixabayPhotoSearchTask();
                        photoSearchTask.execute(searchText);
                    }
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mCurrentState == STATE_GRID) {
            menu.getItem(0).setIcon(R.drawable.ic_grid_white);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_list_white);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_switch) {
            switchListGrid();
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    private void switchListGrid() {
        switch (mCurrentState) {
            case STATE_LIST:
                mCurrentState = STATE_GRID;
                ((StaggeredGridLayoutManager) mLayoutManager).setSpanCount(GRID_SPAN_COUNT);
                break;
            case STATE_GRID:
                mCurrentState = STATE_LIST;
                ((StaggeredGridLayoutManager) mLayoutManager).setSpanCount(LIST_SPAN_COUNT);
                break;
            default:
                Log.e(TAG, "Error state: " + mCurrentState);
                mCurrentState = STATE_LIST;
                break;
        }
    }

    private void hideIME(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private String generatePixabaySearchUrl(String strSearch) {
        if (TextUtils.isEmpty(strSearch)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("https://pixabay.com/api/?key=")
                .append(BuildConfig.PIXABAY_API_KEY)
                .append("&q=")
                .append(strSearch.trim().replace(" ", "+")) // Use '+' to concat string in url.
                .append("&image_type=photo");
        return sb.toString();
    }

    private class PixabayPhotoSearchTask extends AsyncTask<String, Void, List<Map<String, String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage(getString(R.string.progress_search));
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected List<Map<String, String>> doInBackground(String... params) {
            if (params == null || params.length <= 0) {
                return null;
            }

            JSONObject jsonObject = JsonHelper.getJsonFromUrl(params[0]);
            // TODO: Report different error state to user, e.g., no Internet connection.
            if (jsonObject == null) {
                return null;
            }

            JSONArray jsonArray;
            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
            try {
                jsonArray = jsonObject.getJSONArray("hits");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Map<String, String> map = new ArrayMap<String, String>();
                    JSONObject object = jsonArray.getJSONObject(i);
                    map.put("width", object.getString("previewWidth"));
                    map.put("height", object.getString("previewHeight"));
                    map.put("url", object.getString("previewURL"));
                    list.add(map);
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON data: " + e.toString());
            }

            return list;
        }

        @Override
        protected void onPostExecute(List<Map<String, String>> list) {
            if (list != null && list.size() > 0) {
                ((ListAdapter) mAdapter).setData(list);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(mContext,
                        getString(R.string.search_failed, mEditSearch.getText().toString()),
                        Toast.LENGTH_SHORT)
                        .show();
            }

            mProgressDialog.dismiss();

            super.onPostExecute(list);
        }
    }
}
