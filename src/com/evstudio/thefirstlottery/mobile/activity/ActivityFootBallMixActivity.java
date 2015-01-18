package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.AdapterListViewMixFootball;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * 混合投注
 * Created by zyn on 15/1/14.
 */
public class ActivityFootBallMixActivity extends SherlockActivity {

    /** 列表 */
    private ListView lvFootballMix;
    private AdapterListViewMixFootball adapter;
    private ArrayList<FootballInfoMix> mArrayList = new ArrayList<FootballInfoMix>();

    /** 投注按钮 */
    private BootstrapButton bbFtMixSubmit;

    private TextView tvTip;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_mix_layout);

        initializeView();
    }

    private void initializeView(){
        lvFootballMix = (ListView) findViewById(R.id.lvFootballMix);
        adapter = new AdapterListViewMixFootball(this,mArrayList);
        lvFootballMix.setAdapter(adapter);
        bbFtMixSubmit = (BootstrapButton)findViewById(R.id.bbFtMixSubmit);
        bbFtMixSubmit.setOnClickListener(submitClick);

        tvTip = (TextView)findViewById(R.id.tvFtMixTip);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        HttpRestClient.getDirect("http://info.sporttery.cn/interface/interface_mixed.php?action=fb_list" +
                "&pke=0.2822370194658922&_=" +
                String.valueOf(System.currentTimeMillis()), null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String strResponse = null;
                try {
                    strResponse = new String(bytes, "GBK");
                } catch (UnsupportedEncodingException e) {
                    strResponse = new String(bytes);
                    e.printStackTrace();
                }
                strResponse = strResponse.substring(9);
                strResponse = strResponse.substring(0, strResponse.length() - 11);
                try {
                    JSONArray jsonArray = new JSONArray(strResponse);
                    for (int k = 0; k < jsonArray.length(); k++) {
                        FootballInfoMix infoMix = new FootballInfoMix();
                        infoMix.teamInfo = jsonArray.getString(k);
                        JSONArray jaGame = new JSONArray(infoMix.teamInfo);
                        String[] jaResult = new String[6];
                        for (int j = 0; j < 6; j++) {
                            jaResult[j] = jaGame.getString(j);
                        }
                        jaGame = new JSONArray(jaResult[0]);
                        infoMix.gameNumber = jaGame.getString(0);
                        infoMix.matchName = jaGame.getString(1);
                        infoMix.teamInfo = jaGame.getString(2);
                        infoMix.sellTime = jaGame.getString(3);
                        infoMix.gid = jaGame.getString(4);
                        infoMix.matchColor = jaGame.getString(5);

                        String[] teams = infoMix.teamInfo.split("\\$");
                        infoMix.homeTeam = teams[0];
                        infoMix.odd = teams[1];
                        infoMix.awayTeam = teams[2];

                        jaGame = new JSONArray(jaResult[1]);
                        for (int m = 0; m < infoMix.oddPl.length; m++)
                            infoMix.oddPl[m] = jaGame.getString(m);

                        jaGame = new JSONArray(jaResult[2]);
                        for (int m = 0; m < infoMix.scores.length; m++)
                            infoMix.scores[m] = jaGame.getString(m);

                        jaGame = new JSONArray(jaResult[3]);
                        for (int m = 0; m < infoMix.goals.length; m++)
                            infoMix.goals[m] = jaGame.getString(m);

                        jaGame = new JSONArray(jaResult[4]);
                        for (int m = 0; m < infoMix.halfWin.length; m++)
                            infoMix.halfWin[m] = jaGame.getString(m);

                        jaGame = new JSONArray(jaResult[5]);
                        for (int m = 0; m < infoMix.oriPl.length; m++)
                            infoMix.oriPl[m] = jaGame.getString(m);

                        mArrayList.add(infoMix);
                    }
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                progressDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootBallMixActivity.this).setTitle("网络异常")
                        .setMessage("网络连接异常，请重试！")
                        .create();
                alertDialog.show();
                return;
            }
        });
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("至少需要选择两场比赛！");
        builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.show();
    }

    private void showDialog2(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("至多选择八场比赛！");
        builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.show();
    }

    Button.OnClickListener submitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == adapter || null == adapter.selectedItems) {
                return;
            }

            if (adapter.selectedItems.size() < 2) {
                showDialog(ActivityFootBallMixActivity.this);
                return;
            }

            if (adapter.selectedItems.size() > 8) {
                showDialog2(ActivityFootBallMixActivity.this);
                return;
            }

            Intent intent = new Intent();
            ArrayList<FootballInfoMix> games = new ArrayList<FootballInfoMix>();
            Iterator iter = adapter.selectedItems.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, FootballInfoMix> entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                games.add((FootballInfoMix) val);
            }
            intent.putExtra("games", games);
            intent.setClass(ActivityFootBallMixActivity.this, ActivityFootBallHhResult.class);
            ActivityFootBallMixActivity.this.startActivity(intent);
        }
    };

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if( null == json )
            return;
        if ("mixChange".equals(json)) {
            if (null != tvTip) {
                this.tvTip.setText("已选中" + adapter.selectedItems.size() + "场比赛");
            }
        }
    }
}
