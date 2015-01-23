package com.evstudio.thefirstlottery.mobile.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.evstudio.thefirstlottery.mobile.core.Sh11x5BetItemFragment;

import java.util.List;

/**
 * Created by ericren on 14-9-12.
 * 胡莹莹注释
 * 上海11选5的投注标题栏适配器
 */
public class Sh11x5BetFragmentAdapter  extends FragmentStatePagerAdapter {
    private List<Sh11x5BetItemFragment> list;

    public Sh11x5BetFragmentAdapter(FragmentManager fm, List<Sh11x5BetItemFragment> fragments) {
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
