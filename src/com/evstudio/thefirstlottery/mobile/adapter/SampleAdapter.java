package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;

import java.util.ArrayList;
import java.util.Random;

//import com.tandong.sa.view.
//import com.tandong.sa.stagger.DynamicHeightTextView;

/**
 * ADAPTER
 * 胡莹莹注释
 * 上海11选5中走势图界面中根据走势图选择新一期的号码的适配器：
 * 有“点击返回”，11个号码供选择
 */

public class SampleAdapter extends ArrayAdapter<String[]> {

    private static final String TAG = "SampleAdapter";

    class ViewHolder {
        TextView tvPeriods;
        TextView tvNumber1;
        TextView tvNumber2;
        TextView tvNumber3;
        TextView tvNumber4;
        TextView tvNumber5;
        TextView tvNumber6;
        TextView tvNumber7;
        TextView tvNumber8;
        TextView tvNumber9;
        TextView tvNumber10;
        TextView tvNumber11;
//        TextView txtLineTwo;
    }

    ViewHolder vh;

    private final LayoutInflater mLayoutInflater;
    private final Random mRandom;
    private final ArrayList<Integer> mBackgroundColors;

//    private ImageView imageView;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public SampleAdapter(final Context context, final int textViewResourceId) {
        super(context, textViewResourceId);
        mLayoutInflater = LayoutInflater.from(context);
        mRandom = new Random();
        mBackgroundColors = new ArrayList<Integer>();
        mBackgroundColors.add(R.color.white);
        mBackgroundColors.add(R.color.grey);

//        imageView = new ImageView(context);
//        imageView.setImageResource(R.drawable.hao_normal_round_red);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
//        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_sample, parent, false);
            vh = new ViewHolder();
            vh.tvPeriods = (TextView) convertView.findViewById(R.id.tv_periods);
            vh.tvNumber1 = (TextView) convertView.findViewById(R.id.tv_number1);
            vh.tvNumber2 = (TextView) convertView.findViewById(R.id.tv_number2);
            vh.tvNumber3 = (TextView) convertView.findViewById(R.id.tv_number3);
            vh.tvNumber4 = (TextView) convertView.findViewById(R.id.tv_number4);
            vh.tvNumber5 = (TextView) convertView.findViewById(R.id.tv_number5);
            vh.tvNumber6 = (TextView) convertView.findViewById(R.id.tv_number6);
            vh.tvNumber7 = (TextView) convertView.findViewById(R.id.tv_number7);
            vh.tvNumber8 = (TextView) convertView.findViewById(R.id.tv_number8);
            vh.tvNumber9 = (TextView) convertView.findViewById(R.id.tv_number9);
            vh.tvNumber10 = (TextView) convertView.findViewById(R.id.tv_number10);
            vh.tvNumber11 = (TextView) convertView.findViewById(R.id.tv_number11);
            convertView.setTag(vh);
//        } else {
//            vh = (ViewHolder) convertView.getTag();
//        }

        double positionHeight = getPositionRatio(position);
        int backgroundIndex = position >= mBackgroundColors.size() ? position % mBackgroundColors.size() : position;

        convertView.setBackgroundResource(mBackgroundColors.get(backgroundIndex));

        Log.d(TAG, "getView position:" + position + " h:" + positionHeight);

        String[] tvValues = getItem(position);
        vh.tvPeriods.setText(tvValues[0].substring(6));
        for (int i = 1; i <= 5; i++) {
            int j = 0;
            try {
                j = Integer.parseInt(tvValues[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (j) {
                case 1:
                        if (i == 1)
                            vh.tvNumber1.setBackgroundResource(R.drawable.ball_red);
                        else
                            vh.tvNumber1.setBackgroundResource(R.drawable.ball_yellow);
//                    vh.tvNumber1.setBackgroundColor(Color.RED);
                        vh.tvNumber1.setTextColor(Color.WHITE);
                    break;

                case 2:
                    if (i == 1)
                        vh.tvNumber2.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber2.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber2.setTextColor(Color.WHITE);
                    break;
                case 3:
                    if (i == 1)
                        vh.tvNumber3.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber3.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber3.setTextColor(Color.WHITE);
                    break;
                case 4:
                    if (i == 1)
                        vh.tvNumber4.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber4.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber4.setTextColor(Color.WHITE);
                    break;
                case 5:
                    if (i == 1)
                        vh.tvNumber5.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber5.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber5.setTextColor(Color.WHITE);
                    break;
                case 6:
                    if (i == 1)
                        vh.tvNumber6.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber6.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber6.setTextColor(Color.WHITE);
                    break;
                case 7:
                    if (i == 1)
                        vh.tvNumber7.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber7.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber7.setTextColor(Color.WHITE);
                    break;
                case 8:
                    if (i == 1)
                        vh.tvNumber8.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber8.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber8.setTextColor(Color.WHITE);
                    break;
                case 9:
                    if (i == 1)
                        vh.tvNumber9.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber9.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber9.setTextColor(Color.WHITE);
                    break;
                case 10:
                    if (i == 1)
                        vh.tvNumber10.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber10.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber10.setTextColor(Color.WHITE);
                    break;
                case 11:
                    if (i == 1)
                        vh.tvNumber11.setBackgroundResource(R.drawable.ball_red);
                    else
                        vh.tvNumber11.setBackgroundResource(R.drawable.ball_yellow);
                    vh.tvNumber11.setTextColor(Color.WHITE);
                    break;
                default:
                    break;
            }
        }
        return convertView;
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
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
        // the width
    }

}