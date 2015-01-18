package com.evstudio.thefirstlottery.mobile.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import com.evstudio.thefirstlottery.mobile.R;
import com.evstudio.thefirstlottery.mobile.common.HttpRestClient;
import com.evstudio.thefirstlottery.mobile.common.MyToast;
import com.evstudio.thefirstlottery.mobile.common.UpdateManager;
import com.evstudio.thefirstlottery.mobile.functions.GetTheNew;
import com.evstudio.thefirstlottery.mobile.functions.MessageProcess;
import com.evstudio.thefirstlottery.mobile.functions.UserFunctions;
import com.evstudio.thefirstlottery.mobile.pojo.Sh11x5Next;
import com.tandong.sa.json.Gson;
import com.tandong.sa.json.reflect.TypeToken;
import com.tandong.sa.loopj.JsonHttpResponseHandler;
import com.tandong.sa.loopj.RequestParams;
import com.tandong.sa.system.SystemInfo;
import com.tandong.sa.tools.AssistTool;
import org.apache.http.Header;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;

/**
 * Created by ericren on 14-8-22.
 */
public class SplashScreen extends Activity {
    private SystemInfo systemInfo;
    private AssistTool assistTool;
    private final String actionUrl = "mobile/startApp";
    private final RequestParams params = new RequestParams();
    private Context context;

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminate(true);
        setProgressBarVisibility(true);
        assistTool = new AssistTool(this);
        assistTool.userinfo = UserFunctions.userInfoTag;
        assistTool.setFullScreen(this);
        context = this;

        setContentView(R.layout.splashscreen);
        //Display the current version number
        PackageManager pm = getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo("com.evstudio.thefirstlottery.mobile", 0);
            TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
            versionNumber.setText("Version " + pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }

//        是否已经提交过设备信息
        boolean isPostedDevice = assistTool.getPreferenceBoolean("isPostedDevice");
        if (!isPostedDevice) {
            String deviceInfo = getDeviceInfo();
            try {
                params.put("deviceInfo", URLEncoder.encode(deviceInfo, "utf8"));
            } catch (UnsupportedEncodingException e) {
                Log.e(this.getClass().getName(), e.getMessage());
            }
        } else {
            params.put("deviceInfo", "");
        }

//        是否已经注册
        boolean isRegistered = assistTool.getPreferenceBoolean("isRegistered");
        if (isRegistered) {
            String mobile = assistTool.getPreferenceString("mobile");
            params.put("mobile", mobile);
        }

        params.put("version", assistTool.getVersion());
        params.put("versionCode", assistTool.getVersionCode());
        final String time = assistTool.toNormalTime(System.currentTimeMillis(),
                "yyyy-MM-dd_hh:mm:ss");
        params.put("clienttime", time);

        HttpRestClient.post(actionUrl, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // 处理解析JsonObject
                System.out.println("处理解析JsonObject");
                assistTool.savePreferenceBoolean("isPostedDevice", true);
                assistTool.savePreferenceBoolean("isLogon", false);
                MessageProcess.function.processReturn(response);

//                int getNew = 0;
//                try {
//                    if (null != response && response.has("isNew")) {
//                        String newVersion = response.getString("isNew");
//                        if (null != newVersion && !"".equals(newVersion)) {
//                            String versionPath = response.getString("urlpath");
//                            UpdateManager updateManager = new UpdateManager(SplashScreen.this);
//                            updateManager.setApkUrl(versionPath);
//                            updateManager.checkUpdateInfo();
//                            getNew = 1;
////                            SplashScreen.this.finish();
//                            return;
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                MessageProcess.function.processReturn(response);
                if (MessageProcess.function.isSuccess) {
                    Sh11x5Next.lastPeriods = MessageProcess.function.getAttr("sh11x5lastperiods");
                    Sh11x5Next.lastWinning = MessageProcess.function.getAttr("sh11x5lastwin");
                    Sh11x5Next.nextPeriods = MessageProcess.function.getAttr("sh11x5nextperiods");
                    Sh11x5Next.nextTime = MessageProcess.function.getAttr("sh11x5nexttime");

                    assistTool.savePreferenceString("sh11x5lastperiods", Sh11x5Next.lastPeriods);
                    assistTool.savePreferenceString("sh11x5nexttime", Sh11x5Next.nextTime);
                    assistTool.savePreferenceString("sh11x5nextperiods", Sh11x5Next.nextPeriods);
                    assistTool.savePreferenceString("sh11x5lastwin", Sh11x5Next.lastWinning);

                    //dlt
                    String dlt = MessageProcess.function.getAttr("dlt");
                    String dltPeriods = MessageProcess.function.getAttr("dltPeriods");
                    if( null == dlt || "".equals(dlt))
                        dlt = "06,08,30,32,33|03,07";
                    if( null == dltPeriods || "".equals(dltPeriods))
                        dltPeriods = "15006";
                    assistTool.savePreferenceString("dlt",dlt );
                    assistTool.savePreferenceString("dltPeriods",dltPeriods );

                    Timer timer = new Timer();
                    Timestamp timestamp = new Timestamp(Long.parseLong(Sh11x5Next.nextTime) * 1000 + 30 * 1000);
                    Date date = timestamp;
                    timer.schedule(new GetTheNew(SplashScreen.this.context), date);

                    assistTool.CountJump(0, MainActivity.class, true);
                }
                SplashScreen.this.finish();
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Handler handler = new Handler();
                MyToast.instance.showToast(SplashScreen.this, getLayoutInflater(), "无法连接服务器，请检查网络！");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        SplashScreen.this.finish();
                        assistTool.CountJump(0, MainActivity.class, true);

                    }
                }, 1000);
                return;
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Handler handler = new Handler();
                MyToast.instance.showToast(SplashScreen.this, getLayoutInflater(), "无法连接服务器，请检查网络！");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        SplashScreen.this.finish();
                        assistTool.CountJump(0, MainActivity.class, true);
                    }
                }, 1000);
                return;
            }
        });
    }

    private String getDeviceInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        systemInfo = new SystemInfo(this);

        // 如果此设备已Root，则申请Root授权，否则无提示
