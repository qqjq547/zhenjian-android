package com.watayouxiang.httpclient.model.response;

import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/06
 *     desc   :
 * </pre>
 */
public class ForbiddenFlagResp {

    /**
     * forbiddenflag（禁言标识：1：时长禁言；2：未禁言；3：长久禁用）
     */
    private int flag;
    /**
     * 权限字段：1：有权限禁言；2：无权限
     */
    private int grant;
    /**
     * 群角色：1群主，2成员，3管理员
     */
    private int grouprole;
    /**
     * 踢人权限字段：1：有；2：无
     */
    private int kickgrant;
    /**
     * 角色管理权限：1：有；2：无
     */
    private int rolegrant;
    /**
     * 是否为群用户：1是，2否
     */
    private int userstatus;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getGrant() {
        return grant;
    }

    public void setGrant(int grant) {
        this.grant = grant;
    }

    public int getGrouprole() {
        return grouprole;
    }

    public void setGrouprole(int grouprole) {
        this.grouprole = grouprole;
    }

    public int getKickgrant() {
        return kickgrant;
    }

    public void setKickgrant(int kickgrant) {
        this.kickgrant = kickgrant;
    }

    public int getRolegrant() {
        return rolegrant;
    }

    public void setRolegrant(int rolegrant) {
        this.rolegrant = rolegrant;
    }

    public int getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(int userstatus) {
        this.userstatus = userstatus;
    }

    // ====================================================================================
    // 拓展
    // ====================================================================================

    /**
     * 是否有禁言权限
     */
    public boolean haveForbiddenPermission() {
        return grant == 1;
    }

    /**
     * 是否被禁言
     */
    public boolean isForbidden() {
        return flag == 1 || flag == 3;
    }

    /**
     * 禁言类型：1：用户时长禁言；3：用户长久禁言 ； 4：群禁言--------必填
     */
    public String getForbiddenMode() {
        if (flag == 1 || flag == 3) {
            return flag + "";
        }
        return null;
    }

    public GroupRoleEnum getGroupRoleEnum() {
        return GroupRoleEnum.codeOf(getGrouprole());
    }
}
