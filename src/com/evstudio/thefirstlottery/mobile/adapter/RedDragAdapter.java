package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.RedDragBean;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-8.
 */
public class RedDragAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<RedDragBean> redDragList;

    public RedDragAdapter(Activity activity, ArrayList<RedDragBean> redDragList) {
        this.activity = activity;
        this.redDragList = redDragList;
    }

    @Override
    public int getCount() {
        return redDragList.size();
    }

    @Override
    public Object getItem(int position) {
        return redDragList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.new_layout_dlt_item, null);
        }

        final TextView tvBetItem = ViewHolder.get(convertView, R.id.tvBetItem);
        tvBetItem.setText(redDragList.get(position).getNumber() + "");
        tvBetItem.setSelected(redDragList.get(position).isSel());

        return convertView;
    }
}
