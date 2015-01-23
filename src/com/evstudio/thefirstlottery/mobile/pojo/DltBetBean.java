package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by zhouyong on 15-1-12.
 * 胡莹莹注释
 * 大乐透投注实体类
 */
public class DltBetBean implements Serializable {

    private String dltNumber;
    //投注类型：单式、复式
    private int dltStyle;
    //一注金额
    private String moneyTip;
    //投注金额
    private int iMoney;
    //注数
    private int iZhu;
    //倍数
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
