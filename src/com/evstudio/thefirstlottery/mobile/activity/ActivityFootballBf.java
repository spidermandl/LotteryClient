package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.tandong.sa.tools.AssistTool;

/**
 * Created by eric on 14/11/20.
 */
public class ActivityFootballBf extends SherlockActivity {

    private ActionBar actionBar;
    private ProgressDialog progressDialog;
    private AssistTool assistTool;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_bf);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        actionBar.setIcon( R.drawable.app_icon_new_04 );
        actionBar.setTitle("竞彩足球（比分）");

        assistTool = new AssistTool(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
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
        menu.add("胜负平/让球胜负平").setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity( ActivityFootBallSfp.class, true);
                return false;
            }
        });
        menu.add("任9/胜负彩").setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity( ActivityFootballToto.class, true);
                return false;
            }
        });
        menu.add("比分").setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity( ActivityFootballBf.class, true);
                return false;
            }
        });
        menu.add("混合投注").setOnMenuItemClickListener( new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                assistTool.gotoActivity( ActivityFootballHh.class, true);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}