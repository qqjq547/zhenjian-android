package com.watayouxiang.httpclient.model.response;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/26
 *     desc   :
 * </pre>
 */
public class PaySendRedPacketListResp {

    /**
     * firstPage : true
     * lastPage : true
     * list : [{"walletid":"6288883250000000103","chatmode":1,"remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201124154303038169451261334955","mode":2,"uid":23436,"receivedcount":0,"currency":"CNY","refundamount":1,"id":48,"bizcreattime":"2020-11-24 15:43:03","bizcompletetime":"","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 15:43:03","refundtype":"BALANCE","serialnumber":"1331141263105331200","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124053361515915259857715200","refundcount":1,"receivedamount":0,"bizid":"890000595","coinsyn":2,"debitdatetime":"","updatetime":"2020-11-25 15:43:10","device":2,"paymenttype":"","queuetime":"2020-11-25 15:43:10","status":"TIMEOUT"},{"walletid":"6288883250000000103","chatmode":1,"remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201124155441486159570425919363","mode":2,"uid":23436,"receivedcount":0,"currency":"CNY","refundamount":1,"id":51,"bizcreattime":"2020-11-24 15:54:41","bizcompletetime":"","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 15:54:41","refundtype":"BALANCE","serialnumber":"1331144192356913152","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124461494515918189042188288","refundcount":1,"receivedamount":0,"bizid":"890000595","coinsyn":2,"debitdatetime":"","updatetime":"2020-11-25 15:54:48","device":2,"paymenttype":"","queuetime":"2020-11-25 15:54:48","status":"TIMEOUT"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390756","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201124160403992101315518417935","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":53,"bizcreattime":"2020-11-24 16:04:04","bizcompletetime":"2020-11-24 17:41:58","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 16:04:04","refundtype":"","serialnumber":"1331146551887798272","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124691040515920548665348096","refundcount":0,"receivedamount":1,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 16:04:09","updatetime":"2020-11-24 17:41:58","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-24 17:41:58","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390780","remark":"666","localerrormsg":"","timeout":1440,"reqid":"20201124173854168106617908521727","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":63,"bizcreattime":"2020-11-24 17:38:54","bizcompletetime":"2020-11-24 17:44:29","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 17:38:54","refundtype":"","serialnumber":"1331170419419521024","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124518319515944416167710720","refundcount":0,"receivedamount":1,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 17:39:02","updatetime":"2020-11-24 17:44:29","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-24 17:44:29","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390785","remark":"$$$","localerrormsg":"","timeout":1440,"reqid":"20201124174845583161876425869641","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":73,"bizcreattime":"2020-11-24 17:48:45","bizcompletetime":"2020-11-24 17:48:57","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 17:48:45","refundtype":"","serialnumber":"1331172898924273664","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124054965515946895659876352","refundcount":0,"receivedamount":1,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 17:48:53","updatetime":"2020-11-24 17:48:56","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-24 17:48:56","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390788","remark":"x","localerrormsg":"","timeout":1440,"reqid":"20201124175250982113104154795268","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":74,"bizcreattime":"2020-11-24 17:52:51","bizcompletetime":"2020-11-24 17:54:06","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 17:52:51","refundtype":"","serialnumber":"1331173927942234112","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124446479515947924686229504","refundcount":0,"receivedamount":1,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 17:53:09","updatetime":"2020-11-24 17:54:06","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-24 17:54:06","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390792","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201124175619773107476114707851","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":75,"bizcreattime":"2020-11-24 17:56:19","bizcompletetime":"","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-24 17:56:20","refundtype":"","serialnumber":"1331174803842932736","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124292145515948800695975936","refundcount":0,"receivedamount":0,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 17:56:24","updatetime":"2020-11-24 17:56:27","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-24 17:56:27","status":"SEND"},{"walletid":"6288883250000000103","chatmode":2,"mgsid":"352033","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201124183710877190740699530319","mode":2,"uid":23436,"receivedcount":2,"currency":"CNY","refundamount":0,"id":82,"bizcreattime":"2020-11-24 18:37:11","bizcompletetime":"2020-11-25 11:11:16","chatbizid":"362","amount":2,"packetcount":1,"createtime":"2020-11-24 18:37:11","refundtype":"","serialnumber":"1331185084455329792","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124294064515959081241264128","refundcount":0,"receivedamount":2,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 18:37:17","updatetime":"2020-11-25 11:11:17","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-25 11:11:17","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":2,"mgsid":"352034","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201124184912260101647863933483","mode":2,"uid":23436,"receivedcount":2,"currency":"CNY","refundamount":0,"id":83,"bizcreattime":"2020-11-24 18:49:12","bizcompletetime":"2020-11-25 11:15:13","chatbizid":"362","amount":2,"packetcount":1,"createtime":"2020-11-24 18:49:12","refundtype":"","serialnumber":"1331188110226235392","ip":"60.177.221.26","appversion":"2.1.10","token":"20201124512016515962106898927616","refundcount":0,"receivedamount":2,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-24 18:49:23","updatetime":"2020-11-25 11:15:12","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-25 11:15:12","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390794","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201125104600293143166124329948","mode":1,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":84,"bizcreattime":"2020-11-25 10:46:00","bizcompletetime":"","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-25 10:46:01","refundtype":"","serialnumber":"1331428899149717504","ip":"60.177.221.26","appversion":"2.1.10","token":"20201125690082516202895948238848","refundcount":0,"receivedamount":0,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-25 10:46:08","updatetime":"2020-11-25 10:46:11","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-25 10:46:11","status":"SEND"},{"walletid":"6288883250000000103","chatmode":2,"mgsid":"352050","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201125180849324158594446838990","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":87,"bizcreattime":"2020-11-25 18:08:49","bizcompletetime":"","chatbizid":"362","amount":3,"packetcount":3,"createtime":"2020-11-25 18:08:49","refundtype":"","serialnumber":"1331540335582650368","ip":"183.156.73.201","appversion":"2.1.10","token":"20201125440804516314332322451456","refundcount":0,"receivedamount":0,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-2 5 18:09:07","updatetime":"2020-11-25 18:09:07","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-25 18:09:07","status":"SEND"},{"walletid":"6288883250000000103","chatmode":2,"mgsid":"352051","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201125180937084164064847221929","mode":1,"uid":23436,"receivedcount":2,"currency":"CNY","refundamount":0,"id":88,"bizcreattime":"2020-11-25 18:09:37","bizcompletetime":"2020-11-26 11:42:16","chatbizid":"362","amount":2,"packetcount":2,"createtime":"2020-11-25 18:09:37","refundtype":"","serialnumber":"1331540535839690752","ip":"183.156.73.201","appversion":"2.1.10","token":"20201125564975516314532436885504","refundcount":0,"receivedamount":2,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-25 18:09:46","updatetime":"2020-11-26 11:42:16","device":2,"paymenttype":"BANK_CARD","queuetime":"2020-11-26 11:42:16","status":"SUCCESS"},{"walletid":"6288883250000000103","chatmode":1,"mgsid":"390795","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201125181030124195311076716097","mode":1,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":89,"bizcreattime":"2020-11-25 18:10:30","bizcompletetime":"","chatbizid":"22627","amount":1,"packetcount":1,"createtime":"2020-11-25 18:10:30","refundtype":"","serialnumber":"1331540758238470144","ip":"183.156.73.201","appversion":"2.1.10","token":"20201125190721516314754982461440","refundcount":0,"receivedamount":0,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-25 18:10:42","updatetime":"2020-11-25 18:10:42","device":2,"paymenttype":"BANK_CARD","queuetime":"2020-11-25 18:10:42","status":"SEND"},{"walletid":"6288883250000000103","chatmode":2,"mgsid":"352054","remark":"恭喜发财，大吉大利","localerrormsg":"","timeout":1440,"reqid":"20201126104326814171929047642828","mode":2,"uid":23436,"receivedcount":1,"currency":"CNY","refundamount":0,"id":95,"bizcreattime":"2020-11-26 10:43:26","bizcompletetime":"2020-11-26 10:43:39","chatbizid":"362","amount":1,"packetcount":1,"createtime":"2020-11-26 10:43:26","refundtype":"","serialnumber":"1331790640945704960","ip":"183.156.73.201","appversion":"2.1.10","token":"20201126446033516564637790359552","refundcount":0,"receivedamount":1,"bizid":"890000595","coinsyn":2,"debitdatetime":"2020-11-26 10:43:37","updatetime":"2020-11-26 10:43:39","device":2,"paymenttype":"BALANCE","queuetime":"2020-11-26 10:43:39","status":"SUCCESS"}]
     * pageNumber : 1
     * pageSize : 16
     * totalPage : 1
     * totalRow : 14
     */

