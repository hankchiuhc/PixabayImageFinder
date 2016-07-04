package com.hankhc.pixabayimagefinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hankchiu on 16/7/2.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Map<String, String>> mData = new ArrayList<Map<String, String>>();

    public ListAdapter(Context context) {
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImgView;

        public ViewHolder(View v) {
            super(v);
            mImgView = (ImageView) v.findViewById(R.id.iv_photo);
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the image URL for the current position.
        String url = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.ic_crop_original_black)
                .error(R.drawable.ic_error_black)
                .fit()
                .tag(mContext)
                .into(holder.mImgView);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String getItem(int position) {
        Map<String, String> data = mData.get(position);
        if (data != null) {
            return data.get("url");
        } else {
            return "";
        }
    }

    public void setData(List<Map<String, String>> data) {
        // Always clear old data.
        mData.clear();
        mData.addAll(data);
    }
}
