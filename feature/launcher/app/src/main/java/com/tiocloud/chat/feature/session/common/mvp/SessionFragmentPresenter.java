package com.tiocloud.chat.feature.session.common.mvp;

import android.util.Log;

import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.proxy.MsgListProxy;
import com.tiocloud.session.event.AgreeJoinGroupEvent;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.imclient.model.body.wx.WxFaultItem;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgImage;
import com.watayouxiang.imclient.utils.LoggerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SessionFragmentPresenter extends SessionFragmentContract.Presenter {
    public SessionFragmentPresenter(SessionFragmentContract.View view) {
        super(new SessionFragmentModel(), view, true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAgreeJoinGroupEvent(AgreeJoinGroupEvent event) {
        String mid = event.mid;
        if (mid == null) return;
        MsgListProxy proxy = getView().getMsgListProxy();
        proxy.handleApplyMsg(mid);
    }

    //上传图片或视频时的模拟数据
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDefultMsg(WxFaultItem wxFaultItem) {
        Log.d("===onDefultMsg===", "=="+wxFaultItem.progress);
        TioMsg tioMsg = new TioMsg();
        tioMsg.setFault(true);
        tioMsg.setChatLinkId(wxFaultItem.chatlinkid);
        tioMsg.setMsgType(TioMsgType.image);
        tioMsg.setSendMsg(true);
        tioMsg.setTime(System.currentTimeMillis());
        tioMsg.setAvatar(currResp.avatar);
        tioMsg.setName(currResp.loginname);
        InnerMsgImage innerMsgImage = new InnerMsgImage();
        innerMsgImage.coverurl = wxFaultItem.url;
        innerMsgImage.progress = wxFaultItem.progress;
        tioMsg.setContentObj(innerMsgImage);
//        getView().getMsgListProxy().handleFaultMsg(tioMsg);
    }

    private UserCurrResp currResp;
    // 用户系统通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserSysNtf(WxUserSysNtf ntf) {
        switch (ntf.code) {
            case 41://收到通知后 删除指定好友的所有本地聊天记录 备注：收到此通知时数据库聊天记录已经清空，可手动删除当前会话聊天记录 也可以刷新当前会话的聊天记录
//                Log.d("===删除指定好友===","===111=");
//                getCurUserInfo();
                break;
        }
    }
    @Override
    public void getCurUserInfo() {
        getModel().getUserCurrReq().get(new TioCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp userCurrResp) {
                currResp = userCurrResp;
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });

    }
}
