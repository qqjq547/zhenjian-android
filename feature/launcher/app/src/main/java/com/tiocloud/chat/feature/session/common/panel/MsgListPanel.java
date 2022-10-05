package com.tiocloud.chat.feature.session.common.panel;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.model.SessionContainer;
import com.tiocloud.chat.feature.session.common.proxy.MsgListProxy;
import com.watayouxiang.androidutils.recyclerview.BaseFetchLoadAdapter;
import com.watayouxiang.imclient.model.body.wx.WxFaultItem;

import java.util.ArrayList;
import java.util.List;

/**
 * author : TaoWang
 * date : 2019-12-27
 * desc :
 */
public class MsgListPanel implements MsgListProxy {
    // container
    private final SessionContainer container;

    // message list
    private RecyclerView recyclerView;
    private List<TioMsg> items;
    private MsgAdapter adapter;
    private NewMsgTipHelper newMsgTipHelper;

    public MsgListPanel(SessionContainer container) {
        this.container = container;
        initListView();
    }

    private void initListView() {
        // list
        recyclerView = container.rootView.findViewById(R.id.messageListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.requestDisallowInterceptTouchEvent(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState != RecyclerView.SCROLL_STATE_IDLE) {
                    container.proxy.collapseInputPanel();
                }
            }
        });
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // adapter
        items = new ArrayList<>();
        adapter = new MsgAdapter(recyclerView, items, container.chatLinkId) {
            @Override
            public boolean onAvatarLongClick(View v, TioMsg msg) {
                return container.proxy.onAvatarLongClick(v, msg);
            }

            @Override
            public View.OnClickListener onAvatarClick(TioMsg msg) {
                return container.proxy.onAvatarClick(msg);
            }
        };
        recyclerView.setAdapter(adapter);

        // 新消息提示
        TextView tvNewMsgTip = container.rootView.findViewById(R.id.tv_newMsgTip);
        newMsgTipHelper = new NewMsgTipHelper(recyclerView, this, tvNewMsgTip);
    }

    public void release() {
        if (adapter != null) {
            adapter.release();
            adapter = null;
        }
    }
    public void  upData(){
        if (adapter != null) {
          adapter.notifyDataSetChanged();
        }
    }

    // ====================================================================================
    // MsgListProxy
    // ====================================================================================

    @Override
    public void sendMsg(TioMsg message) {
        newMsgTipHelper.onBeforeDataChanged();
        adapter.appendData(message);
        newMsgTipHelper.onAfterDataChanged(message);
    }

    @Override
    public void deleteMsg(long mid) {
        adapter.deleteMsgByMid(mid);
    }

    @Override
    public void clearList() {
        adapter.clearData();
    }

    @Override
    public void fetchMoreEnd(List<TioMsg> msgList) {
        adapter.fetchMoreEnd(msgList, true);
    }

    @Override
    public void fetchMoreComplete(List<TioMsg> msgList) {
        adapter.fetchMoreComplete(msgList);
    }

    @Override
    public void readAllMsg() {
        adapter.readAllMsg();
    }

    @Override
    public void scrollToBottom() {
        recyclerView.scrollToPosition(adapter.getBottomDataPosition());
    }

    @Override
    public void scrollToBottomDelayed() {
        ThreadUtils.getMainHandler().postDelayed(this::scrollToBottom, 200);
    }

    @Override
    public void setOnFetchMoreListener(BaseFetchLoadAdapter.RequestFetchMoreListener requestFetchMoreListener) {
        adapter.setOnFetchMoreListener(requestFetchMoreListener);
    }

    @Override
    public void handleApplyMsg(String mid) {
        try {
            long _mid = Long.parseLong(mid);
            adapter.handleApplyMsg(_mid);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void handleFaultMsg(TioMsg tioMsg) {
           adapter.handleFaultMsg(tioMsg);
    }


}
