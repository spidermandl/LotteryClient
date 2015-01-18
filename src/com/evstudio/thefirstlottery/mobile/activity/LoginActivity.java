package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;
import com.tandong.sa.verifi.Form;
import com.tandong.sa.verifi.NotEmptyVerifior;
import com.tandong.sa.verifi.PhoneVerifior;
import com.tandong.sa.verifi.Verifi;

import org.apache.http.Header;
import org.json.JSONObject;

public class LoginActivity extends SherlockActivity implements View.OnClickListener {

    public static LoginActivity activity;
    private AssistTool assistTool;
    private ActionBar actionBar;
    private Form form;
    private RequestParams requestParams;
    private Bundle parentBundle;

    private BootstrapButton bbbtnLogin;
    private BootstrapButton bbbtnRegister;
    private EditText etPhone;
    private EditText etPasswd;

    private ProgressDialog progressDialog;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        activity = this;
        parentBundle = savedInstanceState;
        actionBar = getSupportActionBar();
        actionBar.setTitle("用户登录");

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        assistTool = new AssistTool(this);

        bbbtnLogin = (BootstrapButton) findViewById(R.id.btnLoginSubmit);
        bbbtnRegister = (BootstrapButton) findViewById(R.id.btnLoginRegister);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etPasswd = (EditText) findViewById(R.id.etPassword);

        form = new Form();

        Verifi etPhone_noEmpty = new Verifi(etPhone);
        etPhone_noEmpty.addValidator(new NotEmptyVerifior(this));
        etPhone_noEmpty.addValidator(new PhoneVerifior(this));
        Verifi etPassword_noEmpty = new Verifi(etPasswd);
        etPassword_noEmpty.addValidator(new NotEmptyVerifior(this));

        form.addValidates(etPhone_noEmpty);
        form.addValidates(etPassword_noEmpty);

        bbbtnLogin.setOnClickListener(this);
        bbbtnRegister.setOnClickListener(this);

        if( assistTool.getPreferenceBoolean("isRegistered")){
            etPhone.setText( assistTool.getPreferenceString("mobile") );
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(this.getLocalClassName(), "Get It" + String.valueOf(item.getItemId()));
                LoginActivity.this.setResult(RESULT_CANCELED);
                this.finish();
                break;
            default:
                Log.d(this.getLocalClassName(), String.valueOf(item.getItemId()));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginSubmit:
                if (form.validate()) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("正在登录");
                    progressDialog.setMessage("正在进行用户验证，请稍候......");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    requestParams = new RequestParams();
                    requestParams.put("mobile", etPhone.getText().toString());
                    requestParams.put("passwd", etPasswd.getText().toString());

                    HttpRestClient.post("mobile/login", requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(this.getClass().getName(), response.toString());
                            MessageProcess.function.processReturn(response);
                            Log.d(this.getClass().getName(), "" + MessageProcess.function.getReturnCode());
                            progressDialog.dismiss();
                            if (MessageProcess.function.isSuccess) {
                                assistTool.savePreferenceString("userid", MessageProcess.function.getAttr("userid"));
                                assistTool.savePreferenceString("mobile",etPhone.getText().toString() );
                                assistTool.savePreferenceString("cash", MessageProcess.function.getAttr("cash"));
                                assistTool.savePreferenceString("award", MessageProcess.function.getAttr("award"));

                                assistTool.savePreferenceBoolean("isLogon", true);
                                assistTool.savePreferenceBoolean("isRegistered", true);

                                Bundle bundle = new Bundle();
                                bundle.putBoolean("isLogon",true);
                                LoginActivity.this.setResult(RESULT_OK, LoginActivity.this.getIntent().putExtras(bundle));
                                EventBus.getDefault().post("logined");
                                LoginActivity.this.finish();
                            } else {
                                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).setTitle("登录失败")
                                        .setMessage(MessageProcess.function.getAttr("returnmsg"))
                                        .create();
                                alertDialog.show();
                            }
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            Log.d(this.getClass().getName(), responseString);
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Log.d(this.getClass().getName(), errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                break;
            case R.id.btnLoginRegister:
                assistTool.gotoActivity(RegisterActivity.class, false, parentBundle);
                break;
        }
    }
}
