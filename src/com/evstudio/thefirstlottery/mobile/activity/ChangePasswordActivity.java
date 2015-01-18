package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by zhoujun on 15/1/12.
 */
public class ChangePasswordActivity extends SherlockActivity {
    private ActionBar actionBar;
    private BootstrapButton btnLoginSubmit;
    private EditText etOldPassword,etNewPassword,etConfirmPassword;
    private AssistTool assistTool;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assistTool = new AssistTool(this);
        setContentView(R.layout.activity_change_password);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        initView();
    }
    private void initView(){
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btnLoginSubmit = (BootstrapButton) findViewById(R.id.btnLoginSubmit);
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }
    private void changePassword(){
        String oldPassword = etOldPassword.getText().toString();
        if(TextUtils.isEmpty(oldPassword)){
            assistTool.showToast("请输入旧密码");
            return;
        }
        String newPassword = etNewPassword.getText().toString();
        if(TextUtils.isEmpty(newPassword)){
            assistTool.showToast("请输入新密码");
            return;
        }
        String confirmPassword = etConfirmPassword.getText().toString();
        if(!newPassword.equals(confirmPassword)){
            assistTool.showToast("两次输入的密码不一致");
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("保存中，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("userId",assistTool.getPreferenceString("userid"));
        params.add("oldPassword",oldPassword);
        params.add("newPassword",newPassword);
        HttpRestClient.post("clientUser/modifyPassword",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                MessageProcess.function.processReturn(response);
                if(MessageProcess.function.isSuccess){
                    assistTool.showToast("修改密码成功");
                    finish();
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this).setTitle("修改失败")
                            .setMessage(MessageProcess.function.getReturnMsg())
                            .create();
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressDialog.dismiss();
                assistTool.showToast("连接服务器失败");
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}