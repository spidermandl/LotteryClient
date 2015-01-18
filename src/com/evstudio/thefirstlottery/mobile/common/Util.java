package com.evstudio.thefirstlottery.mobile.common;

import android.content.Context;

import com.tandong.sa.tools.AssistTool;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ericren on 14-9-15.
 */
public class Util {
    private Util(){};
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat countDownFormat = new SimpleDateFormat("HH:mm:ss");
    public static void initApp(Context context){
        AssistTool assistTool = new AssistTool(context);
    }

    public static String getCountDown(long millis) {
        int iMinute = (int) ((millis / 1000) /60);
        int lSecond = (int) ((millis / 1000) %60);
        String strReturn = "";
        if( iMinute ==0  && lSecond == 0 ){
            strReturn = "等待开奖中";
        }else{
            strReturn = String.format("%02d:%02d",iMinute,lSecond);
        }
        return strReturn;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //unicode转汉字
    public static String unicode2zhConvert(String utfString){
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while((i=utfString.indexOf("\\u", pos)) != -1){
            sb.append(utfString.substring(pos, i));
            if(i+5 < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }

        return sb.toString();
    }

    public static void main( String[] argv){
        System.out.println( Util.unicode2zhConvert("\\u9632\\u5fa1\\u6b63\\u4e49"));
    }

    /**
     * 验证手机号.
     */
    public static boolean checkNumber(String number) {
        if (!isNumber(number)) {
            return false;
        }
        int len = number.length();
        if (len == 11) {
            if (number.matches("^13.*|^14.*|^15.*|^18.*")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 是否是数字.
     */
    public static boolean isNumber(String barcode) {
        if (barcode.replaceAll("[^0-9]", "").equals(barcode)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }
}
