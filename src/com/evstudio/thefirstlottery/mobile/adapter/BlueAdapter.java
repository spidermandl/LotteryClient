package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.BlueBean;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-8.
 * 胡莹莹注释
 * 大乐透彩票中胆区篮球适配器
 */
public class BlueAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<BlueBean> blueList;

    public BlueAdapter(Activity activity, ArrayList<BlueBean> biueList) {
        this.activity = activity;
        this.blueList = biueList;
    }

    @Override
    public int getCount() {
        return blueList.size();
    }

    @Override
    public Object getItem(int position) {
        return blueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.new_layout_dlt_blue_item, null);
        }

        TextView tvBetItem = ViewHolder.get(convertView, R.id.tvBetItem);

        tvBetItem.setText(blueList.get(position).getNumber()+"");
        tvBetItem.setSelected(blueList.get(position).isSel());
        return convertView;
    }
}
