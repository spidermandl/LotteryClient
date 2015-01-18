package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ProgressBar;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import org.apache.http.Header;

/**
 * Created by eric on 14/11/9.
 */
public class ActivityFootballWeb extends SherlockActivity {
    private WebView ftWebView;
    private ActionBar actionBar;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview_football);
        ftWebView = (WebView) findViewById(R.id.ftWebView);
        actionBar = getSupportActionBar();

        ftWebView.getSettings().setJavaScriptEnabled(true);
        ftWebView.getSettings().setSaveFormData(false);
        ftWebView.getSettings().setSupportZoom(false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HttpRestClient.getDirect("http://m.okooo.com/jingcai/", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String strResponse = new String(bytes, "GBK");
//                    ftWebView.loadDataWithBaseURL(null, strResponse, "text/html", "GBK", null);
                    ftWebView.getSettings().setAllowContentAccess(true);
                    ftWebView.loadUrl("file:///android_asset/html/football51.html");
                } catch (Exception e) {

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }
}