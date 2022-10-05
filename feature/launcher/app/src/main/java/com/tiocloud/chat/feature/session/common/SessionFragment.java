   package com.tiocloud.chat.feature.session.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.MessageUpdataEvent;
import com.tiocloud.chat.feature.session.common.action.util.ActionUtil;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.model.SessionContainer;
import com.tiocloud.chat.feature.session.common.model.SessionExtras;
import com.tiocloud.chat.feature.session.common.mvp.SessionFragmentContract;
import com.tiocloud.chat.feature.session.common.mvp.SessionFragmentPresenter;
import com.tiocloud.chat.feature.session.common.panel.MsgInputPanel;
import com.tiocloud.chat.feature.session.common.panel.MsgListPanel;
import com.tiocloud.chat.feature.session.common.proxy.MsgListProxy;
import com.tiocloud.chat.feature.session.common.proxy.SessionProxy;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

/**
 * author : TaoWang
 * date : 2019-12-30
 * desc : 会话页
 */
public abstract class SessionFragment extends TioFragment implements SessionProxy, SessionFragmentContract.View {

    private MsgInputPanel inputPanel;
    private MsgListPanel listPanel;
    private SessionFragmentPresenter presenter;
    private String  getGroupIdText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.message_fragment, container, false);
    }
    @NonNull
    @Override
    public String getGroupId() {
        return getArguments().getString(SessionExtras.EXTRA_GROUP_ID);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // mvp
        presenter = new SessionFragmentPresenter(this);

        // 设置 Actions
        ActionUtil.setParams(getSessionActivity().getActions(), this, getChatLinkId());
        // 初始化页面
        SessionContainer container = new SessionContainer(getSessionActivity(), this, getView(),
                getSessionActivity().getActions(), getChatLinkId());

        listPanel = new MsgListPanel(container);
        inputPanel = new MsgInputPanel(container);
        presenter.getCurUserInfo();

    }



    @Override
    public void onPause() {
        super.onPause();
        if (inputPanel != null) {
            inputPanel.collapse(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActionUtil.onActivityResult(requestCode, resultCode, data, getSessionActivity().getActions());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, getSessionActivity().getActions());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActionUtil.release(getSessionActivity().getActions());
        presenter.detachView();
        inputPanel.release();
        listPanel.release();
    }

    // ====================================================================================
    // getter
    // ====================================================================================

    @Override
    @NonNull
    public abstract String getChatLinkId();

    @Override
    @NonNull
    public SessionActivity getSessionActivity() {
        return (SessionActivity) Objects.requireNonNull(getActivity());
    }

    @Override
    public MsgInputPanel getInputPanel() {
        return inputPanel;
    }

    @Override
    public MsgListProxy getMsgListProxy() {
        return listPanel;
    }

    // ====================================================================================
    // proxy
    // ====================================================================================

    @Override
    public boolean onBackPressed() {
        if (inputPanel != null) {
            return inputPanel.collapse(true);
        }
        return false;
    }

    @Override
    public void onInputPanelExpand() {
        if (listPanel != null) {
            listPanel.scrollToBottomDelayed();
        }
    }

    @Override
    public void collapseInputPanel() {
        if (inputPanel != null) {
            inputPanel.collapse(false);
        }
    }

    @Override
    public boolean onAvatarLongClick(View v, TioMsg msg) {
        return false;
    }

    @Override
    public View.OnClickListener onAvatarClick(TioMsg msg) {
        return new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                UserDetailActivity.start(getTioActivity(), msg.getUid());
            }
        };
    }

}
