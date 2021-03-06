package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by ericren on 14-11-4.
 * 胡莹莹注释
 * 足球比赛信息实体类
 */

public class FootballGameInfo implements Serializable{
    //id
    public String id;

    //date
    public String gameDate;
    //num
    public String gameNumber;
    //time
    public String beginTime;
    public String sellTime;

    //l_cn_abbr
    public String matchName;
    //h_cn_abbr
    public String hostTeam;
    //a_cn_abbr
    public String clientTeam;

    //主对胜算
    public float oddsHost;
    //客队胜算
    public float oddsClient;
    //d
    public float oddsDraw;

    //主让球胜算
    public float oddsAssignHost;
    //客让球胜算
    public float oddsAssignClient;
    //
    public float oddsAssignDraw;

    //fixedodds
    public String assignor;

    //l_background_color
    public String backgroundColor;

    //weather
    public String weather;

    public int[] selected = {0, 0, 0, 0, 0, 0};

    public float[] odds = {0f,0f,0f,0f,0f,0f};

    public void  initOdds (){
        odds[0] = oddsHost;
        odds[1] = oddsDraw;
        odds[2] = oddsClient;
        odds[3] = oddsAssignHost;
        odds[4] = oddsAssignDraw;
        odds[5] = oddsAssignClient;
    }

    public boolean isSelected() {
        int sumSelected = 0;
        for (int i = 0; i < 6; i++)
            sumSelected += selected[i];
        return sumSelected > 0;
    }

    public float getMaxPl(){    //获得最大赔率
        float fmax = 0;
        initOdds();
        for( int i = 0; i < 6; i ++ ){
            if( selected[i] == 1 ){
                if( fmax < odds[i] )
                    fmax = odds[i];
            }
        }
        return fmax;
    }

    public float getMinPl(){      //获得最小赔率
        float fmin = 0;
        initOdds();
        for( int i = 0; i < 6; i ++ ){
            if( selected[i] == 1 ){
                if( fmin > odds[i] )
                    fmin = odds[i];
            }
        }
        return fmin;
    }
}
