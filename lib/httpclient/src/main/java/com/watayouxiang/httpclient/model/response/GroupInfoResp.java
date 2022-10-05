package com.watayouxiang.httpclient.model.response;

import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class GroupInfoResp {

    /**
     * groupuser : {"createtime":"2020-02-25 11:25:20","groupid":"138","groupnick":"","grouprole":1,"id":"721","isaddress":1,"lastmsgid":"346287","msgfreeflag":2,"nickviewflag":1,"status":1,"uid":23436,"updatetime":"2020-02-25 14:36:27","viewstatus":1}
     * state : ok
     * group : {"avatar":"/wx/group/avatar/22/9010/1119563/88097616/74541310984/9/143627/1232192617530400768_sm.jpg","createtime":"2020-02-25 11:25:20","id":"138","intro":"","joinmode":3,"joinnum":4,"msgactflag":2,"name":"wata、王涛1、王涛2、王涛3、王涛4","notice":"","status":1,"uid":23436,"updatetime":"2020-02-25 14:36:27"}
     */

    public GroupUser groupuser;
    /**
     * 当前群的状态：1、正常；3、群主解散
     */
    public String state;
    public Group group;

    /**
     * 用户在群中的身份信息
     */
    public static class GroupUser {
        /**
         * createtime : 2020-02-25 11:25:20
         * groupid : 138
         * groupnick :
         * grouprole : 1
         * id : 721
         * isaddress : 1
         * lastmsgid : 346287
         * msgfreeflag : 2
         * nickviewflag : 1
         * status : 1
         * uid : 23436
         * updatetime : 2020-02-25 14:36:27
         * viewstatus : 1
         */

        public String createtime;
        public String groupid;
        /**
         * 原本的昵称
         */
        public String srcnick;
        /**
         * 群昵称
         */
        public String groupnick;
        /**
         * 群成员角色：1：群主；2：成员；3：管理员
         */
        public int grouprole;
        public String id;
        public int isaddress;
        public String lastmsgid;
        /**
         * 消息免打扰：1开启，2关闭
         */
        public int msgfreeflag;
        public int nickviewflag;
        public int status;
        public int uid;
        public String updatetime;
        public int viewstatus;

        public GroupRoleEnum getGroupRoleEnum() {
            return GroupRoleEnum.codeOf(grouprole);
        }
    }

    public static class Group {
        /**
         * avatar : /wx/group/avatar/22/9010/1119563/88097616/74541310984/9/143627/1232192617530400768_sm.jpg
         * createtime : 2020-02-25 11:25:20
         * id : 138
         * intro :
         * joinmode : 3
         * joinnum : 4
         * msgactflag : 2
         * name : wata、王涛1、王涛2、王涛3、王涛4
         * notice :
         * noticetime : "2020-03-20 09:12:59"
         * status : 1
         * uid : 23436
         * updatetime : 2020-02-25 14:36:27
         * applyflag : 1
         * friendflag : 1
         */

        public String avatar;
        public String createtime;
        public String id;
        public String intro;
        public int joinnum;
        /**
         * 是否进行消息激活群：1：是；2：否
         */
        public int msgactflag;
        public String name;
        /**
         * 公告
         */
        public String notice;
        /**
         * 公告时间
         */
        public String noticetime;
        public int status;
        public int uid;
        public String updatetime;
        /**
         * 成员邀请开关：1 开启，2 关闭
         */
        public int applyflag;
        /**
         * 全员禁用：1：是；2：否
         */
        public int forbiddenflag;
        /**
         * 进群审核：1开启审核，2关闭审核
         */
        public int joinmode;
        /**
         * 群内互加好友开关：1开启，2关闭
         */
        public int friendflag;
        /**
         * 群内系统通知开关：1开启，2关闭
         */
        public int sysnoticeflag;
        /**
         * 显示人数开关：1开启，2关闭
         */
        public int shownumflag;
        /**
         * 清理15天不上线会员： // 1 自动清理 2 不自动清理
         */
        public int clearmemberflag;
        /**
         * 进入弹出公告开关：1开启，2关闭
         */
        public int goinshownoticeflag;
        /**
         * 是否允许成员退群开关：// 1 允许 2 不允许
         */
        public int leaveflag;


        /**
         * 群成员显示
         * 参数为1 普通成员 可以看到 群成员列表
         * 参数为2 普通成员 将看不到 群成员列表(仅管理员与群主可见)
         */
        public int groupmembershowflag;
        // ====================================================================================
        // 拓展
        // ====================================================================================

        public boolean isAllowInviteMember() {
            return applyflag == 1;
        }

        public boolean isForbiddenMemberTalk() {
            return forbiddenflag == 1;
        }
    }
}
