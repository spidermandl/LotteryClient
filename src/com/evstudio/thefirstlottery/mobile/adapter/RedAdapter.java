package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.RedBean;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-8.
 *  胡莹莹注释
 * 大乐透彩票中胆区红球适配器
 */
public class RedAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<RedBean> redList;

    public RedAdapter(Activity activity, ArrayList<RedBean> redList) {
        this.activity = activity;
        this.redList = redList;
    }

    @Override
    public int getCount() {
        return redList.size();
    }

    @Override
    public Object getItem(int position) {
        return redList.get(position);
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
        tvBetItem.setText(redList.get(position).getNumber() + "");
        tvBetItem.setSelected(redList.get(position).isSel());

        return convertView;
    }
}
