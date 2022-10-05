package com.tiocloud.chat.feature.session.p2p.mvp;

import android.util.Log;

import com.watayouxiang.httpclient.model.response.ActChatResp;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-10
 * desc :
 */
public class P2PActivityPresenter extends P2PActivityContract.Presenter {

    private String mChatLinkId;
    private boolean isFirstCall = true;

    public P2PActivityPresenter(P2PActivityContract.View view) {
        super(new P2PActivityModel(), view, false);
    }

    @Override
    public void init() {
        getView().initUI();
    }

    @Override
    public void active(String uid) {
        getModel().actChat(uid, new BaseModel.DataProxy<ActChatResp>() {
            @Override
            public void onSuccess(ActChatResp actChatResp) {
                enter(actChatResp.chat.id);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort("激活会话失败");
                getView().getActivity().finish();
            }
        });
    }

    @Override
    public void enter(String chatLinkId) {
        mChatLinkId = chatLinkId;
        getChatInfo(chatLinkId);
        getView().showFragment(chatLinkId);
    }


    private void getChatInfo(String chatLinkId) {
        getModel().getChatInfo(chatLinkId, new BaseModel.DataProxy<WxChatItemInfoResp>() {
            @Override
            public void onSuccess(WxChatItemInfoResp resp) {
                super.onSuccess(resp);
                getView().onChatInfoResp(resp);
            }
        });
    }

    @Override
    public void getChatInfo() {
        if (isFirstCall) {
            isFirstCall = false;
            return;
        }
        if (mChatLinkId != null) {
            getChatInfo(mChatLinkId);
        }
    }
}
