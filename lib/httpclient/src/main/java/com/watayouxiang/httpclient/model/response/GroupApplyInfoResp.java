package com.watayouxiang.httpclient.model.response;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/09
 *     desc   :
 * </pre>
 */
public class GroupApplyInfoResp {

    /**
     * apply : {"groupavator":"/user/avatar/69/8201/1118754/88096807/74541310175/87/182100/1352199510859849728.gif","createtime":"2021-02-08 14:08:49","operuid":22627,"groupnick":"王涛1","applymsg":"2","groupid":"795","id":671,"updatetime":"2021-02-08 14:08:49","srcnick":"王涛1","grouprole":2,"status":2}
     * items : [{"nick":"王涛2","uid":22626,"createtime":"2021-02-08 14:08:49","phone":"19000000723","groupid":"795","id":754,"avatar":"/user/avatar/68/8200/1118753/88096806/74541310174/61/173906/1352188964764786688_sm.jpg","updatetime":"2021-02-08 14:08:49","aid":671,"email":"19000000723@qq.com","status":2},{"nick":"🤣哆唻咪_32729","uid":32729,"createtime":"2021-02-08 14:08:49","phone":"17367126816","groupid":"795","id":755,"avatar":"/user/avatar/6/6854/1128856/88106909/74541320277/34/183030/1344591756431335424_sm.jpg","updatetime":"2021-02-08 14:08:49","aid":671,"email":"47027900@qq.com","status":2}]
     */

    private ApplyBean apply;
    private List<ItemsBean> items;

    public ApplyBean getApply() {
        return apply;
    }

    public void setApply(ApplyBean apply) {
        this.apply = apply;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ApplyBean {
        /**
         * groupavator : /user/avatar/69/8201/1118754/88096807/74541310175/87/182100/1352199510859849728.gif
         * createtime : 2021-02-08 14:08:49
         * operuid : 22627
         * groupnick : 王涛1
         * applymsg : 2
         * groupid : 795
         * id : 671
         * updatetime : 2021-02-08 14:08:49
         * srcnick : 王涛1
         * grouprole : 2
         * status : 2
         */

        private String groupavator;
        private String createtime;
        private int operuid;
        private String groupnick;
        private String applymsg;
        private String groupid;
        private int id;
        private String updatetime;
        private String srcnick;
        private int grouprole;
        /**
         * 是否已处理：1已处理，2未处理
         */
        private int status;

        public String getGroupavator() {
            return groupavator;
        }

        public void setGroupavator(String groupavator) {
            this.groupavator = groupavator;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getOperuid() {
            return operuid;
        }

        public void setOperuid(int operuid) {
            this.operuid = operuid;
        }

        public String getGroupnick() {
            return groupnick;
        }

        public void setGroupnick(String groupnick) {
            this.groupnick = groupnick;
        }

        public String getApplymsg() {
            return applymsg;
        }

        public void setApplymsg(String applymsg) {
            this.applymsg = applymsg;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getSrcnick() {
            return srcnick;
        }

        public void setSrcnick(String srcnick) {
            this.srcnick = srcnick;
        }

        public int getGrouprole() {
            return grouprole;
        }

        public void setGrouprole(int grouprole) {
            this.grouprole = grouprole;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class ItemsBean {
        /**
         * nick : 王涛2
         * uid : 22626
         * createtime : 2021-02-08 14:08:49
         * phone : 19000000723
         * groupid : 795
         * id : 754
         * avatar : /user/avatar/68/8200/1118753/88096806/74541310174/61/173906/1352188964764786688_sm.jpg
         * updatetime : 2021-02-08 14:08:49
         * aid : 671
         * email : 19000000723@qq.com
         * status : 2
         */

        private String nick;
        private int uid;
        private String createtime;
        private String phone;
        private String groupid;
        private int id;
        private String avatar;
        private String updatetime;
        private int aid;
        private String email;
        private int status;

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public int getAid() {
            return aid;
        }

        public void setAid(int aid) {
            this.aid = aid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
