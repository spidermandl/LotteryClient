package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;

import java.util.ArrayList;

/**
 * Created by eric on 14/11/11.
 *  胡莹莹注释
 * 足彩中任选9比赛结果适配器
 */
public class AdapterListViewTotoResult extends BaseAdapter {
    private ArrayList<FootballGameInfo> arrayList;

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Handler mHandler;

    public AdapterListViewTotoResult(Context ctx, ArrayList<FootballGameInfo> list, Handler handler) {
        context = ctx;
        arrayList = list;
        mLayoutInflater = LayoutInflater.from(context);
        mHandler = handler;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        FootballGameInfo footballGameInfo;
        if( null == convertView){
            convertView = mLayoutInflater.inflate(R.layout.listview_ft_sfp_items, parent, false);
            vh = new ViewHolder();
            vh.tvsfpr_item_content = (TextView)convertView.findViewById(R.id.tvsfpr_item_content);
            vh.tvsfpr_item_gameNumber = (TextView)convertView.findViewById(R.id.tvsfpr_item_gameNumber);
            vh.tvsfpr_item_match = (TextView)convertView.findViewById(R.id.tvsfpr_item_match);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }
        footballGameInfo = (FootballGameInfo)getItem(position);
        vh.tvsfpr_item_gameNumber.setText( footballGameInfo.gameNumber );
        vh.tvsfpr_item_match.setText(footballGameInfo.hostTeam + "  VS  " + footballGameInfo.clientTeam );
        String strTip = "";
        if( footballGameInfo.selected[0] + footballGameInfo.selected[1] + footballGameInfo.selected[2] > 0 ){
            strTip += "胜负平    ";
            if( footballGameInfo.selected[0] == 1)
                strTip += "    胜";
            if( footballGameInfo.selected[1] == 1)
                strTip += "    平";
            if( footballGameInfo.selected[2] == 1)
                strTip += "    负";
            strTip += "\r\n";
        }

        if( footballGameInfo.selected[3] + footballGameInfo.selected[4] + footballGameInfo.selected[5] > 0 ){
            strTip += "让球胜负平  ";
            if( footballGameInfo.selected[3] == 1)
                strTip += "    胜";
            if( footballGameInfo.selected[4] == 1)
                strTip += "    平";
            if( footballGameInfo.selected[5] == 1)
                strTip += "    负";
        }

        vh.tvsfpr_item_content.setText(strTip);


        return convertView;
    }

    class ViewHolder{
        TextView tvsfpr_item_gameNumber;
        TextView tvsfpr_item_match;
        TextView tvsfpr_item_content;
    }
}
