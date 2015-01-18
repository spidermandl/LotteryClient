package com.evstudio.thefirstlottery.mobile.functions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ericren on 14-9-15.
 */
public class MessageProcess {
    public static MessageProcess function = new MessageProcess();

    private String returnCode;
    private String returnMsg;
    public JSONObject content;
    public boolean isSuccess = false;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public boolean processReturn(JSONObject jsonContent) {
        if( null != function )
            function = new MessageProcess();
        boolean bReturn = false;

        try {

            function.content = jsonContent;
            if(jsonContent.has("returncode")){
                function.returnCode = jsonContent.getString("returncode");
            }
            if(jsonContent.has("resultCode")){
                function.returnCode = jsonContent.getString("resultCode");
            }
            if(jsonContent.has("returnmsg")){
                function.returnMsg = jsonContent.getString("returnmsg");
            }
            if(jsonContent.has("result")){
                function.returnMsg = jsonContent.getString("result");
            }
            if (null != function.returnCode && "0".equals(function.returnCode))
                function.isSuccess = true;

            bReturn = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bReturn;
    }

    public String getAttr(String key) {
        String strValue = "";
        try {
            if (this.content.has(key))
                strValue = this.content.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return strValue;
    }
}
