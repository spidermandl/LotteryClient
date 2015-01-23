package com.evstudio.thefirstlottery.mobile.common;

import com.evstudio.thefirstlottery.mobile.pojo.SH11X5PushBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zyn on 15/1/12.
 * 胡莹莹注释
 * 标示默认上海11选5中11个号码选中状态和所有投注列表，静态属性和静态方法
 */
public class Constants {
    /** 11选5趋势图临时下注 */
    public static int[] SH11X5SelectNums = {0,0,0,0,0,0,0,0,0,0,0}; // 0未选 1选中

    public static void defaultSH11X5(int state){
        for (int i = 0; i < SH11X5SelectNums.length; i++)
            SH11X5SelectNums[i] = state;
    }

    /** */
    public static List<SH11X5PushBean> pushBeanList = new ArrayList<SH11X5PushBean>();
}
