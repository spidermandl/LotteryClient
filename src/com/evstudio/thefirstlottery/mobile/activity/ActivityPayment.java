package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.alipay.android.msp.AlipayUtil;
import com.alipay.android.msp.Fiap;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.dina.ui.widget.UITableView;

/**
 * Created by eric on 14/11/19.
 */
public class ActivityPayment extends SherlockActivity {

    private ActionBar actionBar;
    private UITableView uiTableView;

    private AssistTool assistTool;
    private ProgressDialog progressDialog;
    private RequestParams requestParams;

    private String price = "0.01";

    private String orderId = ""; // 生成的订单号


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_payment);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        uiTableView = (UITableView) findViewById(R.id.uitvPaymentSelect);

        setTitle("充值");
        createList();
        uiTableView.commit();

        assistTool = new AssistTool(this);

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


    private void createList() {
        CustomClickListener listener = new CustomClickListener();
        uiTableView.setClickListener(listener);
        uiTableView.addBasicItem(R.drawable.pay_allipay, "支付宝", "方便快捷");
        uiTableView.addBasicItem(R.drawable.pay_allipay1, "银联", "银联支付");
    }

    private class CustomClickListener implements UITableView.ClickListener {
        @Override
        public void onClick(int index) {
            final EditText inputServer = new EditText(ActivityPayment.this);
            inputServer.setInputType(InputType.TYPE_CLASS_PHONE);
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityPayment.this);
            switch (index) {
                case 0:
                    builder.setTitle("请输入充值金额（支付宝）").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                            inputServer.getText().toString();
//                            ProgressDialog progressDialog = new ProgressDialog(ActivityPayment.this);
//                            progressDialog.setTitle("提交充值");
//                            progressDialog.setMessage("正在进行充值，您的充值金额为：" + inputServer.getText().toString() + "元");
//                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                            progressDialog.setCanceledOnTouchOutside(false);
//                            progressDialog.show();

                            if (inputServer.getText().toString().trim() == null || "".equals(inputServer.getText().toString().trim())){
                                Toast.makeText(ActivityPayment.this,"请输入金额",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            setPrice(inputServer.getText().toString().trim());
                            getAlipayOrder();
                        }
                    });
                    builder.show();
                    break;
                case 1:
                    builder.setTitle("请输入充值金额（银联）").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                            .setNegativeButton("取消", null);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            inputServer.getText().toString();
                            ProgressDialog progressDialog = new ProgressDialog(ActivityPayment.this);
                            progressDialog.setTitle("提交充值");
                            progressDialog.setMessage("正在进行充值，您的充值金额为：" + inputServer.getText().toString() + "元");
                            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
//                            Fiap fiap = new Fiap(ActivityPayment.this);
//                            fiap.android_pay(Double.parseDouble(inputServer.getText().toString()));
                        }
                    });
                    builder.show();
                    break;
                default:
                    break;
            }
//            Toast.makeText(UserInfoActivity.this, "item clicked: " + index, Toast.LENGTH_SHORT).show();
        }
    }

    private void getAlipayOrder(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在生成订单");
        progressDialog.setMessage("正在进行验证，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        requestParams = new RequestParams();
        requestParams.put("userid", assistTool.getPreferenceString("userid"));
        requestParams.put("price", price);
        requestParams.put("goodName", "第一彩票点数充值");

        HttpRestClient.post("pay/aliPayOrder", requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String result = response.getString("result");
                    if (result.equals("success")){

                        orderId = response.getString("orderId");
//                        Toast.makeText(ActivityPayment.this,"订单生成成功:"+orderId,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                       AlipayUtil.getInstance(ActivityPayment.this).setOrderInformation(orderId, price, "第一彩票", "第一彩票消费充值", "http://www.thefirstlottery.com/tmserver/pay/aliPay").toAlipay();

                    }else{
                        Toast.makeText(ActivityPayment.this,"订单生成失败",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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

//                    assistTool.gotoActivity( MainActivity.class, true );
    }

    public void setPrice(String price){
        this.price = price;
    }

}