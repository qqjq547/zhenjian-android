package com.tiocloud.newpay.feature.bankcard_my.adapter;

import com.watayouxiang.httpclient.model.response.PayBankCardListResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   :
 * </pre>
 */
public class CardItem {

    // 银行名称：建设银行
    private String bankName = null;
    // 银行卡 logo 链接
    private String bankLogo = null;
    // 银行卡 logo背景 链接
    private String bankLogoBg = null;
    // 银行卡类型：储蓄卡/信用卡
    private String bankCardType = null;
    // 银行卡号：**** **** **** 2178
    private String bankCardNo = null;
    // 银行卡背景色："#1EA2FB"
    private String bankCardBgColor = null;
    // 原始数据
    private PayBankCardListResp.Data originData;

    public String getBankCardType() {
        return bankCardType;
    }

    public void setBankCardType(String bankCardType) {
        this.bankCardType = bankCardType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getBankLogo() {
        return bankLogo;
    }

    public void setBankLogo(String bankLogo) {
        this.bankLogo = bankLogo;
    }

    public String getBankLogoBg() {
        return bankLogoBg;
    }

    public void setBankLogoBg(String bankLogoBg) {
        this.bankLogoBg = bankLogoBg;
    }

    public String getBankCardBgColor() {
        return bankCardBgColor;
    }

    public void setBankCardBgColor(String bankCardBgColor) {
        this.bankCardBgColor = bankCardBgColor;
    }

    public void setOriginData(PayBankCardListResp.Data data) {
        this.originData = data;
    }

    public PayBankCardListResp.Data getOriginData() {
        return originData;
    }
}
