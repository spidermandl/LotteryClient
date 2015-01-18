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
import com.evstudio.thefirstlottery.mobile.adapter.AdapterListViewToto;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.evstudio.thefirstlottery.mobile.pojo.FootballTotoInfo;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by eric on 14/11/20.
 */
public class ActivityFootballToto extends SherlockActivity {

    private ActionBar actionBar;

    private ListView listView;
    private TextView tvGameNumber;
    private TextView tvSellEnd;
    private TextView tvBuyTip;
    private BootstrapButton bbSubmit;

    private AdapterListViewToto adapter;

    private ProgressDialog progressDialog;
    private AssistTool assistTool;

    private String sellNumber;
    private String endTime;

    private ArrayList<FootballTotoInfo> arrayList = new ArrayList<FootballTotoInfo>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_toto);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.app_icon_new_04);
        actionBar.setTitle("竞彩足球（任选9场）");

        listView = (ListView) findViewById(R.id.lvFootballToto);
        tvGameNumber = (TextView) findViewById(R.id.tvFtTotoGameNumber);
        tvSellEnd = (TextView) findViewById(R.id.tvFtTotoSellEnd);
        tvBuyTip = (TextView) findViewById(R.id.tvFtTotoTip);
        bbSubmit = (BootstrapButton) findViewById(R.id.bbFtTotoSubmit);
        bbSubmit.setOnClickListener(submitClick);
        assistTool = new AssistTool(this);

        adapter = new AdapterListViewToto(this, arrayList, null);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        EventBus.getDefault().register(this);

        HttpRestClient.getDirect("http://m.okooo.com/I/?method=touch.soccer.match.matchlist&" +
                "lotterytype=ToTo&lotteryno=", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String strResponse = new String(bytes);
                try {
                    JSONObject jsonObject = new JSONObject(strResponse);
                    sellNumber = jsonObject.getString("lottery_no");
                    endTime = jsonObject.getString("end_time");
                    tvGameNumber.setText("期数:" + sellNumber);
                    tvSellEnd.setText("停售时间:" + endTime);

                    String matchlist = jsonObject.getString("matchlist");
                    JSONObject jsonObjectMatchlist = new JSONObject(matchlist);
                    for (int k = 1; k <= 14; k++) {
                        String strGame = jsonObjectMatchlist.getString("m" + String.valueOf(k));
                        JSONObject jsonObjectGame = new JSONObject(strGame);

                        FootballTotoInfo totoInfo = new FootballTotoInfo();
                        totoInfo.gameName = jsonObjectGame.getString("LeagueCnName");
                        totoInfo.gameNumber = jsonObjectGame.getInt("MatchID");
                        totoInfo.gameNumberOfDay = jsonObjectGame.getInt("MatchOrder");
                        totoInfo.stopSellTime = jsonObjectGame.getString("MatchTime");
                        totoInfo.teams[0] = jsonObjectGame.getString("HomeName");
                        totoInfo.teams[1] = jsonObjectGame.getString("AwayName");
                        totoInfo.sellDate = jsonObjectGame.getString("MatchTime");
                        totoInfo.homeGoals = jsonObjectGame.getString("HomeGoals");
                        totoInfo.awayGoals = jsonObjectGame.getString("AwayGoals");
                        totoInfo.gameResult = jsonObjectGame.getString("Result");
                        totoInfo.teamOdds[0] = jsonObjectGame.getString("home");
                        totoInfo.teamOdds[1] = jsonObjectGame.getString("draw");
                        totoInfo.teamOdds[2] = jsonObjectGame.getString("away");

                        arrayList.add(totoInfo);
                    }

                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

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

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if ("totoSelected".equals(json)) {
            if (null != adapter) {
                this.tvBuyTip.setText("已选中" + adapter.selectedItems.size() + "场比赛,至少选择9场比赛");
            }
        }
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("至少需要选择9场比赛！");
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

            if (adapter.selectedItems.size() < 9) {
                showDialog(ActivityFootballToto.this);
                return;
            }

            Intent intent = new Intent();
            ArrayList<FootballTotoInfo> totoInfos = new ArrayList<FootballTotoInfo>();
            Iterator iter = adapter.selectedItems.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, FootballTotoInfo> entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                totoInfos.add((FootballTotoInfo) val);
            }
            intent.putExtra("games", totoInfos);
            intent.putExtra("sellNumber", sellNumber );
            intent.putExtra("endTime", endTime);
            intent.setClass(ActivityFootballToto.this, ActivityFootballTotoResult.class);
            ActivityFootballToto.this.startActivity(intent);
        }
    };

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
}