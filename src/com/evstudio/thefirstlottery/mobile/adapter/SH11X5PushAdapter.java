package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.SH11X5PushBean;

import java.util.List;

/**
 * @author zhouyong
 * @version V 1.0
 * @ClassName: ActAdapter.java
 * @Description: 活动适配器
 * @date 2014年10月11日
 */

public class SH11X5PushAdapter extends BaseAdapter {
    private Activity activity;
    private List<SH11X5PushBean> list;
    private String[] mSelectTexts = {"任选一", "任选二", "任选三", "任选四", "任选五",
            "任选六", "任选七", "任选八", "组选前二", "组选前三", "直选前二", "直选前三"};

    public SH11X5PushAdapter(Activity activity, List<SH11X5PushBean> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SH11X5PushBean mSH11X5PushBean = list.get(position);
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.sh11x5_push_item, null);
        }

        TextView tvBetcontent = (TextView) ViewHolder.get(convertView, R.id.tvBetcontent);
        TextView tvBettype = (TextView) ViewHolder.get(convertView, R.id.tvBettype);
        TextView tvNumber = (TextView) ViewHolder.get(convertView, R.id.tvNumber);
        TextView tvMoney = (TextView) ViewHolder.get(convertView, R.id.tvMoney);
        TextView tvMultiple = (TextView) ViewHolder.get(convertView, R.id.tvMultiple);

        tvBetcontent.setText(mSH11X5PushBean.getBetcontent());
        tvBettype.setText(getBettype(mSH11X5PushBean.getBettype()));
        tvMultiple.setText(mSH11X5PushBean.getBetcount());


        tvNumber.setText( String.valueOf(mSH11X5PushBean.getZhushu()) );
        tvMoney.setText( String.valueOf( mSH11X5PushBean.getMoney()) );

        return convertView;
    }

    private String getBettype(String bettype) {
        int index = 1;
        if (bettype.substring(0, 1).equals("0")) {
            index = Integer.parseInt(bettype.substring(1, 2));
        } else {
            index = Integer.parseInt(bettype);
        }
        return mSelectTexts[index - 1];
    }

}
