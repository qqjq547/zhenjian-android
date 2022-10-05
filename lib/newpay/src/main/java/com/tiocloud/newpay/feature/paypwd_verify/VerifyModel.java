package com.tiocloud.newpay.feature.paypwd_verify;

import android.graphics.Color;

import java.io.Serializable;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/09
 *     desc   :
 * </pre>
 */
public class VerifyModel implements Serializable {

    private boolean statusBar_show = false;
    private Integer statusBar_color = null;

    private boolean titleBar_show = false;
    private String titleBar_title = null;

    private Integer fragment_backgroundColor = null;

    public VerifyModel(boolean statusBar_show, Integer statusBar_color,
                       boolean titleBar_show, String titleBar_title,
                       Integer fragment_backgroundColor) {
        this.statusBar_show = statusBar_show;
        this.statusBar_color = statusBar_color;
        this.titleBar_show = titleBar_show;
        this.titleBar_title = titleBar_title;
        this.fragment_backgroundColor = fragment_backgroundColor;
    }

    public static VerifyModel getInstance_modifyPayPwd() {
        return new VerifyModel(
                true, Color.WHITE,
                true, "修改支付密码",
                null
        );
    }

    public static VerifyModel getInstance_addBankCard() {
        return new VerifyModel(
                false, null,
                false, null,
                Color.WHITE
        );
    }

    public static VerifyModel getInstance_removeBankCard() {
        return new VerifyModel(
                false, null,
                false, null,
                Color.WHITE
        );
    }

    // ====================================================================================
    // getter setter
    // ====================================================================================

    public boolean isStatusBar_show() {
        return statusBar_show;
    }

    public void setStatusBar_show(boolean statusBar_show) {
        this.statusBar_show = statusBar_show;
    }

    public Integer getStatusBar_color() {
        return statusBar_color;
    }

    public void setStatusBar_color(Integer statusBar_color) {
        this.statusBar_color = statusBar_color;
    }

    public boolean isTitleBar_show() {
        return titleBar_show;
    }

    public void setTitleBar_show(boolean titleBar_show) {
        this.titleBar_show = titleBar_show;
    }

    public String getTitleBar_title() {
        return titleBar_title;
    }

    public void setTitleBar_title(String titleBar_title) {
        this.titleBar_title = titleBar_title;
    }

    public Integer getFragment_backgroundColor() {
        return fragment_backgroundColor;
    }

    public void setFragment_backgroundColor(Integer fragment_backgroundColor) {
        this.fragment_backgroundColor = fragment_backgroundColor;
    }

}
