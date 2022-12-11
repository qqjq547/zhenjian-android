package com.tiocloud.chat.feature.home.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.tiocloud.chat.BuildConfig;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.databinding.TioChatFragmentBinding;
import com.tiocloud.chat.feature.home.chat.adapter.ChatAdapter;
import com.tiocloud.chat.feature.home.chat.mvp.ChatContract;
import com.tiocloud.chat.feature.home.chat.mvp.ChatPresenter;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.feature.main.fragment.MainChatFragment;
import com.tiocloud.chat.feature.main.model.MainTab;
import com.tiocloud.chat.feature.session.group.GroupSessionActivity;
import com.tiocloud.chat.feature.session.p2p.P2PSessionActivity;
import com.tiocloud.chat.widget.popupwindow.HomeOpWindow;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.db.event.ChatListTableEvent;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.imclient.event.WxGroupBean;
import com.watayouxiang.imclient.model.body.wx.WxGroupOperNtf;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author : TaoWang
 * date : 2020-01-13
 * desc : 聊天
 */
public class ChatFragment extends TioFragment implements ChatContract.View {

    private ChatPresenter presenter;
    private TioChatFragmentBinding binding;
    @Nullable
    private ChatAdapter listAdapter;

    @Nullable
    @Override
    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = TioChatFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new ChatPresenter(this);
        presenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void initUI() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        // 初始化刷新控件
        initRefreshView();
        // 初始化列表
        MainActivity mainActivity = getMainActivity();
        if (mainActivity != null) {
            initChatListView(mainActivity);
        }
    }


    /**
     * 初始化 - 会话列表
     */
    private void initChatListView(@NonNull MainActivity activity) {
        /* 未读消息数变化 */
        listAdapter = new ChatAdapter(binding.recyclerView) {
            @Override
            public void onTotalUnreadChanged(int totalUnread) {
                super.onTotalUnreadChanged(totalUnread);
                activity.updateMsgUnReadCount(totalUnread);
            }
        };
        /* 单击逻辑 */
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            ChatListResp.List item = listAdapter.getData().get(position);
//            TioLogger.d("会话信息：" + item);
            TioApplication.getInstanceKit().chatmode=item.chatmode;
//            Log.d("===会话信息：" , item.id);

            switch (item.chatmode) {
                case 1:// 私聊
                    // 进入私聊页
                    P2PSessionActivity.enter(activity, item.id);
                    break;
                case 2:// 群聊
                    // 进入群聊页
                    GroupSessionActivity.enter(activity, item.id);
                    break;
            }
        });
        /* 长按逻辑 */
        listAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            ChatListResp.List list = listAdapter.getData().get(position);
            new HomeOpWindow(view).show(list);
            return true;
        });
    }

    /**
     * 初始化 - 刷新控件
     */
    private void initRefreshView() {
        binding.refreshView.setEnabled(BuildConfig.DEBUG);
        binding.refreshView.setOnRefreshListener(() -> presenter.getChatList(true));
    }
    // 群操作通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxGroupBean(WxGroupBean ntf) {

        Log.d("===解散群聊===","-----");

        presenter.getChatList(true);

    }
    @Override
    public void onChatListRespSuccess(@Nullable ChatListResp lists) {
        if (listAdapter != null) {
            listAdapter.setNewData(lists);
        }
    }

    @Override
    public void onEndRefresh() {
        if (binding != null) {
            binding.refreshView.setRefreshing(false);
        }
    }

    @Override
    public void onChatListTableEvent(@NonNull ChatListTableEvent event) {
        if (!event.isOk()) {
            TioToast.showLong("聊天列表同步失败");
            return;
        }
        if (listAdapter == null) return;
        if (event.isAll()) {
            listAdapter.setNewData(event.getChatList());
        } else {
            List<ChatListResp.List> delList = event.getDelList();
            listAdapter.removeItem(delList);
            List<ChatListResp.List> chatList = event.getChatList();
            listAdapter.updateItem(chatList);
        }
        if(listAdapter.getData().size()>0) {
            binding.recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.recyclerView.scrollToPosition(0);
                }
            }, 500);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public List<ChatListResp.List> getChatList(){
        if (listAdapter!=null){
            return listAdapter.getData();
        }else {
            return new ArrayList<>();
        }
    }
}
