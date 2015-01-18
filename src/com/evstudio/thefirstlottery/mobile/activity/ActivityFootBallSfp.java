package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cn.hugo.android.scanner.Intents;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.AdapterListViewSfp;
import com.evstudio.thefirstlottery.mobile.adapter.FootballMatchAdapter;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.stream.JsonReader;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by eric on 14/11/9.
 */
public class ActivityFootBallSfp extends SherlockActivity {

    private ActionBar actionBar;

    private String TAG = "FOOTBALL_SFP";

    private ListView lvSfp;
    private TextView txSpfTip;
    private BootstrapButton bbtnSpf;

    private ArrayList<FootballGameInfo> arrayList = new ArrayList<FootballGameInfo>();
    private ProgressDialog progressDialog;

    private AdapterListViewSfp adapter;

    private AssistTool assistTool;

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ft_spf);

        lvSfp = (ListView) findViewById(R.id.lvGameListSpf);
        txSpfTip = (TextView) findViewById(R.id.txSfpTip);
        bbtnSpf = (BootstrapButton) findViewById(R.id.bbtnSfp);
        bbtnSpf.setOnClickListener(submitClick);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon(R.drawable.app_icon_new_04);
        actionBar.setTitle("竞彩足球（胜负平/让球胜负平）");

        assistTool = new AssistTool(this);

        adapter = new AdapterListViewSfp(this, arrayList, null);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        HttpRestClient.getDirect("http://i.sporttery.cn/odds_calculator/get_odds?" +
                "i_format=json&" +
                "i_callback=getData&" +
                "poolcode[]=hhad&" +
                "poolcode[]=had&", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String strResponse = new String(bytes);
                strResponse = strResponse.substring(8);
                strResponse = strResponse.substring(0, strResponse.length() - 2);
//                System.out.println("response:" + jsonFormatter(strResponse));
                try {
                    JsonReader reader = new JsonReader(new StringReader(strResponse));
                    reader.beginObject();
                    if (reader.hasNext()) {
                        //data
                        String rootName = reader.nextName();
                        reader.beginObject();
                        while (reader.hasNext()) {
                            FootballGameInfo football = new FootballGameInfo();
                            String idName = reader.nextName();
                            football.id = idName;
                            reader.beginObject();
                            while (reader.hasNext()) {
                                //id
                                while (reader.hasNext()) {
                                    String tagName = reader.nextName();
                                    if (null != tagName) {
                                        if ("id".equals(tagName))
                                            football.id = reader.nextString();
                                        else if ("date".equals(tagName))
                                            football.gameDate = reader.nextString();
                                        else if ("num".equals(tagName))
                                            football.gameNumber = reader.nextString();
                                        else if ("time".equals(tagName))
                                            football.beginTime = reader.nextString();
                                        else if ("l_cn_abbr".equals(tagName))
                                            football.matchName = reader.nextString();
                                        else if ("h_cn_abbr".equals(tagName))
                                            football.hostTeam = reader.nextString();
                                        else if ("a_cn_abbr".equals(tagName))
                                            football.clientTeam = reader.nextString();
                                        else if ("l_background_color".equals(tagName)) {
                                            football.backgroundColor = reader.nextString();
                                        } else if ("weather".equals(tagName))
                                            football.weather = reader.nextString();
                                        else if ("hhad".equals(tagName)) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String childTagName = reader.nextName();
                                                if ("h".equals(childTagName))
                                                    football.oddsAssignHost = Float.parseFloat(reader.nextString());
                                                else if ("a".equals(childTagName))
                                                    football.oddsAssignClient = Float.parseFloat(reader.nextString());
                                                else if ("d".equals(childTagName))
                                                    football.oddsAssignDraw = Float.parseFloat(reader.nextString());
                                                else if ("fixedodds".equals(childTagName)) {
                                                    football.assignor = reader.nextString();
                                                } else
                                                    System.out.println(reader.nextString());
                                            }
                                            reader.endObject();
                                        } else if ("had".equals(tagName)) {
                                            reader.beginObject();
                                            while (reader.hasNext()) {
                                                String childTagName = reader.nextName();
                                                if ("h".equals(childTagName))
                                                    football.oddsHost = Float.parseFloat(reader.nextString());
                                                else if ("a".equals(childTagName))
                                                    football.oddsClient = Float.parseFloat(reader.nextString());
                                                else if ("d".equals(childTagName))
                                                    football.oddsDraw = Float.parseFloat(reader.nextString());
                                                else
                                                    System.out.println(reader.nextString());
                                            }
                                            reader.endObject();
                                        } else if ("match_info".equals(tagName)) {
                                            reader.beginArray();
                                            if (reader.hasNext()) {
                                                reader.beginObject();
                                                while (reader.hasNext()) {
                                                    String childTagName = reader.nextName();
                                                    System.out.println("childTag:" + childTagName + ",childValue:" + reader.nextString());
                                                }
                                                reader.endObject();
                                            }
                                            reader.endArray();
                                        } else {
                                            System.out.println(reader.nextString());
                                        }
                                    }
                                }
                            }
                            reader.endObject();
                            arrayList.add(football);
                        }
                        reader.endObject();
                    }
                    reader.endObject();

                    //TO do
                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                lvSfp.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });

        EventBus.getDefault().register(this);
    }

    public void onEventMainThread(String json) {// 接收事件内容地方（参数类型可以自己根据实际情况定义）
        if ("sfpselected".equals(json)) {
            if (null != adapter) {
                this.txSpfTip.setText("已选中" + adapter.selectedItems.size() + "场比赛");
            }
        }
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

    Button.OnClickListener submitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == adapter || null == adapter.selectedItems) {
                return;
            }

            if (adapter.selectedItems.size() < 2) {
                showDialog(ActivityFootBallSfp.this);
                return;
            }

            if (adapter.selectedItems.size() > 8) {
                showDialog2(ActivityFootBallSfp.this);
                return;
            }

            Intent intent = new Intent();
            ArrayList<FootballGameInfo> games = new ArrayList<FootballGameInfo>();
            Iterator iter = adapter.selectedItems.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<Integer, FootballGameInfo> entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                games.add((FootballGameInfo) val);
            }
            intent.putExtra("games", games);
            intent.setClass(ActivityFootBallSfp.this, ActivityFootBallSfpResult.class);
            ActivityFootBallSfp.this.startActivity(intent);
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
//                assistTool.gotoActivity(ActivityFootballHh.class, true);
                //  TODO 新的混合投注界面
                assistTool.gotoActivity(ActivityFootBallMixActivity.class, true);
                return false;
            }
        });

        MenuItem menuItem = subMenu.getItem();
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }
}