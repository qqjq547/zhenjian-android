package com.watayouxiang.imclient.app;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.Utils;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.client.IMConfig;
import com.watayouxiang.imclient.client.TaoIMClient;
import com.watayouxiang.imclient.engine.EventEngine;
import com.watayouxiang.imclient.engine.JsonEngine;
import com.watayouxiang.imclient.engine.TioEventEngine;
import com.watayouxiang.imclient.engine.TioJsonEngine;
import com.watayouxiang.imclient.event.TioEventPoster;
import com.watayouxiang.imclient.http.HttpRequest;
import com.watayouxiang.imclient.model.ImServer;
import com.watayouxiang.imclient.model.body.MsgTip;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;
import com.watayouxiang.imclient.model.body.wx.WxHandshakeResp;
import com.watayouxiang.imclient.model.body.wx.WxSessionOperReq;
import com.watayouxiang.imclient.model.body.wx.WxUpdateTokenReq;
import com.watayouxiang.imclient.model.body.wx.WxUpdateTokenResp;
import com.watayouxiang.imclient.packet.PacketDecoder;
import com.watayouxiang.imclient.packet.PacketEncoder;
import com.watayouxiang.imclient.packet.PacketReceiver;
import com.watayouxiang.imclient.packet.TioCommand;
import com.watayouxiang.imclient.packet.TioHandshake;
import com.watayouxiang.imclient.packet.TioPacket;
import com.watayouxiang.imclient.packet.TioPacketBuilder;
import com.watayouxiang.imclient.packet.TioPacketDecoder;
import com.watayouxiang.imclient.packet.TioPacketDecoderOperation;
import com.watayouxiang.imclient.packet.TioPacketEncoder;
import com.watayouxiang.imclient.packet.TioPacketReceiver;
import com.watayouxiang.imclient.prefernces.IMPreferences;
import com.watayouxiang.imclient.utils.LoggerUtils;
import com.watayouxiang.imclient.utils.StringUtils;

import java.util.Locale;
import java.util.Map;

/**
 * author : TaoWang
 * date : 2020/3/23
 * desc :
 */
public abstract class AppIMClient extends TaoIMClient<TioPacket> implements TioPacketDecoderOperation, AppIMClientOperation {

    private TioPacketDecoder mPacketDecoder = new TioPacketDecoder();
    private TioHandshake mHandshake;
    private boolean mIsHandshake;
    private AppIMKickOutListener mKickOutListener;
    private EventEngine mEventEngine;
    private JsonEngine mJsonEngine;
    // "App?????????????????????????????????"??????????????????
    private boolean mAutoDisconnectOnAppBackground = true;
    // "??????????????????" ??????
    private WxSessionOperReq mWxSessionOperReq = null;

    public AppIMClient() {
        // ??????IM??????????????????????????????
        registerCallback(new TioEventPoster());
        // ???????????? App???????????????
        AppUtils.registerAppStatusChangedListener(mOnAppStatusChangedListener);
        // ???????????? ????????????
        NetworkUtils.registerNetworkStatusChangedListener(mNetworkStatusChangedListener);
    }

    // =======================================================================================
    // abstract
    // =======================================================================================

    @Override
    public TioPacket getHeartBeatPacket() {
        return TioPacketBuilder.getHeartbeatReq();
    }

    @Override
    public PacketEncoder<TioPacket> getPacketEncoder() {
        return new TioPacketEncoder();
    }

    @Override
    public PacketDecoder<TioPacket> getPacketDecoder() {
        return mPacketDecoder;
    }

    @Override
    public PacketReceiver getPacketReceiver() {
        return new TioPacketReceiver();
    }

