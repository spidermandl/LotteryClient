package com.evstudio.thefirstlottery.mobile.core;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.common.Util;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;

/**
 * Created by ericren on 14-9-24.
 * 胡莹莹注释
 * 此类是监听并显示上海11选5彩票销售截止时间倒计时的自定义TextView
 */
public class CountDownTextView extends TextView implements Runnable{

    Handler handler = new Handler();

    public CountDownTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        this.setTextColor(getResources().getColor(R.color.red));
        handler.postDelayed(this, 1000);
    }

    /**
     * Starts executing the active part of the class' code. This method is
     * called when a thread is started that has been created with a class which
     * implements {@code Runnable}.
     */
    @Override
    public void run() {
        long countdown = Long.parseLong(Sh11x5Next.nextTime) * 1000 - System.currentTimeMillis() -  120 * 1000;
        countdown = countdown < 0?0:countdown;
        //显示彩票销售信息
        this.setText(Util.getCountDown(countdown));
        handler.postDelayed(this, 1000);
    }
}