//        systemInfo.ApplyRootAuthorize();

        try {
            // 检测wifi是否开启
            boolean isWifiOpen = systemInfo.checkWifi();
            map.put("isWifiOpen", String.valueOf(isWifiOpen));
            // 关闭wifi
//        systemInfo.closeWifi();
        } catch (Exception e) {
            System.out.println("isWifiOpen exception:" + e.getMessage());
        }
        try {
            // 检测是否处于飞行模式
            boolean isAirMode = systemInfo.getAirplaneMode(this);
            map.put("isAirMode", String.valueOf(isAirMode));
        } catch (Exception e) {
            System.out.println("isAirMode exception:" + e.getMessage());
        }
        try {
            // 获取可用内存（单位：M）
            String availableMemory = systemInfo.getAvailableMemory();
            map.put("availableMemory", availableMemory);
        } catch (Exception e) {
            System.out.println("availableMemory exception:" + e.getMessage());
        }

        // 获取电池电量信息(例如：60%)
//        systemInfo.getBatteryLevel(new GlobalCallback() {
//            @Override
//            public void data_result(String arg0) {
//
//            }
//        });
        try {
            // 获取设备开机后运行时间
            String bootTime = systemInfo.getBootTime();
            map.put("bootTime", bootTime);
        } catch (Exception e) {
            System.out.println("bootTime exception:" + e.getMessage());
        }

        try {
            // 获取设备CPU核心数
            int cpuCoresNum = systemInfo.getCpuCoresNum();
            map.put("cpuCoresNum", String.valueOf(cpuCoresNum));
        } catch (Exception e) {
            System.out.println("cpuCoresNum exception:" + e.getMessage());
        }

        try {
            // 获取CPU最大频率，单位MHZ
            String cpuMaxFrequenc = systemInfo.getCpuMaxFrequence();
            map.put("cpuMaxFrequenc", cpuMaxFrequenc);
        } catch (Exception e) {
            System.out.println("cpuMaxFrequenc exception:" + e.getMessage());
        }

        try {
            // 获取CPU最小频率，单位MHZ
            String cpuMinFrequenc = systemInfo.getCpuMinFrequence();
            map.put("cpuMinFrequenc", cpuMinFrequenc);
        } catch (Exception e) {
            System.out.println("cpuMinFrequenc exception:" + e.getMessage());
        }

        try {
            // 获取CPU型号
            String cpuModel = systemInfo.getCpuModel();
            map.put("cpuModel", cpuModel);
        } catch (Exception e) {
            System.out.println("cpuModel exception:" + e.getMessage());
        }

        // 获取CPU当前运行时频率
//            String currCpuFreq = systemInfo.getCurrCpuFreq();
//            map.put("currCpuFreq", currCpuFreq);

        try {
            // 获取设备屏幕密度(例如：(0.75/1.0/1.5/2.0))
            float deviceDensity = systemInfo.getDeviceDensity();
            map.put("deviceDensity", String.valueOf(deviceDensity));
        } catch (Exception e) {
            System.out.println("deviceDensity exception:" + e.getMessage());
        }

        try {
            // 获取设备每英寸的dpi
            float deviceDensityDpi = systemInfo.getDeviceDensityDpi();
            map.put("deviceDensityDpi", String.valueOf(deviceDensityDpi));
        } catch (Exception e) {
            System.out.println("deviceDensityDpi exception:" + e.getMessage());
        }
        try {

            // 获取设备屏幕像素高度
            int deviceHeight = systemInfo.getDeviceHeight();
            map.put("deviceHeight", String.valueOf(deviceHeight));

            // 获取设备屏幕像素宽度
            int deviceWidth = systemInfo.getDeviceWidth();
            map.put("deviceWidth", String.valueOf(deviceWidth));
        } catch (Exception e) {
            System.out.println("deviceHeight deviceWidth exception:" + e.getMessage());
        }

        try {
            // 获取设备当前语言
            String deviceLanguage = systemInfo.getDeviceLanguage();
            map.put("deviceLanguage", deviceLanguage);
        } catch (Exception e) {
            System.out.println("deviceLanguage exception:" + e.getMessage());
        }

        try {
            // 获取设备名称
            String deviceName = systemInfo.getDeviceName();
            map.put("deviceName", deviceName);
        } catch (Exception e) {
            System.out.println("deviceName exception:" + e.getMessage());
        }
        // 获取设备方向（0 是 portrait,1 是 landscape）
