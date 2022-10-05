package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   :
 * </pre>
 */
public class PayRechargeQueryResp {
    /**
     * walletId : 6288883250000000103
     * amount : 1
     * completeDateTime : 2020-11-27 11:22:41
     * serialnumber : 1332162865855082496
     * merchantId : 890000595
     * bankname : 中国邮政储蓄
     * bankicon : https://tx.t-io.org/bank/default.png
     * bankcardnumber : 621***5341
     * bankcode : POST
     * status : SUCCESS
     */

    /**
     * walletId : 6288883250000000103
     * amount : 25555500
     * completeDateTime : 2020-11-27 13:40:10
     * serialnumber : 1332197454241734656
     * merchantId : 890000595
     * bankname : 中国邮政储蓄
     * ordererrormsg : 交易金额或次数超过限制
     * bankicon : https://tx.t-io.org/bank/default.png
     * bankcardnumber : 621***5341
     * bankcode : POST
     * status : FAIL
     */

    /**
     * 新生支付
     *
     * 2021-03-16 15:48:08.076 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-16 15:48:08.076 I/TioHttpClient: 	| {
     * 2021-03-16 15:48:08.076 I/TioHttpClient: 	|     "data": {
     * 2021-03-16 15:48:08.076 I/TioHttpClient: 	|         "agrno": "202103160001267012",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "amount": "10",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "appversion": "2.4.4",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "bankcardnumber": "",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "bankcode": "PSBC",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "bankicon": "",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "bankname": "",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "bizcreattime": "2021-03-16 15:47:51",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "bizfee": 0,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "checkdate": "",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "checkflag": 2,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "coinsyn": 2,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "createtime": "2021-03-16 15:47:51",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "device": 2,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "id": 199,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "ip": "115.227.197.169",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "merfee": 0,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "merid": "300008795977",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "merorderid": "2021031615215167",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "merstatus": "1",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "notifyurl": "https:\/\/tx.t-io.org\/mytio\/paycallback\/recharge.tio_x?uid=37878",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "ordererrormsg": "",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "querysyn": 2,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "recvacctamount": "0",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "reqid": "T007_37878_20210316154751",
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "status": 1,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "timeout": 5,
     * 2021-03-16 15:48:08.077 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-16 15:48:08.078 I/TioHttpClient: 	|         "updatetime": "2021-03-16 15:48:08",
     * 2021-03-16 15:48:08.078 I/TioHttpClient: 	|         "walletid": "100011563457"
     * 2021-03-16 15:48:08.078 I/TioHttpClient: 	|     },
     * 2021-03-16 15:48:08.078 I/TioHttpClient: 	|     "ok": true
     * 2021-03-16 15:48:08.078 I/TioHttpClient: 	| }
     * 2021-03-16 15:48:08.078 I/TioHttpClient: 	--------------------------------------------------
     */

    private String walletId;
    private String amount;
    private String completeDateTime;
    private String serialnumber;
    private String merchantId;
    private String bankname;
    private String ordererrormsg;
    private String bankicon;
    private String bankcardnumber;
    private String bankcode;
    // 新生支付：1成功，2处理中，3失败
    private String status;

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

    public String getCompleteDateTime() {
        return completeDateTime;
    }

    public void setCompleteDateTime(String completeDateTime) {
        this.completeDateTime = completeDateTime;
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

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getOrdererrormsg() {
        return ordererrormsg;
    }

    public void setOrdererrormsg(String ordererrormsg) {
        this.ordererrormsg = ordererrormsg;
    }

    public String getBankicon() {
        return bankicon;
    }

    public void setBankicon(String bankicon) {
        this.bankicon = bankicon;
    }

    public String getBankcardnumber() {
        return bankcardnumber;
    }

    public void setBankcardnumber(String bankcardnumber) {
        this.bankcardnumber = bankcardnumber;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
