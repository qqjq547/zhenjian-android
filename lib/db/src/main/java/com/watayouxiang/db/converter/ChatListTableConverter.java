package com.watayouxiang.db.converter;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.db.dao.FocusTableCrud;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.db.utils.Utils;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.httpclient.model.response.internal.ChatListBean;
import com.watayouxiang.imclient.model.body.wx.WxFriendChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;
import com.watayouxiang.imclient.model.body.wx.internal.ChatItems;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/01
 *     desc   :
 * </pre>
 */
public class ChatListTableConverter {

    // ====================================================================================
    // WxFriendChatNtf（私聊通知 603）
    // ====================================================================================

    @NonNull
    public static ChatListTable getInstance(@NonNull WxFriendChatNtf friend) {
        ChatListTable item = new ChatListTable();
        update(item, friend);
        return item;
    }

    public static void update(@NonNull ChatListTable item, @NonNull WxFriendChatNtf friend) {
        // 会话类型
        item.setChatmode(1);
        // 激活会话
        if (friend.actflag == 1) {
            // 会话头像
            item.setAvatar(friend.actavatar);
            // 会话名称
            item.setName(friend.actname);
        }
        // chatLinkId
        item.setId(friend.chatlinkid);
        // 消息id
        item.setLastmsgid(friend.mid);
        // fromUid
        item.setLastmsguid(friend.uid);
        // 发送者
        item.setFromnick(friend.nick);
        // 是否为系统消息
        item.setSysflag(friend.sendbysys);
        // 系统消息key
        item.setSysmsgkey(friend.sysmsgkey);
        // 操作者 昵称
        item.setOpernick(friend.opernick);
        // 被操作者 昵称
        item.setTonicks(friend.tonicks);
        // 最后一条消息
        item.setMsgresume(friend.getShowContent());
        // 对方是否已读
        item.setToreadflag(friend.readflag);
        // 未读消息数
        if (String.valueOf(friend.uid).equals(Utils.getCurrUid())) {
            item.setNotreadcount(0);
        } else if (friend.ct == 10 || friend.ct == 11) {
            // 音频电话
            // 视频电话
            // 未读消息数不计数
        } else {
            // 不在焦点表 && 不是系统发送 -> 未读数+1
            if (FocusTableCrud.query(friend.chatlinkid) == null && friend.sendbysys != 1) {
                item.setNotreadcount(item.getNotreadcount() + 1);
            }
        }
        // msgfreeflag
        if (friend.msgfreeflag != 0) {
            item.setMsgfreeflag(friend.msgfreeflag);
        }
        // 发送时间
        String _time = Utils.long2DataString(friend.t);
        item.setSendtime(_time);
        // chatuptime
        item.setChatuptime(_time);
    }

    // ====================================================================================
    // WxGroupChatNtf（群聊通知 607）
    // ====================================================================================

    @NonNull
    public static ChatListTable getInstance(@NonNull WxGroupChatNtf group) {
        ChatListTable item = new ChatListTable();
        update(item, group);
        return item;
    }

    public static void update(@NonNull ChatListTable item, @NonNull WxGroupChatNtf group) {
        // 会话类型
        item.setChatmode(2);
        // 激活会话
        if (group.actflag == 1) {
            // 会话头像
            item.setAvatar(group.actavatar);
            // 会话名称
            item.setName(group.actname);
        }
        // chatLinkId
        item.setId(group.chatlinkid);
        // fromUid
        item.setLastmsguid(group.f);
        // 消息id
        item.setLastmsgid(String.valueOf(group.mid));
        // 是否为系统消息
        item.setSysflag(group.sendbysys);
        // 系统消息key
        item.setSysmsgkey(group.sysmsgkey);
        // 操作者 昵称
        item.setOpernick(group.opernick);
        // 被操作者 昵称
        item.setTonicks(group.tonicks);
        // 发送者
        item.setFromnick(group.nick);
        // 最后一条消息
        item.setMsgresume(group.getShowContent());
        // 别人艾特我，我是否已读
        if (Utils.contains(group.at, "all") ||
                Utils.contains(group.at, Utils.getCurrUid())) {
            /* 有人艾特我，就标记有未读艾特消息 */
            item.setAtreadflag(2);
        }
        if (String.valueOf(group.f).equals(Utils.getCurrUid())) {
            /* 如果消息发送人是自己，那么将未读消息标记成已读艾特 */
            if (item.getAtreadflag() == 2) {
                item.setAtreadflag(1);
            }
        }
        // 未读消息数
        if (String.valueOf(group.f).equals(Utils.getCurrUid())) {
            item.setNotreadcount(0);
        } else {
            // 不在焦点表 && 不是系统发送 -> 未读数+1
            if (FocusTableCrud.query(group.chatlinkid) == null && group.sendbysys != 1) {
                item.setNotreadcount(item.getNotreadcount() + 1);
            }
        }
        // bizId
        if (TextUtils.isEmpty(item.getBizid())) {
            item.setBizid(String.valueOf(group.g));
        }
        // topFlag
        if (item.getTopflag() == 0) {
            item.setTopflag(2);
        }
        // msgfreeflag
        if (group.msgfreeflag != 0) {
            item.setMsgfreeflag(group.msgfreeflag);
        }
        // 发送时间
        String _time = Utils.long2DataString(group.t);
        item.setSendtime(_time);
        // chatuptime
        item.setChatuptime(_time);
    }

