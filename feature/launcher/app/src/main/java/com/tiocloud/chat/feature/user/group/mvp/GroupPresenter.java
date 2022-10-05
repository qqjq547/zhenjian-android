package com.tiocloud.chat.feature.user.group.mvp;

import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiocloud.chat.feature.session.group.GroupSessionActivity;
import com.tiocloud.chat.feature.user.group.adapter.GroupAdapter;
import com.tiocloud.chat.widget.TioRefreshView;
import com.tiocloud.chat.widget.popupwindow.GroupOpWindow;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupOperNtf;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public class GroupPresenter extends GroupContract.Presenter {

    // ====================================================================================
    // eventBus
    // ====================================================================================

    public GroupPresenter(GroupContract.View view) {
        super(view);
    }

    // 群操作通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxGroupOperNtf(WxGroupOperNtf ntf) {
        WxGroupOperNtf.Oper oper = WxGroupOperNtf.Oper.valueOf(ntf.oper);
        if (oper == null) return;
        switch (oper) {
            case CHANGE_OUT_GROUP:
            case DEL_GROUP:
            case EXIT_GROUP:
            case UPDATE_GROUP_NAME:
            case EWMOVE_GROUP://收到通知后 删除指定好友的所有本地聊天记录 备注：收到此通知时数据库聊天记录已经清空，可手动删除当前会话聊天记录 也可以刷新当前会话的聊天记录
                Log.d("===删除指定好友===","==本地记录==");
                load();
                break;
        }
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    private GroupAdapter adapter;
    private TioRefreshView refreshView;

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        adapter = new GroupAdapter(recyclerView);
        ListItemClickListener listener = new ListItemClickListener();
        adapter.setOnItemClickListener(listener);
        adapter.setOnItemLongClickListener(listener);
    }

    private final class ListItemClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            MailListResp.Group group = ((GroupAdapter) adapter).getData().get(position);
            String groupId = group.groupid;
            GroupSessionActivity.active(getView().getTioActivity(), groupId);
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            MailListResp.Group group = ((GroupAdapter) adapter).getData().get(position);
            return new GroupOpWindow(view).show(group);
        }
    }

    @Override
    public void initRefreshView(TioRefreshView refreshView) {
        this.refreshView = refreshView;
        refreshView.setOnRefreshListener(this::load);
        refreshView.setEnabled(true);
    }

    // ====================================================================================
    // data
    // ====================================================================================

    @Override
    public void load() {
        if (adapter == null) return;
        if (refreshView == null) return;

        getModel().requestMailList(new BaseModel.DataProxy<List<MailListResp.Group>>() {
            @Override
            public void onSuccess(List<MailListResp.Group> groups) {
                // 组数统计头
                adapter.setGroupSize(groups.size());
//                getModel().setGroupNum(getView().getMainGroupFragment(), groups.size());
                // 数据
                adapter.setNewData(groups);
                refreshView.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort(msg);
                refreshView.setRefreshing(false);
            }
        });
    }
}
