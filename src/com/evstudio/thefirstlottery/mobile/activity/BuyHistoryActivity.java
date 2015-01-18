package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.BuyHistoryAdapter;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by ericren on 14-9-9.
 */
public class BuyHistoryActivity extends SherlockActivity{
    private ActionBar actionBar;
    private AssistTool assistTool;
    private ListView listView;
    private BuyHistoryAdapter adapter;

//    private ArrayAdapter<String> adapter;

    private RequestParams requestParams;

//    private String[] strPlay  = {"上海11选5", "大乐透", "竞彩足球", "竞彩篮球"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_buyhistory);


        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setTitle("购票历史（上海11选5）");

        listView = (ListView) findViewById(R.id.listviewBuyHistory);
        adapter = new BuyHistoryAdapter(this, R.id.his_panel_content);
        listView.setAdapter(adapter);

//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

//        for (int i = 0; i < strPlay.length; i++) {
//            ActionBar.Tab tab = actionBar.newTab();
//            tab.setText(strPlay[i]);
//            tab.setTabListener(this);
//            actionBar.addTab(tab, i);
//        }

        assistTool = new AssistTool(this);
        if (!assistTool.getPreferenceBoolean("isLogon")) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
                    .setMessage("您尚未登录，请登录后查看！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        /**
                         * This method will be invoked when a button in the dialog is clicked.
                         *
                         * @param dialog The dialog that received the click.
                         * @param which  The button that was clicked (e.g.
                         *               {@link android.content.DialogInterface#BUTTON1}) or the position
                         */
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            assistTool.gotoActivity(LoginActivity.class, true);
                        }
                    })
                    .create();
            alertDialog.show();
            return;
        }

        adapter.notifyDataSetChanged();

        requestParams = new RequestParams();
        requestParams.put("userid", assistTool.getPreferenceString("userid"));
        requestParams.put("querytype", Sh11x5Next.historyTemp);

        HttpRestClient.post("mobile/userHistory", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Gson gson = new Gson();
                ArrayList<String[]> list = gson.fromJson(response.toString(),
                        new TypeToken<ArrayList<String[]>>() {
                        }.getType()
                );

                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++)
                        adapter.add(list.get(i));
                    adapter.notifyDataSetChanged();
                }
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
//        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu subMenu = menu.addSubMenu("购票历史");

        subMenu.add("上海11选5").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(BuyHistoryActivity.class, true);
                return false;
            }
        });
        subMenu.add("大乐透").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityHistoryDlt.class, true);
                return false;
            }
        });
        subMenu.add("竞彩足球").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityHistoryFootball.class, true);
                return false;
            }
        });
        subMenu.add("竞彩篮球").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity(ActivityHistoryBasketball.class, true);
                return false;
            }
        });

        MenuItem menuItem = subMenu.getItem();
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }
}