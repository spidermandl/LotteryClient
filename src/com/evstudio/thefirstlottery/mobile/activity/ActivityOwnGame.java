package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.tandong.sa.tools.AssistTool;

import br.com.dina.ui.widget.UIButton;
import br.com.dina.ui.widget.UITableView;
import cn.hugo.android.scanner.CaptureActivity;

/**
 * Created by eric on 14/11/20.
 */
public class ActivityOwnGame extends SherlockActivity {
    private ActionBar actionBar;
    private UIButton uiButton;
    private AssistTool assistTool;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_owngame);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        assistTool = new AssistTool(this);

        uiButton = (UIButton) findViewById(R.id.btnOwnGame1);
        uiButton.addClickListener(new UIButton.ClickListener() {
            @Override
            public void onClick(View view) {
                assistTool.gotoActivity(CaptureActivity.class, false, bundle);
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
}