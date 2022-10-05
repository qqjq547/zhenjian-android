package com.watayouxiang.httpclient.model.response;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/25
 *     desc   :
 * </pre>
 */
public class PayRedInfoResp {
    /**
     * 新生支付
     *
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	| {
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|     "data": {
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|         "grablist": [
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|             {
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "avatar": "\/user\/base\/avatar\/20200115\/7\/1432571217333833301630976.png",
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "cny": "2",
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "grabtime": "2021-03-19 14:34:35",
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "id": 490,
     * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "nick": "wata",
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "rid": 63,
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "uid": 37878,
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "walletid": "100011563457"
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|             },
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|             {
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "avatar": "\/user\/base\/avatar\/20210319108\/0931501372722447232671744.png",
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "cny": "8",
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "grabtime": "2021-03-19 10:05:34",
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "id": 487,
     * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "nick": "胆大包天",
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|                 "rid": 63,
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|                 "uid": 37887,
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|                 "walletid": "100011759402"
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             }
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|         ],
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|         "info": {
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "acceptnum": 2,
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "avatar": "\/user\/base\/avatar\/20210319108\/0931501372722447232671744.png",
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "bizcompletetime": "2021-03-19 10:05:29",
     * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "bizcreattime": "2021-03-19 10:05:00",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "bless": "恭喜发财，吉祥如意",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "chatbizid": "666",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "chatmode": 2,
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "cny": "10",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "id": 63,
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "mode": 2,
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "nick": "胆大包天",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "num": 2,
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "remark": "",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "reqid": "T007_37887_20210319100500",
     * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "status": 1,
     * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|             "uid": 37887
     * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|         }
     * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|     },
     * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|     "ok": true
     * 2021-03-19 14:42:38.459 I/TioHttpClient: 	| }
     * 2021-03-19 14:42:38.459 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-19 14:42:38.459 I/TioHttpClient: <-- END HTTP
     */

    /**
     * grablist : [{"amount":1,"avatar":"/user/avatar/22/9010/1119563/88097616/74541310984/27/110231/1254969215140634624_sm.jpg","bizcompletetime":"2020-11-25 14:52:38","id":38,"nick":"wata","serialnumber":"1331485708040413184","walletid":"6288883250000000103"}]
     * info : {"amount":2,"avatar":"/user/avatar/26/9014/1119567/88097620/74541310988/32/191939/1236974927777767424_sm.jpeg","bizcompletetime":"","bizcreattime":"2020-11-25 14:31:38","id":86,"mode":1,"nick":"貌比潘安","packetcount":2,"receivedamount":0,"receivedcount":1,"remark":"恭喜发财，大吉大利","serialnumber":"1331485678990663680","status":"SEND","uid":23440}
     */

    private InfoBean info;
    private List<GrablistBean> grablist;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<GrablistBean> getGrablist() {
        return grablist;
    }

    public void setGrablist(List<GrablistBean> grablist) {
        this.grablist = grablist;
    }

    public static class InfoBean {

        /**
         * 新生支付
         *
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|         "info": {
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "acceptnum": 2,
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "avatar": "\/user\/base\/avatar\/20210319108\/0931501372722447232671744.png",
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "bizcompletetime": "2021-03-19 10:05:29",
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             "bizcreattime": "2021-03-19 10:05:00",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "bless": "恭喜发财，吉祥如意",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "chatbizid": "666",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "chatmode": 2,
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "cny": "10",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "id": 63,
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "mode": 2,
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "nick": "胆大包天",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "num": 2,
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "remark": "",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "reqid": "T007_37887_20210319100500",
         * 2021-03-19 14:42:38.458 I/TioHttpClient: 	|             "status": 1,
         * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|             "uid": 37887
         * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|         }
         * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|     },
         * 2021-03-19 14:42:38.459 I/TioHttpClient: 	|     "ok": true
         * 2021-03-19 14:42:38.459 I/TioHttpClient: 	| }
         * 2021-03-19 14:42:38.459 I/TioHttpClient: 	--------------------------------------------------
         */

        /**
         * amount : 2
         * avatar : /user/avatar/26/9014/1119567/88097620/74541310988/32/191939/1236974927777767424_sm.jpeg
         * bizcompletetime :
         * bizcreattime : 2020-11-25 14:31:38
         * id : 86
         * mode : 1
         * chatmode : 2
         * nick : 貌比潘安
         * packetcount : 2
         * receivedamount : 0
         * receivedcount : 1
         * remark : 恭喜发财，大吉大利
         * serialnumber : 1331485678990663680
         * status : SEND
         * uid : 23440
         */