    private boolean firstPage;
    private boolean lastPage;
    private int pageNumber;
    private int pageSize;
    private int totalPage;
    private int totalRow;
    private List<ListBean> list;

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * walletid : 6288883250000000103
         * chatmode : 1
         * remark : 恭喜发财，大吉大利
         * localerrormsg :
         * timeout : 1440
         * reqid : 20201124154303038169451261334955
         * mode : 2
         * uid : 23436
         * receivedcount : 0
         * currency : CNY
         * refundamount : 1
         * id : 48
         * bizcreattime : 2020-11-24 15:43:03
         * bizcompletetime :
         * chatbizid : 22627
         * amount : 1
         * packetcount : 1
         * createtime : 2020-11-24 15:43:03
         * refundtype : BALANCE
         * serialnumber : 1331141263105331200
         * ip : 60.177.221.26
         * appversion : 2.1.10
         * token : 20201124053361515915259857715200
         * refundcount : 1
         * receivedamount : 0
         * bizid : 890000595
         * coinsyn : 2
         * debitdatetime :
         * updatetime : 2020-11-25 15:43:10
         * device : 2
         * paymenttype :
         * queuetime : 2020-11-25 15:43:10
         * status : TIMEOUT
         * mgsid : 390756
         */

        /**
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|             {
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "chatbizid": "666",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "chatmode": 2,
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "num": 2,
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "agrno": "",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "starttime": "2021-03-18 18:53:40",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "bless": "333",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "backtime": "2021-03-19 18:53:40",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "reqid": "T003_37878_20210318185339",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "cny": "3",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "mode": 2,
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "uid": 37878,
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "merorderid": "2021031818221985",
         * 2021-03-19 15:36:34.895 I/TioHttpClient: 	|                 "id": 51,
         * 2021-03-19 15:36:34.896 I/TioHttpClient: 	|                 "acceptnum": 1,
         * 2021-03-19 15:36:34.896 I/TioHttpClient: 	|                 "bizcreattime": "2021-03-18 00:00:00",
         * 2021-03-19 15:36:34.896 I/TioHttpClient: 	|                 "paytype": 1,
         * 2021-03-19 15:36:34.896 I/TioHttpClient: 	|                 "status": 1
         * 2021-03-19 15:36:34.896 I/TioHttpClient: 	|             },
         */

