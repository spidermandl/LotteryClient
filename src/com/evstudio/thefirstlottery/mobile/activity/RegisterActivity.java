package com.evstudio.thefirstlottery.mobile.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.Util;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;
import com.tandong.sa.verifi.ConfirmVerifi;
import com.tandong.sa.verifi.Form;
import com.tandong.sa.verifi.NotEmptyVerifior;
import com.tandong.sa.verifi.PhoneVerifior;
import com.tandong.sa.verifi.Verifi;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by ericren on 14-9-15.
 */
public class RegisterActivity extends SherlockActivity implements View.OnClickListener {
    private ActionBar actionBar;

    private EditText etPhone;
    private EditText etPassword;
    private EditText etCfPassword;
    private BootstrapButton btnRegister;
    private Form form;

    private ProgressDialog progressDialog;
    private Bundle parentBundle;
    private RequestParams requestParams;


    private AssistTool assistTool;

    private TextView tvVerificationCode;
    private int tick = 30;
    /**
     * 手机号码输入格式
     */
    public static final String RegxPhoneNumberInput = "^1[3-8][0-9]{9}$";
    /**
     * 重新获取验证码倒数
     */
    public static final int GetValidationTicks = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentBundle = savedInstanceState;

        setContentView(R.layout.layout_register);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        etPhone = (EditText) findViewById(R.id.etRegisterPhone);
        etPassword = (EditText) findViewById(R.id.etRegisterPassword);
        etCfPassword = (EditText) findViewById(R.id.etRegisterCfPassword);
        btnRegister = (BootstrapButton) findViewById(R.id.btnRegisterSubmit);

        tvVerificationCode = (TextView) findViewById(R.id.tvVerificationCode);
        tvVerificationCode.setOnClickListener(this);

        assistTool = new AssistTool(this);
        form = new Form();

        Verifi etPhone_noEmpty = new Verifi(etPhone);
        etPhone_noEmpty.addValidator(new NotEmptyVerifior(this));
        etPhone_noEmpty.addValidator(new PhoneVerifior(this));
        Verifi etPassword_noEmpty = new Verifi(etPassword);
        etPassword_noEmpty.addValidator(new NotEmptyVerifior(this));
        ConfirmVerifi confirmFields = new ConfirmVerifi(etPassword,
                etCfPassword);

        form.addValidates(etPhone_noEmpty);
        form.addValidates(etPassword_noEmpty);
        form.addValidates(confirmFields);

        btnRegister.setOnClickListener(this);

        tvVerificationCode.setOnClickListener(this);
        tvVerificationCode.setClickable(false);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phoneNumber = etPhone.getText().toString().trim();
                if (tick <= 0 || tick == GetValidationTicks) {
                    if (!Pattern.matches(RegxPhoneNumberInput, phoneNumber)) {
                        tvVerificationCode.setSelected(false);
                        tvVerificationCode.setClickable(false);
                        tvVerificationCode.setText(R.string.get_verification_code);
                    } else {
                        tvVerificationCode.setClickable(true);
                        tvVerificationCode.setSelected(true);
                    }
                }
            }
        });
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvVerificationCode:
                String phoneNumber = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    showToast("请输入手机号");
                    return;
                }
                if (!Util.checkNumber(phoneNumber)) {
                    showToast("请输入正确的手机号");
                    return;
                }
                getVerification(phoneNumber);

                timer.start();
                break;
            case R.id.btnRegisterSubmit:
                if (form.validate()) {
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setTitle("注册新用户");
                    progressDialog.setMessage("正在进行新用户注册，请稍候......");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    requestParams = new RequestParams();
                    requestParams.put("mobile", etPhone.getText().toString());
                    requestParams.put("passwd", etPassword.getText().toString());

                    HttpRestClient.post("mobile/register", requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d(this.getClass().getName(), response.toString());
                            MessageProcess.function.processReturn(response);
                            Log.d(this.getClass().getName(), "" + MessageProcess.function.getReturnCode());
                            if (MessageProcess.function.isSuccess) {
                                progressDialog.dismiss();
                                assistTool.savePreferenceString("userid", MessageProcess.function.getAttr("userid"));
                                assistTool.savePreferenceString("mobile", etPhone.getText().toString());
                                assistTool.savePreferenceBoolean("isLogon", true);
                                assistTool.savePreferenceBoolean("isRegistered", true);
                                assistTool.gotoActivity(MainActivity.class, true);
                            }
                            EventBus.getDefault().post("logined");
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            Log.d(this.getClass().getName(), responseString);
                            System.out.println("onFailure String responseString");
                            super.onFailure(statusCode, headers, responseString, throwable);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Log.d(this.getClass().getName(), errorResponse.toString());
                            System.out.println("onFailure JSONObject errorResponse");
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getVerification(String phone) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("tel_phone", phone);
        HttpRestClient.post("http://shop.jiayidian.net/api/v1/u/user/create_user", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = actionBar.getNavigationItemCount();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 验证码倒计时
     */
    private CountDownTimer timer = new CountDownTimer(tick * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvVerificationCode.setSelected(false);
            tvVerificationCode.setClickable(false);
            tvVerificationCode.setText((millisUntilFinished / 1000) + "秒后可重发");
            tick = (int) (millisUntilFinished / 1000);
        }

        @Override
        public void onFinish() {
            tvVerificationCode.setSelected(true);
            tvVerificationCode.setText("重新获取验证码");
            tvVerificationCode.setClickable(true);
        }
    };
}