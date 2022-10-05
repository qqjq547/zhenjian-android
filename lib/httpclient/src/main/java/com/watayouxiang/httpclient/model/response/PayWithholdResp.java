package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/18
 *     desc   :
 * </pre>
 */
public class PayWithholdResp {

    /**
     * walletId : 6288883250000000103
     * amount : 11
     * serialnumber : 1329299153347559424
     * arrivalAmount : 11
     * merchantId : 890000595
     * orderStatus : INIT
     * token : 20201119543395514073150146072576
     * createDateTime : 2020-11-19 13:43:09
     */

    /**
     * 新生支付
     * <p>
     * 2021-03-17 11:41:34.612 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-17 11:41:34.612 I/TioHttpClient: 	| {
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|     "data": {
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "agrno": "202103160001270162",
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "amount": "10",
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "appversion": "2.4.4",
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "arrivalamount": "10",
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "bizcreattime": "2021-03-17 00:00:00",
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "bizfee": 0,
     * 2021-03-17 11:41:34.613 I/TioHttpClient: 	|         "device": 2,
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "id": 113,
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "ip": "115.227.197.169",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "merid": "300008795977",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "merorderid": "2021031711581918",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "merstatus": "-1",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "notifyurl": "https:\/\/tx.t-io.org\/mytio\/paycallback\/withhold.tio_x?uid=37878",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "ordererrormsg": "",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "reqid": "T002_37878_20210317114133",
     * 2021-03-17 11:41:34.614 I/TioHttpClient: 	|         "status": -1,
     * 2021-03-17 11:41:34.615 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-17 11:41:34.615 I/TioHttpClient: 	|         "walletid": "100011563457"
     * 2021-03-17 11:41:34.615 I/TioHttpClient: 	|     },
     * 2021-03-17 11:41:34.615 I/TioHttpClient: 	|     "ok": true
     * 2021-03-17 11:41:34.615 I/TioHttpClient: 	| }
     * 2021-03-17 11:41:34.615 I/TioHttpClient: 	--------------------------------------------------
     */

    private String walletId;
    private String amount;
    private String serialnumber;
    private String arrivalAmount;
    private String merchantId;
    private String orderStatus;
    private String token;
    private String createDateTime;

    public String id;
    public String reqid;

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

    public String getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(String arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
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
