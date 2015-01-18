package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by eric on 14/11/20.
 */
public class FootballTotoInfo implements Serializable {
    public String gameName;
    public String sellDate;
    public int gameNumberOfDay;
    public int gameNumber;
    public String[] teams = {"", ""};
    public String[] teamOdds = {"", "", ""};
    public String stopSellTime;
    public String homeGoals;
    public String awayGoals;
    public String gameResult;
    public int[] selected = {0, 0, 0};

    public boolean isSelected() {
        int sumSelected = 0;
        for (int i = 0; i < 3; i++)
            sumSelected += selected[i];
        return sumSelected > 0;
    }
}
