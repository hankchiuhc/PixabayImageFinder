package com.hankhc.pixabayimagefinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.squareup.picasso.Picasso;

/**
 * Created by hankchiu on 16/7/2.
 */
public class ListScrollListener extends RecyclerView.OnScrollListener {
    private final Context mContext;

    public ListScrollListener(Context context) {
        mContext = context;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        final Picasso picasso = Picasso.with(mContext);
        if (newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            picasso.resumeTag(mContext);
        } else {
            picasso.pauseTag(mContext);
        }
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
    }
}
