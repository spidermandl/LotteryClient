package com.evstudio.thefirstlottery.mobile.pojo;

/**
 * Created by ericren on 14-9-15.
 * 胡莹莹注释
 * 彩民实体类
 */
public class ClientUser {
    public static ClientUser user = new ClientUser();
    //用户id
    private String userid;
    //用户手机号
    private String mobile;
    //用户等级
    private String levelid;
    private String valid;
    //剩余金额
    private String remaining;
    //冻结资金
    private String blockedfund;

    public String getBlockedfund() {
        return blockedfund;
    }

    public void setBlockedfund(String blockedfund) {
        this.blockedfund = blockedfund;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLevelid() {
        return levelid;
    }

    public void setLevelid(String levelid) {
        this.levelid = levelid;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }
}
