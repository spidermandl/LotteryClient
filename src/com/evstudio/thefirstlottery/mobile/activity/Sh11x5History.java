package com.evstudio.thefirstlottery.mobile.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.Sh11x5HistoryAdapter;
import com.evstudio.thefirstlottery.mobile.common.Constants;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.fragments.Sh11x5DrawZs;
import com.evstudio.thefirstlottery.mobile.view.SH11X5SelectPop;
import com.evstudio.thefirstlottery.mobile.view.SH11X5ViewPager;
import com.tandong.sa.loopj.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ericren on 14-10-25.
 */
public class Sh11x5History extends SherlockFragmentActivity implements ViewPager.OnPageChangeListener, View.OnClickListener, SH11X5SelectPop.SH11X5UpdateCallback,Sh11x5DrawZs.CancelHistoryCallback {
    private String[] strTitles = {"走势图"};
    private String[] mSelectTexts = {"任选一", "任选二", "任选三","任选四", "任选五",
            "任选六", "任选七", "任选八", "组选前二", "组选前三", "直选前二", "直选前三"};

    /** 导航标题栏 */
    private View navView;
    private TextView tvSelect11X5;
    private RelativeLayout rlBack;
    private Button btnGoHistory;
    private TextView tvBackName;
    private Button btnSelect;

    /** 选择玩法 */
    private PopupWindow popupWindow;
    private View popupView;

    /** 选择显示期的数量 */
    private String[] numberArrs = { "30", "50", "100" };
	private int defaultItem = 0;
    private int selectNumber = 30; // 选择显示的期数

    /** viewpager */
    private SH11X5ViewPager mPager;
    private List<Fragment> mFragmentList;
    private String[] playCmd = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "11", "10", "12"};
    private int[] selectNumbers = {1,2,3,4,5,6,7,8,2,3,2,3};
    private Sh11x5HistoryAdapter adapter;
    private ProgressDialog progressDialog;

    Sh11x5DrawZs fragment;

    /** 选择下注 */
    TextView tvPeriods,tvNumber1,tvNumber2,tvNumber3,tvNumber4,tvNumber5,tvNumber6,tvNumber7,tvNumber8,tvNumber9,tvNumber10,tvNumber11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sh11x5_history);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initializeView();


    }

    private void initializeView(){
        navView = findViewById(R.id.navView);
        tvSelect11X5 = (TextView) findViewById(R.id.tvSelect11X5);
        tvSelect11X5.setOnClickListener(this);

        tvSelect11X5.setText(mSelectTexts[Integer.parseInt(getIntent().getStringExtra("selectIndex"))]);

        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        btnGoHistory = (Button) findViewById(R.id.btnGoHistory);
        btnGoHistory.setVisibility(View.GONE);
        tvBackName = (TextView) findViewById(R.id.tvBackName);
        tvBackName.setText("走势图");
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSelect.setBackgroundResource(0);
        btnSelect.setTextColor(Color.WHITE);
        btnSelect.setVisibility(View.VISIBLE);
        btnSelect.setOnClickListener(this);

        selectInit();

        popupView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.popupwindow, null);
        SH11X5SelectPop.newInstance(popupView).setSelectIndex(Integer.parseInt(getIntent().getStringExtra("selectIndex")));

        SH11X5SelectPop.newInstance(popupView).setSH11X5UpdateCallback(this);
        SH11X5SelectPop.newInstance(popupView).initialize();

        /** viewpager */
        mFragmentList = new ArrayList<Fragment>();
        mPager = (SH11X5ViewPager) findViewById(R.id.vpSh11x5Draw);
        adapter = new Sh11x5HistoryAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(this);

        requestData();
    }

    private void requestData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HttpRestClient.post("mobile/getSh11x5Recent?recent="+selectNumber, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                //将Fragment加入到List中，并将Tab的title传递给Fragment
                Bundle args = new Bundle();

//                Sh11x5DrawFragment sh11x5DrawFragment = new Sh11x5DrawFragment();
//
//                args.putString("title", strTitles[0]);
//                args.putString("data", response.toString());
//                sh11x5DrawFragment.setArguments(args);
//
//                fragmentList.add(sh11x5DrawFragment);

                if (fragment !=null){
                    fragment.ListNumberUpdate(response.toString());
                    return;
                }

                fragment = new Sh11x5DrawZs();
                fragment.setcancelHistoryCallback(Sh11x5History.this);
                args.putString("title", strTitles[0]);
                args.putString("data", response.toString());
                fragment.setArguments(args);
                mFragmentList.add(fragment);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MyToast.instance.showToast(Sh11x5History.this, getLayoutInflater(), "无法连接服务器，请检查网络！");
                progressDialog.dismiss();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void selectInit(){
        tvPeriods = (TextView) findViewById(R.id.tv_periods);
        tvNumber1 = (TextView) findViewById(R.id.tv_number1);
        tvNumber2 = (TextView) findViewById(R.id.tv_number2);
        tvNumber3 = (TextView) findViewById(R.id.tv_number3);
        tvNumber4 = (TextView) findViewById(R.id.tv_number4);
        tvNumber5 = (TextView) findViewById(R.id.tv_number5);
        tvNumber6 = (TextView) findViewById(R.id.tv_number6);
        tvNumber7 = (TextView) findViewById(R.id.tv_number7);
        tvNumber8 = (TextView) findViewById(R.id.tv_number8);
        tvNumber9 = (TextView) findViewById(R.id.tv_number9);
        tvNumber10 = (TextView) findViewById(R.id.tv_number10);
        tvNumber11 = (TextView) findViewById(R.id.tv_number11);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SH11X5SelectPop.release();
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
//        actionBar.setSelectedNavigationItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void initPopWindow() {
        // final PopupWindow popupwindow;
        popupWindow = new PopupWindow(Sh11x5History.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // popupWindow.setHeight((int) (250 * popDensity));
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.setContentView(popupView);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(navView); // 下方

    }

    private void selectNumbers(){
        new AlertDialog.Builder(this).setTitle("期数选择").setSingleChoiceItems(numberArrs, // 单选框有几项,各是什么名字
                defaultItem, // 默认的选项
                new DialogInterface.OnClickListener() { // 点击单选框后的处理
                    public void onClick(DialogInterface dialog, int which) { // 点击了哪一项
                        defaultItem = which;
                        btnSelect.setText(numberArrs[which]+"期");
                        selectNumber = Integer.parseInt(numberArrs[which]);
                        requestData();
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSelect11X5:
                initPopWindow();
                break;

            case R.id.rlBack:
                Intent intent = new Intent();
                intent.putExtra("selectIndex", SH11X5SelectPop.newInstance(popupView).getSelectIndex() + "");
                setResult(RESULT_OK, intent);
                finish();
                break;

            case R.id.btnSelect:
                selectNumbers();
                break;
            default:
                break;
        }
    }

    @Override
    public void update(int selectIndex) {
        tvSelect11X5.setText(mSelectTexts[selectIndex]);
        popupWindow.dismiss();
    }

    @Override
    public void cancelHistory() {
        Intent intent = new Intent();
        intent.putExtra("selectIndex", SH11X5SelectPop.newInstance(popupView).getSelectIndex() + "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("selectIndex", SH11X5SelectPop.newInstance(popupView).getSelectIndex() + "");
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}