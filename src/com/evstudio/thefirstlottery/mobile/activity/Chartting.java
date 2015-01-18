package com.evstudio.thefirstlottery.mobile.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.List;

/**
 * Created by ericren on 14-10-21.
 */
public class Chartting extends SherlockFragmentActivity {
    private ActionBar actionBar;
    private List<Fragment> mFragmentList;
    private String[] mTabTitles = {"开奖", "走势", "属性", "012路", "冷热"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
