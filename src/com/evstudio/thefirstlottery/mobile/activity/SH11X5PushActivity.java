package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.SH11X5PushAdapter;
import com.evstudio.thefirstlottery.mobile.common.Constants;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.pojo.SH11X5PushBean;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;

import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 上海11选5正式下注
 * Created by zyn on 15/1/12.
 */
public class SH11X5PushActivity extends SherlockActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    /**
     * 导航栏
     */
    private TextView tvSelect11X5;
    private RelativeLayout rlBack;
    private Button btnGoHistory;

    /**
     * listview
     */
    private ListView dalListView;
    private SH11X5PushAdapter adapter;
    List<SH11X5PushBean> dataList = new ArrayList<SH11X5PushBean>();

    /**
     * 投注
     */
    private TextView tvPush;

    /** */
    private ProgressDialog progressDialog;
    private int combination;
    private AssistTool assistTool;

    private TextView tvAddMine;
    private TextView tvAddRandom;
    private EditText etMultiple;

    private TextView tvYiZhu;
    private TextView tvJinEr;

    private TextView tvYuEr;

    private int iZhu;
    private int iJinEr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh11x5_push_layout);
        initializeView();
    }

    private void initializeView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        assistTool = new AssistTool(this);

        tvYiZhu = (TextView) findViewById(R.id.tvYiZhu);
        tvJinEr = (TextView) findViewById(R.id.tvJinEr);
        tvYuEr = (TextView) findViewById(R.id.tvYuEr);

        tvYuEr.setText(assistTool.getPreferenceString("cash") + "元");

        tvSelect11X5 = (TextView) findViewById(R.id.tvSelect11X5);
        tvAddMine = (TextView) findViewById(R.id.tvAddMine);
        tvAddMine.setOnClickListener(this);

        tvAddRandom = (TextView) findViewById(R.id.tvAddRandom);
        tvAddRandom.setOnClickListener(this);

        tvSelect11X5.setVisibility(View.GONE);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        btnGoHistory = (Button) findViewById(R.id.btnGoHistory);
        btnGoHistory.setVisibility(View.GONE);

        tvPush = (TextView) findViewById(R.id.tvPush);
        tvPush.setOnClickListener(this);
        etMultiple = (EditText) findViewById(R.id.etMultiple);

        dalListView = (ListView) findViewById(R.id.dalListView);
        dalListView.setOnItemClickListener(this);

        dataList = Constants.pushBeanList;

        adapter = new SH11X5PushAdapter(SH11X5PushActivity.this, dataList);
        dalListView.setAdapter(adapter);

        iZhu = 0;
        iJinEr = 0;

        for (int i = 0; i < dataList.size(); i++) {
            iZhu += dataList.get(i).getZhushu();
            iJinEr += dataList.get(i).getMoney();
        }

        tvJinEr.setText(String.valueOf(iJinEr) + "元");
        tvYiZhu.setText(String.valueOf(iZhu) + "注");

        etMultiple.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != s && !"".equals(String.valueOf(s))){
                    int i = Integer.parseInt(s.toString());
                    tvJinEr.setText(String.valueOf(iJinEr * i) + "元");
                    tvYiZhu.setText(String.valueOf(iZhu) + "注");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlBack:
                finish();
                break;

            case R.id.tvAddMine:
                finish();
                break;

            case R.id.tvAddRandom:
                break;
            case R.id.tvPush:
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
                if (cash < iJinEr * beishu) {
                    AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
                            .setMessage("您的余额不足，无法投注！\r\n您的投注金额为：" +
                                    String.valueOf(iJinEr) +
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
                postContent = gson.toJson(dataList);
                RequestParams requestParams = new RequestParams();
                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
                requestParams.put("periods", Sh11x5Next.nextPeriods);
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
                        cash = cash - iJinEr;
                        assistTool.savePreferenceString("cash", String.valueOf(cash));

                        MyToast.instance.showToast(SH11X5PushActivity.this, getLayoutInflater(), "投注成功，感谢您的投注！");
                        EventBus.getDefault().post("sh11x5betted");
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(SH11X5PushActivity.this).setTitle("提示")
                                .setMessage("投注失败，请检查您的网络！")
                                .create();
                        alertDialog.show();
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
//                            }
//                        })
//                        .setNegativeButton("否", null)
//                        .show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        // TODO 购彩历史界面，先从这里跳转进去做测试
//        Intent push = new Intent(SH11X5PushActivity.this,SH11X5HistoryActivity.class);
//        startActivity(push);
    }
}
