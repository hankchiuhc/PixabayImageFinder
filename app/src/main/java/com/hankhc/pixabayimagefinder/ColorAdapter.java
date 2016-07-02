package com.hankhc.pixabayimagefinder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by hankchiu on 16/7/2.
 */
public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {
    private String[] mDataSet;
    private Context mContext;
    private Random mRandom = new Random();

    public ColorAdapter(Context context, String[] DataSet) {
        mDataSet = DataSet;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.tv_desc);
        }
    }

    @Override
    public ColorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.listitem_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataSet[position]);
        // Set a random height for TextView
        holder.mTextView.getLayoutParams().height = getRandomIntInRange(250, 75);
        // Set a random color for TextView background
        holder.mTextView.setBackgroundColor(getRandomHSVColor());
    }

    @Override
    public int getItemCount() {
        return mDataSet.length;
    }

    // Custom method to get a random number between a range
    protected int getRandomIntInRange(int max, int min) {
        return mRandom.nextInt((max - min) + min) + min;
    }

    // Custom method to generate random HSV color
    protected int getRandomHSVColor() {
        // Generate a random hue value between 0 to 360
        int hue = mRandom.nextInt(361);
        // We make the color depth full
        float saturation = 1.0f;
        // We make a full bright color
        float value = 1.0f;
        // We avoid color transparency
        int alpha = 255;
        // Finally, generate the color
        int color = Color.HSVToColor(alpha, new float[]{hue, saturation, value});
        // Return the color
        return color;
    }
}
