package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ericren on 14-10-25.
 */
public class Sh11x5LvHistoryAdapter extends ArrayAdapter<String[]> {
    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;


    class ViewHolder {
        TextView tvPeriods;
        TextView tvDraw;
    }

    ViewHolder vh;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     */
    public Sh11x5LvHistoryAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.white);
        mBackgroundColors.add(R.color.grey);
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(R.layout.layout_fm_lv_sh11x5_history, parent, false);
        vh = new ViewHolder();
        vh.tvPeriods = (TextView) convertView.findViewById(R.id.fmLvItemPeriod);
        vh.tvDraw = (TextView) convertView.findViewById(R.id.fmLvItemDraw);

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ? position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        String[] tvValues = getItem(position);
        vh.tvPeriods.setText(tvValues[0]);
        vh.tvDraw.setText(tvValues[1] + ","
                + tvValues[2] + ","
                + tvValues[3] + ","
                + tvValues[4] + ","
                + tvValues[5] + "");

        return convertView;
    }
}
