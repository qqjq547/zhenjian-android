package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/24
 *     desc   :
 * </pre>
 */
public class PayGrabRedPacketResp {

    /**
     * 新生支付
     *
     * 2021-03-19 14:39:21.780 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-19 14:39:21.780 I/TioHttpClient: 	| {
     * 2021-03-19 14:39:21.781 I/TioHttpClient: 	|     "data": {
     * 2021-03-19 14:39:21.781 I/TioHttpClient: 	|         "appversion": "2.4.4",
     * 2021-03-19 14:39:21.781 I/TioHttpClient: 	|         "bizcreattime": 1616083200000,
     * 2021-03-19 14:39:21.781 I/TioHttpClient: 	|         "chatbizid": "666",
     * 2021-03-19 14:39:21.782 I/TioHttpClient: 	|         "chatmode": 2,
     * 2021-03-19 14:39:21.782 I/TioHttpClient: 	|         "cny": "1",
     * 2021-03-19 14:39:21.782 I/TioHttpClient: 	|         "device": 2,
     * 2021-03-19 14:39:21.783 I/TioHttpClient: 	|         "grabtime": 1616135961751,
     * 2021-03-19 14:39:21.783 I/TioHttpClient: 	|         "id": 491,
     * 2021-03-19 14:39:21.783 I/TioHttpClient: 	|         "ip": "115.196.1.38",
     * 2021-03-19 14:39:21.784 I/TioHttpClient: 	|         "merid": "300008795977",
     * 2021-03-19 14:39:21.784 I/TioHttpClient: 	|         "merorderid": "2021031914593130",
     * 2021-03-19 14:39:21.784 I/TioHttpClient: 	|         "ordererrormsg": "",
     * 2021-03-19 14:39:21.784 I/TioHttpClient: 	|         "randomid": 52,
     * 2021-03-19 14:39:21.785 I/TioHttpClient: 	|         "remark": "",
     * 2021-03-19 14:39:21.785 I/TioHttpClient: 	|         "reqid": "T003_37878_20210319143921",
     * 2021-03-19 14:39:21.785 I/TioHttpClient: 	|         "rid": 51,
     * 2021-03-19 14:39:21.786 I/TioHttpClient: 	|         "senduid": 37878,
     * 2021-03-19 14:39:21.786 I/TioHttpClient: 	|         "sendwalletid": "100011563465",
     * 2021-03-19 14:39:21.786 I/TioHttpClient: 	|         "status": 1,
     * 2021-03-19 14:39:21.787 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-19 14:39:21.787 I/TioHttpClient: 	|         "walletid": "100011563457"
     * 2021-03-19 14:39:21.787 I/TioHttpClient: 	|     },
     * 2021-03-19 14:39:21.788 I/TioHttpClient: 	|     "ok": true
     * 2021-03-19 14:39:21.788 I/TioHttpClient: 	| }
     * 2021-03-19 14:39:21.788 I/TioHttpClient: 	--------------------------------------------------
     */

    /**
     * amount : 1
     * completeDateTime : 2020-11-24 14:53:33
     * serialnumber : 1331078729685929984
     * merchantId : 890000595
     * receiveWalletId : 6288883250000000103
     * status : SUCCESS
     * reqid : 13310786445667368961905733174708
     */

    // 抢到的红包总额
    private String amount;
    private String completeDateTime;
    // 订单号
    private String serialnumber;
    // 商户号
    private String merchantId;
    // 抢红包的钱包id
    private String receiveWalletId;
    // 订单状态
    private String status;
    private String reqid;

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

    public String getReceiveWalletId() {
        return receiveWalletId;
    }

    public void setReceiveWalletId(String receiveWalletId) {
        this.receiveWalletId = receiveWalletId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }
}
