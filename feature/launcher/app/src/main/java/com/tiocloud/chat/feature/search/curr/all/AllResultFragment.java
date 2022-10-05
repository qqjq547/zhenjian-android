package com.tiocloud.chat.feature.search.curr.all;

import androidx.recyclerview.widget.RecyclerView;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.tiocloud.chat.feature.search.curr.all.adapter.AllListAdapter;
import com.tiocloud.chat.feature.search.curr.all.adapter.model.ItemType;
import com.tiocloud.chat.feature.search.curr.all.adapter.model.MultiItem;
import com.tiocloud.chat.feature.search.curr.all.adapter.model.SectionMultipleItem;
import com.tiocloud.chat.feature.search.curr.main.SearchFragment;
import com.tiocloud.chat.feature.search.curr.main.base.BaseResultFragment;
import com.tiocloud.chat.feature.session.group.GroupSessionActivity;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * author : TaoWang
 * date : 2020-02-13
 * desc :
 */
public class AllResultFragment extends BaseResultFragment<List<SectionMultipleItem>> {

    // ====================================================================================
    // data
    // ====================================================================================

    private List<SectionMultipleItem> data2Items(List<MailListResp.Friend> friends, List<MailListResp.Group> groups) {
        if (friends == null) friends = new ArrayList<>();
        if (groups == null) groups = new ArrayList<>();

        int showCount = 3;// 最多显示个数
        List<SectionMultipleItem> items = new ArrayList<>();

        // 好友
        int friendSize = friends.size();
        if (friendSize > 0) {
            items.add(new SectionMultipleItem("好友", 1, friendSize > showCount));
            for (int i = 0; i < (Math.min(friendSize, showCount)); i++)
                items.add(new SectionMultipleItem(new MultiItem(friends.get(i)), ItemType.FRIEND));
        }

        // 群组
        int groupSize = groups.size();
        if (groupSize > 0) {
            items.add(new SectionMultipleItem("群聊", 2, groupSize > showCount));
            for (int i = 0; i < (Math.min(groupSize, showCount)); i++)
                items.add(new SectionMultipleItem(new MultiItem(groups.get(i)), ItemType.GROUP));
        }

        return items;
    }

    @Override
    protected void loadData(String keyWord) {
        getModel().requestMailList(null, keyWord, new BaseModel.DataProxy<MailListResp>() {
            @Override
            public void onSuccess(MailListResp resp) {
                List<MailListResp.Friend> friends = resp.fd;
                List<MailListResp.Group> groups = resp.group;
                notifySuccess(data2Items(friends, groups));
            }

            @Override
            public void onFailure(String msg) {
                notifyError();
            }
        });
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    protected int successLayoutId() {
        return R.layout.tio_search_all_result_fragment;
    }

    @Override
    protected void initSuccessLayout(List<SectionMultipleItem> data, String keyWord) {
        // list
        RecyclerView recyclerView = findViewById(R.id.rv_list);
        // adapter
        new AllListAdapter(data, recyclerView, keyWord) {
            @Override
            protected void onClickMoreBtn(int pageIndex) {
                super.onClickMoreBtn(pageIndex);
                SearchFragment searchFragment = (SearchFragment) getParentFragment();
                if (searchFragment != null) {
                    searchFragment.setCurrentItem(pageIndex);
                }
            }

            @Override
            protected void onClickFriendItem(MailListResp.Friend friend) {
                super.onClickFriendItem(friend);
                UserDetailActivity.start(getActivity(), String.valueOf(friend.uid));
            }

            @Override
            protected void onClickGroupItem(MailListResp.Group group) {
                super.onClickGroupItem(group);
                GroupSessionActivity.active(getActivity(), group.groupid);
            }
        };
    }
}
