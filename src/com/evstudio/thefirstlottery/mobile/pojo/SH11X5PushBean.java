package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by zyn on 15/1/12.
 * 胡莹莹注释
 * 上海11选5：彩民投注的信息实体类
 */
public class SH11X5PushBean implements Serializable {
    private String bettype; // 0~12 代表打法
    private String betcontent; // 选择的号码
    private String betcount; // 选择的倍数
    private  int zhushu = 0; //注数
    private int money = 0;  //投注金额

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
