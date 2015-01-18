package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.AdapterListViewSfpResult;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.pojo.FootballGameInfo;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.json.Gson;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 14/11/10.
 */
public class ActivityFootBallSfpResult extends SherlockActivity {
    private ActionBar actionBar;

    private ListView listView;
    private AdapterListViewSfpResult adapter;

    private ArrayList<FootballGameInfo> arrayList = new ArrayList<FootballGameInfo>();

    private Button btnXz;
    private Button btnQd;

    private TextView tvFtSfpWf;
    private TextView tvFtSfpTip;

    private EditText etSfpBs;

    private ProgressDialog progressDialog;

    private List<Integer> listPlay = new ArrayList<Integer>();

    private AssistTool assistTool;

    private float maxPay = 1;

    private int piaoshu = 1;

    private DecimalFormat moneyDf = new DecimalFormat("####.00");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_football_sfp_result);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        assistTool = new AssistTool(this);

        listView = (ListView) findViewById(R.id.lvFtSfpResult);
        btnXz = (Button) findViewById(R.id.btnFtSfpResult);
        btnQd = (Button) findViewById(R.id.btnFtSfpResultSubmit);

        tvFtSfpWf = (TextView) findViewById(R.id.tvFtSfpResultWf);
        tvFtSfpTip = (TextView) findViewById(R.id.tvFtSfpResultTip);

        etSfpBs = (EditText) findViewById(R.id.etFtSfpBs);
        etSfpBs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != s && !"".equals(String.valueOf(s)))
                    setTip(Integer.parseInt(String.valueOf(s)));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //得到传过来的Intent对象
        Intent intent = getIntent();
        //获得Intent中的Extra
        arrayList = (ArrayList<FootballGameInfo>) intent.getSerializableExtra("games");

        adapter = new AdapterListViewSfpResult(this, arrayList, null);
        listView.setAdapter(adapter);

        listPlay.add(0);

        for (int i = 0; i < arrayList.size(); i++) {
            FootballGameInfo ginfo = (FootballGameInfo) arrayList.get(i);
            maxPay *= ginfo.getMaxPl();
        }
        maxPay *= 2;


        tvFtSfpWf.setText("玩法:" + arrayList.size() + "串1");
        setTip(Integer.parseInt(String.valueOf(etSfpBs.getText())));

        btnXz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = new String[arrayList.size() - 1];
                boolean[] selected = new boolean[arrayList.size() - 1];
                int j = 0;
                for (int i = arrayList.size(); i > 1; i--) {
                    items[j] = String.valueOf(i) + "串1";
                    selected[j] = false;
                    j++;
                }
                for (int i = 0; i < listPlay.size(); i++) {
                    int k = listPlay.get(i).intValue();
                    selected[k] = true;
                }

                new AlertDialog.Builder(ActivityFootBallSfpResult.this).setCancelable(true)
                        .setTitle("选择玩法")
                        .setMultiChoiceItems(items, selected, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    listPlay.add(which);
                                } else {
                                    for (int i = 0; i < listPlay.size(); i++) {
                                        int k = listPlay.get(i).intValue();
                                        if (k == which) {
                                            listPlay.remove(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        })
                        .setPositiveButton("确认",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialoginterface, int i) {
                                        maxPay = 0;
                                        int ticks = 0;
                                        List<String> listMax = new ArrayList<String>();
                                        for (int k = 0; k < arrayList.size(); k++) {
                                            FootballGameInfo ginfo = arrayList.get(k);
                                            listMax.add(String.valueOf(ginfo.getMaxPl()));
                                        }
                                        for (int k = 0; k < listPlay.size(); k++) {
                                            int j = listPlay.get(k);
                                            j = arrayList.size() - j;
                                            List<String> listPl = combine(
                                                    (String[]) listMax.toArray(new String[listMax.size()]),
                                                    j);
                                            for (int m = 0; m < listPl.size(); m++) {
                                                float fTemp = 1;
                                                String[] strPlTemp = listPl.get(m).split(" ");
                                                for (int n = 0; n < strPlTemp.length; n++)
                                                    fTemp *= Float.parseFloat(strPlTemp[n]);
                                                maxPay += fTemp * 2;
                                                ticks++;
                                            }
                                        }
                                        setTip(ticks, Integer.parseInt(String.valueOf(etSfpBs.getText())));
                                        setWf();
                                        dialoginterface.dismiss();
                                    }
                                })
                        .show();//显示对话框
            }
        });

        btnQd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!assistTool.getPreferenceBoolean("isLogon")) {
                    MyToast.instance.showToast(ActivityFootBallSfpResult.this,
                            LayoutInflater.from(ActivityFootBallSfpResult.this), "您尚未登录，请先登录！");
                    assistTool.gotoActivity(LoginActivity.class, false);
                    return;
                }

                if (null != listPlay && listPlay.size() == 0) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootBallSfpResult.this).setTitle("提示")
                            .setMessage("请先选择玩法！")
                            .create();
                    alertDialog.show();
                    return;
                }

                int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                if (cash < 2 * Integer.parseInt(String.valueOf(etSfpBs.getText())) * piaoshu) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootBallSfpResult.this).setTitle("提示")
                            .setMessage("您的余额不足，当前余额为" + String.valueOf(cash) + "，请充值！")
                            .create();
                    alertDialog.show();
                    return;
                }

                progressDialog = new ProgressDialog(ActivityFootBallSfpResult.this);
                progressDialog.setTitle("正在提交");
                progressDialog.setMessage("正在提交购买彩票，请稍候......");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();
                Gson gson = new Gson();
                String ftinfo = gson.toJson(arrayList);
                String playType = gson.toJson(listPlay);

                RequestParams requestParams = new RequestParams();
                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
                requestParams.put("bettype", playType);
                requestParams.put("betcontent", ftinfo);
                requestParams.put("betcount", etSfpBs.getText());
                requestParams.put("periods", "51");

                HttpRestClient.post("mobile/doBet", requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        progressDialog.dismiss();
                        int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                        cash = cash - 2 * Integer.parseInt(String.valueOf(etSfpBs.getText())) * piaoshu;
                        assistTool.savePreferenceString("cash", String.valueOf(cash));

                        MyToast.instance.showToast(ActivityFootBallSfpResult.this,
                                LayoutInflater.from(ActivityFootBallSfpResult.this), "投注成功，感谢您的投注！");

                        EventBus.getDefault().post("betSuccess");

                        super.onSuccess(statusCode, headers, response);

                        ActivityFootBallSfpResult.this.finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressDialog.dismiss();
                        AlertDialog alertDialog = new AlertDialog.Builder(ActivityFootBallSfpResult.this).setTitle("提示")
                                .setMessage("投注失败，请检查您的网络！")
                                .create();
                        alertDialog.show();
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });

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

    public void setTip(int beishu) {
        tvFtSfpTip.setText("投注金额:" + String.valueOf(2 * this.piaoshu * beishu)
                + "元,最高奖金:" + moneyDf.format(maxPay * beishu) + "元");
    }

    public void setTip(int _piaoshu, int beishu) {
        this.piaoshu = _piaoshu;
        tvFtSfpTip.setText("投注金额:" + String.valueOf(2 * _piaoshu * beishu)
                + "元,最高奖金:" + moneyDf.format(maxPay * beishu) + "元");
    }

    public void setWf() {
        String strWf = "玩法:";
        for (int i = 0; i < listPlay.size(); i++) {
            strWf += String.valueOf(arrayList.size() - listPlay.get(i)) + "串1 ";
        }
        tvFtSfpWf.setText(strWf);
    }


    private List<String> combine(String[] a, int num) {
        List<String> list = new ArrayList<String>();
        StringBuffer sb = new StringBuffer();
        String[] b = new String[a.length];
        for (int i = 0; i < b.length; i++) {
            if (i < num) {
                b[i] = "1";
            } else
                b[i] = "0";
        }

        int point = 0;
        int nextPoint = 0;
        int count = 0;
        int sum = 0;
        String temp = "1";
        while (true) {
            // 判断是否全部移位完毕
            for (int i = b.length - 1; i >= b.length - num; i--) {
                if (b[i].equals("1"))
                    sum += 1;
            }
            // 根据移位生成数据
            for (int i = 0; i < b.length; i++) {
                if (b[i].equals("1")) {
                    point = i;
                    sb.append(a[point]);
                    sb.append(" ");
                    count++;
                    if (count == num)
                        break;
                }
            }
            // 往返回值列表添加数据
            list.add(sb.toString());

            // 当数组的最后num位全部为1 退出
            if (sum == num) {
                break;
            }
            sum = 0;

            // 修改从左往右第一个10变成01
            for (int i = 0; i < b.length - 1; i++) {
                if (b[i].equals("1") && b[i + 1].equals("0")) {
                    point = i;
                    nextPoint = i + 1;
                    b[point] = "0";
                    b[nextPoint] = "1";
                    break;
                }
            }
            // 将 i-point个元素的1往前移动 0往后移动
            for (int i = 0; i < point - 1; i++)
                for (int j = i; j < point - 1; j++) {
                    if (b[i].equals("0")) {
                        temp = b[i];
                        b[i] = b[j + 1];
                        b[j + 1] = temp;
                    }
                }
            // 清空 StringBuffer
            sb.setLength(0);
            count = 0;
        }
        //
        System.out.println("数据长度 " + list.size());
        return list;
    }

}