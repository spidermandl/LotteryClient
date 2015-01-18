package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import com.evstudio.thefirstlottery.mobile.R;
import com.tandong.sa.tools.AssistTool;

/**
 * Created by eric on 15/1/4.
 */
public class ActivityHistoryFootball extends SherlockActivity {

    private ActionBar mActionBar;
    private AssistTool mAssistTools;
    private TextView tvTip;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_history_football);

        mActionBar = getSupportActionBar();

        mAssistTools = new AssistTool(this);

        tvTip = (TextView)findViewById(R.id.tvHistoryFootballTip);

        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("购票历史（竞彩足球）");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable(){

            public void run() {
                //execute the task
                setBlank();
                progressDialog.dismiss();
            }
        }, 3000);
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
        SubMenu subMenu = menu.addSubMenu("购票历史");

        subMenu.add("上海11选5").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAssistTools.gotoActivity(BuyHistoryActivity.class, true);
                return false;
            }
        });
        subMenu.add("大乐透").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAssistTools.gotoActivity(ActivityHistoryDlt.class, true);
                return false;
            }
        });
        subMenu.add("竞彩足球").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAssistTools.gotoActivity(ActivityHistoryFootball.class, true);
                return false;
            }
        });
        subMenu.add("竞彩篮球").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAssistTools.gotoActivity(ActivityHistoryBasketball.class, true);
                return false;
            }
        });

        MenuItem menuItem = subMenu.getItem();
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS|MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return super.onCreateOptionsMenu(menu);
    }

    private void setBlank(){
        tvTip.setText("暂无数据");
    }
}