package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.os.Bundle;

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
public class ActivityBlank extends SherlockActivity {

    private ActionBar mActionBar;
    private AssistTool mAssistTools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_blank);

        mActionBar = getSupportActionBar();

        mAssistTools = new AssistTool(this);
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
}