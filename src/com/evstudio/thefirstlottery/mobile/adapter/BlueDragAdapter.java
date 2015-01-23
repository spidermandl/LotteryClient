package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.BlueDragBean;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-8.
 *  胡莹莹注释
 * 大乐透彩票中拖区篮球适配器
 */
public class BlueDragAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<BlueDragBean> blueDragList;

    public BlueDragAdapter(Activity activity, ArrayList<BlueDragBean> blueDragList) {
        this.activity = activity;
        this.blueDragList = blueDragList;
    }

    @Override
    public int getCount() {
        return blueDragList.size();
    }

    @Override
    public Object getItem(int position) {
        return blueDragList.get(position);
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

        tvBetItem.setText(blueDragList.get(position).getNumber()+"");
        tvBetItem.setSelected(blueDragList.get(position).isSel());
        return convertView;
    }
}