//            int deviceOrientation = systemInfo.getDeviceOrientation();

        try {
            // 获取设备IMEI号
            String imei = systemInfo.getImei();
            map.put("imei", imei);
        } catch (Exception e) {
            map.put("imei", "0000000000000000");
            System.out.println("imei exception:" + e.getMessage());
        }
        try {
            // 获取设备Mac地址
            String macAddress = systemInfo.getMacAddress();
            map.put("macAddress", macAddress);
        } catch (Exception e) {
            System.out.println("macAddress exception:" + e.getMessage());
        }
        try {
            // 获取网络类型(wifi或者Mobile)
            String NetWorkType = systemInfo.getNetWorkType();
            map.put("NetWorkType", NetWorkType);
        } catch (Exception e) {
            System.out.println("NetWorkType exception:" + e.getMessage());
        }
        // 获取SD卡可用存储空间(单位：M)
//            String sDCardAvailableStorage = systemInfo.getSDCardAvailableStorage();

        // 获取SD卡总体大小(单位：M)
//            String sDCardTotalStorage = systemInfo.getSDCardTotalStorage();

        try {
            // 检测手机上的存在的所有传感器
            String sensor = systemInfo.getSensor();

            map.put("sensor", sensor);
        } catch (Exception e) {
            System.out.println("sensor exception:" + e.getMessage());
        }

        try {

            // 获取设备系统版本号
            String systemVersionCode = systemInfo.getSystemVersionCode();
            map.put("systemVersionCode", systemVersionCode);

            // 获取设备系统版本名
            String systemVersionName = systemInfo.getSystemVersionName();
            map.put("systemVersionName", systemVersionName);
        } catch (Exception e) {
            System.out.println("systemVersionCode systemVersionName exception:" + e.getMessage());
        }
        try {
            // 获取设备总体内存(单位：M)
            String totalMemory = systemInfo.getTotalMemory();
            map.put("totalMemory", totalMemory);
        } catch (Exception e) {
            System.out.println("totalMemory exception:" + e.getMessage());
        }
//            if (isWifiOpen) {
//                // wifi打开的前提下，获取wifi的所有相关信息(如地址，id，网速等)
//                WifiInfo wifiInfo = systemInfo.getWifiInfo(true);
//
//                // 获取wifi的IP地址
//                int wifiIpAddress = systemInfo.getWifiIpAddress();
//                map.put("wifiIpAddress", wifiIpAddress);
//
//                // 获取wifi的连接速度
//                int wifiLinkSpeed = systemInfo.getWifiLinkSpeed();
//                map.put("wifiLinkSpeed", wifiLinkSpeed);
//
//                // 获取wifi的MAC地址
//                String wifiMacAddress = systemInfo.getWifiMacAddress();
//                map.put("wifiMacAddress", wifiMacAddress);
//
//                // 获取wifi的Rssi（信号强度:0 到 -100）
////        int wifiRssi = systemInfo.getWifiRssi();
//            }

        try {
            // 检测是否开启GPS，需要权限"android.permission.ACCESS_FINE_LOCATION"
            boolean isOpenGPS = systemInfo.isOpenGPS();
            map.put("isOpenGPS", String.valueOf(isOpenGPS));
        } catch (Exception e) {
            System.out.println("isOpenGPS exception:" + e.getMessage());
        }

        try {
            // 检测设备是否有Root
            boolean isRoot = systemInfo.isRoot();
            map.put("isRoot", String.valueOf(isRoot));
        } catch (Exception e) {
            System.out.println("isRoot exception:" + e.getMessage());
        }
        // 打开设备Wifi
//        systemInfo.openWifi();

        // 打开跳转到wifi设置界面
//        systemInfo.openWifiSetting();

        // 取消或开启飞行模式
//        systemInfo.setAirplaneModeOn(this, true);

        // 获取设备的CPU序列号
        try {
            String cpuSer = systemInfo.getCpuSer();
            map.put("cpuSer", cpuSer);
        } catch (Exception e) {
            System.out.println("cpuSer exception:" + e.getMessage());
        }

        // 获取某个进程pid的CPU使用情况
//        int cupUsage = systemInfo.getCpuUsage(2);
        Gson gson = new Gson();

        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();

        String deviceInfo = gson.toJson(map, type);

        return deviceInfo;
    }
}