        private String walletid;
        private int chatmode;
        private String remark;
        private String localerrormsg;
        private int timeout;
        private String reqid;
        private int mode;
        private int uid;
        private int receivedcount;
        public int acceptnum;
        private String currency;
        private int refundamount;
        private int id;
        private String bizcreattime;
        public String starttime;
        private String bizcompletetime;
        private String chatbizid;
        private int amount;
        public int cny;
        private int packetcount;
        public int num;
        private String createtime;
        private String refundtype;
        private String serialnumber;
        private String ip;
        private String appversion;
        private String token;
        private int refundcount;
        private int receivedamount;
        private String bizid;
        private int coinsyn;
        private String debitdatetime;
        private String updatetime;
        private int device;
        private String paymenttype;
        private String queuetime;
        private String status;
        private String mgsid;

        public String getWalletid() {
            return walletid;
        }

        public void setWalletid(String walletid) {
            this.walletid = walletid;
        }

        public int getChatmode() {
            return chatmode;
        }

        public void setChatmode(int chatmode) {
            this.chatmode = chatmode;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getLocalerrormsg() {
            return localerrormsg;
        }

        public void setLocalerrormsg(String localerrormsg) {
            this.localerrormsg = localerrormsg;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public String getReqid() {
            return reqid;
        }

        public void setReqid(String reqid) {
            this.reqid = reqid;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getReceivedcount() {
            return receivedcount;
        }

        public void setReceivedcount(int receivedcount) {
            this.receivedcount = receivedcount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getRefundamount() {
            return refundamount;
        }

        public void setRefundamount(int refundamount) {
            this.refundamount = refundamount;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBizcreattime() {
            return bizcreattime;
        }

        public void setBizcreattime(String bizcreattime) {
            this.bizcreattime = bizcreattime;
        }

        public String getBizcompletetime() {
            return bizcompletetime;
        }

        public void setBizcompletetime(String bizcompletetime) {
            this.bizcompletetime = bizcompletetime;
        }

        public String getChatbizid() {
            return chatbizid;
        }

        public void setChatbizid(String chatbizid) {
            this.chatbizid = chatbizid;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public int getPacketcount() {
            return packetcount;
        }

        public void setPacketcount(int packetcount) {
            this.packetcount = packetcount;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getRefundtype() {
            return refundtype;
        }

        public void setRefundtype(String refundtype) {
            this.refundtype = refundtype;
        }

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getAppversion() {
            return appversion;
        }

        public void setAppversion(String appversion) {
            this.appversion = appversion;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getRefundcount() {
            return refundcount;
        }

        public void setRefundcount(int refundcount) {
            this.refundcount = refundcount;
        }

        public int getReceivedamount() {
            return receivedamount;
        }

        public void setReceivedamount(int receivedamount) {
            this.receivedamount = receivedamount;
        }

        public String getBizid() {
            return bizid;
        }

        public void setBizid(String bizid) {
            this.bizid = bizid;
        }

        public int getCoinsyn() {
            return coinsyn;
        }

        public void setCoinsyn(int coinsyn) {
            this.coinsyn = coinsyn;
        }

        public String getDebitdatetime() {
            return debitdatetime;
        }

        public void setDebitdatetime(String debitdatetime) {
            this.debitdatetime = debitdatetime;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getDevice() {
            return device;
        }

        public void setDevice(int device) {
            this.device = device;
        }

        public String getPaymenttype() {
            return paymenttype;
        }

        public void setPaymenttype(String paymenttype) {
            this.paymenttype = paymenttype;
        }

        public String getQueuetime() {
            return queuetime;
        }

        public void setQueuetime(String queuetime) {
            this.queuetime = queuetime;
        }

        public String getStatus() {
            return status;
        }

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

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMgsid() {
            return mgsid;
        }

        public void setMgsid(String mgsid) {
            this.mgsid = mgsid;
        }
    }
}
