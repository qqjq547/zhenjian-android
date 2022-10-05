package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/17
 *     desc   :
 * </pre>
 */
public class PayOpenFlagResp {

    /**
     * walletid : 6288883250000000103
     * uid : 23436
     * openflag : 1
     * openid : 5
     */

    /**
     * 新生支付
     *
     * 2021-03-15 14:32:03.699 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-15 14:32:03.699 I/TioHttpClient: 	| {
     * 2021-03-15 14:32:03.699 I/TioHttpClient: 	|     "data": {
     * 2021-03-15 14:32:03.699 I/TioHttpClient: 	|         "walletid": "100011563457",
     * 2021-03-15 14:32:03.699 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-15 14:32:03.700 I/TioHttpClient: 	|         "openflag": 1,
     * 2021-03-15 14:32:03.700 I/TioHttpClient: 	|         "openid": 91
     * 2021-03-15 14:32:03.700 I/TioHttpClient: 	|     },
     * 2021-03-15 14:32:03.700 I/TioHttpClient: 	|     "ok": true
     * 2021-03-15 14:32:03.700 I/TioHttpClient: 	| }
     * 2021-03-15 14:32:03.700 I/TioHttpClient: 	--------------------------------------------------
     *
     * paypwdflag 钱包支付密码设置开关：1：已；2：未
     */

    // 钱包支付密码设置开关：1：已；2：未
    public int paypwdflag;

    /**
     * 钱包id
     */
    private String walletid;
    /**
     * 用户uid
     */
    private int uid;
    /**
     * 开户状态：1：已开户；2：未开户
     */
    private int openflag;
    /**
     * 开户信息id
     */
    private int openid;

    public String getWalletid() {
        return walletid;
    }

    public void setWalletid(String walletid) {
        this.walletid = walletid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getOpenflag() {
        return openflag;
    }

    public void setOpenflag(int openflag) {
        this.openflag = openflag;
    }

    public int getOpenid() {
        return openid;
    }

    public void setOpenid(int openid) {
        this.openid = openid;
    }
}
