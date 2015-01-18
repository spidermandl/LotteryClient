package com.evstudio.thefirstlottery.mobile.activity;

import java.util.ArrayList;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 10;

    public static ArrayList<String> generateSampleData() {
        final ArrayList<String> data = new ArrayList<String>(SAMPLE_DATA_ITEM_COUNT);

        data.add("2014090944 07,04,03,05,06");
        data.add("2014090943 06,05,08,02,11");
        data.add("2014090942 04,02,08,03,10");
        data.add("2014090941 09,04,08,11,02");
        data.add("2014090940 09,04,08,06,07");
        data.add("2014090939 02,06,08,09,01");
        data.add("2014090938 03,06,05,02,10");
        data.add("2014090937 05,04,08,09,11");
        data.add("2014090936 01,04,08,07,02");
        data.add("2014090935 04,11,07,09,08");

//		for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
//			data.add("SAMPLE #");
//		}

        return data;
    }

}
