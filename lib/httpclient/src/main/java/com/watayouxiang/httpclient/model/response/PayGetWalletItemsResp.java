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
public class PayGetWalletItemsResp {

    /**
     * firstPage : true
     * lastPage : false
     * list : [{"amount":9,"createtime":"2020-11-27 15:17:38","orderstatus":"SUCCESS","serialnumber":"1332222005457592320","bizstr":"充值","remark":"","othercny":0,"coinflag":1,"mode":1,"uid":23436,"bizid":114,"id":70,"bizcompletetime":"2020-11-27 15:17:38","bizcreattime":"2020-11-27 15:17:32","updatetime":"2020-11-27 15:17:38","status":1},{"amount":2,"createtime":"2020-11-27 15:17:15","orderstatus":"SUCCESS","serialnumber":"1332221884191879168","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":49,"id":69,"bizcompletetime":"2020-11-27 15:17:14","bizcreattime":"2020-11-27 15:17:03","updatetime":"2020-11-27 15:17:15","status":1},{"amount":3,"createtime":"2020-11-27 15:16:44","orderstatus":"SUCCESS","serialnumber":"1332221766264827904","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":46,"id":68,"bizcompletetime":"2020-11-27 15:16:45","bizcreattime":"2020-11-27 15:16:35","updatetime":"2020-11-27 15:16:44","status":1},{"amount":1,"createtime":"2020-11-27 15:14:05","orderstatus":"SUCCESS","serialnumber":"1332221097936031744","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":44,"id":67,"bizcompletetime":"2020-11-27 15:14:05","bizcreattime":"2020-11-27 15:13:56","updatetime":"2020-11-27 15:14:05","status":1},{"amount":2,"createtime":"2020-11-27 15:12:40","orderstatus":"SUCCESS","serialnumber":"1332220745580953600","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":43,"id":66,"bizcompletetime":"2020-11-27 15:12:40","bizcreattime":"2020-11-27 15:12:32","updatetime":"2020-11-27 15:12:40","status":1},{"amount":2,"createtime":"2020-11-27 15:04:24","orderstatus":"SUCCESS","serialnumber":"1332218652992344064","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":39,"id":65,"bizcompletetime":"2020-11-27 15:04:24","bizcreattime":"2020-11-27 15:04:13","updatetime":"2020-11-27 15:04:24","status":1},{"amount":1,"createtime":"2020-11-27 14:34:51","orderstatus":"SUCCESS","serialnumber":"1332211228596113408","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":38,"id":64,"bizcompletetime":"2020-11-27 14:34:51","bizcreattime":"2020-11-27 14:34:43","updatetime":"2020-11-27 14:34:51","status":1},{"amount":1,"createtime":"2020-11-27 14:26:46","orderstatus":"SUCCESS","serialnumber":"1332209195696660480","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":37,"id":63,"bizcompletetime":"2020-11-27 14:26:46","bizcreattime":"2020-11-27 14:26:38","updatetime":"2020-11-27 14:26:46","status":1},{"amount":1,"createtime":"2020-11-27 14:19:52","orderstatus":"SUCCESS","serialnumber":"1332207464753541120","bizstr":"充值","remark":"","othercny":0,"coinflag":1,"mode":1,"uid":23436,"bizid":112,"id":62,"bizcompletetime":"2020-11-27 14:19:52","bizcreattime":"2020-11-27 14:19:45","updatetime":"2020-11-27 14:19:52","status":1},{"amount":1658600,"createtime":"2020-11-27 14:19:28","orderstatus":"FAIL","serialnumber":"1332207365122035712","bizstr":"充值","remark":"交易金额或次数超过限制","othercny":0,"coinflag":1,"mode":1,"uid":23436,"bizid":111,"id":61,"bizcompletetime":"2020-11-27 14:19:28","bizcreattime":"2020-11-27 14:19:22","updatetime":"2020-11-27 14:19:28","status":2},{"amount":1,"createtime":"2020-11-27 14:16:42","orderstatus":"SUCCESS","serialnumber":"1331073304844447744","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":20,"id":46,"bizcompletetime":"2020-11-24 11:13:09","bizcreattime":"2020-11-24 11:13:01","updatetime":"2020-11-27 14:16:42","status":1},{"amount":1,"createtime":"2020-11-27 14:16:42","orderstatus":"SUCCESS","serialnumber":"1331072814656143360","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":19,"id":45,"bizcompletetime":"2020-11-24 11:11:13","bizcreattime":"2020-11-24 11:11:04","updatetime":"2020-11-27 14:16:42","status":1},{"amount":1,"createtime":"2020-11-27 14:16:42","orderstatus":"SUCCESS","serialnumber":"1331072120784044032","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":18,"id":44,"bizcompletetime":"2020-11-24 11:08:27","bizcreattime":"2020-11-24 11:08:18","updatetime":"2020-11-27 14:16:42","status":1},{"amount":1,"createtime":"2020-11-27 14:16:42","orderstatus":"SUCCESS","serialnumber":"1331071634253164544","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":17,"id":43,"bizcompletetime":"2020-11-24 11:06:32","bizcreattime":"2020-11-24 11:06:22","updatetime":"2020-11-27 14:16:42","status":1},{"amount":1,"createtime":"2020-11-27 14:16:42","orderstatus":"SUCCESS","serialnumber":"1331071486391361536","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":16,"id":42,"bizcompletetime":"2020-11-24 11:05:57","bizcreattime":"2020-11-24 11:05:47","updatetime":"2020-11-27 14:16:42","status":1},{"amount":1,"createtime":"2020-11-27 14:16:42","orderstatus":"SUCCESS","serialnumber":"1331071149257408512","bizstr":"提现","remark":"","othercny":0,"coinflag":2,"mode":2,"uid":23436,"bizid":15,"id":41,"bizcompletetime":"2020-11-24 11:04:36","bizcreattime":"2020-11-24 11:04:27","updatetime":"2020-11-27 14:16:42","status":1}]
     * pageNumber : 1
     * pageSize : 16
     * totalPage : 3
     * totalRow : 39
     */

