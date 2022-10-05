package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   :
 * </pre>
 */
public class PayWithholdQueryResp {

    /**
     * walletId : 6288883250000000103
     * amount : 1
     * arrivalAmount : 1
     * serialnumber : 1332211228596113408
     * bankicon : https://tx.t-io.org/bank/default.png
     * bankcardnumber : 621***5341
     * reqid : 20201127143442727192269017740303
     * merchantId : 890000595
     * bankname : 中国邮政储蓄
     * bankcode : POST
     * status : PROCESS
     */

    /**
     * 新生支付
     * <p>
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	| {
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|     "data": {
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "amount": "1",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "arrivalamount": "1",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "backcolor": "#2BC5AF",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "bankcode": "PSBC",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "banklogo": "\/bankicon\/POST.png",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "bankname": "中国邮政储蓄银行",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "bankwatermark": "\/bankicon\/youzheng.png",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "cardno": "621799*********5341",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "id": 131,
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "merorderid": "2021031714643758",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "reqid": "T002_37878_20210317141608",
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "status": -1,
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|         "walletid": "100011563457"
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|     },
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	|     "ok": true
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	| }
     * 2021-03-17 14:16:09.909 I/TioHttpClient: 	--------------------------------------------------
     */

    private String walletId;
    private String amount;
    private String arrivalAmount;
    private String serialnumber;
    private String bankicon;
    private String bankcardnumber;
    private String reqid;
    private String merchantId;
    private String bankname;
    private String bankcode;
    private String status;

    public String arrivalamount;
    public String banklogo;
    public String cardno;

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

    public String getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(String arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
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

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
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
