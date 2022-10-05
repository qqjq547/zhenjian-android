package com.tiocloud.chat.mvp.socket;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.jpush.PushLauncher;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.listener.OnCookieListener;
import com.watayouxiang.httpclient.model.response.ImServerResp;
import com.watayouxiang.httpclient.preferences.CookieUtils;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.httpclient.utils.PreferencesUtil;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.client.IMConfig;
import com.watayouxiang.imclient.packet.TioCommand;
import com.watayouxiang.imclient.packet.TioHandshake;
import com.watayouxiang.imclient.prefernces.IMPreferences;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Cookie;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc : 长连接表现层
 */
public class SocketPresenter extends SocketContract.Presenter {

    public SocketPresenter(SocketContract.View view) {
        super(view);
        autoUpdateSocketToken();
    }

    @Override
    public boolean isConnected() {
        return TioIMClient.getInstance().isConnected();
    }

    @Override
    public void connectSocket(final ConnectCallback callback) {
        // 确保没有建立长连接
        if (isConnected()) {
            TioLogger.i("connectSocket --> already connect");
            callback.onConnectSocketResp(ConnectResp.AlreadyConnect);
            return;
        }

        // 确保登录
        if (!getModel().isLogin()) {
            TioLogger.i("connectSocket --> not login");
            callback.onConnectSocketResp(ConnectResp.UnLogin);
            return;
        }

        // 确保存在token
        final String token = getModel().getSocketToken();
        if (token == null) {
            TioLogger.i("connectSocket --> token null");
            callback.onConnectSocketResp(ConnectResp.TokenNull);
            return;
        }

        // 确保长连接地址存在
        getModel().requestImServer(new BaseModel.DataProxy<ImServerResp>() {
            @Override
            public void onSuccess(ImServerResp data) {
                if (realConnectSocket(data.ip, data.port, data.ssl == 1, token, data.timeout)) {
                    callback.onConnectSocketResp(ConnectResp.ConnectSuccess);
                } else {
                    callback.onConnectSocketResp(ConnectResp.AlreadyConnect);
                }
            }

            @Override
            public void onFailure(String msg) {
                TioLogger.i("connectSocket --> ImServer null");
                TioToast.showShort("获取 IM 地址失败：" + msg);
                callback.onConnectSocketResp(ConnectResp.ImServerNull);
            }
        });
    }

    @Override
    public void exitApp() {
        AppUtils.exitApp();
    }

    @Override
    public void finishAllActivity() {
        ActivityUtils.finishAllActivities();
    }

    private boolean realConnectSocket(String ip, int port, boolean openSsl, String token, long timeout) {
        // 确保没有建立长连接
        if (isConnected()) {
            TioLogger.i("connectSocket --> already connect");
            return false;
        }
        // 握手密钥
        String handShakeKey = IMPreferences.getHandShakeKey();
        if (handShakeKey == null) {
            handShakeKey = TioConfig.HAND_SHAKE_KEY;
        }
        if (handShakeKey == null) {
            TioLogger.i("connectSocket --> handShakeKey = null");
            return false;
        }
        // 超时时长
        if (timeout == 0) {
            timeout = HttpPreferences.getImHeartbeatTimeout();
            if (timeout == -1) {
                TioLogger.i("connectSocket --> imHeartbeatTimeout = " + timeout);
                return false;
            }
        }
        timeout = Math.max(timeout / 2, IMConfig.HEARTBEAT_INTERVAL_MIN);

        // 服务器地址配置
        TioIMClient.getInstance().setConfig(new IMConfig.Builder(ip, port)
                .setHeartBeatInterval(timeout)
                .setOpenSsl(openSsl)
                .build());
        // 握手配置
        TioIMClient.getInstance().setHandshake(new TioHandshake.Builder(
                token,
                handShakeKey,
                TioCommand.WX_HANDSHAKE_REQ)
                .setCid("official")
                .setJpushinfo(PushLauncher.getInstance().getRegistrationID())
                .build());
        // 启动
        TioIMClient.getInstance().connect();

        TioLogger.i("connectSocket --> connect success");
        return true;
    }
    private int newTioCookieNum=0;
    private void autoUpdateSocketToken() {
//        TioHttpClient.getInstance().setOnCookieListener(new OnCookieListener() {
//            @Override
//            public void onSaveTioCookiesFromResponse(@NonNull @NotNull List<Cookie> tioCookies) {
//                String getCookie= PreferencesUtil.getString("session_cookie_nameNew","");
//                for (int i=0;i<tioCookies.size();i++){
//                    if(newTioCookieNum==0){
//                        Log.d("===newTioCookie===","=="+tioCookies.get(0).value());
////                        TioIMClient.getInstance().updateToken(tioCookies.get(0).value());
////                        PreferencesUtil.saveString("session_cookie_nameNew",tioCookies.get(0).value());
//
//                        TioIMClient.getInstance().updateToken(getCookie);
//                    }
//                    newTioCookieNum++;
//                }
//            }
//        });
        TioHttpClient.getInstance().setOnCookieListener(tioCookies -> {
            // 获取新token
            String newTioCookie = CookieUtils.getCookie(tioCookies);
//        String newTioCookie=PreferencesUtil.getString("session_cookie_nameNew","");
//            Log.d("===newTioCookie==","=="+newTioCookie);
////            // 更新token
            if (newTioCookie != null) {
                TioIMClient.getInstance().updateToken(newTioCookie);
            }
//
        });
    }
}
