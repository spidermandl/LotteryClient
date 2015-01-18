package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.AdapterListViewFtHh;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.evstudio.thefirstlottery.mobile.pojo.FootballInfoMix;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by eric on 14/11/20.
 */
public class ActivityFootballHh extends SherlockActivity {

    private ActionBar actionBar;

    private ProgressDialog progressDialog;
    private AssistTool assistTool;

    private ArrayList<FootballInfoMix> arrayList = new ArrayList<FootballInfoMix>();
    private AdapterListViewFtHh adapter;

    private ListView listView;
    private TextView tvTip;
    private BootstrapButton bbSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_hunhe);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.app_icon_new_04);
        actionBar.setTitle("竞彩足球（混合投注）");

        assistTool = new AssistTool(this);

        listView = (ListView)findViewById(R.id.lvFootballHh);
        tvTip = (TextView)findViewById(R.id.tvFtHhTip);
        bbSubmit = (BootstrapButton)findViewById(R.id.bbFtHhSubmit);
        bbSubmit.setOnClickListener( submitClick );

        adapter = new AdapterListViewFtHh( this, arrayList, null );
        listView.setAdapter(adapter);

        EventBus.getDefault().register(this);

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
//                String strResponse = null;
//                try {
//                    strResponse = new String(bytes, "GBK");
//                } catch (UnsupportedEncodingException e) {
//                    strResponse = new String(bytes);
//                    e.printStackTrace();
//                }
//                strResponse = strResponse.substring(9);
//                strResponse = strResponse.substring(0, strResponse.length() - 11);
//                try {
//                    JSONArray jsonArray = new JSONArray(strResponse);
//                    for (int k = 0; k < jsonArray.length(); k++) {
//                        FootballInfoMix infoMix = new FootballInfoMix();
//                        infoMix.teamInfo = jsonArray.getString(k);
//                        JSONArray jaGame = new JSONArray(infoMix.teamInfo);
//                        String[] jaResult = new String[6];
//                        for (int j = 0; j < 6; j++) {
//                            jaResult[j] = jaGame.getString(j);
//                        }
//                        jaGame = new JSONArray(jaResult[0]);
//                        infoMix.gameNumber = jaGame.getString(0);
//                        infoMix.matchName = jaGame.getString(1);
//                        infoMix.teamInfo = jaGame.getString(2);
//                        infoMix.sellTime = jaGame.getString(3);
//                        infoMix.gid = jaGame.getString(4);
//                        infoMix.matchColor = jaGame.getString(5);
//
//                        String[] teams = infoMix.teamInfo.split("\\$");
//                        infoMix.homeTeam = teams[0];
//                        infoMix.odd = teams[1];
//                        infoMix.awayTeam = teams[2];
//
//                        jaGame = new JSONArray(jaResult[1]);
//                        for (int m = 0; m < infoMix.oddPl.length; m++)
//                            infoMix.oddPl[m] = jaGame.getString(m);
//
//                        jaGame = new JSONArray(jaResult[2]);
//                        for (int m = 0; m < infoMix.scores.length; m++)
//                            infoMix.scores[m] = jaGame.getString(m);
//
//                        jaGame = new JSONArray(jaResult[3]);
//                        for (int m = 0; m < infoMix.goals.length; m++)
//                            infoMix.goals[m] = jaGame.getString(m);
//
//                        jaGame = new JSONArray(jaResult[4]);
//                        for (int m = 0; m < infoMix.halfWin.length; m++)
//                            infoMix.halfWin[m] = jaGame.getString(m);
//
//                        jaGame = new JSONArray(jaResult[5]);
//                        for (int m = 0; m < infoMix.oriPl.length; m++)
//                            infoMix.oriPl[m] = jaGame.getString(m);
//
//                        arrayList.add(infoMix);
//                    }
//                    adapter.notifyDataSetChanged();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                progressDialog.dismiss();
                AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootballHh.this).setTitle("网络异常")
                        .setMessage("网络连接异常，请重试！")
                        .create();
                alertDialog.show();
                return;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu("更多玩法");

        subMenu.add("胜负平/让球胜负平").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityFootBallSfp.class, true);
                return false;
            }
        });
        subMenu.add("任选9场").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityFootballToto.class, true);
                return false;
            }
        });
        subMenu.add("胜负彩").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityFootballToto14.class, true);
                return false;
            }
        });
        subMenu.add("混合投注").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityFootballHh.class, true);
                return false;
            }
        });

        MenuItem menuItem = subMenu.getItem();
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if ("hhSelected".equals(json)) {
            if (null != adapter) {
                this.tvTip.setText("已选中" + adapter.selectedItems.size() + "场比赛,至少选择2场比赛");
            }
        }
    }

    Button.OnClickListener submitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == adapter || null == adapter.selectedItems) {
                return;
            }

            if (adapter.selectedItems.size() < 2) {
                showDialog(ActivityFootballHh.this);
                return;
            }

            if (adapter.selectedItems.size() > 8) {
                showDialog2(ActivityFootballHh.this);
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
            intent.setClass(ActivityFootballHh.this, ActivityFootBallHhResult.class);
            ActivityFootballHh.this.startActivity(intent);
        }
    };

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
}