    // ====================================================================================
    // ChatItems（用户操作通知 700）
    // ====================================================================================

    @NonNull
    public static ChatListTable getInstance(@NonNull ChatItems chat) {
        ChatListTable table = new ChatListTable();
        update(table, chat);
        return table;
    }

    public static void update(@NonNull ChatListTable table, @NonNull ChatItems chat) {
        // 会话类型
        int chatmode = chat.getChatmode();
        if (chatmode != 0) {
            table.setChatmode(chatmode);
        }
        // linkflag
        int linkflag = chat.getLinkflag();
        if (linkflag != 0) {
            table.setLinkflag(linkflag);
        }
        // readflag
        int readflag = chat.getReadflag();
        if (readflag != 0) {
            table.setReadflag(readflag);
        }
        // uid
        int uid = chat.getUid();
        if (uid != 0) {
            table.setUid(uid);
        }
        // sysflag
        int sysflag = chat.getSysflag();
        if (sysflag != 0) {
            table.setSysflag(sysflag);
        }
        // linkid
        String linkid = chat.getLinkid();
        if (linkid != null) {
            table.setLinkid(linkid);
        }
        // msgresume
        String msgresume = chat.getMsgresume();
        if (msgresume != null) {
            table.setMsgresume(msgresume);
        }
        // topflag
        int topflag = chat.getTopflag();
        if (topflag != 0) {
            table.setTopflag(topflag);
        }
        // fromnick
        String fromnick = chat.getFromnick();
        if (fromnick != null) {
            table.setFromnick(fromnick);
        }
        // notreadcount
        int notreadcount = chat.getNotreadcount();
        if (notreadcount != 0) {
            table.setNotreadcount(notreadcount);
        }
        // chatlinkid
        String chatlinkid = chat.getChatlinkid();
        if (chatlinkid != null) {
            table.setId(chatlinkid);
        }
        // atreadflag
        int atreadflag = chat.getAtreadflag();
        if (atreadflag != 0) {
            table.setAtreadflag(atreadflag);
        }
        // viewflag
        int viewflag = chat.getViewflag();
        if (viewflag != 0) {
            table.setViewflag(viewflag);
        }
        // avatar
        String avatar = chat.getAvatar();
        if (avatar != null) {
            table.setAvatar(avatar);
        }
        // bizid
        String bizid = chat.getBizid();
        if (bizid != null) {
            table.setBizid(bizid);
        }
        // name
        String name = chat.getName();
        if (name != null) {
            table.setName(name);
        }
        // toreadflag
        int toreadflag = chat.getToreadflag();
        if (toreadflag != 0) {
            table.setToreadflag(toreadflag);
        }
        // atnotreadcount
        int atnotreadcount = chat.getAtnotreadcount();
        if (atnotreadcount != 0) {
            table.setAtnotreadcount(atnotreadcount);
        }
        // lastmsgid
        String lastmsgid = chat.getLastmsgid();
        if (lastmsgid != null) {
            table.setLastmsgid(lastmsgid);
        }
        // lastmsguid
        int lastmsguid = chat.getLastmsguid();
        if (lastmsguid != 0) {
            table.setLastmsguid(lastmsguid);
        }
        // sysmsgkey
        String sysmsgkey = chat.getSysmsgkey();
        if (sysmsgkey != null) {
            table.setSysmsgkey(sysmsgkey);
        }
        // opernick
        String opernick = chat.getOpernick();
        if (opernick != null) {
            table.setOpernick(opernick);
        }
        // tonicks
        String tonicks = chat.getTonicks();
        if (tonicks != null) {
            table.setTonicks(tonicks);
        }
        // joinnum
        int joinnum = chat.getJoinnum();
        if (joinnum != 0) {
            table.setJoinnum(joinnum);
        }
        // fidkey
        String fidkey = chat.getFidkey();
        if (fidkey != null) {
            table.setFidkey(fidkey);
        }
        // 群角色
        int bizrole = chat.getBizrole();
        if (bizrole != 0) {
            table.setBizrole(bizrole);
        }
        // notreadstartmsgid
        long notreadstartmsgid = chat.getNotreadstartmsgid();
        if (notreadstartmsgid != 0) {
            table.setNotreadstartmsgid(notreadstartmsgid);
        }
        // atnotreadstartmsgid
        long atnotreadstartmsgid = chat.getAtnotreadstartmsgid();
        if (atnotreadstartmsgid != 0) {
            table.setAtnotreadstartmsgid(atnotreadstartmsgid);
        }
        // sendtime
        String sendtime = chat.getSendtime();
        if (sendtime != null) {
            table.setSendtime(sendtime);
        }
        // chatuptime
        String chatuptime = chat.getChatuptime();
        if (chatuptime != null) {
            table.setChatuptime(chatuptime);
        }
        // msgfreeflag
        int msgfreeflag = chat.getMsgfreeflag();
        if (msgfreeflag != 0) {
            table.setMsgfreeflag(msgfreeflag);
        }
    }