        /**
         * 红包总金额（分）
         */
        private int amount;
        public int cny;
        private String avatar;
        /**
         * 抢完红包的时间
         */
        private String bizcompletetime;
        public String starttime;
        public String endtime;
        /**
         * 创建时间
         */
        private String bizcreattime;
        private int id;
        /**
         * 1 普通，2 手气红包
         */
        private int mode;
        /**
         * 1 私聊，2 群聊
         */
        private int chatmode;
        private String nick;
        /**
         * 红包个数
         */
        private int packetcount;
        public int num;
        /**
         * 已抢金额（分）
         */
        private int receivedamount;
        /**
         * 已抢红包个数
         */
        private int receivedcount;
        /**
         * 祝福语
         */
        private String remark;
        public String bless;
        private String serialnumber;
        /**
         * 红包状态：
         * <p>
         * SUCCESS-已抢完;
         * TIMEOUT-24小时超时;
         * SEND-抢红包中
         */
        private String status;
        /**
         * 发红包人uid
         */
        private int uid;

        public int getChatmode() {
            return chatmode;
        }

        public void setChatmode(int chatmode) {
            this.chatmode = chatmode;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBizcompletetime() {
            return bizcompletetime;
        }

        public void setBizcompletetime(String bizcompletetime) {
            this.bizcompletetime = bizcompletetime;
        }

        public String getBizcreattime() {
            return bizcreattime;
        }

        public void setBizcreattime(String bizcreattime) {
            this.bizcreattime = bizcreattime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getMode() {
            return mode;
        }

        public void setMode(int mode) {
            this.mode = mode;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public int getPacketcount() {
            return packetcount;
        }

        public void setPacketcount(int packetcount) {
            this.packetcount = packetcount;
        }

        public int getReceivedamount() {
            return receivedamount;
        }

        public void setReceivedamount(int receivedamount) {
            this.receivedamount = receivedamount;
        }

        public int getReceivedcount() {
            return receivedcount;
        }

        public void setReceivedcount(int receivedcount) {
            this.receivedcount = receivedcount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
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

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }

    public static class GrablistBean {

        /**
         * 新生支付
         *
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	--------------------------------------------------
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	| {
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|     "data": {
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|         "grablist": [
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|             {
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "avatar": "\/user\/base\/avatar\/20200115\/7\/1432571217333833301630976.png",
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "cny": "2",
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "grabtime": "2021-03-19 14:34:35",
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "id": 490,
         * 2021-03-19 14:42:38.455 I/TioHttpClient: 	|                 "nick": "wata",
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "rid": 63,
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "uid": 37878,
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "walletid": "100011563457"
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|             },
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|             {
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "avatar": "\/user\/base\/avatar\/20210319108\/0931501372722447232671744.png",
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "cny": "8",
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "grabtime": "2021-03-19 10:05:34",
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "id": 487,
         * 2021-03-19 14:42:38.456 I/TioHttpClient: 	|                 "nick": "胆大包天",
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|                 "rid": 63,
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|                 "uid": 37887,
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|                 "walletid": "100011759402"
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|             }
         * 2021-03-19 14:42:38.457 I/TioHttpClient: 	|         ],
         */

        /**
         * amount : 1
         * avatar : /user/avatar/22/9010/1119563/88097616/74541310984/27/110231/1254969215140634624_sm.jpg
         * bizcompletetime : 2020-11-25 14:52:38
         * id : 38
         * uid : 23436
         * nick : wata
         * serialnumber : 1331485708040413184
         * walletid : 6288883250000000103
         */

        /**
         * 金额（分）
         */
        private int amount;
        public int cny;
        private String avatar;
        /**
         * 领取的时间
         */
        private String bizcompletetime;
        public String grabtime;
        private int id;
        private int uid;
        private String nick;
        private String serialnumber;
        private String walletid;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBizcompletetime() {
            return bizcompletetime;
        }

        public void setBizcompletetime(String bizcompletetime) {
            this.bizcompletetime = bizcompletetime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
        }

        public String getWalletid() {
            return walletid;
        }

        public void setWalletid(String walletid) {
            this.walletid = walletid;
        }
    }
}
