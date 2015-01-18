package com.evstudio.thefirstlottery.mobile.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.activity.ActivityFootballMix;
import com.evstudio.thefirstlottery.mobile.common.ViewHolder;
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;
import com.evstudio.thefirstlottery.mobile.view.FootBallMixDescription;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 足彩混合投注适配器
 * Created by zyn on 15/1/14.
 */
public class AdapterListViewMixFootball extends BaseAdapter {

    private ArrayList<FootballInfoMix> mArrayList = new ArrayList<FootballInfoMix>();
    private Activity activity;
    public HashMap<Integer, FootballInfoMix> selectedItems = new HashMap<Integer, FootballInfoMix>();
    public HashMap<Integer, Button> clickButtons = new HashMap<Integer, Button>();

    public AdapterListViewMixFootball(Activity ctx, ArrayList<FootballInfoMix> list) {
        mArrayList = list;
        activity = ctx;
        EventBus.getDefault().register(this);
    }


    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.adapter_football_item, null);
        }

        FootballInfoMix infoMix = mArrayList.get(position);
        TextView tvFtDate = (TextView) ViewHolder.get(convertView, R.id.tvFtDate); // 周三001
        TextView tvFtMatch = (TextView) ViewHolder.get(convertView, R.id.tvFtMatch); // 欧冠
        TextView tvFtTime = (TextView) ViewHolder.get(convertView, R.id.tvFtTime); // 23:58
        TextView tvFtWeather = (TextView) ViewHolder.get(convertView, R.id.tvFtWeather); // tvFtWeather
        TextView tvSfpHTeam = (TextView) ViewHolder.get(convertView, R.id.tvSfpHTeam); // 主队
        TextView tvSfpGTeam = (TextView) ViewHolder.get(convertView, R.id.tvSfpGTeam); // 客队
        TextView tvSfpPl3 = (TextView) ViewHolder.get(convertView, R.id.tvSfpPl3); // 主队赔率
        TextView tvSfpPl1 = (TextView) ViewHolder.get(convertView, R.id.tvSfpPl1); // ping赔率
        TextView tvSfpPl0 = (TextView) ViewHolder.get(convertView, R.id.tvSfpPl0); // 客队赔率
        Button btnPlayMethod =
                (Button) ViewHolder.get(convertView, R.id.btnPlayMethod); // 玩法按钮
        btnPlayMethod.setBackgroundResource(0);
        btnPlayMethod.setTag(position);
        btnPlayMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FootBallMixDescription.class);
                // TODO 需要传递值
                intent.putExtra("gameinfo", mArrayList.get(position));
                intent.putExtra("gameIndex", position );
//                activity.startActivityForResult(intent, 300);
                activity.startActivity(intent);
            }
        });

        if( infoMix.isSelected() ) {
            int iSelected = 0;
            for (int i = 0; i < infoMix.selected.length; i++) {
                if (infoMix.selected[i] == 1)
                    iSelected++;
            }
            btnPlayMethod.setText("选中" + String.valueOf(iSelected) + "项");
        }else{
            btnPlayMethod.setText("玩法");
        }

        // TODO 进行数据填充 ！！！
        tvFtDate.setText(infoMix.gameNumber);
        tvFtMatch.setText(infoMix.matchName);
        tvFtMatch.setBackgroundColor(android.graphics.Color.parseColor(infoMix.matchColor));
        tvFtTime.setText(infoMix.sellTime);
        tvSfpHTeam.setText(infoMix.homeTeam);
        tvSfpGTeam.setText( infoMix.awayTeam);
        tvSfpPl3.setText( infoMix.oriPl[0]);
        tvSfpPl1.setText( infoMix.oriPl[1]);
        tvSfpPl0.setText(infoMix.oriPl[2]);
        tvFtWeather.setText("");


        return convertView;
    }

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if( null == json )
            return;
        if (json.startsWith("infoMix##")) {
            String[] values = json.split("##");
            Gson gson = new Gson();
            int iMixIndex = Integer.parseInt(values[1]);
            FootballInfoMix iMix = gson.fromJson( values[2], new TypeToken<FootballInfoMix>() {
            }.getType());

            mArrayList.set( iMixIndex, iMix );
            this.notifyDataSetChanged();

            if( !iMix.isSelected() ) {
                if (null != selectedItems.get(iMixIndex)) {
                    selectedItems.remove(iMixIndex);
                }
            }else{
                selectedItems.put(iMixIndex, iMix);
            }

            EventBus.getDefault().post("mixChange");
//            ((ActivityFootballMix)activity).setTip();
//            if (null != adapter) {
//                this.txSpfTip.setText("已选中" + adapter.selectedItems.size() + "场比赛");
//            }
        }
    }
}
