package com.evstudio.thefirstlottery.mobile.common;

import android.os.Looper;
import com.tandong.sa.loopj.AsyncHttpClient;
import com.tandong.sa.loopj.AsyncHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.loopj.SyncHttpClient;

/**
 * Created by ericren on 14-8-30.
 */
public class HttpRestClient {
    private static final String BASE_URL = "http://www.thefirstlottery.com/tmserver/";

//    private static AsyncHttpClient client = new AsyncHttpClient();

    public static AsyncHttpClient syncClient = new SyncHttpClient();
    public static AsyncHttpClient asyncClient = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().setURLEncodingEnabled(true);
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().setURLEncodingEnabled(true);
        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static void getDirect(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        getClient().setURLEncodingEnabled(true);
        getClient().get(url, params, responseHandler);
    }

    public static void postDirect(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
//        getClient().setURLEncodingEnabled(true);
        getClient().post(url, params, responseHandler);
    }

    private static AsyncHttpClient getClient()
    {
        // Return the synchronous HTTP client when the thread is not prepared
        if (Looper.myLooper() == null)
            return syncClient;
        return asyncClient;
    }
}
