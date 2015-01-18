package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by zhouyong on 15-1-12.
 */
public class DltBetBean implements Serializable {

    private String dltNumber;
    private int dltStyle;
    private String moneyTip;
    private int iMoney;
    private int iZhu;
    private int iBeishu;

    public int getiBeishu() {
        return iBeishu;
    }

    public void setiBeishu(int iBeishu) {
        this.iBeishu = iBeishu;
    }

    public int getiMoney() {
        return iMoney;
    }

    public void setiMoney(int iMoney) {
        this.iMoney = iMoney;
    }

    public int getiZhu() {
        return iZhu;
    }

    public void setiZhu(int iZhu) {
        this.iZhu = iZhu;
    }

    public String getDltNumber() {
        return dltNumber;
    }

    public void setDltNumber(String dltNumber) {
        this.dltNumber = dltNumber;
    }

    public int getDltStyle() {
        return dltStyle;
    }

    public void setDltStyle(int dltStyle) {
        this.dltStyle = dltStyle;
    }

    public String getMoneyTip() {
        return moneyTip;
    }

    public void setMoneyTip(String moneyTip) {
        this.moneyTip = moneyTip;
    }
}