    // ====================================================================================
    // ChatListBean（会话同步响应）
    // ====================================================================================

    @NonNull
    public static ChatListTable getInstance(@NonNull ChatListBean resp) {
        ChatListTable table = new ChatListTable();

        // 会话类型
        table.setChatmode(resp.getChatmode());
        // linkflag
        table.setLinkflag(resp.getLinkflag());
        // readflag
        table.setReadflag(resp.getReadflag());
        // uid
        table.setUid(resp.getUid());
        // 系统标识：1：是系统消息；2：是正常消息
        table.setSysflag(resp.getSysflag());
        // 群用户id / 好友关系id
        table.setLinkid(resp.getLinkid());
        // 最后一条消息
        table.setMsgresume(resp.getMsgresume());
        // 置顶标记
        table.setTopflag(resp.getTopflag());
        // fromnick
        table.setFromnick(resp.getFromnick());
        // 自己未读的消息条数
        table.setNotreadcount(resp.getNotreadcount());
        // 回话id
        table.setId(resp.getChatlinkid());
        // 艾特已读标识：1 已读艾特，2 未读艾特
        table.setAtreadflag(resp.getAtreadflag());
        // viewflag
        table.setViewflag(resp.getViewflag());
        // 头像
        table.setAvatar(resp.getAvatar());
        // 好友的uid或者群的groupid
        table.setBizid(resp.getBizid());
        // 名称
        table.setName(resp.getName());
        // 我发的消息，对方是否已读
        table.setToreadflag(resp.getToreadflag());
        // 艾特信息未读数（暂未用）
        table.setAtnotreadcount(resp.getAtnotreadcount());
        // 最后一条消息的 id
        table.setLastmsgid(resp.getLastmsgid());
        // 最后一条消息的发送者的 uid
        table.setLastmsguid(resp.getLastmsguid());
        // 系统消息key
        table.setSysmsgkey(resp.getSysmsgkey());
        // 操作者 昵称
        table.setOpernick(resp.getOpernick());
        // 被操作者 昵称
        table.setTonicks(resp.getTonicks());
        // 群聊激活时的群人数
        table.setJoinnum(resp.getJoinnum());
        // 角色：1：群主；2：成员；3：管理员
        table.setBizrole(resp.getBizrole());
        // 最后一条消息发送的时间
        table.setSendtime(resp.getSendtime());
        // 聊天更新时间（必定存在的时间，排序依据）
        table.setChatuptime(resp.getChatuptime());
        // 消息免打扰
        table.setMsgfreeflag(resp.getMsgfreeflag());

        return table;
    }

