/*
 * pxqvlupbzmzkxk本软件由武陟县美塔信息科技有限公司采购自杭州钛特云科技有限公司，武陟县美塔信息科技有限公司需严格遵守合同，不得以任何形式转卖源代码，不得利用本软件从事违法犯罪活动dczsjr
 * grantinfo
 */
package com.tiocloud.newpay.temp;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 开户请求Vo
 *
 * @author xufei
 * 2020年11月2日 下午7:07:44
 */
public class OpenUserVo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4990253834557702763L;

    /**
     * uid-Y
     */
    private Integer uid;

    /**
     * 姓名-Y
     */
    private String name;


    /**
     * 证件号码-Y
     */
    private String cardno;

    /**
     * 手机号-Y
     */
    private String mobile;

    /**
     * mac地址-S
     */
    private String mac;

    /**
     * nickName-S
     */
    private String nickName;

    /**
     * 职业-N
     */
    private String profession = "A";

    /**
     * 证件类型:默认-IDCARD-N
     */
    private String cardtype = "IDCARD";

    /**
     * ip-N
     */
    private String ip;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getCardtype() {
        return cardtype;
    }

    public void setCardtype(String cardtype) {
        this.cardtype = cardtype;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return TextUtils.isEmpty(mac) ? "0.0.0.0" : mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
