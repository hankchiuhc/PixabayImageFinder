package com.hankhc.pixabayimagefinder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int STATE_LIST = 1;
    private static final int STATE_GRID = 2;
    private int mCurrentState = STATE_LIST;

    private static final int LIST_SPAN_COUNT = 1;
    private static final int GRID_SPAN_COUNT = 3;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_image_list);

        mLayoutManager = new StaggeredGridLayoutManager(LIST_SPAN_COUNT,
                StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new ListAdapter(this.getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setOnScrollListener(new ListScrollListener(this.getApplicationContext()));
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
}
