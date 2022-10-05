package com.tiocloud.newpay.feature.bill.fragment.adapter;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.httpclient.model.response.PayGetWalletItemsResp;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/04
 *     desc   :
 * </pre>
 */
public class BillItem {

    private String title;
    private String subtitle;
    private String rightTitle;
    private String rightTitleTextColor;
    private String rightSubtitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = StringUtils.null2Length0(title);
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = StringUtils.null2Length0(subtitle);
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public void setRightTitle(PayGetWalletItemsResp.ListBean bean) {
        String money = MoneyUtils.fen2yuan(bean.cny + "");
        boolean isPlus = bean.getCoinflag() == 1;
        this.rightTitle = (isPlus ? "+" : "-") + money;
    }

    public String getRightTitleTextColor() {
        return rightTitleTextColor;
    }

    public void setRightTitleTextColor(PayGetWalletItemsResp.ListBean bean) {
        // 订单状态：SUCCESS;FAIL;PROCESS
        String status = bean.getStatus_newPay2payEase();
        boolean isPlus = bean.getCoinflag() == 1;
        String temp;
        if ("PROCESS".equals(status) || "SUCCESS".equals(status)) {
            if (isPlus) {
                temp = "#4C94FF";
            } else {
                temp = "#333333";
            }
        } else {
            temp = "#999999";
        }
        this.rightTitleTextColor = temp;
    }

    public String getRightSubtitle() {
        return rightSubtitle;
    }

    public void setRightSubtitle(PayGetWalletItemsResp.ListBean bean) {
        // 订单状态：SUCCESS;FAIL;PROCESS
        String orderStatus = bean.getStatus_newPay2payEase();
        String temp = "";
        if ("FAIL".equals(orderStatus)) {
            temp += "失败";
        } else if ("PROCESS".equals(orderStatus)) {
            temp = "处理中";
        } else if ("SUCCESS".equals(orderStatus)) {
            temp = "";
        } else {
            temp = String.format(Locale.getDefault(), "订单状态(%s)", orderStatus);
        }
        this.rightSubtitle = temp;
    }
}
