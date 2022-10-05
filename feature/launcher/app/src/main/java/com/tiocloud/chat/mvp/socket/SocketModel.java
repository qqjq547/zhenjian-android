package com.tiocloud.chat.mvp.socket;

import android.text.TextUtils;
import android.util.Log;

import com.lzy.okgo.cache.CacheMode;
import com.watayouxiang.httpclient.preferences.CookieUtils;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.ImServerReq;
import com.watayouxiang.httpclient.model.response.ImServerResp;
import com.watayouxiang.httpclient.utils.PreferencesUtil;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public class SocketModel extends SocketContract.Model {
    @Override
    public void requestImServer(final DataProxy<ImServerResp> proxy) {
        ImServerReq imServerReq = new ImServerReq();
        imServerReq.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        imServerReq.setCancelTag(this);
        imServerReq.get(new TioCallbackImpl<ImServerResp>() {
            @Override
            public void onTioSuccess(ImServerResp imServerResp) {
                TioLogger.d("req ImServer success: " + imServerResp.toString());
                proxy.onSuccess(imServerResp);
            }

            @Override
            public void onTioError(String msg) {
                TioLogger.d("req ImServer error: " + msg);
                if (msg != null) {
                    proxy.onFailure(msg);
                }
            }
        });
    }

    @Override
    public boolean isLogin() {
        return CurrUserTableCrud.curr_isLogin();
    }

    @Override
    public String getSocketToken() {
//        String getCookie=PreferencesUtil.getString("session_cookie_nameNew","");
//        Log.d("===getCookie===","=="+ CookieUtils.getCookie());
//        if(!TextUtils.isEmpty(getCookie)){
//            return getCookie;
//        }else {
//          PreferencesUtil.saveString("session_cookie_nameNew",CookieUtils.getCookie());
          return CookieUtils.getCookie();
//        }

    }
}
