package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.DltBetAdapter;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.pojo.DltBetBean;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-12.
 */
public class DltBetListActivity extends SherlockActivity implements View.OnClickListener {
    private ActionBar actionBar;

    private TextView tvManual, tvAuto, tvBalance, tvTouzhu, tvDltZhu, tvDltJinEr;
    private ListView dalListView;

    private DltBetAdapter adapter;

    private ArrayList<DltBetBean> dltBetList;
    private AssistTool assistTool;

    private EditText etMultiple;

    private CheckBox cbIsZhuijia;

    private int iMoney = 0;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dlt_bet_list);

        dltBetList = (ArrayList<DltBetBean>) getIntent().getExtras().getSerializable("list");

        initView();
        initData();

    }


    private void initView() {

        actionBar = getSupportActionBar();
        actionBar.setTitle("大乐透-投注列表");
        actionBar.setIcon(R.drawable.app_icon_new_03);

        actionBar.setDisplayHomeAsUpEnabled(true);
        tvManual = (TextView) findViewById(R.id.tvManual);
        tvManual.setOnClickListener(this);

        tvAuto = (TextView) findViewById(R.id.tvAuto);
        tvAuto.setOnClickListener(this);

        tvBalance = (TextView) findViewById(R.id.tvBalance);

        tvTouzhu = (TextView) findViewById(R.id.tvTouzhu);
        tvTouzhu.setOnClickListener(this);

        tvDltZhu = (TextView) findViewById(R.id.tvYiZhu);
        tvDltJinEr = (TextView) findViewById( R.id.tvDltJinEr );

        dalListView = (ListView) findViewById(R.id.dalListView);

        etMultiple = (EditText)findViewById(R.id.etMultiple);

        cbIsZhuijia = (CheckBox)findViewById(R.id.cbIsZhuijia);
        cbIsZhuijia.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( cbIsZhuijia.isSelected() ){
                    iMoney = iMoney / 2 *3;
                }else{
                    iMoney = iMoney / 3 *2;
                }
                tvDltJinEr.setText(String.valueOf(iMoney) + "元");
            }
        });
    }

    private void initData() {
        assistTool = new AssistTool(this);
        if (dltBetList != null) {
            adapter = new DltBetAdapter(dltBetList, this);
            dalListView.setAdapter(adapter);
        }
        tvBalance.setText("你当前余额:" + Integer.parseInt(assistTool.getPreferenceString("cash")));
        int iZhu = 0;
        int iMoney = 0;
        for( int i = 0; i < dltBetList.size(); i ++ ){
            iZhu += dltBetList.get(i).getiMoney() /2;
            iMoney += dltBetList.get(i).getiMoney();
        }
        tvDltZhu.setText( String.valueOf(iZhu ) + "注");
        tvDltJinEr.setText(String.valueOf(iMoney) + "元");


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvManual:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tvAuto:
                setResult(RESULT_OK);
                finish();
                break;
            case R.id.tvTouzhu:
// TODO 到时投注要传的参数请看SH11X5PushBean实体中的值 直接用dataList get出来实体然后用实体里的值
                if (!assistTool.getPreferenceBoolean("isLogon")) {
                    MyToast.instance.showToast(this,
                            LayoutInflater.from(this), "您尚未登录，请先登录！");
                    assistTool.gotoActivity(LoginActivity.class, false);
                    return;
                }
                int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                int beishu = Integer.parseInt(etMultiple.getText().toString());


//                combination = selectDataList.size();
//                int playnumber = selectNumber;
//                int finalnumber = 1;
////                for (int itmp = combination; itmp > playnumber; itmp--) {
////                    finalnumber *= itmp;
////                }
//                int needDiv = 1;
//                for( int ik =1; ik <= playnumber; ik ++ ){
//                    finalnumber *= combination;
//                    combination --;
//                    needDiv *= ik;
//                }
//
////                combination = combination * (combination - 1) * (combination - 2) / 6;
//                combination = finalnumber/needDiv;
//
                if (cash < iMoney * beishu) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
                            .setMessage("您的余额不足，无法投注！\r\n您的投注金额为：" +
                                    String.valueOf(iMoney) +
                                    "\r\n您当前余额为：" + String.valueOf(cash) + "元").create();
                    alertDialog.show();
                    return;
                }
//
//                new AlertDialog.Builder(contextView.getContext())
//                        .setTitle("确认您的投注")
//                        .setMessage("您的投注内容是：\r\n上海11选5第" + Sh11x5Next.nextPeriods + "期\r\n" +
//                                "玩法：" + thisTitle + "\r\n" +
//                                "投注内容：" + betContent + "\r\n" +
//                                "投注倍数:" + sh11x5BetEtBei.getText() + "\r\n" +
//                                "投注金额：" + String.valueOf(2 * combination * Integer.parseInt(sh11x5BetEtBei.getText().toString())) + "元" + "\r\n" +
//                                "您的当前余额为：" + assistTool.getPreferenceString("cash") + "元")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
                progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("投注");
                progressDialog.setMessage("正在投注，请稍候......");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
//
                String postContent = "";
                Gson gson = new Gson();
                postContent = gson.toJson(dltBetList);
                RequestParams requestParams = new RequestParams();
                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
                requestParams.put("periods", "");
                requestParams.put("iszhuijia", this.cbIsZhuijia.isChecked());
                requestParams.put("betcount", String.valueOf(beishu));
                requestParams.put("betcontent", postContent);
//                                requestParams.put("bettype", playCmd);
//                                requestParams.put("betcontent", betContent);
//                                requestParams.put("betcount", sh11x5BetEtBei.getText());
//                                requestParams.put("periods", Sh11x5Next.nextPeriods);
                HttpRestClient.post("mobile/doBet", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressDialog.dismiss();
                        int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                        cash = cash - iMoney;
                        assistTool.savePreferenceString("cash", String.valueOf(cash));

                        MyToast.instance.showToast(DltBetListActivity.this, getLayoutInflater(), "投注成功，感谢您的投注！");
                        EventBus.getDefault().post("sh11x5betted");
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(DltBetListActivity.this).setTitle("提示")
                                .setMessage("投注失败，请检查您的网络！")
                                .create();
                        alertDialog.show();
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
                break;
            default:
                break;
        }
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
}
