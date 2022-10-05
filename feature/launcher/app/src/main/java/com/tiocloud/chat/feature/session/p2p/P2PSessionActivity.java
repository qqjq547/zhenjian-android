package com.tiocloud.chat.feature.session.p2p;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.MessageUpdataEvent;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.utils2.SPUtilsNew;
import com.tiocloud.chat.feature.session.common.SessionActivity;
import com.tiocloud.chat.feature.session.common.action.model.base.BaseAction;
import com.tiocloud.chat.feature.session.common.model.SessionExtras;
import com.tiocloud.chat.feature.session.common.model.SessionType;
import com.tiocloud.chat.feature.session.p2p.customization.P2PActions;
import com.tiocloud.chat.feature.session.p2p.fragment.P2PSessionFragment;
import com.tiocloud.chat.feature.session.p2p.mvp.P2PActivityContract;
import com.tiocloud.chat.feature.session.p2p.mvp.P2PActivityPresenter;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.session.feature.session_info_p2p.P2PSessionInfoActivity;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020-02-09
 * desc : 私聊
 */
public class P2PSessionActivity extends SessionActivity implements P2PActivityContract.View {

    public static void active(@NonNull Context context, @NonNull String uid) {
        start(context, null, uid, null);
    }

    public static void enter(@NonNull Context context, @NonNull String chatLinkId) {
        start(context, null, null, chatLinkId);
    }

    public static void start(@NonNull Context context,
                             @Nullable Class<? extends Activity> backToClass,
                             @Nullable String uid,
                             @Nullable String chatLinkId) {
        checkPermission(() -> {
            boolean handshake = TioIMClient.getInstance().isHandshake();

//            Log.d("=====进入私聊页：uid = %s, chatLinkId = %s, handshake = %b",uid+"==="+ chatLinkId+"===="+ handshake);
            if (!handshake) {
                TioToast.showShort("当前网络异常");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(SessionExtras.EXTRA_BACK_TO_CLASS, backToClass);
            intent.putExtra(SessionExtras.EXTRA_USER_ID, uid);
            intent.putExtra(SessionExtras.EXTRA_CHAT_LINK_ID, chatLinkId);
            intent.putExtra(SessionExtras.EXTRA_ACTIONS, P2PActions.get());
            intent.setClass(context, P2PSessionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        });
    }

    // ====================================================================================
    // param
    // ====================================================================================

    @Nullable
    public String getUid() {
        return getIntent().getStringExtra(SessionExtras.EXTRA_USER_ID);
    }

    @Override
    public void setUid(String uid) {
        getIntent().putExtra(SessionExtras.EXTRA_USER_ID, uid);
    }

    @Nullable
    public String getChatLinkId() {
        return getIntent().getStringExtra(SessionExtras.EXTRA_CHAT_LINK_ID);
    }

    @NonNull
    @Override
    public SessionType getSessionType() {
        return SessionType.P2P;
    }

    @Nullable
    @Override
    protected Class<? extends Activity> getBackToClass() {
        return (Class<? extends Activity>) getIntent().getSerializableExtra(SessionExtras.EXTRA_BACK_TO_CLASS);
    }

    @Nullable
    @Override
    public ArrayList<BaseAction> getActions() {
        return (ArrayList<BaseAction>) getIntent().getSerializableExtra(SessionExtras.EXTRA_ACTIONS);
    }

    // ====================================================================================
    // init
    // ====================================================================================

    private P2PActivityPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        presenter = new P2PActivityPresenter(this);
        presenter.init();
    }
    private float fontSizeScale;
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        Configuration config = res.getConfiguration();
        if(fontSizeScale>0.5){
            config.fontScale= fontSizeScale;//1 设置正常字体大小的倍数
        }
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        presenter.init();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageUpdataEvent updataEvent) {
        //收到通知后 删除指定好友的所有本地聊天记录 备注：收到此通知时数据库聊天记录已经清空，可手动删除当前会话聊天记录 也可以刷新当前会话的聊天记录
//        Log.d("===删除指定好友===","===222=");
        String chatLinkId = getChatLinkId();
        if(chatLinkId != null) {
            presenter.enter(chatLinkId);
        }

    }

    @Override
    public void initUI() {
        setTitle(getString(R.string.p2p_talk));
        fontSizeScale = (float) SPUtilsNew.get(this, BaseConstants.SP_FontScale, 0.0f);

        String uid = getUid();
        String chatLinkId = getChatLinkId();
        if (uid != null) {
            presenter.active(uid);
        } else if (chatLinkId != null) {
            presenter.enter(chatLinkId);
        } else {
            TioToast.showShort("初始化失败");
            getActivity().finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getChatInfo();
    }

    private WxChatItemInfoResp.DataBean data;

    @Override
    public void onChatInfoResp(WxChatItemInfoResp resp) {
        data = resp.data;
        // 设置uid
        setUid(data.bizid);
        // 标题
        getTitleBar().setTitle(StringUtil.nonNull(data.name));
        // menu
        getTitleBar().getMoreBtn().setOnClickListener(v -> P2PSessionInfoActivity.start(getActivity(), resp.chatlinkid));
    }

    @Override
    public void showFragment(String chatLinkId) {
        replaceFragment(P2PSessionFragment.create(chatLinkId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();

        EventBus.getDefault().unregister(this);


    }

    public WxChatItemInfoResp.DataBean getChatItemDataBean(){
        return data;
    }
}
