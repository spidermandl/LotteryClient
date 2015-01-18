package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.Util;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tag.helper.StringUtil;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by zhoujun on 15/1/12.
 */
public class PersonalInfoActivity extends SherlockActivity {
    private ActionBar actionBar;
    private BootstrapButton btnLoginSubmit;
    private EditText etEmail,etName,etCard,etBankName,etBankNo;
    private AssistTool assistTool;
    private ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assistTool = new AssistTool(this);
        setContentView(R.layout.activity_personal_info);
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        initView();
        getInfoTask();
    }
    private void initView(){
        btnLoginSubmit = (BootstrapButton) findViewById(R.id.btnLoginSubmit);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etName = (EditText) findViewById(R.id.etName);
        etCard = (EditText) findViewById(R.id.etCard);
        etBankName = (EditText) findViewById(R.id.etBankName);
        etBankNo = (EditText) findViewById(R.id.etBankNo);
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });
    }
    private void saveInfo(){
        String email = etEmail.getText().toString();
        if(!Util.isEmail(email)){
            assistTool.showToast("请输入邮箱");
            return;
        }
        String name = etName.getText().toString();
        if(TextUtils.isEmpty(name)){
            assistTool.showToast("请输入真实姓名");
            return;
        }
        String card = etCard.getText().toString();
        if(TextUtils.isEmpty(card)){
            assistTool.showToast("请输入身份证号码");
            return;
        }
        if(card.length() != 18){
            assistTool.showToast("请输入合法身份证号码");
            return;
        }
        String bankName = etBankName.getText().toString();
        if(TextUtils.isEmpty(bankName)){
            assistTool.showToast("请输入银行名称");
            return;
        }
        String bankNo = etBankNo.getText().toString();
        if(TextUtils.isEmpty(bankNo)){
            assistTool.showToast("请输入银行卡号");
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在提交，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("userId",assistTool.getPreferenceString("userid"));
        params.add("email",email);
        params.add("userName",name);
        params.add("idCard",card);
        params.add("bankType",bankName);
        params.add("bankCode",bankNo);
        HttpRestClient.post("clientUser/saveUser",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                MessageProcess.function.processReturn(response);
                if(MessageProcess.function.isSuccess){
                    assistTool.showToast("保存成功");
                    finish();
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(PersonalInfoActivity.this).setTitle("保存失败")
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
    private void getInfoTask(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.add("uid",assistTool.getPreferenceString("userid"));
        HttpRestClient.get("clientUser/userInfo",params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                MessageProcess.function.processReturn(response);
                if(MessageProcess.function.isSuccess){
                    etEmail.setText(MessageProcess.function.getAttr("email"));
                    etName.setText(MessageProcess.function.getAttr("userName"));
                    etCard.setText(MessageProcess.function.getAttr("idCard"));
                    etBankName.setText(MessageProcess.function.getAttr("bankType"));
                    etBankNo.setText(MessageProcess.function.getAttr("bankCode"));
                }else {
                    AlertDialog alertDialog = new AlertDialog.Builder(PersonalInfoActivity.this).setTitle("获取信息失败")
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