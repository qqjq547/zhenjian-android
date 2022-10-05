package com.watayouxiang.db.sync;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lzy.okgo.model.Response;
import com.watayouxiang.db.TioDBHelper;
import com.watayouxiang.db.converter.ChatListTableConverter;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.event.ChatListTableEvent;
import com.watayouxiang.db.event.DBEventBus;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.SynAckReq;
import com.watayouxiang.httpclient.model.request.SynChatReq;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.httpclient.model.response.SynChatResp;
import com.watayouxiang.httpclient.model.response.internal.ChatListBean;
import com.watayouxiang.httpclient.model.response.internal.SynItemBean;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/01
 *     desc   : HTTP 同步 {@link ChatListTable}
 * </pre>
 */
public class ChatListTableSync {
    private static class Holder {
        private static final ChatListTableSync holder = new ChatListTableSync();
    }

    public static ChatListTableSync getInstance() {
        return Holder.holder;
    }

    private ChatListTableSync() {
    }

    /**
     * 开始同步
     */
    public void sync() {
        TioDBHelper.runInTx(this::syncInternal);
    }

    private void syncInternal() {
        // 获取会话同步数据
        SynChatResp synChatResp = reqSynChat(null);
        if (synChatResp == null) {
            DBEventBus.post_UIThread(ChatListTableEvent.getInstance_syncError());
            return;
        }

        // 同步到本地数据库
        ChatListTableEvent event = sync2ChatListTable(synChatResp);

        // 同步确认
        boolean synAck = reqSynAck(synChatResp);
        if (!synAck) {
            DBEventBus.post_UIThread(ChatListTableEvent.getInstance_syncError());
            return;
        }

        // 成功回调
        DBEventBus.post_UIThread(event);
    }

    /**
     * 同步确认
     */
    private boolean reqSynAck(@Nullable SynChatResp synChatResp) {
        boolean synAck = false;
        if (synChatResp != null) {
            SynItemBean synitem = synChatResp.getSynitem();
            if (synitem != null) {
                int id = synitem.getId();
                synAck = reqSynAck(String.valueOf(id));
            }
        }
        return synAck;
    }

    /**
     * 同步确认
     */
    private boolean reqSynAck(@NonNull String synid) {
        SynAckReq synAckReq = new SynAckReq(synid);
        Response<BaseResp<String>> response = synAckReq.post();
        return response.isSuccessful();
    }

    /**
     * 同步到本地数据库
     */
    @NonNull
    private ChatListTableEvent sync2ChatListTable(@NonNull SynChatResp synChatResp) {
        boolean isAll = synChatResp.getAll() == 1;
        if (isAll) {
            ChatListTableCrud.deleteAll();
        }
        // 插入、更新
        List<ChatListBean> chatlist = synChatResp.getChatlist();
        List<ChatListTable> insertTables = ChatListTableCrud.insertOrReplaceInTx(chatlist);
        List<ChatListResp.List> insertLists = ChatListTableConverter.convert2ChatListResp(insertTables);
        // 删除
        List<ChatListBean> dellist = synChatResp.getDellist();
        List<ChatListTable> deleteTables = ChatListTableCrud.deleteInTx(dellist);
        List<ChatListResp.List> deleteLists = ChatListTableConverter.convert2ChatListResp(deleteTables);
        // 构造 event
        return ChatListTableEvent.getInstance_syncSuccess(isAll, insertLists, deleteLists);
    }

    /**
     * 获取会话同步数据
     */
    @Nullable
    private SynChatResp reqSynChat(@Nullable String syntime) {
        SynChatReq synChatReq = new SynChatReq(syntime);
        Response<BaseResp<SynChatResp>> response = synChatReq.get();
        if (!response.isSuccessful()) {
            return null;
        }
        BaseResp<SynChatResp> body = response.body();
        if (body == null) {
            return null;
        }
        return body.getData();
    }
}
