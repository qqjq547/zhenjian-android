package com.watayouxiang.imclient.event;

import android.util.Log;

import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.client.IMCallback;
import com.watayouxiang.imclient.engine.EventEngine;
import com.watayouxiang.imclient.model.body.MsgTip;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.imclient.model.body.wx.WxFocusNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendMsgResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxGroupMsgResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupOperNtf;
import com.watayouxiang.imclient.model.body.wx.WxHandshakeResp;
import com.watayouxiang.imclient.model.body.wx.WxUserOperNtf;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;
import com.watayouxiang.imclient.packet.TioCommand;
import com.watayouxiang.imclient.packet.TioPacket;

import org.greenrobot.eventbus.EventBus;

import java.nio.ByteBuffer;

/**
 * author : TaoWang
 * date : 2020-02-06
 * desc : Event Poster
 */
public class TioEventPoster implements IMCallback<TioPacket> {
    @Override
    public void onConnecting() {
        getEventEngine().post(new TioStateEvent(TioState.CONNECTING));
    }

    @Override
    public void onConnected() {
        getEventEngine().post(new TioStateEvent(TioState.CONNECT));
    }

    @Override
    public void onDisconnected() {
        getEventEngine().post(new TioStateEvent(TioState.DISCONNECT));
    }

    @Override
    public void onError(Exception e) {
        getEventEngine().post(new TioStateEvent(TioState.ERROR));
    }

    @Override
    public void onSendBegin(TioPacket packet) {

    }

    @Override
    public void onSendCancel(TioPacket packet) {

    }

    @Override
    public void onSendEnd(TioPacket packet) {

    }

    @Override
    public void onReceiveBegin(ByteBuffer buffer) {

    }

    @Override
    public void onReceiveCancel() {

    }

    @Override
    public void onReceiveEnd(TioPacket packet) {
        if (packet == null) return;

        short command = packet.getCommand();
        Object body = packet.getBody();
        if (command == TioCommand.WX_HANDSHAKE_RESP) {// ????????????
            WxHandshakeResp wxHandshakeResp = (WxHandshakeResp) body;
            getEventEngine().post(wxHandshakeResp);
        } else if (command == TioCommand.WX_GROUP_CHAT_NTF) {// ????????????
            WxGroupChatNtf wxGroupChatNtf = (WxGroupChatNtf) body;
            getEventEngine().post(wxGroupChatNtf);
        } else if (command == TioCommand.WX_FRIEND_CHAT_NTF) {// ????????????
            WxFriendChatNtf wxFriendChatNtf = (WxFriendChatNtf) body;
            getEventEngine().post(wxFriendChatNtf);
        } else if (command == TioCommand.WX_USER_OPER_NTF) {// ??????????????????
            WxUserOperNtf wxUserOperNtf = (WxUserOperNtf) body;
            getEventEngine().post(wxUserOperNtf);
        } else if (command == TioCommand.WX_FRIEND_ERROR_NTF) {// ??????????????????(????????????)
            WxFriendErrorNtf errorNtf = (WxFriendErrorNtf) body;
            getEventEngine().post(errorNtf);
        } else if (command == TioCommand.WX_USER_SYS_NTF) {// ??????????????????
            WxUserSysNtf wxUserSysNtf = (WxUserSysNtf) body;
            getEventEngine().post(wxUserSysNtf);
        } else if (command == TioCommand.WX_GROUP_OPER_NTF) {// ???????????????
            WxGroupOperNtf wxGroupOperNtf = (WxGroupOperNtf) body;
//            Log.d("====??????==2222==","=="+command);
            EventBus.getDefault().post(new WxGroupBean());
            getEventEngine().post(wxGroupOperNtf);
        } else if (command == TioCommand.WX_FRIEND_MSG_RESP) {// ????????????????????????
            WxFriendMsgResp wxFriendMsgResp = (WxFriendMsgResp) body;
            getEventEngine().post(wxFriendMsgResp);
        } else if (command == TioCommand.WX_GROUP_MSG_RESP) {// ????????????????????????
            WxGroupMsgResp wxGroupMsgResp = (WxGroupMsgResp) body;
            getEventEngine().post(wxGroupMsgResp);
        } else if (command == TioCommand.WX_CHAT_ITEM_INFO_RESP) {// ????????????????????????
            WxChatItemInfoResp wxChatItemInfoResp = (WxChatItemInfoResp) body;
            getEventEngine().post(wxChatItemInfoResp);
        } else if (command == TioCommand.MSG_TIP) {// ????????????
            MsgTip msgTip = (MsgTip) body;
            getEventEngine().post(msgTip);
        } else if (command == TioCommand.WX_FOCUS_NTF) {// ?????????????????????
            WxFocusNtf wxFocusNtf = (WxFocusNtf) body;
            getEventEngine().post(wxFocusNtf);
        }
    }

    private EventEngine getEventEngine() {
        return TioIMClient.getInstance().getEventEngine();
    }

}