    @Override
    public boolean sendPacket(TioPacket packet) {
        // ????????????????????????
        if (packet == null) {
            LoggerUtils.i("????????????????????????");
            return false;
        }
        // ????????????????????????
        if (!isConnected()) {
            LoggerUtils.i("????????????????????????");
            return false;
        }
        short command = packet.getCommand();
        Object body = packet.getBody();
        // ????????????????????????????????????????????????
        if (!isHandshake() && command != TioCommand.WX_HANDSHAKE_REQ) {
            LoggerUtils.i("????????????????????????????????????????????????");
            return false;
        }
        // ????????????
        if (command == TioCommand.WX_SESSION_OPER_REQ) {// ???????????????????????????????????????
            WxSessionOperReq wxSessionOperReq = (WxSessionOperReq) body;
            if ("1".equals(wxSessionOperReq.oper)) {
                mWxSessionOperReq = wxSessionOperReq;
            } else if ("2".equals(wxSessionOperReq.oper)) {
                mWxSessionOperReq = null;
            }
        }
        return super.sendPacket(packet);
    }

    // ====================================================================================
    // callback
    // ====================================================================================

    @Override
    public void onRelease() {
        super.onRelease();
        mPacketDecoder = null;
        mHandshake = null;
        mKickOutListener = null;
        mEventEngine = null;
        mJsonEngine = null;
        // ???????????? App???????????????
        AppUtils.unregisterAppStatusChangedListener(mOnAppStatusChangedListener);
        // ???????????? ????????????
        NetworkUtils.unregisterNetworkStatusChangedListener(mNetworkStatusChangedListener);
    }

    @Override
    public void onConnected() {
        if (mHandshake != null) {
            sendPacket(mHandshake.getPacket());
        }
        // ????????????
        super.onConnected();
    }

    @Override
    public void onSendPacketEnd(TioPacket packet) {
        short command = packet.getCommand();
        Object body = packet.getBody();
        if (command == TioCommand.WX_UPDATE_TOKEN_REQ) {
            // ??????token??????
            WxUpdateTokenReq wxUpdateTokenReq = (WxUpdateTokenReq) body;
            LoggerUtils.d("??????token??????: " + wxUpdateTokenReq.toString());
        }
        // ????????????
        super.onSendPacketEnd(packet);
    }

    @Override
    public void onReceivePacketEnd(TioPacket packet) {
        short command = packet.getCommand();
        Object body = packet.getBody();
        if (command == TioCommand.WX_HANDSHAKE_RESP) {
            WxHandshakeResp handshakeResp = (WxHandshakeResp) body;
            // ????????????
            mIsHandshake = true;

            // ??????ip
            IMPreferences.saveIp(handshakeResp.ip);

            // ?????????????????? "??????????????????" ??????
            if (mWxSessionOperReq != null) {
                sendPacket(TioPacketBuilder.getWxSessionOperReq(mWxSessionOperReq));
            }

        } else if (command == TioCommand.WX_FRIEND_ERROR_NTF) {
            // ????????????
            WxFriendErrorNtf wxFriendErrorNtf = (WxFriendErrorNtf) body;
            WxFriendErrorNtf.Code code = wxFriendErrorNtf.getCode();
            if (code == WxFriendErrorNtf.Code.KICK_OUT) {
                if (mKickOutListener != null) {
                    mKickOutListener.onKickOut(wxFriendErrorNtf.msg);
                }
            }
        } else if (command == TioCommand.MSG_TIP) {
            // ????????????
            MsgTip msgTip = (MsgTip) body;
            if (msgTip.code == 1) {
                // ?????????
                if (mKickOutListener != null) {
                    mKickOutListener.onKickOut(msgTip.msg);
                }
            }
        } else if (command == TioCommand.WX_UPDATE_TOKEN_RESP) {
            // ??????token??????
            WxUpdateTokenResp wxUpdateTokenResp = (WxUpdateTokenResp) body;
            LoggerUtils.d("??????token??????: " + wxUpdateTokenResp.toString());
        }
        // ????????????
        super.onReceivePacketEnd(packet);
    }

    @Override
    public void onError(Exception e) {
        mIsHandshake = false;
        // ????????????
        super.onError(e);
    }

    @Override
    public void onDisconnected() {
        mIsHandshake = false;
        // ????????????
        super.onDisconnected();
    }

    // =======================================================================================
    // operation
    // =======================================================================================

    @Override
    public void setCommandBodyMap(Map<Short, Class> bodyMap) {
        mPacketDecoder.setCommandBodyMap(bodyMap);
    }

