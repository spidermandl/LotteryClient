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
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;

import java.util.ArrayList;

/**
 * Created by eric on 14/11/11.
 * 胡莹莹注释
 * 足彩混合投注比赛结果适配器
 */
public class AdapterListViewHhResult extends BaseAdapter {
    private ArrayList<FootballInfoMix> arrayList;

    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Handler mHandler;

    public AdapterListViewHhResult(Context ctx, ArrayList<FootballInfoMix> list, Handler handler) {
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
        FootballInfoMix footballGameInfo;
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
        footballGameInfo = (FootballInfoMix)getItem(position);
        vh.tvsfpr_item_gameNumber.setText( footballGameInfo.gameNumber );
        vh.tvsfpr_item_match.setText(footballGameInfo.homeTeam + "  VS  " + footballGameInfo.awayTeam );
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
            strTip += "\r\n";
        }

        int iCheckSelected = 0;
        for( int i = 6; i < 15; i ++ ){
            if( footballGameInfo.selected[i] == 1 ) {
                iCheckSelected = i;
                break;
            }
        }
        if( iCheckSelected > 0 ){
            strTip += "半全场胜负平  ";
            for( int i = iCheckSelected; i < 15; i ++ ){
                if( footballGameInfo.selected[i] == 1 ){
                    strTip += footballGameInfo.gameTips[i] + " ";
                }
            }
            strTip += "\r\n";
        }
        iCheckSelected = 0;

        for( int i = 15; i < 23; i ++ ){
            if( footballGameInfo.selected[i] == 1 ) {
                iCheckSelected = i;
                break;
            }
        }
        if( iCheckSelected > 0 ){
            strTip += "总进球数  ";
            for( int i = iCheckSelected; i < 23; i ++ ){
                if( footballGameInfo.selected[i] == 1 ){
                    strTip += footballGameInfo.gameTips[i] + " ";
                }
            }
            strTip += "\r\n";
        }
        iCheckSelected = 0;

        for( int i = 23; i < 54; i ++ ){
            if( footballGameInfo.selected[i] == 1 ) {
                iCheckSelected = i;
                break;
            }
        }
        if( iCheckSelected > 0 ){
            strTip += "比分  ";
            for( int i = iCheckSelected; i < 54; i ++ ){
                if( footballGameInfo.selected[i] == 1 ){
                    strTip += footballGameInfo.gameTips[i] + " ";
                }
            }
            strTip += "\r\n";
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
