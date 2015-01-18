package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.beardedhen.androidbootstrap.BootstrapButton;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.BlueAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.BlueDragAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.RedAdapter;
import com.evstudio.thefirstlottery.mobile.adapter.RedDragAdapter;
import com.evstudio.thefirstlottery.mobile.pojo.BlueBean;
import com.evstudio.thefirstlottery.mobile.pojo.BlueDragBean;
import com.evstudio.thefirstlottery.mobile.pojo.DltBetBean;
import com.evstudio.thefirstlottery.mobile.pojo.RedBean;
import com.evstudio.thefirstlottery.mobile.pojo.RedDragBean;
import com.evstudio.thefirstlottery.mobile.view.NoScrollListenerGridView;
import com.tandong.sa.tools.AssistTool;

import java.util.ArrayList;

/**
 * Created by zhouyong on 15-1-9.
 */
public class NewActivityDltMultiple extends SherlockActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private NoScrollListenerGridView redBoldGridView, blueBoldGridView, redDragGridView, blueDragGridView;

    /**
     * 胆区
     */
    private ArrayList<RedBean> redList = new ArrayList<RedBean>();
    private ArrayList<BlueBean> blueList = new ArrayList<BlueBean>();
    /**
     * 拖区
     */
    private ArrayList<RedDragBean> redDragList = new ArrayList<RedDragBean>();
    private ArrayList<BlueDragBean> blueDragList = new ArrayList<BlueDragBean>();

    private ArrayList<Integer> redSelList = new ArrayList<Integer>();
    private ArrayList<Integer> blueSelList = new ArrayList<Integer>();
    private ArrayList<Integer> redDragSelList = new ArrayList<Integer>();
    private ArrayList<Integer> blueDragSelList = new ArrayList<Integer>();

    private RedAdapter redAdapter;
    private BlueAdapter blueAdapter;
    private RedDragAdapter redDragAdapter;
    private BlueDragAdapter blueDragAdapter;


    private ProgressDialog progressDialog;

    private BootstrapButton bbSubmit;
    private BootstrapButton bbRandom;

    private EditText etBeishu;

    private String betContent = "";

    private AssistTool assistTool;

    private int combination;
    private String money;

    private ArrayList<DltBetBean> dltBetList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_layout_dlt_multiple);
        dltBetList = (ArrayList<DltBetBean>) getIntent().getExtras().getSerializable("list");

        initView();
        initData();
    }

    private void initView() {
        redBoldGridView = (NoScrollListenerGridView) findViewById(R.id.redBoldGridView);
        blueBoldGridView = (NoScrollListenerGridView) findViewById(R.id.blueBoldGridView);
        redDragGridView = (NoScrollListenerGridView) findViewById(R.id.redDragGridView);
        blueDragGridView = (NoScrollListenerGridView) findViewById(R.id.blueDragGridView);

        actionBar = getSupportActionBar();
        actionBar.setTitle("大乐透");
        actionBar.setIcon(R.drawable.app_icon_new_03);

        actionBar.setDisplayHomeAsUpEnabled(true);


        bbSubmit = (BootstrapButton) findViewById(R.id.dltBetBtnSubmit);
        bbSubmit.setOnClickListener(this);

        etBeishu = (EditText) findViewById(R.id.dltBetEtBei);

        assistTool = new AssistTool(this);

    }

    private void initData() {

        for (int i = 1; i < 36; i++) {
            redList.add(initBeanData(i));
        }
        for (int j = 1; j < 13; j++) {
            blueList.add(initBlueBeanData(j));
        }

        for (int m = 1; m < 36; m++) {
            redDragList.add(initRedDragBeanData(m));
        }
        for (int k = 1; k < 13; k++) {
            blueDragList.add(initBlueDragBeanData(k));
        }

        redAdapter = new RedAdapter(this, redList);
        redBoldGridView.setAdapter(redAdapter);

        blueAdapter = new BlueAdapter(this, blueList);
        blueBoldGridView.setAdapter(blueAdapter);

        redDragAdapter = new RedDragAdapter(this, redDragList);
        redDragGridView.setAdapter(redDragAdapter);

        blueDragAdapter = new BlueDragAdapter(this, blueDragList);
        blueDragGridView.setAdapter(blueDragAdapter);

        gridViewOnItemClick();
    }

    private void gridViewOnItemClick() {
        redBoldGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (redList.get(position).isSel()) {
                    redList.get(position).setSel(false);
                } else {
                    redList.get(position).setSel(true);
                }
                redAdapter.notifyDataSetChanged();
            }
        });

        blueBoldGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (blueList.get(position).isSel()) {
                    blueList.get(position).setSel(false);
                } else {
                    blueList.get(position).setSel(true);
                }
                blueAdapter.notifyDataSetChanged();
            }
        });

        redDragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (redDragList.get(position).isSel()) {
                    redDragList.get(position).setSel(false);
                } else {
                    redDragList.get(position).setSel(true);
                }
                redDragAdapter.notifyDataSetChanged();
            }
        });

        blueDragGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (blueDragList.get(position).isSel()) {
                    blueDragList.get(position).setSel(false);
                } else {
                    blueDragList.get(position).setSel(true);
                }
                blueDragAdapter.notifyDataSetChanged();
            }
        });
    }

    private RedBean initBeanData(int i) {
        RedBean redBean = new RedBean();
        redBean.setNumber(i);
        redBean.setSel(false);
        return redBean;
    }

    private BlueBean initBlueBeanData(int i) {
        BlueBean blueBean = new BlueBean();
        blueBean.setNumber(i);
        blueBean.setSel(false);
        return blueBean;
    }

    private RedDragBean initRedDragBeanData(int i) {
        RedDragBean redDragBean = new RedDragBean();
        redDragBean.setNumber(i);
        redDragBean.setSel(false);
        return redDragBean;
    }

    private BlueDragBean initBlueDragBeanData(int i) {
        BlueDragBean blueDragBean = new BlueDragBean();
        blueDragBean.setNumber(i);
        blueDragBean.setSel(false);
        return blueDragBean;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dltBetBtnSubmit:
                initSelData();
                if (blueSelList.size() < 2 || redSelList.size() < 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewActivityDltMultiple.this);
                    builder.setTitle("提示");
                    builder.setMessage("选择不完整，请重新选择！");
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });

                    builder.show();
                    return;
                }

                StringBuffer strBuff = new StringBuffer();
                for (int i = 0; i < redSelList.size(); i++) {
                    if (redSelList.get(i) < 10)
                        strBuff.append("0");
                    strBuff.append(redSelList.get(i));
                    if (i < redSelList.size() - 1)
                        strBuff.append(",");
                }

                strBuff.append("|");

                for (int i = 0; i < blueSelList.size(); i++) {
                    if (blueSelList.get(i) < 10)
                        strBuff.append("0");
                    strBuff.append(blueSelList.get(i));
                    if (i < blueSelList.size() - 1)
                        strBuff.append(",");
                }
                betContent = strBuff.toString();

                int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));

                int finalnumber = 1;
                for (int itmp = redSelList.size(); itmp > 5; itmp--) {
                    finalnumber *= itmp;
                }
                combination = finalnumber;
                finalnumber = 1;
                for (int itmp = blueSelList.size(); itmp > 2; itmp--) {
                    finalnumber *= itmp;
                }
                combination *= finalnumber;

                if (cash < 2 * Integer.parseInt(etBeishu.getText().toString()) * combination) {
                    AlertDialog alertDialog = new AlertDialog.Builder(NewActivityDltMultiple.this).setTitle("提示")
                            .setMessage("您的余额不足，无法投注！\r\n您的投注金额为：" +
                                    String.valueOf(2 * combination * Integer.parseInt(etBeishu.getText().toString())) +
                                    "\r\n您当前余额为：" + String.valueOf(cash) + "元")
                            .create();
                    alertDialog.show();
                    return;
                }


                money = etBeishu.getText().toString() + "注" + String.valueOf(2 * combination * Integer.parseInt(etBeishu.getText().toString())) + "元";

                Intent intent = new Intent();
                intent.putExtra("list", initDltBetData(betContent, money, 2));
                intent.setClass(NewActivityDltMultiple.this, DltBetListActivity.class);
                startActivityForResult(intent, 2);