    // ====================================================================================
    // ChatListResp.List（会话列表响应）
    // ====================================================================================

    @NonNull
    public static ChatListTable getInstance(@NonNull ChatListResp.List resp) {
        ChatListTable table = new ChatListTable();

        // 会话类型
        table.setChatmode(resp.chatmode);
        // linkflag
        table.setLinkflag(resp.linkflag);
        // readflag
        table.setReadflag(resp.readflag);
        // uid
        table.setUid(resp.uid);
        // sysflag
        table.setSysflag(resp.sysflag);
        // linkid
        table.setLinkid(resp.linkid);
        // msgresume
        table.setMsgresume(resp.msgresume);
        // topflag
        table.setTopflag(resp.topflag);
        // fromnick
        table.setFromnick(resp.fromnick);
        // notreadcount
        table.setNotreadcount(resp.notreadcount);
        // id
        table.setId(resp.id);
        // atreadflag
        table.setAtreadflag(resp.atreadflag);
        // viewflag
        table.setViewflag(resp.viewflag);
        // avatar
        table.setAvatar(resp.avatar);
        // bizid
        table.setBizid(resp.bizid);
        // name
        table.setName(resp.name);
        // toreadflag
        table.setToreadflag(resp.toreadflag);
        // atnotreadcount
        table.setAtnotreadcount(resp.atnotreadcount);
        // lastmsgid
        table.setLastmsgid(resp.lastmsgid);
        // lastmsguid
        table.setLastmsguid(resp.lastmsguid);
        // sendtime
        table.setSendtime(resp.sendtime);
        // sysmsgkey
        table.setSysmsgkey(resp.sysmsgkey);
        // opernick
        table.setOpernick(resp.opernick);
        // tonicks
        table.setTonicks(resp.tonicks);
        // chatuptime
        table.setChatuptime(resp.chatuptime);
        // joinnum
        table.setJoinnum(resp.joinnum);
        // fidkey todo
        table.setFidkey(null);
        // bizrole
        table.setBizrole(resp.bizrole);
        // notreadstartmsgid todo
        table.setNotreadstartmsgid(0);
        // atnotreadstartmsgid todo
        table.setAtnotreadstartmsgid(0);
        // msgfreeflag
        table.setMsgfreeflag(resp.msgfreeflag);

        return table;
    }

    @NonNull
    public static ChatListResp.List convert2ChatListResp(@NonNull ChatListTable table) {
        ChatListResp.List list = new ChatListResp.List();

        list.chatmode = table.getChatmode();
        list.linkflag = table.getLinkflag();
        list.readflag = table.getReadflag();
        list.uid = table.getUid();
        list.sysflag = table.getSysflag();
        list.linkid = table.getLinkid();
        list.msgresume = table.getMsgresume();
        list.topflag = table.getTopflag();
        list.fromnick = table.getFromnick();
        list.notreadcount = table.getNotreadcount();
        list.id = table.getId();
        list.atreadflag = table.getAtreadflag();
        list.viewflag = table.getViewflag();
        list.avatar = table.getAvatar();
        list.bizid = table.getBizid();
        list.name = table.getName();
        list.toreadflag = table.getToreadflag();
        list.atnotreadcount = table.getAtnotreadcount();
        list.lastmsgid = table.getLastmsgid();
        list.lastmsguid = table.getLastmsguid();
        list.sendtime = table.getSendtime();
        list.sysmsgkey = table.getSysmsgkey();
        list.opernick = table.getOpernick();
        list.tonicks = table.getTonicks();
        list.chatuptime = table.getChatuptime();
        list.joinnum = table.getJoinnum();
        list.bizrole = table.getBizrole();
        list.msgfreeflag = table.getMsgfreeflag();

        return list;
    }

    @Nullable
    public static List<ChatListResp.List> convert2ChatListResp(@Nullable List<ChatListTable> tables) {
        if (tables == null || tables.size() == 0) return null;
        List<ChatListResp.List> lists = new ArrayList<>();
        for (ChatListTable table : tables) {
            ChatListResp.List list = convert2ChatListResp(table);
            lists.add(list);
        }
        return lists;
    }
}
