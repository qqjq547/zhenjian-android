package com.tiocloud.chat.feature.session.group.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.chat.feature.forbidden.ForbiddenMvpPresenter;
import com.tiocloud.chat.feature.session.common.SessionFragment;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.model.SessionExtras;
import com.tiocloud.chat.feature.session.common.panel.MsgInputPanel;
import com.tiocloud.chat.feature.session.group.fragment.ait.AitManager;
import com.tiocloud.chat.feature.session.group.fragment.mvp.GroupFragmentContract;
import com.tiocloud.chat.feature.session.group.fragment.mvp.GroupFragmentPresenter;
import com.tiocloud.chat.feature.user.detail.mvp.UserPresenter;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.utils.HtmlUtils;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatReq;
import com.watayouxiang.imclient.packet.TioPacket;
import com.watayouxiang.imclient.packet.TioPacketBuilder;

/**
 * author : TaoWang
 * date : 2019-12-27
 * desc : 群聊
 */
public class GroupSessionFragment extends SessionFragment implements GroupFragmentContract.View {

    public static GroupSessionFragment create(@NonNull String chatLinkId, @NonNull String groupId) {
        GroupSessionFragment fragment = new GroupSessionFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SessionExtras.EXTRA_CHAT_LINK_ID, chatLinkId);
        bundle.putString(SessionExtras.EXTRA_GROUP_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public String getGroupId() {
        return getArguments().getString(SessionExtras.EXTRA_GROUP_ID);
    }

    @NonNull
    @Override
    public String getChatLinkId() {
        return getArguments().getString(SessionExtras.EXTRA_CHAT_LINK_ID);
    }

    // ====================================================================================
    // init
    // ====================================================================================

    private GroupFragmentPresenter presenter;
    private AitManager aitManager;
    private ForbiddenMvpPresenter forbiddenPresenter;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new GroupFragmentPresenter(this);
        forbiddenPresenter = new ForbiddenMvpPresenter();
        presenter.init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        aitManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
        aitManager.reset();
        forbiddenPresenter.detachView();
    }

    @Override
    public boolean onAvatarLongClick(View v, TioMsg msg) {
        if (msg == null) return false;
        if (msg.isSendMsg()) return false;

        String uid = msg.getUid();
        String groupId = getGroupId();
        forbiddenPresenter.longClickAvatar(uid, groupId, v, () -> aitManager.insertAitMemberInner(msg));
        return true;
    }
    @Override
    public View.OnClickListener onAvatarClick(TioMsg msg) {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                UserPresenter.checkStart(getTioActivity(), getGroupId(), msg.getUid());
            }
        };
    }
    @Override
    public boolean sendMessage(String msg) {
        String aitStr = aitManager.getAitTeamMemberAccount();
        String escapeMsg = HtmlUtils.escapeHtml(msg);
        int chatLinkId = Integer.parseInt(getChatLinkId());
        WxGroupChatReq body = new WxGroupChatReq(escapeMsg, chatLinkId, aitStr);
        TioPacket packet = TioPacketBuilder.getWxGroupChatReq(body);
        boolean isSend = TioIMClient.getInstance().sendPacket(packet);
        if (isSend) {
            aitManager.reset();
        }
        return isSend;
    }

    @Override
    public void resetUI() {
        // 列表初始化
        getMsgListProxy().setOnFetchMoreListener(() -> presenter.loadMore());
        // 艾特功能实现
        initAitManager();
    }

    // ====================================================================================
    // ait相关
    // ====================================================================================

    private void initAitManager() {
        aitManager = new AitManager(this, getGroupId());
        MsgInputPanel inputPanel = getInputPanel();
        inputPanel.setAitTextWatcher(aitManager);
        aitManager.setTextChangeListener(inputPanel);
    }

}
