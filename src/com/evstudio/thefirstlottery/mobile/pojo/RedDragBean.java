package com.evstudio.thefirstlottery.mobile.pojo;

import java.io.Serializable;

/**
 * Created by zhouyong on 15-1-8.
 */

public class RedDragBean implements Serializable {


    private int number;
    private boolean isSel;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setSel(boolean isSel) {
        this.isSel = isSel;
    }

    public boolean isSel() {
        return isSel;
    }
}