    @Override
    public ImServer.Address requestAddress() {
        return new HttpRequest().getImServerAddress(null);
    }

    @Override
    public void setHandshake(TioHandshake handshake) {
        LoggerUtils.d(StringUtils.valueOf(handshake, "TioHandshake null"));
        mHandshake = handshake;
    }

    @Override
    public TioHandshake getHandshake() {
        return mHandshake;
    }

    @Override
    public boolean isHandshake() {
        return mIsHandshake;
    }

    @Override
    public void setKickOutListener(AppIMKickOutListener listener) {
        mKickOutListener = listener;
    }

    @Override
    public EventEngine getEventEngine() {
        if (mEventEngine == null) {
            mEventEngine = new TioEventEngine();
        }
        return mEventEngine;
    }

    @Override
    public void setEventEngine(EventEngine eventEngine) {
        mEventEngine = eventEngine;
    }

    @Override
    public JsonEngine getJsonEngine() {
        if (mJsonEngine == null) {
            mJsonEngine = new TioJsonEngine();
        }
        return mJsonEngine;
    }

    @Override
    public void setJsonEngine(JsonEngine jsonEngine) {
        mJsonEngine = jsonEngine;
    }

    @Override
    public void updateToken(@NonNull String newToken) {
        String oldToken = null;
        if (mHandshake != null) {
            oldToken = mHandshake.getToken();
            // ??????????????????token
            mHandshake.setToken(newToken);
        }
        if (oldToken != null) {
            // ??????????????????token
            WxUpdateTokenReq wxUpdateTokenReq = new WxUpdateTokenReq(newToken, oldToken);
            TioPacket packet = TioPacketBuilder.getPacket(wxUpdateTokenReq, TioCommand.WX_UPDATE_TOKEN_REQ);
            TioIMClient.getInstance().sendPacket(packet);
        }
    }

    @Override
    public void setAutoDisconnectOnAppBackground(boolean onOff) {
        LoggerUtils.i(String.format(Locale.getDefault(), "%s [App?????????????????????????????????]", onOff ? "??????" : "??????"));
        mAutoDisconnectOnAppBackground = onOff;
    }

    private final NetworkUtils.OnNetworkStatusChangedListener mNetworkStatusChangedListener =
            new NetworkUtils.OnNetworkStatusChangedListener() {
                @Override
                public void onDisconnected() {
                    LoggerUtils.i("OnNetworkStatusChangedListener # onDisconnected");
                    onNetworkStatusChangedListener_onDisconnected();
                }

                @Override
                public void onConnected(NetworkUtils.NetworkType networkType) {
                    LoggerUtils.i(String.format(Locale.getDefault(), "OnNetworkStatusChangedListener # onConnected (%s)", networkType));
                    onNetworkStatusChangedListener_onConnected(networkType);
                    checkConnect();
                }
            };

    protected void onNetworkStatusChangedListener_onDisconnected() {

    }

    protected void onNetworkStatusChangedListener_onConnected(NetworkUtils.NetworkType networkType) {

    }

    private final Utils.OnAppStatusChangedListener mOnAppStatusChangedListener =
            new Utils.OnAppStatusChangedListener() {
                @Override
                public void onForeground(Activity activity) {
                    LoggerUtils.i(String.format(Locale.getDefault(), "OnAppStatusChangedListener # onForeground (%s)", activity));
                    onAppStatusChangedListener_onForeground(activity);
                    checkConnect();
                }

                @Override
                public void onBackground(Activity activity) {
                    LoggerUtils.i(String.format(Locale.getDefault(), "OnAppStatusChangedListener # onBackground (%s)", activity));
                    onAppStatusChangedListener_onBackground(activity);
                    if (mAutoDisconnectOnAppBackground) {
                        disconnect();
                    }
                }
            };

    protected void onAppStatusChangedListener_onBackground(Activity activity) {

    }

    protected void onAppStatusChangedListener_onForeground(Activity activity) {

    }

    private void checkConnect() {
        IMConfig config = getConfig();
        if (config != null) {
            connect();
        }
    }
}