    /**
     * 新生支付
     * <p>
     * {"data":{"firstPage":true,"lastPage":true,"list":[{"createtime":"2021-03-22 15:44:05","orderstatus":"SUCCESS","serialnumber":"","bizstr":"","remark":"提现","cny":"50","othercny":"0","coinflag":2,"reqid":"T002_37878_20210322154403","mode":2,"uid":37878,"bizid":136,"merorderid":"2021032215980357","id":816,"bizcompletetime":"2021-03-22 15:44:05","bizcreattime":"2021-03-22 15:44:04","updatetime":"2021-03-22 15:44:05","status":1},{"createtime":"2021-03-22 15:43:35","orderstatus":"SUCCESS","serialnumber":"","bizstr":"","remark":"抢红包","cny":"1","othercny":"0","coinflag":1,"reqid":"T003_37878_20210322154334","mode":3,"uid":37878,"bizid":506,"merorderid":"2021032215980140","id":815,"bizcompletetime":"2021-03-22 15:43:34","bizcreattime":"2021-03-22 15:43:34","updatetime":"2021-03-22 15:43:35","status":1},{"createtime":"2021-03-22 15:43:32","orderstatus":"SUCCESS","serialnumber":"","bizstr":"","remark":"发红包","cny":"1","othercny":"0","coinflag":2,"reqid":"T003_37878_20210322154331","mode":3,"uid":37878,"bizid":92,"merorderid":"2021032215980114","id":814,"bizcompletetime":"2021-03-22 15:43:32","bizcreattime":"2021-03-22 15:43:32","updatetime":"2021-03-22 15:43:32","status":1},{"createtime":"2021-03-22 15:43:15","orderstatus":"SUCCESS","serialnumber":"","bizstr":"","remark":"抢红包","cny":"1","othercny":"0","coinflag":1,"reqid":"T003_37878_20210322154315","mode":3,"uid":37878,"bizid":505,"merorderid":"2021032215979991","id":813,"bizcompletetime":"2021-03-22 15:43:15","bizcreattime":"2021-03-22 15:43:15","updatetime":"2021-03-22 15:43:15","status":1},{"createtime":"2021-03-22 15:43:10","orderstatus":"SUCCESS","serialnumber":"","bizstr":"","remark":"发红包","cny":"3","othercny":"0","coinflag":2,"reqid":"T003_37878_20210322154310","mode":3,"uid":37878,"bizid":91,"merorderid":"2021032215979955","id":812,"bizcompletetime":"2021-03-22 15:43:10","bizcreattime":"2021-03-22 15:43:10","updatetime":"2021-03-22 15:43:10","status":1},{"createtime":"2021-03-22 15:42:00","orderstatus":"1","serialnumber":"","bizstr":"","remark":"充值","cny":"30","othercny":"0","coinflag":1,"reqid":"T007_37878_20210322154145","mode":1,"uid":37878,"bizid":257,"merorderid":"2021032215979365","id":811,"bizcompletetime":"2021-03-22 15:42:00","bizcreattime":"2021-03-22 15:41:45","updatetime":"2021-03-22 15:42:00","status":1}],"pageNumber":1,"pageSize":16,"totalPage":1,"totalRow":6},"ok":true}
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
         * amount : 9
         * createtime : 2020-11-27 15:17:38
         * orderstatus : SUCCESS
         * serialnumber : 1332222005457592320
         * bizstr : 充值
         * remark :
         * othercny : 0.0
         * coinflag : 1
         * mode : 1
         * uid : 23436
         * bizid : 114
         * id : 70
         * bizcompletetime : 2020-11-27 15:17:38
         * bizcreattime : 2020-11-27 15:17:32
         * updatetime : 2020-11-27 15:17:38
         * status : 1
         */

        private int amount;
        public int cny;
        private String createtime;
        private String orderstatus;
        private String serialnumber;
        public String reqid;
        private String bizstr;
        private String remark;
        private double othercny;
        // 收支状态：1收入，2支出
        private int coinflag;
        // 类型：1充值，2提现，3红包
        private int mode;
        private int uid;
        private int bizid;
        private int id;
        private String bizcompletetime;
        private String bizcreattime;
        private String updatetime;
        private String status;

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(String orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getSerialnumber() {
            return serialnumber;
        }

        public void setSerialnumber(String serialnumber) {
            this.serialnumber = serialnumber;
        }

        public String getBizstr() {
            return bizstr;
        }

        public void setBizstr(String bizstr) {
            this.bizstr = bizstr;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public double getOthercny() {
            return othercny;
        }

        public void setOthercny(double othercny) {
            this.othercny = othercny;
        }

        public int getCoinflag() {
            return coinflag;
        }

        public void setCoinflag(int coinflag) {
            this.coinflag = coinflag;
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

        public int getBizid() {
            return bizid;
        }

        public void setBizid(int bizid) {
            this.bizid = bizid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getStatus() {
            return status;
        }

        public String getStatus_newPay2payEase() {
            if ("1".equals(status)) {
                return "SUCCESS";
            } else if ("3".equals(status)) {
                return "FAIL";
            } else  {
                return "PROCESS";
            }
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
