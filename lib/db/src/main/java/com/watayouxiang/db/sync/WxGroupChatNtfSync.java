package com.watayouxiang.db.sync;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.db.TioDBHelper;
import com.watayouxiang.db.converter.ChatListTableConverter;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.event.ChatListTableEvent;
import com.watayouxiang.db.event.DBEventBus;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class WxGroupChatNtfSync {
    private static class Holder {
        private static final WxGroupChatNtfSync holder = new WxGroupChatNtfSync();
    }

    public static WxGroupChatNtfSync getInstance() {
        return Holder.holder;
    }

    private WxGroupChatNtfSync() {
    }

    public void sync(@Nullable WxGroupChatNtf wxGroupChatNtf) {
        if (wxGroupChatNtf == null) return;
        TioDBHelper.runInTx(() -> syncInternal(wxGroupChatNtf));
    }

    private void syncInternal(@NonNull WxGroupChatNtf ntf) {
        // 查询数据库是否存在
        ChatListTable table = ChatListTableCrud.query_chatLinkId(ntf.chatlinkid);
        if (table != null) {
            // 如果存在，则更新
            ChatListTableConverter.update(table, ntf);
            ChatListTableCrud.update(table);
        } else {
            // 如果不存在，则新增
            table = ChatListTableConverter.getInstance(ntf);
            ChatListTableCrud.insertOrReplace(table);
        }
        // 通知ui，数据更新了
        ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_insertOrUpdate(table);
        DBEventBus.post_UIThread(tableEvent);
    }
}
