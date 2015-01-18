package com.evstudio.thefirstlottery.mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.adapter.SH11X5PushAdapter;
import com.evstudio.thefirstlottery.mobile.common.Constants;
import com.evstudio.thefirstlottery.mobile.pojo.SH11X5PushBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyn on 15/1/13.
 */
public class SH11X5HistoryActivity extends SherlockActivity implements View.OnClickListener {
    /** listview */
    private ListView dalListView;
    private SH11X5PushAdapter adapter;
    List<SH11X5PushBean> dataList = new ArrayList<SH11X5PushBean>();

    /** 导航栏 */
    private TextView tvSelect11X5;
    private RelativeLayout rlBack;
    private Button btnGoHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sh11x5_history_layout);

        initializeView();
    }

    private void initializeView(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tvSelect11X5 = (TextView) findViewById(R.id.tvSelect11X5);
        tvSelect11X5.setVisibility(View.GONE);
        rlBack = (RelativeLayout) findViewById(R.id.rlBack);
        rlBack.setOnClickListener(this);
        btnGoHistory = (Button) findViewById(R.id.btnGoHistory);
        btnGoHistory.setVisibility(View.GONE);

        dalListView = (ListView) findViewById(R.id.dalListView);

        // TODO 这里是要调用接口数据的 暂时用当前选择下注的数据填充 !!!!!!!!
        dataList = Constants.pushBeanList;

        adapter = new SH11X5PushAdapter(SH11X5HistoryActivity.this,dataList);
        dalListView.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
              case R.id.rlBack:
                    finish();
                    break;
              default:
                  break;
        }
    }
}
