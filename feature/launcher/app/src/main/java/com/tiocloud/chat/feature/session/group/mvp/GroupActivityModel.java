package com.tiocloud.chat.feature.session.group.mvp;

import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.ActChatReq;
import com.watayouxiang.httpclient.model.response.ActChatResp;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoReq;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.imclient.packet.TioPacket;
import com.watayouxiang.imclient.packet.TioPacketBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public class GroupActivityModel extends GroupActivityContract.Model {

    public GroupActivityModel() {
        super(true);
    }

    @Override
    public void detachModel() {
        super.detachModel();
        mChatInfoRespProxy = null;
        mChatLinkId = null;
    }

    // ====================================================================================
    // 激活会话
    // ====================================================================================

    @Override
    public void actChat(final String groupid, final DataProxy<ActChatResp> proxy) {
        ActChatReq actChatReq = ActChatReq.getGroup(groupid);
        actChatReq.setCancelTag(this);
        actChatReq.get(new TioCallbackImpl<ActChatResp>() {
            @Override
            public void onTioSuccess(ActChatResp actChatResp) {
                proxy.onSuccess(actChatResp);
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }

    // ====================================================================================
    // 获取聊天信息
    // ====================================================================================

    private String mChatLinkId;
    private DataProxy<WxChatItemInfoResp> mChatInfoRespProxy;

    @Override
    public void getChatInfo(String chatLinkId, DataProxy<WxChatItemInfoResp> proxy) {
        if (chatLinkId != null) {
            mChatLinkId = chatLinkId;
            mChatInfoRespProxy = proxy;
            TioPacket wxChatItemInfoReq = TioPacketBuilder.getWxChatItemInfoReq(new WxChatItemInfoReq(chatLinkId));
            TioIMClient.getInstance().sendPacket(wxChatItemInfoReq);
        }
    }

    // 聊天信息响应
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxChatItemInfoResp(WxChatItemInfoResp event) {
        // 如果不是本次请求的响应则丢弃
        if (!event.chatlinkid.equals(mChatLinkId)) {
            return;
        }
        if (mChatInfoRespProxy != null) {
            mChatInfoRespProxy.onSuccess(event);
        }
    }
}
