package com.watayouxiang.imclient.model.body.wx.msg;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 11/24/20
 *     desc   :
 * </pre>
 */
public class InnerMsgRed {
    /**
     * 红包订单号
     */
    public String serialnumber;

    /**
     * 红包文案
     */
    public String text;

    /**
     * 类型：1：普通红包；2：手气红包
     */
    public int mode;

    /**
     * 红包状态：
     * <p>
     * 易支付
     * SUCCESS-已抢完;
     * TIMEOUT-24小时超时;
     * SEND-抢红包中
     * <p>
     * 新生支付
     * 1发送中，5抢完了，6超时
     */
    public String status;
    public List<String> uids;
    /**
     * 红包id
     */
    public String rid;

    public String getStatus_newPay2payEase() {

        if ("1".equals(status)) {
            return "SEND";
        } else if ("5".equals(status)) {
            return "SUCCESS";
        } else if ("6".equals(status)) {
            return "TIMEOUT";
        }
        return status;
    }
}
