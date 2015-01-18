package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by eric on 14/11/20.
 */
public class FootballInfoMix implements Serializable {
    public String gameNumber;
    public String matchName;
    public String matchColor;

    public String teamInfo;
    public String homeTeam;
    public String awayTeam;
    public String odd;

    public String sellTime;
    public String gid;

    public String[] oriPl = {"", "", "", ""};
    public String[] oddPl = {"", "", "", ""};

    public String[] scores = new String[32];

    public String[] goals = new String[9];

    public String[] halfWin = new String[10];

    public String isSelling = "1";

    public int[] selected = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0};
    public float[] odds = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0};

    public String[] gameTips = {
            "胜","平","负","胜","平","负","胜胜","胜平","胜负","平胜",
            "平平","平负","负胜","负平","负负","0","1","2","3","4",
            "5","6","7+","1:0","2:0","2:1","3:0","3:1","3:2","4:0",
            "4:1","4:2","5:0","5:1","5:2","胜其他","0:0","1:1","2:2","3:3",
            "平其他","0:1","0:2","1:2","0:3","1:3","2:3","0:4","1:4","2:4",
            "0:5","1:5","2:5","负其他"
    };

    public boolean isSelected() {
        int k = 0;
        for (int i = 0; i < selected.length; i++) {
            k += selected[i];
        }

        return k > 0;
    }

    public void initOdds() {
        for (int i = 0; i < 3; i++) {
            odds[i] = Float.parseFloat(oriPl[i]);
        }
        for (int i = 3; i < 6; i++) {
            odds[i] = Float.parseFloat(oddPl[i - 3]);
        }
        for (int i = 6; i < 15; i++) {
            odds[i] = Float.parseFloat(halfWin[i - 6]);
        }
        for (int i = 15; i < 23; i++) {
            odds[i] = Float.parseFloat(goals[i - 15]);
        }
        for (int i = 23; i < 54; i++) {
            odds[i] = Float.parseFloat(scores[i - 23]);
        }
    }

    public float getMaxPl() {
        float fmax = 0;
        initOdds();
        for (int i = 0; i < odds.length; i++) {
            if (selected[i] == 1) {
                if (fmax < odds[i])
                    fmax = odds[i];
            }
        }
        return fmax;
    }

    public float getMinPl() {
        float fmin = 0;
        initOdds();
        for (int i = 0; i < 6; i++) {
            if (selected[i] == 1) {
                if (fmin > odds[i])
                    fmin = odds[i];
            }
        }
        return fmin;
    }
}
