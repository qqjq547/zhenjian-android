package com.tiocloud.session.feature.session_info_p2p.mvp;

import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoReq;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.imclient.packet.TioPacket;
import com.watayouxiang.imclient.packet.TioPacketBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Model extends Contract.Model {
    public Model() {
        super(true);
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
