package com.watayouxiang.db.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.db.converter.ChatListTableConverter;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.db.utils.Utils;
import com.watayouxiang.httpclient.model.response.ChatListResp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/01
 *     desc   : {@link ChatListTable} 同步通知
 * </pre>
 */
public class ChatListTableEvent {
    /**
     * 同步是否成功
     */
    private boolean ok = false;
    /**
     * 是否同步所有
     */
    private boolean isAll = false;
    /**
     * item集合（插入、更新）
     */
    @Nullable
    private List<ChatListResp.List> chatList = null;
    /**
     * item集合（删除）
     */
    @Nullable
    private List<ChatListResp.List> delList = null;

    public ChatListTableEvent() {
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    @Nullable
    public List<ChatListResp.List> getChatList() {
        return chatList;
    }

    public void setChatList(@Nullable List<ChatListResp.List> chatList) {
        this.chatList = chatList;
    }

    @Nullable
    public List<ChatListResp.List> getDelList() {
        return delList;
    }

    public void setDelList(@Nullable List<ChatListResp.List> delList) {
        this.delList = delList;
    }

    @Override
    public String toString() {
        return "ChatListTableEvent{" +
                "ok=" + ok +
                ", isAll=" + isAll +
                ", chatList=" + chatList +
                ", delList=" + delList +
                '}';
    }

    // ====================================================================================
    // 获取事件的方式
    // ====================================================================================

    @NonNull
    private static List<ChatListResp.List> $tables2Resp(@NonNull Collection<ChatListTable> tables) {
        List<ChatListResp.List> resps = new ArrayList<>(tables.size());
        for (ChatListTable table : tables) {
            ChatListResp.List resp = ChatListTableConverter.convert2ChatListResp(table);
            resps.add(resp);
        }
        return resps;
    }

    // 同步成功
    @NonNull
    public static ChatListTableEvent getInstance_syncSuccess(boolean isAll, @Nullable List<ChatListResp.List> chatList, @Nullable List<ChatListResp.List> delList) {
        ChatListTableEvent event = new ChatListTableEvent();
        event.setOk(true);
        event.setAll(isAll);
        event.setChatList(chatList);
        event.setDelList(delList);
        return event;
    }

    // 同步失败
    @NonNull
    public static ChatListTableEvent getInstance_syncError() {
        ChatListTableEvent event = new ChatListTableEvent();
        event.setOk(false);
        return event;
    }

    // ====================================================================================
    // 插入、更新
    // ====================================================================================

    @NonNull
    public static ChatListTableEvent getInstance_insertOrUpdate(@NonNull List<ChatListResp.List> resps) {
        return getInstance_syncSuccess(false, resps, null);
    }

    @NonNull
    public static ChatListTableEvent getInstance_insertOrUpdate(@NonNull Collection<ChatListTable> tables) {
        List<ChatListResp.List> resps = $tables2Resp(tables);
        return getInstance_insertOrUpdate(resps);
    }

    @NonNull
    public static ChatListTableEvent getInstance_insertOrUpdate(@NonNull ChatListResp.List resp) {
        return getInstance_insertOrUpdate(Utils.newArrayList(resp));
    }

    @NonNull
    public static ChatListTableEvent getInstance_insertOrUpdate(@NonNull ChatListTable table) {
        return getInstance_insertOrUpdate(Utils.newArrayList(table));
    }

    // ====================================================================================
    // 删除
    // ====================================================================================

    @NonNull
    public static ChatListTableEvent getInstance_delete(@NonNull List<ChatListResp.List> delList) {
        return getInstance_syncSuccess(false, null, delList);
    }

    @NonNull
    public static ChatListTableEvent getInstance_delete(@NonNull Collection<ChatListTable> tables) {
        List<ChatListResp.List> resps = $tables2Resp(tables);
        return getInstance_syncSuccess(false, null, resps);
    }

    @NonNull
    public static ChatListTableEvent getInstance_delete(@NonNull ChatListResp.List resp) {
        return getInstance_delete(Utils.newArrayList(resp));
    }

    @NonNull
    public static ChatListTableEvent getInstance_delete(@NonNull ChatListTable table) {
        return getInstance_delete(Utils.newArrayList(table));
    }
}
