package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by zyn on 15/1/12.
 */
public class SH11X5PushBean implements Serializable {
    private String bettype; // 0~12 代表打法
    private String betcontent; // 选择的号码
    private String betcount; // 选择的倍数

    public int getZhushu() {
        return zhushu;
    }

    public void setZhushu(int zhushu) {
        this.zhushu = zhushu;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    private  int zhushu = 0;
    private int money = 0;

    public void setBettype(String bettype) {
        this.bettype = bettype;
    }

    public void setBetcontent(String betcontent) {
        this.betcontent = betcontent;
    }

    public void setBetcount(String betcount) {
        this.betcount = betcount;
    }

    public String getBettype() {
        return bettype;
    }

    public String getBetcontent() {
        return betcontent;
    }

    public String getBetcount() {
        return betcount;
    }
}
