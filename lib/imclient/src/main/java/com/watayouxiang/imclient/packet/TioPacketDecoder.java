package com.watayouxiang.imclient.packet;

import android.util.SparseArray;

import com.watayouxiang.imclient.model.body.GroupChatNtf;
import com.watayouxiang.imclient.model.body.HandshakeResp;
import com.watayouxiang.imclient.model.body.JoinGroupNtf;
import com.watayouxiang.imclient.model.body.JoinGroupResp;
import com.watayouxiang.imclient.model.body.LeaveGroupNtf;
import com.watayouxiang.imclient.model.body.MsgTip;
import com.watayouxiang.imclient.model.body.webrtc.WxCall02Ntf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall02_2CancelNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall03ReplyReqNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall04ReplyNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall06OfferSdpNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall08AnswerSdpNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall10OfferIceNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall12AnswerIceNtf;
import com.watayouxiang.imclient.model.body.webrtc.WxCall14EndNtf;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.imclient.model.body.wx.WxFocusNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendMsgResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxGroupMsgResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupOperNtf;
import com.watayouxiang.imclient.model.body.wx.WxHandshakeResp;
import com.watayouxiang.imclient.model.body.wx.WxUpdateTokenResp;
import com.watayouxiang.imclient.model.body.wx.WxUserOperNtf;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import java.util.Map;
import java.util.Set;

/**
 * author : TaoWang
 * date : 2020/3/25
 * desc :
 */
public class TioPacketDecoder extends TioPacketDecoderAbs implements TioPacketDecoderOperation {
    private SparseArray<Class> mBodyMap = new SparseArray<>();

    public TioPacketDecoder() {
        // ????????????
        mBodyMap.put(TioCommand.HANDSHAKE_RESP, HandshakeResp.class);
        // ??????????????????
        mBodyMap.put(TioCommand.JOIN_GROUP_RESP, JoinGroupResp.class);
        // ????????????
        mBodyMap.put(TioCommand.GROUP_CHAT_NTY, GroupChatNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.JOIN_GROUP_NTY, JoinGroupNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.LEAVE_GROUP_NTY, LeaveGroupNtf.class);
        // ????????????
        mBodyMap.put(TioCommand.MSG_TIP, MsgTip.class);

        // ------- WX?????? -------
        // ????????????
        mBodyMap.put(TioCommand.WX_HANDSHAKE_RESP, WxHandshakeResp.class);
        // ????????????
        mBodyMap.put(TioCommand.WX_GROUP_CHAT_NTF, WxGroupChatNtf.class);
        // ????????????
        mBodyMap.put(TioCommand.WX_FRIEND_CHAT_NTF, WxFriendChatNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_USER_OPER_NTF, WxUserOperNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_FRIEND_ERROR_NTF, WxFriendErrorNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_USER_SYS_NTF, WxUserSysNtf.class);
        // ???????????????
        mBodyMap.put(TioCommand.WX_GROUP_OPER_NTF, WxGroupOperNtf.class);
        // ????????????????????????
        mBodyMap.put(TioCommand.WX_FRIEND_MSG_RESP, WxFriendMsgResp.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_GROUP_MSG_RESP, WxGroupMsgResp.class);
        // ????????????????????????
        mBodyMap.put(TioCommand.WX_CHAT_ITEM_INFO_RESP, WxChatItemInfoResp.class);
        // ??????token??????
        mBodyMap.put(TioCommand.WX_UPDATE_TOKEN_RESP, WxUpdateTokenResp.class);
        // ?????????????????????
        mBodyMap.put(TioCommand.WX_FOCUS_NTF, WxFocusNtf.class);

        // ------- WebRTC?????? -------
        // ????????????
        mBodyMap.put(TioCommand.WX_CALL_02_NTF, WxCall02Ntf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_CALL_022_CANCEL_NTF, WxCall02_2CancelNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_CALL_04_REPLY_NTF, WxCall04ReplyNtf.class);
        // offer sdp ??????
        mBodyMap.put(TioCommand.WX_CALL_06_OFFER_SDP_NTF, WxCall06OfferSdpNtf.class);
        // answer sdp ??????
        mBodyMap.put(TioCommand.WX_CALL_08_ANSWER_SDP_NTF, WxCall08AnswerSdpNtf.class);
        // offer ICE ??????
        mBodyMap.put(TioCommand.WX_CALL_10_OFFER_ICE_NTF, WxCall10OfferIceNtf.class);
        // answer ICE ??????
        mBodyMap.put(TioCommand.WX_CALL_12_ANSWER_ICE_NTF, WxCall12AnswerIceNtf.class);
        // ??????????????????
        mBodyMap.put(TioCommand.WX_CALL_14_END_NTF, WxCall14EndNtf.class);
        // ????????????
        mBodyMap.put(TioCommand.WX_CALL_03_REPLY_REQ_NTF, WxCall03ReplyReqNtf.class);
    }

    @Override
    public void setCommandBodyMap(Map<Short, Class> handlerMap) {
        mBodyMap.clear();
        if (handlerMap == null) return;

        Set<Map.Entry<Short, Class>> entrySet = handlerMap.entrySet();
        for (Map.Entry<Short, Class> entry : entrySet) {
            Short key = entry.getKey();
            Class value = entry.getValue();
            mBodyMap.put(key, value);
        }
    }

    @Override
    public Class getClazz(short command) {
        return mBodyMap.get(command);
    }
}
