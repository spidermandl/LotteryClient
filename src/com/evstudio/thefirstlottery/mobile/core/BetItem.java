package com.evstudio.thefirstlottery.mobile.core;

/**
 * Created by ericren on 14-9-12.
 */
public class BetItem {
    private String strDescription;
    private String strTilte;
    private int iDrawable;
    private String nextTime;
    private String lastPeriods;
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
