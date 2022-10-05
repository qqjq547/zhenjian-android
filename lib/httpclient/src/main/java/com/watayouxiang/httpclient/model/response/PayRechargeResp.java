package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/17
 *     desc   :
 * </pre>
 */
public class PayRechargeResp {
    /**
     * walletId : 6288883250000000103
     * amount : 100
     * serialnumber : 1328638214524776448
     * merchantId : 890000595
     * orderStatus : INIT
     * token : 20201117935516513412211356848128
     * createDateTime : 2020-11-17 17:56:49
     */

    /**
     * 新生支付
     *
     * 2021-03-16 15:00:01.776 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-16 15:00:01.777 I/TioHttpClient: 	| {
     * 2021-03-16 15:00:01.777 I/TioHttpClient: 	|     "data": {
     * 2021-03-16 15:00:01.777 I/TioHttpClient: 	|         "agrno": "202103160001267012",
     * 2021-03-16 15:00:01.777 I/TioHttpClient: 	|         "amount": "10",
     * 2021-03-16 15:00:01.777 I/TioHttpClient: 	|         "appversion": "2.4.4",
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "bizcreattime": "2021-03-16 15:00:01",
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "device": 2,
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "id": 194,
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "ip": "115.227.197.169",
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "merid": "300008795977",
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "merorderid": "2021031615194133",
     * 2021-03-16 15:00:01.778 I/TioHttpClient: 	|         "notifyurl": "https:\/\/tx.t-io.org\/mytio\/paycallback\/recharge.tio_x?uid=37878",
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|         "reqid": "T007_37878_20210316150001",
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|         "status": -1,
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|         "timeout": 5,
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|         "walletid": "100011563457"
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|     },
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	|     "ok": true
     * 2021-03-16 15:00:01.779 I/TioHttpClient: 	| }
     * 2021-03-16 15:00:01.780 I/TioHttpClient: 	--------------------------------------------------
     */

    private String walletId;
    private String amount;
    private String serialnumber;
    private String merchantId;
    private String orderStatus;
    private String token;
    private String createDateTime;
    // 商户订单号
    public String merorderid;
    // 预下单订单id
    public String id;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        this.createDateTime = createDateTime;
    }
}
