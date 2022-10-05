package com.tiocloud.chat.feature.home.chat.adapter;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.feature.home.chat.model.ItemComparator;
import com.watayouxiang.httpclient.model.response.ChatListResp;

import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/07/13
 *     desc   : 数据处理
 * </pre>
 */
abstract class BaseDataAdapter extends BaseQuickAdapter<ChatListResp.List, BaseViewHolder> {

    // 总消息未读数
    private int mTotalUnread = 0;
    // item 排序比较器
    private ItemComparator itemComparator;

    public BaseDataAdapter(@LayoutRes int layoutResId, @NonNull RecyclerView recyclerView) {
        super(layoutResId);
        bindToRecyclerView(recyclerView);
    }

    // ====================================================================================
    // 总的未读消息数
    // ====================================================================================

    /**
     * 设置 "总的未读消息数"
     * <p>
     * 如果为 totalUnread = null，则自动计算。
     */
    private void setTotalUnread(Integer totalUnread) {
        // 统计消息未读数
        if (totalUnread == null) {
            totalUnread = 0;
            for (ChatListResp.List item : getData()) {
                totalUnread += item.notreadcount;
            }
        }
        // 更新消息未读数
        if (mTotalUnread != totalUnread) {
            mTotalUnread = totalUnread;
        }
        onTotalUnreadChanged(totalUnread);
    }

    /**
     * 通知 "总的未读消息数" 发生变化
     */
    public void onTotalUnreadChanged(@IntRange(from = 0) int totalUnread) {

    }

    // ====================================================================================
    // item 的增删改查
    // ====================================================================================

    /**
     * 设置所有数据（核心方法）
     */
    @Override
    public void setNewData(@Nullable List<ChatListResp.List> data) {
        super.setNewData(data);
        setTotalUnread(null);
    }

    /**
     * 查（核心方法）
     */
    @Nullable
    @Override
    public ChatListResp.List getItem(@IntRange(from = 0) int position) {
        return super.getItem(position);
    }

    /**
     * 增（核心方法）
     */
    private void addItem(@IntRange(from = 0) int position, @NonNull ChatListResp.List data) {
        // 更新消息未读数
        setTotalUnread(mTotalUnread + data.notreadcount);
        // 插入一个 item
        super.addData(position, data);
    }

    /**
     * 删（核心方法）
     */
    private void removeItem(@IntRange(from = 0) int position) {
        ChatListResp.List item = getItem(position);
        if (item != null) {
            // 更新消息未读数
            setTotalUnread(mTotalUnread - item.notreadcount);
            // 移除一个 item
            super.remove(position);
        }
    }

    /**
     * 改（核心方法）
     * <p>
     * 如果存在，则更新item（item数据、itemView、总的未读消息数）
     */
    private void updateItem(@IntRange(from = 0) int position, @NonNull ChatListResp.List newItem) {
        // 获取 "新item" 的位置
        ChatListResp.List oldItem = getData().remove(position);
        int newPosition = getInsertPosition(newItem);
        // 判断 "新item" 和 "旧item" 位置是否一样
        if (position == newPosition) {
            // 位置不变
            // 插入 新item
            getData().add(position, newItem);
            // 刷新 itemView
            notifyItemChanged(position + getHeaderLayoutCount());
            setTotalUnread(mTotalUnread - oldItem.notreadcount + newItem.notreadcount);
        } else {
            // 位置发生改变
            // 插入 旧item
            getData().add(position, oldItem);
            // 移除 旧itemView
            this.removeItem(position);
            // 插入 新itemView
            this.addItem(newPosition, newItem);
            scrollUpIfFirstCompletelyVisible(newPosition);
        }
    }

    // ====================================================================================
    // 私有方法（无需关注）
    // ====================================================================================

    /**
     * 如果满足以下条件：
     * 1、如果是在第 0 位置插入
     * 2、第 0 个位置完全可见
     * 那么：
     * 滚动到顶部
     */
    private void scrollUpIfFirstCompletelyVisible(@IntRange(from = 0) int insertPosition) {
        if (insertPosition != 0) return;
        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView == null) return;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) return;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int firstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (firstCompletelyVisibleItemPosition == 0) {
            recyclerView.scrollToPosition(0);
        }
    }

    /**
     * 二分查找 "插入的位置"
     * <p>
     * 确保列表数据有序
     * <p>
     * 如果存在，则返回原来的位置
     * 如果不存在，则返插入的位置
     */
    private int getInsertPosition(@NonNull ChatListResp.List item) {
        if (itemComparator == null) {
            itemComparator = new ItemComparator();
        }
        int insertionPoint = Collections.binarySearch(getData(), item, itemComparator);
        if (insertionPoint < 0) {
            insertionPoint = -insertionPoint - 1;
        }
        return insertionPoint;
    }

    /**
     * 顺序查找 "item的位置"
     * <p>
     * 存在：>= 0
     * 不存在：-1
     */
    private int getItemPosition(@Nullable ChatListResp.List item) {
        // 容错处理
        if (item == null) return -1;
        String chatLinkId = item.id;
        if (chatLinkId == null) return -1;
        // 顺序查找
        List<ChatListResp.List> data = getData();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            ChatListResp.List temp = data.get(i);
            if (chatLinkId.equals(temp.id)) {
                return i;
            }
        }
        return -1;
    }

    // ====================================================================================
    // 批量更新、删除
    // ====================================================================================

    public void removeItem(@Nullable List<ChatListResp.List> delList) {
        if (delList == null || delList.size() == 0) return;

        for (ChatListResp.List item : delList) {
            // 顺序查找 "item的位置"
            int position = getItemPosition(item);
            // 存在则删除
            if (position >= 0) {
                removeItem(position);
            }
        }
    }

    public void updateItem(@Nullable List<ChatListResp.List> chatList) {
        if (chatList == null || chatList.size() == 0) return;

        for (ChatListResp.List item : chatList) {
            // 顺序查找 "item的位置"
            int position = getItemPosition(item);
            // 存在则修改
            // 不存在则插入
            if (position >= 0) {
                this.updateItem(position, item);
            } else {
                int insertPosition = getInsertPosition(item);
                this.addItem(insertPosition, item);
            }
        }
    }
}
