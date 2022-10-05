package com.tiocloud.chat.feature.search.curr.all.adapter;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.search.curr.all.adapter.model.ItemType;
import com.tiocloud.chat.feature.search.curr.all.adapter.model.SectionMultipleItem;
import com.tiocloud.chat.feature.search.curr.friend.FriendListAdapter;
import com.tiocloud.chat.feature.search.curr.group.GroupListAdapter;
import com.watayouxiang.httpclient.model.response.MailListResp;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-02-17
 * desc :
 */
public class AllListAdapter extends BaseSectionMultiItemQuickAdapter<SectionMultipleItem, BaseViewHolder>
        implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    private final String keyWord;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data         A new list is created out of this one to avoid mutable list
     * @param recyclerView
     * @param keyWord
     */
    public AllListAdapter(List<SectionMultipleItem> data, RecyclerView recyclerView, String keyWord) {
        super(R.layout.tio_search_head_item, data);
        this.keyWord = keyWord;

        addItemType(ItemType.FRIEND.value, R.layout.tio_search_friend_item);
        addItemType(ItemType.GROUP.value, R.layout.tio_search_group_item);

        setOnItemClickListener(this);
        setOnItemChildClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(this);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SectionMultipleItem item) {
        helper.setText(R.id.tv_headTxt, item.header);
        if (item.hasMore) {
            helper.addOnClickListener(R.id.ll_more);
        } else {
            helper.setVisible(R.id.ll_more, false);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionMultipleItem item) {
        int itemType = item.getItemType();
        if (itemType == ItemType.FRIEND.value) {
            MailListResp.Friend friend = item.t.friend;
            FriendListAdapter.setItemData(helper, friend, keyWord);
        } else if (itemType == ItemType.GROUP.value) {
            MailListResp.Group group = item.t.group;
            GroupListAdapter.setItemData(helper, group, keyWord);
        }
    }

    // ====================================================================================
    // 子控件点击
    // ====================================================================================

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        AllListAdapter listAdapter = (AllListAdapter) adapter;
        SectionMultipleItem item = listAdapter.getData().get(position);
        if (view.getId() == R.id.ll_more) {
            onClickMoreBtn(item.pageIndex);
        }
    }

    protected void onClickMoreBtn(int pageIndex) {

    }

    // ====================================================================================
    // item click
    // ====================================================================================

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        AllListAdapter listAdapter = (AllListAdapter) adapter;
        SectionMultipleItem item = listAdapter.getData().get(position);
        int itemType = item.getItemType();
        if (itemType == ItemType.FRIEND.value) {
            MailListResp.Friend friend = item.t.friend;
            onClickFriendItem(friend);
        } else if (itemType == ItemType.GROUP.value) {
            MailListResp.Group group = item.t.group;
            onClickGroupItem(group);
        }
    }

    protected void onClickGroupItem(MailListResp.Group group) {

    }

    protected void onClickFriendItem(MailListResp.Friend friend) {

    }
}
