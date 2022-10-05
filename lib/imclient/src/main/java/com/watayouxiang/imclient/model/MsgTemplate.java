package com.watayouxiang.imclient.model;

import androidx.annotation.Nullable;

/**
 * author : TaoWang
 * date : 2020/4/16
 * desc : 会话模板
 */
public enum MsgTemplate {
    // 创建群消息模板
    create("create", "%%% 邀请 ### 加入了群聊"),
    // 进群消息模板
    join("join", "%%% 邀请 ### 加入了群聊"),
    // 群主退群模板
    ownerleave("ownerleave", "%%% 退出了群聊，### 自动成为群主"),
    // 主动退群模板
    leave("leave", "%%% 退出了群聊"),
    // 踢人退群模板
    operkick("operkick", "%%% 将 ### 移除了群聊"),
    // 被踢者接受到的模板
    tokick("tokick", "### 被 %%% 移除了群聊"),
    // 撤回消息模板
    msgback("msgback", "%%% 撤回了一条消息"),
    // 群转移模板
    ownerchange("ownerchange", "%%% 将群主转让给了 ###"),
    // 开启群邀请信息开关
    applyopen("applyopen", "%%% 开启了群邀请开关：所有人都可以邀请人员进群"),
    // 关闭群邀请信息开关
    applyclose("applyclose", "%%% 关闭了群邀请开关：只有群主或者群管理员才能邀请人员进群"),
    // 开启群审核开关
    reviewopen("reviewopen", "%%% 开启群审核开关：成员进群前，必须群主或者群管理员审核通过"),
    // 关闭群审核开关
    reviewclose("reviewclose", "%%% 关闭了群审核开关：成员进群不需要审核"),
    // 修改群公告
    updatenotice("updatenotice", "%%% 修改了群公告：###"),
    // 修改群名称
    updatename("updatename", "%%% 修改了群名称：###"),
    // 解散群
    delgroup("delgroup", "%%% 解散了群"),
    // 禁言模板
    forbidden("forbidden", "### 已被禁言"),
    // 解除禁言
    cancelforbidden("cancelforbidden", "### 已被解除禁言"),
    // 解除禁言2
    cancelforbidden2("cancelforbidden_bvgf", "%%% 已被解除禁言"),
    // 抢红包模板
    grab("grab", "%%% 领取了 ### 的红包"),
    // 管理员撤回消息模板
    managermsgback("managermsgback", "%%% 撤回了一条成员消息");

    String key;
    String value;

    MsgTemplate(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static MsgTemplate getTemplate(String key) {
        for (MsgTemplate e : values()) {
            if (e.key.equals(key)) {
                return e;
            }
        }
        return null;
    }

    @Nullable
    public static String getTipMsg(@Nullable String key, @Nullable String operNick, @Nullable String toNicks, @Nullable String currNick) {
        if (key == null || operNick == null || toNicks == null || currNick == null) return null;

        operNick = operNick.equals(currNick) ? "你" : "\"" + operNick + "\"";
        toNicks = toNicks.equals(currNick) ? "你" : "\"" + toNicks + "\"";

        MsgTemplate template = getTemplate(key);
        if (template == null) return null;
        String value = template.value;

        value = value.replace("%%%", operNick);
        value = value.replace("###", toNicks);

        return value;
    }
}
