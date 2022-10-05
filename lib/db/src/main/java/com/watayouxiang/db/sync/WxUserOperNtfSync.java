package com.watayouxiang.db.sync;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.db.TioDBHelper;
import com.watayouxiang.db.converter.ChatListTableConverter;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.event.ChatListTableEvent;
import com.watayouxiang.db.event.DBEventBus;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.imclient.model.body.wx.WxUserOperNtf;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class WxUserOperNtfSync {
    private static class Holder {
        private static final WxUserOperNtfSync holder = new WxUserOperNtfSync();
    }

    public static WxUserOperNtfSync getInstance() {
        return Holder.holder;
    }

    private WxUserOperNtfSync() {
    }

    public void sync(@Nullable WxUserOperNtf wxUserOperNtf) {
        if (wxUserOperNtf == null) return;
        TioDBHelper.runInTx(() -> syncInternal(wxUserOperNtf));
    }

    private void syncInternal(@NonNull WxUserOperNtf ntf) {
        if (ntf.oper == 1 || ntf.oper == 2 || ntf.oper == 5 || ntf.oper == 11) {
            // 1: 删除聊天会话(同时删除聊天记录)；
            // 2: 拉黑
            // 5：主动删除好友通知；
            // 11: 删除聊天会话(不删除聊天记录)；
            ChatListTable table = ChatListTableCrud.query_chatLinkId(ntf.chatlinkid);
            if (table != null) {
                ChatListTableCrud.delete_chatLinkId(ntf.chatlinkid);
                ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_delete(table);
                DBEventBus.post_UIThread(tableEvent);
            }
        } else if (ntf.oper == 21) {
            // 置顶
            ChatListTable table = ChatListTableCrud.update_topFlag(ntf.chatlinkid, 1);
            if (table != null) {
                ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_insertOrUpdate(table);
                DBEventBus.post_UIThread(tableEvent);
            }
        } else if (ntf.oper == 22) {
            // 取消置顶
            ChatListTable table = ChatListTableCrud.update_topFlag(ntf.chatlinkid, 2);
            if (table != null) {
                ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_insertOrUpdate(table);
                DBEventBus.post_UIThread(tableEvent);
            }
        } else if (ntf.oper == 7) {
            // 我发的消息，对方是否已读
            ChatListTable table = ChatListTableCrud.update_toReadFlag(ntf.chatlinkid, 1);
            if (table != null) {
                ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_insertOrUpdate(table);
                DBEventBus.post_UIThread(tableEvent);
            }
        } else {
            // 3 恢复拉黑
            // 4 激活会话
            // 10 删除消息
            // 25 消息免打扰
            if (ntf.chatItems != null) {
                String chatlinkid = ntf.chatItems.getChatlinkid();
                ChatListTable table = ChatListTableCrud.query_chatLinkId(chatlinkid);
                if (table != null) {
                    // 存在，更新
                    ChatListTableConverter.update(table, ntf.chatItems);
                    ChatListTableCrud.update(table);
                } else {
                    // 不存在，插入
                    table = ChatListTableConverter.getInstance(ntf.chatItems);
                    ChatListTableCrud.insertOrReplace(table);
                }
                if (table != null) {
                    ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_insertOrUpdate(table);
                    DBEventBus.post_UIThread(tableEvent);
                }
            }
        }
    }
}
