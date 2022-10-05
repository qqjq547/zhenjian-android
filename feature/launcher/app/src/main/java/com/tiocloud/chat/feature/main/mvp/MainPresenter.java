package com.tiocloud.chat.feature.main.mvp;

import android.util.Log;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.tiocloud.account.feature.login.LoginActivity;
import com.tiocloud.chat.mvp.socket.SocketPresenter;
import com.tiocloud.chat.preferences.TioMemory;
import com.tiocloud.chat.util.AppUpdateTool;
import com.tiocloud.jpush.PushLauncher;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.audiorecord.tio.TioBellVibrate;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.MsgTip;
import com.watayouxiang.imclient.model.body.wx.WxFriendChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public class MainPresenter extends MainContract.Presenter {
    private final SocketPresenter socketPresenter;
    private AppUpdateTool appUpdateTool;

    public MainPresenter(MainContract.View view) {
        super(new MainModel(), view, true);
        socketPresenter = new SocketPresenter(() -> getView().getActivity());
    }

    @Override
    public void detachView() {
        super.detachView();
        socketPresenter.detachView();
        if (appUpdateTool != null) {
            appUpdateTool.release();
        }
    }

    @Override
    public void init() {
        BarUtils.transparentStatusBar(getView().getActivity());
        getView().initViews();
        connectSocket();
    }

    @Override
    public void checkAppUpdate() {
        ThreadUtils.getMainHandler().postDelayed(() -> {
            if (appUpdateTool == null) {
                appUpdateTool = new AppUpdateTool(getView().getMainActivity());
            }
            appUpdateTool.checkUpdateNormal();
        }, 300);
    }

    @Override
    public void clearAllNotifications() {
        PushLauncher.getInstance().clearAllNotifications();
    }

    private void connectSocket() {
        socketPresenter.connectSocket(resp -> {
            switch (resp) {
                case UnLogin:
                case TokenNull:
                    LoginActivity.start(getView().getActivity());
                    socketPresenter.finishAllActivity();
                    break;
                case ImServerNull:
                    socketPresenter.exitApp();
                    break;
                case AlreadyConnect:
                case ConnectSuccess:
                    break;
            }
        });
    }

    // ====================================================================================
    // 发言过快提示
    // ====================================================================================

    // 消息提示
    // 发言过快提示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgTip(MsgTip ntf) {
        String msg = ntf.msg;
        if (msg == null) return;
        TioToast.showShort(msg);
    }

    // ====================================================================================
    // 群聊私聊消息，响铃实现
    // ====================================================================================

    // 私聊通知
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onWxFriendChatNtf(WxFriendChatNtf ntf) {
        //控制私聊撤回详细是否显示
        if (ntf.sendbysys == 1) {
            SPStaticUtils.put("ChatNtf", ntf.showmsg);
        }
//        Log.d("=====message=11==",TioMemory.getInChatLinkId()+"=showmsg=="+ntf.chatlinkid);

//        if (ntf.readflag == 2||ntf.readflag == 1// 未读消息
//                && ntf.uid != TioDBPreferences.getCurrUid()// 不是自己发的消息
//                && !StringUtils.equals(ntf.chatlinkid, TioMemory.getInChatLinkId())// 不在会话中
//                && TioIMClient.getInstance().isHandshake()// 长连接已建立
//                && CurrUserTableCrud.curr_isOpenMsgRemind(true)// 打开通知
//                && notChatDND(ntf.chatlinkid)// 非消息免打扰
//        )
            if (ntf.uid != TioDBPreferences.getCurrUid()// 不是自己发的消息
                    && !StringUtils.equals(ntf.chatlinkid, TioMemory.getInChatLinkId())// 不在会话中
                    && TioIMClient.getInstance().isHandshake()// 长连接已建立
                    && CurrUserTableCrud.curr_isOpenMsgRemind(true)// 打开通知
                    && notChatDND(ntf.chatlinkid)// 非消息免打扰
            ){
//            if(!TioMemory.getInChatLinkId().equals(ntf.chatlinkid)){
                TioBellVibrate.getInstance().start(TioBellVibrate.Bell.MSG_NTF_P2P);
//            }

        }

    }

    // 群聊通知
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onWxGroupChatNtf(WxGroupChatNtf ntf) {

        if (ntf.sendbysys == 1) {
            SPStaticUtils.put("ChatNtf", ntf.showmsg);
            Log.d("=====message=22==","=showmsg=="+ntf.showmsg);
        }
        if (ntf.f != TioDBPreferences.getCurrUid()// 不是自己发的消息
                && !StringUtils.equals(ntf.chatlinkid, TioMemory.getInChatLinkId())// 不在会话中
                && TioIMClient.getInstance().isHandshake()// 长连接已建立
                && CurrUserTableCrud.curr_isOpenMsgRemind(true)// 打开通知
                && notChatDND(ntf.chatlinkid)// 非消息免打扰
        ) {
            TioBellVibrate.getInstance().start(TioBellVibrate.Bell.MSG_NTF_GROUP);
        }
    }

    private boolean notChatDND(String chatLinkId) {
        ChatListTable table = ChatListTableCrud.query_chatLinkId(chatLinkId);
        if (table != null) {
            return table.getMsgfreeflag() != 1;
        }
        return true;
    }
}
