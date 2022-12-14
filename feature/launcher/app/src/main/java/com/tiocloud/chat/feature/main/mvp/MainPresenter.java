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
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.audiorecord.tio.TioBellVibrate;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.httpclient.model.request.FoundListResp;
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
        getFoundList();
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
    // ??????????????????
    // ====================================================================================

    // ????????????
    // ??????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgTip(MsgTip ntf) {
        String msg = ntf.msg;
        if (msg == null) return;
        TioToast.showShort(msg);
    }

    // ====================================================================================
    // ?????????????????????????????????
    // ====================================================================================

    // ????????????
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onWxFriendChatNtf(WxFriendChatNtf ntf) {
        //????????????????????????????????????
        if (ntf.sendbysys == 1) {
            SPStaticUtils.put("ChatNtf", ntf.showmsg);
        }
//        Log.d("=====message=11==",TioMemory.getInChatLinkId()+"=showmsg=="+ntf.chatlinkid);

//        if (ntf.readflag == 2||ntf.readflag == 1// ????????????
//                && ntf.uid != TioDBPreferences.getCurrUid()// ????????????????????????
//                && !StringUtils.equals(ntf.chatlinkid, TioMemory.getInChatLinkId())// ???????????????
//                && TioIMClient.getInstance().isHandshake()// ??????????????????
//                && CurrUserTableCrud.curr_isOpenMsgRemind(true)// ????????????
//                && notChatDND(ntf.chatlinkid)// ??????????????????
//        )
            if (ntf.uid != TioDBPreferences.getCurrUid()// ????????????????????????
                    && !StringUtils.equals(ntf.chatlinkid, TioMemory.getInChatLinkId())// ???????????????
                    && TioIMClient.getInstance().isHandshake()// ??????????????????
                    && CurrUserTableCrud.curr_isOpenMsgRemind(true)// ????????????
                    && notChatDND(ntf.chatlinkid)// ??????????????????
            ){
//            if(!TioMemory.getInChatLinkId().equals(ntf.chatlinkid)){
                TioBellVibrate.getInstance().start(TioBellVibrate.Bell.MSG_NTF_P2P);
//            }

        }

    }

    // ????????????
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onWxGroupChatNtf(WxGroupChatNtf ntf) {

        if (ntf.sendbysys == 1) {
            SPStaticUtils.put("ChatNtf", ntf.showmsg);
            Log.d("=====message=22==","=showmsg=="+ntf.showmsg);
        }
        if (ntf.f != TioDBPreferences.getCurrUid()// ????????????????????????
                && !StringUtils.equals(ntf.chatlinkid, TioMemory.getInChatLinkId())// ???????????????
                && TioIMClient.getInstance().isHandshake()// ??????????????????
                && CurrUserTableCrud.curr_isOpenMsgRemind(true)// ????????????
                && notChatDND(ntf.chatlinkid)// ??????????????????
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
    private void getFoundList(){
        getModel().requestFoundList(new BaseModel.DataProxy<FoundListResp>() {
            @Override
            public void onSuccess(FoundListResp founds) {
                getView().setFoundList(founds);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort(msg);
            }
        });
    }
}
