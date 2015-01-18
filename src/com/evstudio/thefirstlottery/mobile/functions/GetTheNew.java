package com.evstudio.thefirstlottery.mobile.functions;

import android.content.Context;
import android.util.Log;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.eventbus.EventBus;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.tools.AssistTool;
import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by ericren on 14-9-18.
 */
public class GetTheNew extends TimerTask {
    public static GetTheNew instance;
    private Timer timer = new Timer();
    private Date date;
    private Context context;

    public GetTheNew(Context context) {
        super();
        this.context = context;
    }

    /**
     * The task to run should be specified in the implementation of the {@code run()}
     * method.
     */
    @Override
    public void run() {
        Log.d(this.getClass().getName(), "Timer is start");
        HttpRestClient.post("mobile/getSh11x5Next", new RequestParams(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Log.d(this.getClass().getName(), "Timer is start 1");
                super.onSuccess(statusCode, headers, responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(this.getClass().getName(), "Timer is start 2");
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(this.getClass().getName(), "Timer is start 3");
                Timestamp timestamp;
                MessageProcess.function.processReturn(response);
                if (MessageProcess.function.isSuccess) {
                    String nextPeriods = MessageProcess.function.getAttr("sh11x5nextperiods");
                    if (null != nextPeriods && !nextPeriods.equals(Sh11x5Next.nextPeriods)) {
                        Sh11x5Next.nextPeriods = MessageProcess.function.getAttr("sh11x5nextperiods");
                        Sh11x5Next.nextTime = MessageProcess.function.getAttr("sh11x5nexttime");
                        Sh11x5Next.lastWinning = MessageProcess.function.getAttr("sh11x5lastwin");
                        Sh11x5Next.lastPeriods = MessageProcess.function.getAttr("sh11x5lastperiods");
                        AssistTool assistTool = new AssistTool(context);

                        assistTool.savePreferenceString("sh11x5lastperiods", Sh11x5Next.lastPeriods);
                        assistTool.savePreferenceString("sh11x5nexttime", Sh11x5Next.nextTime);
                        assistTool.savePreferenceString("sh11x5nextperiods", Sh11x5Next.nextPeriods);
                        assistTool.savePreferenceString("sh11x5lastwin", Sh11x5Next.lastWinning);

                        EventBus.getDefault().post("sh11x5New");

                        timestamp = new Timestamp(Long.parseLong(Sh11x5Next.nextTime) * 1000 + 30 * 1000);
                    } else {
                            timestamp = new Timestamp(System.currentTimeMillis() + 10 * 1000);
                    }

                    date = timestamp;
                    try {
                        timer.purge();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        timer.schedule(new GetTheNew(context), date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 1000 * 30);
//                date = timestamp;
//                if( null != instance ){
//                    timer.purge();
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                try {
//                    instance = new GetTheNew();
//                    instance.timer.schedule( instance, date);
//                }catch (Exception e ){
//                    e.printStackTrace();
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    timer.purge();
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Timestamp timestamp = new Timestamp(System.currentTimeMillis() + 1000 * 10);
                date = timestamp;
                try {
                    timer.schedule(new GetTheNew(context), date);
//                    instance.timer.schedule(instance, date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
