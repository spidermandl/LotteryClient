package com.evstudio.thefirstlottery.mobile.core;

/**
 * Created by ericren on 14-9-12.
 * 胡莹莹注释
 * 本类为彩票投注实体类，用作定义彩票投注信息
 */
public class BetItem {
	//彩票描述
    private String strDescription;
    //彩票名称
    private String strTilte;
    private int iDrawable;
    //下期开奖时间
    private String nextTime;
    private String lastPeriods;
    //近期开奖
    private String lastDraw;

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrTilte() {
        return strTilte;
    }

    public void setStrTilte(String strTilte) {
        this.strTilte = strTilte;
    }

    public int getiDrawable() {
        return iDrawable;
    }

    public void setiDrawable(int iDrawable) {
        this.iDrawable = iDrawable;
    }

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public String getLastPeriods() {
        return lastPeriods;
    }

    public void setLastPeriods(String lastPeriods) {
        this.lastPeriods = lastPeriods;
    }

    public String getLastDraw() {
        return lastDraw;
    }

    public void setLastDraw(String lastDraw) {
        this.lastDraw = lastDraw;
    }

}