//                new AlertDialog.Builder(NewActivityDltMultiple.this)
//                        .setTitle("确认您的投注")
//                        .setMessage("您的投注内容是：大乐透\r\n" +
//                                "投注内容：" + betContent + "\r\n" +
//                                "投注倍数:" + etBeishu.getText() + "\r\n" +
//                                "投注金额：" + String.valueOf(2 * combination * Integer.parseInt(etBeishu.getText().toString())) + "元" + "\r\n" +
//                                "您的当前余额为：" + assistTool.getPreferenceString("cash") + "元")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                progressDialog = new ProgressDialog(NewActivityDltMultiple.this);
//                                progressDialog.setTitle("投注");
//                                progressDialog.setMessage("正在投注，请稍候......");
//                                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                                progressDialog.setCanceledOnTouchOutside(false);
//                                progressDialog.show();
//
//                                RequestParams requestParams = new RequestParams();
//                                requestParams.put("mobileuid", assistTool.getPreferenceString("userid"));
//                                requestParams.put("bettype", "03");
//                                requestParams.put("betcontent", betContent);
//                                requestParams.put("betcount", etBeishu.getText());
//                                requestParams.put("periods", "03");
//                                HttpRestClient.post("mobile/doBet", requestParams, new JsonHttpResponseHandler() {
//                                    @Override
//                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                        progressDialog.dismiss();
//                                        int cash = Integer.parseInt(assistTool.getPreferenceString("cash"));
//                                        cash = cash - 2 * Integer.parseInt(etBeishu.getText().toString()) * combination;
//                                        assistTool.savePreferenceString("cash", String.valueOf(cash));
//
//                                        MyToast.instance.showToast(NewActivityDltMultiple.this,
//                                                NewActivityDltMultiple.this.getLayoutInflater(), "投注成功，感谢您的投注！");
//                                        EventBus.getDefault().post("sh11x5betted");
//                                        super.onSuccess(statusCode, headers, response);
//                                    }
//
//                                    @Override
//                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                        progressDialog.dismiss();
//                                        AlertDialog alertDialog = new AlertDialog.Builder(NewActivityDltMultiple.this).setTitle("提示")
//                                                .setMessage("投注失败，请检查您的网络！")
//                                                .create();
//                                        alertDialog.show();
//                                        super.onFailure(statusCode, headers, responseString, throwable);
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("否", null)
//                        .show();
//                break;
        }
    }

    private void initSelData() {
        redSelList.clear();
        blueSelList.clear();
        redDragSelList.clear();
        blueDragSelList.clear();
        for (int i = 0; i < redList.size(); i++) {
            if (redList.get(i).isSel()) {
                redSelList.add(redList.get(i).getNumber());
            }
        }
        for (int j = 0; j < blueList.size(); j++) {
            if (blueList.get(j).isSel()) {
                blueSelList.add(blueList.get(j).getNumber());
            }
        }
        for (int m = 1; m < redDragList.size(); m++) {
            if (redDragList.get(m).isSel()) {
                redDragSelList.add(redDragList.get(m).getNumber());
            }
        }
        for (int k = 1; k < blueDragList.size(); k++) {
            if (blueDragList.get(k).isSel()) {
                blueDragSelList.add(blueDragList.get(k).getNumber());
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = actionBar.getNavigationItemCount();
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case 0:
                Intent intent = new Intent();
                intent.putExtra("list", dltBetList);
                intent.setClass(NewActivityDltMultiple.this, NewDltActivity.class);
                startActivity(intent);
                finish();
//                assistTool.gotoActivity(NewDltActivity.class, true, null);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("转为单式").
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                        MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    private ArrayList<DltBetBean> initDltBetData(String dltNumber, String money, int style) {
        DltBetBean dltBetBean = new DltBetBean();
        dltBetBean.setDltNumber(dltNumber);
        dltBetBean.setMoneyTip(money);
        dltBetBean.setDltStyle(style);
        dltBetList.add(dltBetBean);
        return dltBetList;
    }
}
