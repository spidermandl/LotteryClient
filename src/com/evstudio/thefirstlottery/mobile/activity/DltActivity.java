package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ericren on 14-11-14.
 */
public class DltActivity extends SherlockActivity {
    private ActionBar actionBar;
    private TextView[] redTv = new TextView[35];
    private TextView[] blueTv = new TextView[12];

    private ProgressDialog progressDialog;

    private BootstrapButton bbSubmit;
    private BootstrapButton bbRandom;

    private EditText etBeishu;

    private String betContent = "";

    private AssistTool assistTool;

    private int combination;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dlt);

        actionBar = getSupportActionBar();
        actionBar.setTitle("大乐透");
        actionBar.setIcon(R.drawable.app_icon_new_03);

        actionBar.setDisplayHomeAsUpEnabled(true);

        bbSubmit = (BootstrapButton) findViewById(R.id.dltBetBtnSubmit);
        bbRandom = (BootstrapButton) findViewById(R.id.dltRandom);
        bbRandom.setOnClickListener(randomClick);

        etBeishu = (EditText) findViewById(R.id.dltBetEtBei);

        assistTool = new AssistTool(this);

        bbSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> listRed = new ArrayList<Integer>();
                ArrayList<Integer> listBlue = new ArrayList<Integer>();
                for (int i = 0; i < 35; i++) {
                    if (redTv[i].isSelected()) {
                        listRed.add(Integer.parseInt(String.valueOf(redTv[i].getText())));
                    }
                }
                for (int i = 0; i < 12; i++) {
                    if (blueTv[i].isSelected()) {
                        listBlue.add(Integer.parseInt(String.valueOf(blueTv[i].getText())));
                    }
                }

                if (listBlue.size() < 2 || listRed.size() < 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(DltActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("选择不完整，请重新选择！");
                    builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    builder.show();
                    return;
                }

                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < listRed.size(); i++) {
                    if (listRed.get(i) < 10)
                        strBuff.append("0");
                    strBuff.append(listRed.get(i));
                    if (i < listRed.size() - 1)
                        strBuff.append(",");
                }

                strBuff.append("|");

                for (int i = 0; i < listBlue.size(); i++) {
                    if (listBlue.get(i) < 10)
                        strBuff.append("0");
                    strBuff.append(listBlue.get(i));
                    if (i < listBlue.size() - 1)
                        strBuff.append(",");
                }
                betContent = strBuff.toString();

                int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));

                int finalnumber = 1;
                int iRedCount = listRed.size();
                for (int itmp = 1; itmp <= 5; itmp++) {
                    finalnumber *= iRedCount;
                    iRedCount --;
                }
                combination = finalnumber/ 120;
                finalnumber = listBlue.size() * ( listBlue.size() - 1) /2;

                combination *= finalnumber;

                if (cash < 2 * Integer.parseInt(etBeishu.getText().toString()) * combination) {
                    AlertDialog alertDialog = new AlertDialog.Builder(DltActivity.this).setTitle("提示")
                            .setMessage("您的余额不足，无法投注！\r\n您的投注金额为：" +
                                    String.valueOf(2 * combination * Integer.parseInt(etBeishu.getText().toString())) +
                                    "\r\n您当前余额为：" + String.valueOf(cash) + "元")
                            .create();
                    alertDialog.show();
                    return;
                }




                new AlertDialog.Builder(DltActivity.this)
                        .setTitle("确认您的投注")
                        .setMessage("您的投注内容是：大乐透\r\n" +
                                "投注内容：" + betContent + "\r\n" +
                                "投注倍数:" + etBeishu.getText() + "\r\n" +
                                "投注金额：" + String.valueOf(2 * combination * Integer.parseInt(etBeishu.getText().toString())) + "元" + "\r\n" +
                                "您的当前余额为：" + assistTool.getPreferenceString("cash") + "元")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog = new ProgressDialog(DltActivity.this);
                                progressDialog.setTitle("投注");
                                progressDialog.setMessage("正在投注，请稍候......");
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();

                                RequestParams requestParams = new RequestParams();
                                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
                                requestParams.put("bettype", "03");
                                requestParams.put("betcontent", betContent);
                                requestParams.put("betcount", etBeishu.getText());
                                requestParams.put("periods", "03");
                                HttpRestClient.post("mobile/doBet", requestParams, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        progressDialog.dismiss();
                                        int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
                                        cash = cash - 2 * Integer.parseInt(etBeishu.getText().toString()) * combination;
                                        assistTool.savePreferenceString("cash", String.valueOf(cash));

                                        MyToast.instance.showToast(DltActivity.this,
                                                DltActivity.this.getLayoutInflater(), "投注成功，感谢您的投注！");
                                        EventBus.getDefault().post("sh11x5betted");
                                        super.onSuccess(statusCode, headers, response);
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        progressDialog.dismiss();
                                        AlertDialog alertDialog = new AlertDialog.Builder(DltActivity.this).setTitle("提示")
                                                .setMessage("投注失败，请检查您的网络！")
                                                .create();
                                        alertDialog.show();
                                        super.onFailure(statusCode, headers, responseString, throwable);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("否", null)
                        .show();
            }
        });


        redTv[0] = (TextView) findViewById(R.id.tvBetItem1);
        redTv[1] = (TextView) findViewById(R.id.tvBetItem2);
        redTv[2] = (TextView) findViewById(R.id.tvBetItem3);
        redTv[3] = (TextView) findViewById(R.id.tvBetItem4);
        redTv[4] = (TextView) findViewById(R.id.tvBetItem5);
        redTv[5] = (TextView) findViewById(R.id.tvBetItem6);
        redTv[6] = (TextView) findViewById(R.id.tvBetItem7);
        redTv[7] = (TextView) findViewById(R.id.tvBetItem8);
        redTv[8] = (TextView) findViewById(R.id.tvBetItem9);
        redTv[9] = (TextView) findViewById(R.id.tvBetItem10);
        redTv[10] = (TextView) findViewById(R.id.tvBetItem11);
        redTv[11] = (TextView) findViewById(R.id.tvBetItem12);
        redTv[12] = (TextView) findViewById(R.id.tvBetItem13);
        redTv[13] = (TextView) findViewById(R.id.tvBetItem14);
        redTv[14] = (TextView) findViewById(R.id.tvBetItem15);
        redTv[15] = (TextView) findViewById(R.id.tvBetItem16);
        redTv[16] = (TextView) findViewById(R.id.tvBetItem17);
        redTv[17] = (TextView) findViewById(R.id.tvBetItem18);
        redTv[18] = (TextView) findViewById(R.id.tvBetItem19);
        redTv[19] = (TextView) findViewById(R.id.tvBetItem20);
        redTv[20] = (TextView) findViewById(R.id.tvBetItem21);
        redTv[21] = (TextView) findViewById(R.id.tvBetItem22);
        redTv[22] = (TextView) findViewById(R.id.tvBetItem23);
        redTv[23] = (TextView) findViewById(R.id.tvBetItem24);
        redTv[24] = (TextView) findViewById(R.id.tvBetItem25);
        redTv[25] = (TextView) findViewById(R.id.tvBetItem26);
        redTv[26] = (TextView) findViewById(R.id.tvBetItem27);
        redTv[27] = (TextView) findViewById(R.id.tvBetItem28);
        redTv[28] = (TextView) findViewById(R.id.tvBetItem29);
        redTv[29] = (TextView) findViewById(R.id.tvBetItem30);
        redTv[30] = (TextView) findViewById(R.id.tvBetItem31);
        redTv[31] = (TextView) findViewById(R.id.tvBetItem32);
        redTv[32] = (TextView) findViewById(R.id.tvBetItem33);
        redTv[33] = (TextView) findViewById(R.id.tvBetItem34);
        redTv[34] = (TextView) findViewById(R.id.tvBetItem35);

        blueTv[0] = (TextView) findViewById(R.id.tvBetItemB1);
        blueTv[1] = (TextView) findViewById(R.id.tvBetItemB2);
        blueTv[2] = (TextView) findViewById(R.id.tvBetItemB3);
        blueTv[3] = (TextView) findViewById(R.id.tvBetItemB4);
        blueTv[4] = (TextView) findViewById(R.id.tvBetItemB5);
        blueTv[5] = (TextView) findViewById(R.id.tvBetItemB6);
        blueTv[6] = (TextView) findViewById(R.id.tvBetItemB7);
        blueTv[7] = (TextView) findViewById(R.id.tvBetItemB8);
        blueTv[8] = (TextView) findViewById(R.id.tvBetItemB9);
        blueTv[9] = (TextView) findViewById(R.id.tvBetItemB10);
        blueTv[10] = (TextView) findViewById(R.id.tvBetItemB11);
        blueTv[11] = (TextView) findViewById(R.id.tvBetItemB12);

        for (int i = 0; i < 35; i++) {
            redTv[i].setOnClickListener(tvClick);
        }

        for (int i = 0; i < 12; i++) {
            blueTv[i].setOnClickListener(tvClick);
        }
    }

    TextView.OnClickListener tvClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.setSelected(!v.isSelected());
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = actionBar.getNavigationItemCount();

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case 0:
                assistTool.gotoActivity(ActivityDltMultiple.class, true, null);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    BootstrapButton.OnClickListener randomClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ArrayList<TextView> arrayListRed = new ArrayList<TextView>();
            ArrayList<TextView> arrayListBlue = new ArrayList<TextView>();

            for (int i = 0; i < redTv.length; i++) {
                redTv[i].setSelected(false);
                arrayListRed.add(redTv[i]);
            }
            for (int i = 0; i < blueTv.length; i++) {
                blueTv[i].setSelected(false);
                arrayListBlue.add(blueTv[i]);
            }

            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < 5; i++) {
                int randomNumber = random.nextInt(arrayListRed.size());
                TextView tv = arrayListRed.get(randomNumber);
                arrayListRed.remove(randomNumber);
                tv.setSelected(true);
            }
            for (int i = 0; i < 2; i++) {
                int randomNumber = random.nextInt(arrayListBlue.size());
                TextView tv = arrayListBlue.get(randomNumber);
                arrayListBlue.remove(randomNumber);
                tv.setSelected(true);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("转为复式").
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                        MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

}