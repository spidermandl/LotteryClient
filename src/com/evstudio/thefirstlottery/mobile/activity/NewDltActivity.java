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
import com.evstudio.thefirstlottery.mobile.adapter.RedAdapter;
import com.evstudio.thefirstlottery.mobile.pojo.BlueBean;
import com.evstudio.thefirstlottery.mobile.pojo.DltBetBean;
import com.evstudio.thefirstlottery.mobile.pojo.RedBean;
import com.evstudio.thefirstlottery.mobile.view.NoScrollListenerGridView;
import com.tandong.sa.tools.AssistTool;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by zhouyong on 15-1-8.
 * <p/>
 * 大乐透投注界面
 */
public class NewDltActivity extends SherlockActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private NoScrollListenerGridView redGridView, blueGridView;

    private ArrayList<RedBean> redList = new ArrayList<RedBean>();
    private ArrayList<BlueBean> blueList = new ArrayList<BlueBean>();


    private ArrayList<Integer> redSelList = new ArrayList<Integer>();
    private ArrayList<Integer> blueSelList = new ArrayList<Integer>();


    private RedAdapter redAdapter;
    private BlueAdapter blueAdapter;


    private ProgressDialog progressDialog;

    private BootstrapButton bbSubmit;
    private BootstrapButton bbRandom;

    private EditText etBeishu;

    private String betContent = "";

    private AssistTool assistTool;

    private int combination;


    private ArrayList<DltBetBean> dltBetList = new ArrayList<DltBetBean>();
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layou_dlt);
        if (getIntent().getExtras() != null) {
            dltBetList = (ArrayList<DltBetBean>) getIntent().getExtras().getSerializable("list");
        }
        initView();
        initData();

    }

    private void initView() {

        redGridView = (NoScrollListenerGridView) findViewById(R.id.redGridView);
        blueGridView = (NoScrollListenerGridView) findViewById(R.id.blueGridView);

        actionBar = getSupportActionBar();
        actionBar.setTitle("大乐透");
        actionBar.setIcon(R.drawable.app_icon_new_03);

        actionBar.setDisplayHomeAsUpEnabled(true);


        bbSubmit = (BootstrapButton) findViewById(R.id.dltBetBtnSubmit);
        bbSubmit.setOnClickListener(this);
        bbRandom = (BootstrapButton) findViewById(R.id.dltRandom);
        bbRandom.setOnClickListener(this);

        etBeishu = (EditText) findViewById(R.id.dltBetEtBei);

        assistTool = new AssistTool(this);

    }

    private void initData() {
        initListData();
        redAdapter = new RedAdapter(this, redList);
        redGridView.setAdapter(redAdapter);

        blueAdapter = new BlueAdapter(this, blueList);
        blueGridView.setAdapter(blueAdapter);


        redGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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


        blueGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

    private ArrayList<DltBetBean> initDltBetData(String dltNumber, String moneyTip, int style, int iMoney, int iZhu, int iBeishu) {
        DltBetBean dltBetBean = new DltBetBean();
        dltBetBean.setDltNumber(dltNumber);
        dltBetBean.setMoneyTip(moneyTip);
        dltBetBean.setiMoney(iMoney);
        dltBetBean.setiZhu(iZhu);
        dltBetBean.setiBeishu(iBeishu);
        dltBetBean.setDltStyle(style);
        dltBetList.add(dltBetBean);
        return dltBetList;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dltBetBtnSubmit:
                initSelData();
                if (blueSelList.size() < 2 || redSelList.size() < 5) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewDltActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("选择不完整，至少选择5个红球和2个篮球，请重新选择！");
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
                int iRedCount = redSelList.size();
                for (int itmp = 1; itmp <= 5; itmp++) {
                    finalnumber *= iRedCount;
                    iRedCount--;
                }
                combination = finalnumber / 120;
                finalnumber = blueSelList.size() * (blueSelList.size() - 1) / 2;

                combination *= finalnumber;

                if (cash < 2 * Integer.parseInt(etBeishu.getText().toString()) * combination) {
                    AlertDialog alertDialog = new AlertDialog.Builder(NewDltActivity.this).setTitle("提示")
                            .setMessage("您的余额不足，无法投注！\r\n您的投注金额为：" +
                                    String.valueOf(2 * combination * Integer.parseInt(etBeishu.getText().toString())) +
                                    "\r\n您当前余额为：" + String.valueOf(cash) + "元")
                            .create();
                    alertDialog.show();
                    return;
                }

                int iMoney = 2 * combination * Integer.parseInt(etBeishu.getText().toString());
                money = String.valueOf(combination) + "注" + String.valueOf(iMoney) + "元";

                Intent intent = new Intent();
                intent.putExtra("list", initDltBetData(betContent, money, 1, iMoney, combination,
                        Integer.parseInt(etBeishu.getText().toString())));
                intent.setClass(NewDltActivity.this, DltBetListActivity.class);
                startActivityForResult(intent, 1);
                break;

            case R.id.dltRandom:
                initListData();
                Random random = new Random(System.currentTimeMillis());
//                ArrayList<RedBean> arrayListRed = new ArrayList<RedBean>( redList );
                ArrayList<Integer> arrayListRed = new ArrayList<Integer>();
                ArrayList<Integer> arrayListBlue = new ArrayList<Integer>( );
                for( int i = 0; i < 35; i ++ ){
                    arrayListRed.add( i) ;
                }
                for( int i = 0; i < 12; i ++ ){
                    arrayListBlue.add(i);
                }
                for (int i = 0; i < 5; i++) {
                    int randomNumber = random.nextInt(arrayListRed.size());
                    int selNumber = arrayListRed.get(randomNumber).intValue();
                    redList.get(selNumber).setSel(true);
                    arrayListRed.remove(randomNumber);
                }
                for (int i = 0; i < 2; i++) {
                    int randomNumber = random.nextInt(arrayListBlue.size());
                    int selNumber = arrayListBlue.get(randomNumber).intValue();
                    blueList.get(selNumber).setSel(true);
                    arrayListBlue.remove(randomNumber);
                }
                redAdapter.notifyDataSetChanged();
                blueAdapter.notifyDataSetChanged();

                break;
        }
    }


    private void initSelData() {
        redSelList.clear();
        blueSelList.clear();
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
    }

    private void initListData() {
        redList.clear();
        blueList.clear();
        for (int i = 1; i < 36; i++) {
            redList.add(initBeanData(i));
        }
        for (int j = 1; j < 13; j++) {
            blueList.add(initBlueBeanData(j));
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
                intent.setClass(NewDltActivity.this, NewActivityDltMultiple.class);
                startActivity(intent);
                finish();
//                assistTool.gotoActivity(NewActivityDltMultiple.class, true, null);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("转为复式").
                setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
                        MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }
}
