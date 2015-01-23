package com.evstudio.thefirstlottery.mobile.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.pojo.FootballTotoInfo;
import com.tandong.sa.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eric on 14/11/20.
 * 胡莹莹注释
 * 足彩中胜负彩适配器
 */
public class AdapterListViewToto14 extends BaseAdapter {
    public ArrayList<FootballTotoInfo> arrayList = new ArrayList<FootballTotoInfo>();
    private HashMap<Integer, View> map = new HashMap<Integer, View>();
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Handler mHandler;

    public HashMap<Integer, FootballTotoInfo> selectedItems = new HashMap<Integer, FootballTotoInfo>();

    public AdapterListViewToto14(Context ctx, ArrayList<FootballTotoInfo> list, Handler handler) {
        arrayList = list;
        context = ctx;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        FootballTotoInfo totoInfo = (FootballTotoInfo) this.getItem(position);
        if (null == map.get(position)) {
            convertView = mLayoutInflater.inflate(R.layout.listview_football_toto, parent, false);
            vh = new ViewHolder();
            vh.tvTotoClient = (TextView) convertView.findViewById(R.id.tvTotoClient);
            vh.tvTotoHost = (TextView) convertView.findViewById(R.id.tvTotoHost);
            vh.tvTotoMatch = (TextView) convertView.findViewById(R.id.tvTotoMatch);
            vh.tvTotoGameDayNumber = (TextView) convertView.findViewById(R.id.tvTotoGameDayNumber);
            vh.tvTotoTime = (TextView) convertView.findViewById(R.id.tvTotoTime);
            vh.tvTotoPl0 = (TextView) convertView.findViewById(R.id.tvTotoPl0);
            vh.tvTotoPl1 = (TextView) convertView.findViewById(R.id.tvTotoPl1);
            vh.tvTotoPl3 = (TextView) convertView.findViewById(R.id.tvTotoPl3);
            convertView.setTag(vh);
            map.put(position, convertView);
        } else {
            convertView = (View)map.get(position);
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvTotoMatch.setText(totoInfo.gameName);
        vh.tvTotoPl0.setText(totoInfo.teamOdds[2]);
        vh.tvTotoPl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballTotoInfo totoInfo =  (FootballTotoInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    totoInfo.selected[2] = 0;
                    if (!totoInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    totoInfo.selected[2] = 1;
                    selectedItems.put(position, totoInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("totoSelected");
            }
        });
        vh.tvTotoPl1.setText(totoInfo.teamOdds[1]);
        vh.tvTotoPl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballTotoInfo totoInfo =  (FootballTotoInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    totoInfo.selected[1] = 0;
                    if (!totoInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    totoInfo.selected[1] = 1;
                    selectedItems.put(position, totoInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("totoSelected");
            }
        });
        vh.tvTotoPl3.setText(totoInfo.teamOdds[0]);
        vh.tvTotoPl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballTotoInfo totoInfo =  (FootballTotoInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    totoInfo.selected[0] = 0;
                    if (!totoInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    totoInfo.selected[0] = 1;
                    selectedItems.put(position, totoInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("totoSelected");
            }
        });

        vh.tvTotoTime.setText(totoInfo.sellDate);
        vh.tvTotoGameDayNumber.setText(String.valueOf(totoInfo.gameNumberOfDay));
        vh.tvTotoHost.setText(totoInfo.teams[0]);
        vh.tvTotoClient.setText(totoInfo.teams[1]);

        return convertView;
    }

    class ViewHolder {
        TextView tvTotoGameDayNumber;
        TextView tvTotoMatch;
        TextView tvTotoTime;
        TextView tvTotoHost;
        TextView tvTotoClient;
        TextView tvTotoPl3;
        TextView tvTotoPl1;
        TextView tvTotoPl0;
    }
}
