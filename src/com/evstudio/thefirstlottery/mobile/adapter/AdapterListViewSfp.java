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
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.tandong.sa.eventbus.EventBus;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by eric on 14/11/9.
 */
public class AdapterListViewSfp extends BaseAdapter {

    public ArrayList<FootballGameInfo> arrayList = new ArrayList<FootballGameInfo>();
    private HashMap<Integer, View> map = new HashMap<Integer, View>();
    private final LayoutInflater mLayoutInflater;
    private Context context;
    private Handler mHandler;
    private FootballGameInfo ftinfo;
    public HashMap<Integer, FootballGameInfo> selectedItems = new HashMap<Integer, FootballGameInfo>();

    public AdapterListViewSfp(Context ctx, ArrayList<FootballGameInfo> list, Handler handler) {
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
        ftinfo = (FootballGameInfo) this.getItem(position);
        if (null == map.get(position)) {
            convertView = mLayoutInflater.inflate(R.layout.listview_ft_sfp, parent, false);
            vh = new ViewHolder();
            vh.tvFbDate = (TextView) convertView.findViewById(R.id.tvFtDate);
            vh.tvFbMatch = (TextView) convertView.findViewById(R.id.tvFtMatch);
            vh.tvFbTime = (TextView) convertView.findViewById(R.id.tvFtTime);
            vh.tvFbWeather = (TextView) convertView.findViewById(R.id.tvFtWeather);
            vh.tvSfpHTeam = (TextView) convertView.findViewById(R.id.tvSfpHTeam);
            vh.tvSfpGTeam = (TextView) convertView.findViewById(R.id.tvSfpGTeam);
            vh.tvSfpPl3 = (TextView) convertView.findViewById(R.id.tvSfpPl3);
            vh.tvSfpPl1 = (TextView) convertView.findViewById(R.id.tvSfpPl1);
            vh.tvSfpPl0 = (TextView) convertView.findViewById(R.id.tvSfpPl0);
            vh.tvSfpRqPl3 = (TextView) convertView.findViewById(R.id.tvSfpRqPl3);
            vh.tvSfpRqPl1 = (TextView) convertView.findViewById(R.id.tvSfpRqPl1);
            vh.tvSfpRqPl0 = (TextView) convertView.findViewById(R.id.tvSfpRqPl0);
            convertView.setTag(vh);
            map.put(position, convertView);
        } else {
            convertView = (View) map.get(position);
            vh = (ViewHolder) convertView.getTag();
        }

        vh.tvFbDate.setText(ftinfo.gameNumber);
        vh.tvFbMatch.setText(ftinfo.matchName);
        vh.tvFbMatch.setBackgroundColor(android.graphics.Color.parseColor("#" + ftinfo.backgroundColor));
        vh.tvFbTime.setText(ftinfo.gameDate);
        vh.tvFbWeather.setText(ftinfo.weather);
        vh.tvSfpHTeam.setText(ftinfo.hostTeam);
        vh.tvSfpGTeam.setText(ftinfo.clientTeam);

        vh.tvSfpPl3.setText(String.valueOf(ftinfo.oddsHost));
        vh.tvSfpPl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballGameInfo gameInfo =  (FootballGameInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    gameInfo.selected[0] = 0;
                    if (!gameInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    gameInfo.selected[0] = 1;
                    selectedItems.put(position, gameInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("sfpselected");
            }
        });

        vh.tvSfpPl1.setText(String.valueOf(ftinfo.oddsDraw));
        vh.tvSfpPl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballGameInfo gameInfo =  (FootballGameInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    gameInfo.selected[1] = 0;
                    if (!gameInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    gameInfo.selected[1] = 1;
                    selectedItems.put(position, gameInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("sfpselected");
            }
        });

        vh.tvSfpPl0.setText(String.valueOf(ftinfo.oddsClient));
        vh.tvSfpPl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballGameInfo gameInfo =  (FootballGameInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    gameInfo.selected[2] = 0;
                    if (!gameInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    gameInfo.selected[2] = 1;
                    selectedItems.put(position, gameInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("sfpselected");
            }
        });

        vh.tvSfpRqPl3.setText("主 " + String.valueOf(ftinfo.assignor)
                + " 胜 " + String.valueOf(ftinfo.oddsAssignHost));
        vh.tvSfpRqPl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballGameInfo gameInfo =  (FootballGameInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    gameInfo.selected[3] = 0;
                    if (!gameInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    gameInfo.selected[3] = 1;
                    selectedItems.put(position, gameInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("sfpselected");
            }
        });

        vh.tvSfpRqPl1.setText("平 " + String.valueOf(ftinfo.oddsAssignDraw));
        vh.tvSfpRqPl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballGameInfo gameInfo =  (FootballGameInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    gameInfo.selected[4] = 0;
                    if (!gameInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    gameInfo.selected[4] = 1;
                    selectedItems.put(position, gameInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("sfpselected");
            }
        });

        vh.tvSfpRqPl0.setText("负 " + String.valueOf(ftinfo.oddsAssignClient));
        vh.tvSfpRqPl0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FootballGameInfo gameInfo =  (FootballGameInfo) getItem(position);

                Resources resources = context.getResources();
                Drawable drawable;
                TextView textView = (TextView) v;
                if (v.isSelected()) {
                    v.setSelected(false);
                    drawable = resources.getDrawable(R.drawable.footselectitembg);
                    gameInfo.selected[5] = 0;
                    if (!gameInfo.isSelected()) {
                        selectedItems.remove(position);
                    }
                } else {
                    v.setSelected(true);
                    drawable = resources.getDrawable(R.color.blue);
                    gameInfo.selected[5] = 1;
                    selectedItems.put(position, gameInfo);
                }
                v.setBackgroundDrawable(drawable);
                EventBus.getDefault().post("sfpselected");
            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView tvFbDate;
        TextView tvFbMatch;
        TextView tvFbTime;
        TextView tvFbWeather;
        TextView tvSfpHTeam;
        TextView tvSfpGTeam;
        TextView tvSfpPl3;
        TextView tvSfpPl1;
        TextView tvSfpPl0;
        TextView tvSfpRqPl3;
        TextView tvSfpRqPl1;
        TextView tvSfpRqPl0;
    }

//    View.OnClickListener itemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            Resources resources = context.getResources();
//            Drawable drawable;
//            TextView textView = (TextView) v;
//            if (v.isSelected()) {
//                v.setSelected(false);
//                drawable = resources.getDrawable(R.drawable.footselectitembg);
//            } else {
//                v.setSelected(true);
//                drawable = resources.getDrawable(R.color.blue);
//            }
//            v.setBackgroundDrawable(drawable);
//        }
//    };

}
