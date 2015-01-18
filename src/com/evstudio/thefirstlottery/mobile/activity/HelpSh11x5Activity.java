package com.evstudio.thefirstlottery.mobile.activity;

import android.os.Bundle;
import android.webkit.WebView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;

/**
 * Created by ericren on 14-9-10.
 */
public class HelpSh11x5Activity extends SherlockActivity {

    private WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_help_sh11x5);
        webView = (WebView)findViewById(R.id.sh11x5_help);

        webView.loadUrl("///file://assests/html/sh11x5-wf.html");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("上海十一选五玩法");
        getSupportActionBar().setIcon(R.drawable.abs__ic_ab_back_holo_light);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}