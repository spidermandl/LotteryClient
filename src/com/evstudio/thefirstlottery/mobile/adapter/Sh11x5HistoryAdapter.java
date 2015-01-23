package com.evstudio.thefirstlottery.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ericren on 14-10-25.
 * 胡莹莹注释
 * 上海11选5走势图适配器
 */
public class Sh11x5HistoryAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public Sh11x5HistoryAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.list = fragments;
    }


    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
