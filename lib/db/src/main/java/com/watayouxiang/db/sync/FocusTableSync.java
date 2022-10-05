package com.watayouxiang.db.sync;

import androidx.annotation.Nullable;

import com.watayouxiang.db.TioDBHelper;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.dao.FocusTableCrud;
import com.watayouxiang.db.event.ChatListTableEvent;
import com.watayouxiang.db.event.DBEventBus;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.db.table.FocusTable;
import com.watayouxiang.imclient.model.body.wx.WxFocusNtf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/01
 *     desc   :
 * </pre>
 */
public class FocusTableSync {

    private static class Holder {
        private static final FocusTableSync holder = new FocusTableSync();
    }

    public static FocusTableSync getInstance() {
        return Holder.holder;
    }

    private FocusTableSync() {
    }

    public void sync(@Nullable WxFocusNtf wxFocusNtf) {
        TioDBHelper.runInTx(() -> syncInternal(wxFocusNtf));
    }

    private void syncInternal(@Nullable WxFocusNtf wxFocusNtf) {
        // 获取数据
        List<FocusTable> focusTables = getFocusTables(wxFocusNtf);
        // 同步到 FocusTable
        sync2FocusTable(focusTables);
        // 同步到 ChatListTable
        List<ChatListTable> chatListTables = sync2ChatListTable(focusTables);
        // 同步成功
        if (chatListTables != null) {
            ChatListTableEvent tableEvent = ChatListTableEvent.getInstance_insertOrUpdate(chatListTables);
            DBEventBus.post_UIThread(tableEvent);
        }
    }

    @Nullable
    private List<ChatListTable> sync2ChatListTable(@Nullable List<FocusTable> tables) {
        if (tables == null || tables.size() == 0) return null;

        // 如果 FocusTable # code 为 1
        // 那么将 (ChatListTable # id == FocusTable # chatLinkId) item 的未读消息字段（notreadcount）改为0
        List<ChatListTable> chatListTables = new ArrayList<>(2);

        for (FocusTable table : tables) {
            if ("1".equals(table.getCode())) {
                String chatLinkId = table.getChatLinkId();
                ChatListTable chatListTable = ChatListTableCrud.update_notReadCount(chatLinkId, 0, 1);
                if (chatListTable != null) {
                    chatListTables.add(chatListTable);
                }
            }
        }

        return chatListTables;
    }

    // 存到本地数据库（清空数据，再存储数据）
    private void sync2FocusTable(@Nullable List<FocusTable> tables) {
        FocusTableCrud.deleteAll();
        FocusTableCrud.insert(tables);
    }

    // 获取焦点列表
    @Nullable
    private List<FocusTable> getFocusTables(@Nullable WxFocusNtf wxFocusNtf) {
        if (wxFocusNtf == null) return null;
        Map<String, String> focusMap = wxFocusNtf.getFocusMap();
        if (focusMap == null) return null;

        List<FocusTable> tables = new ArrayList<>(focusMap.size());
        Set<Map.Entry<String, String>> entries = focusMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String key = entry.getKey();
            String value = entry.getValue();
            FocusTable focusTable = new FocusTable(key, value);
            tables.add(focusTable);
        }

        return tables;
    }
}
