package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.DltBetBean;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-12.
 */
public class DltBetAdapter extends BaseAdapter {
    private ArrayList<DltBetBean> dltBetList;
    private Activity activity;

    public DltBetAdapter(ArrayList<DltBetBean> dltBetList, Activity activity) {
        this.activity = activity;
        this.dltBetList = dltBetList;
    }

    @Override
    public int getCount() {
        return dltBetList.size();
    }

    @Override
    public Object getItem(int position) {
        return dltBetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DltBetBean dltBetBean = dltBetList.get(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.dlt_bet_item, null);
        }

        TextView tvDltNum = ViewHolder.get(convertView, R.id.tvDltNum);
        TextView tvStyle = ViewHolder.get(convertView, R.id.tvStyle);
        TextView tvTouzhuMoney = ViewHolder.get(convertView, R.id.tvTouzhuMoney);

        tvDltNum.setText(dltBetBean.getDltNumber());
        if (dltBetBean.getDltStyle() == 1) {
            tvStyle.setText("单式投注");
        } else {
            tvStyle.setText("复式投注");
        }
        tvTouzhuMoney.setText(dltBetBean.getMoneyTip());

        return convertView;
    }
}
