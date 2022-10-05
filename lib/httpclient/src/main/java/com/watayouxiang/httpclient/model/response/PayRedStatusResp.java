package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/24
 *     desc   :
 * </pre>
 */
public class PayRedStatusResp {
    /**
     * 新生支付
     *
     * 2021-03-19 14:19:26.335 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-19 14:19:26.336 I/TioHttpClient: 	| {
     * 2021-03-19 14:19:26.336 I/TioHttpClient: 	|     "data": {
     * 2021-03-19 14:19:26.336 I/TioHttpClient: 	|         "openflag": 1,
     * 2021-03-19 14:19:26.337 I/TioHttpClient: 	|         "grabstatus": 2,
     * 2021-03-19 14:19:26.337 I/TioHttpClient: 	|         "redstatus": 1
     * 2021-03-19 14:19:26.337 I/TioHttpClient: 	|     },
     * 2021-03-19 14:19:26.338 I/TioHttpClient: 	|     "ok": true
     * 2021-03-19 14:19:26.338 I/TioHttpClient: 	| }
     * 2021-03-19 14:19:26.338 I/TioHttpClient: 	--------------------------------------------------
     */

    /**
     * openflag : 1
     * grabstatus : INIT
     * redstatus : SEND
     */

    /**
     * 用户是否开户：1：是；2：否
     */
    private int openflag;
    /**
     * 自己抢的状态：
     * <p>
     * 易支付：
     * INIT-未抢;
     * SUCCESS-已抢
     * <p>
     * 新生支付：
     * 1成功，2未抢
     */
    private String grabstatus;
    /**
     * 红包状态：
     * <p>
     * 易支付：
     * SUCCESS-已抢完;
     * TIMEOUT-24小时超时;
     * SEND-抢红包中
     * <p>
     * 新生支付：
     * 1发送中，5抢完了，6超时
     */
    private String redstatus;

    public int getOpenflag() {
        return openflag;
    }

    public void setOpenflag(int openflag) {
        this.openflag = openflag;
    }

    public String getGrabstatus() {
        return grabstatus;
    }

    public String getGrabstatus_newPay2payEase() {
        if ("1".equals(grabstatus)) {
            return "SUCCESS";
        } else if ("2".equals(grabstatus)) {
            return "INIT";
        }
        return grabstatus;
    }

    public void setGrabstatus(String grabstatus) {
        this.grabstatus = grabstatus;
    }

    public String getRedstatus() {
        return redstatus;
    }

    public void setRedstatus(String redstatus) {
        this.redstatus = redstatus;
    }

    public String getRedstatus_newPay2payEase() {
        if ("1".equals(redstatus)) {
            return "SEND";
        } else if ("5".equals(redstatus)) {
            return "SUCCESS";
        } else if ("6".equals(redstatus)) {
            return "TIMEOUT";
        }
        return redstatus;
    }
}
