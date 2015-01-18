package com.evstudio.thefirstlottery.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.evstudio.thefirstlottery.mobile.adapter.Sh11x5BetFragmentAdapter;
import com.evstudio.thefirstlottery.mobile.common.Constants;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.core.Sh11x5BetItemFragment;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.evstudio.thefirstlottery.mobile.view.SH11X5SelectPop;
import com.evstudio.thefirstlottery.mobile.view.SH11X5ViewPager;
import com.tandong.sa.loopj.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyn on 15/1/8.
 */
public class SH1X5Activity extends SherlockFragmentActivity implements View.OnClickListener, SH11X5SelectPop.SH11X5UpdateCallback, ViewPager.OnPageChangeListener {
    private String[] mSelectTexts = {"任选一", "任选二", "任选三","任选四", "任选五",
            "任选六", "任选七", "任选八", "组选前二", "组选前三", "直选前二", "直选前三"};

    /** 导航标题栏 */
    private View navView;
    private TextView tvSelect11X5;
    private RelativeLayout rlBack;
    private Button btnGoHistory;

    /** 选择玩法 */
    private PopupWindow popupWindow;
    private View popupView;

    /** viewpager */
    private SH11X5ViewPager mPager;
    private List<Sh11x5BetItemFragment> mFragmentList;
    private String[] playCmd = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "11", "10", "12"};
    private int[] selectNumbers = {1,2,3,4,5,6,7,8,2,3,2,3};
    private Sh11x5BetFragmentAdapter adapter;
    private ProgressDialog progressDialog;
    private String responseData;

    private final int SELECT_UPDATE_DATA = 200;
    private int selectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        initializeView();

//        Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   ", ""+Sh11x5Next.nextTime);
    }

    private void initializeView(){
        navView = findViewById(R.id.navView);
        tvSelect11X5 = (TextView) findViewById(R.id.tvSelect11X5);
        tvSelect11X5.setOnClickListener(this);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        btnGoHistory = (Button) findViewById(R.id.btnGoHistory);
        btnGoHistory.setOnClickListener(this);

        popupView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.popupwindow, null);

        SH11X5SelectPop.newInstance(popupView).setSH11X5UpdateCallback(this);
        SH11X5SelectPop.newInstance(popupView).initialize();

        /** viewpager */
        mFragmentList = new ArrayList<Sh11x5BetItemFragment>();
        mPager = (SH11X5ViewPager) findViewById(R.id.vpFirst);
        adapter = new Sh11x5BetFragmentAdapter(getSupportFragmentManager(), mFragmentList);
        mPager.setAdapter(adapter);
        mPager.setOnPageChangeListener(this);

        requestData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SH11X5SelectPop.release();
        Constants.defaultSH11X5(0);
        Constants.pushBeanList.clear();
    }

    /**  网络数据请求 */
    private void requestData(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在载入");
        progressDialog.setMessage("正在载入数据，请稍候......");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        HttpRestClient.post("mobile/getSh11x5Recent?recent=15", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Coming:" + response.toString());
                responseData = response.toString();
                progressDialog.dismiss();
                //将Fragment加入到List中，并将Tab的title传递给Fragment
                for (int i = 0; i < mSelectTexts.length; i++) {
                    Sh11x5BetItemFragment fragment = new Sh11x5BetItemFragment();
                    Bundle args = new Bundle();
                    args.putString("title", mSelectTexts[i]);
                    args.putString("arg", playCmd[i]);
                    args.putInt("numbers", selectNumbers[i]);
                    args.putString("data", responseData);
                    fragment.setArguments(args);

                    mFragmentList.add(fragment);
                }
                adapter.notifyDataSetChanged();
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                MyToast.instance.showToast(SH1X5Activity.this, getLayoutInflater(), "无法连接服务器，请检查网络！");
                progressDialog.dismiss();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    private void initPopWindow() {
        // final PopupWindow popupwindow;
        popupWindow = new PopupWindow(SH1X5Activity.this);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // popupWindow.setHeight((int) (250 * popDensity));
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.setContentView(popupView);

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(navView); // 下方

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvSelect11X5:
                initPopWindow();
                break;

            case R.id.rlBack:
                finish();
                break;

            case R.id.btnGoHistory:
                if (SH11X5SelectPop.checkInstance()){
                    selectIndex = SH11X5SelectPop.newInstance(popupView).getSelectIndex();
                    SH11X5SelectPop.release();
                }
                Intent intent = new Intent();
                intent.setClass(SH1X5Activity.this, Sh11x5History.class);

                intent.putExtra("selectIndex", selectIndex + "");
                startActivityForResult(intent, SELECT_UPDATE_DATA);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            // 趋势图选择了投注号码之后更新
            selectIndex = Integer.parseInt(data.getStringExtra("selectIndex"));
            SH11X5SelectPop.newInstance(popupView).setSelectIndex(selectIndex);
            tvSelect11X5.setText(mSelectTexts[SH11X5SelectPop.newInstance(popupView).getSelectIndex()]);
            mPager.setCurrentItem(SH11X5SelectPop.newInstance(popupView).getSelectIndex());
            mFragmentList.get(SH11X5SelectPop.newInstance(popupView).getSelectIndex())
                    .itemAdapter
                    .notifyDataSetChanged();
        }
    }

    @Override
    public void update(int selectIndex) {
        tvSelect11X5.setText(mSelectTexts[selectIndex]);
        SH11X5SelectPop.newInstance(popupView).setSelectIndex(selectIndex);
        Constants.defaultSH11X5(0);
        mPager.setCurrentItem(selectIndex);
        popupWindow.dismiss();
        Log.d("****************************   ",selectIndex+"");
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
