package com.tiocloud.account.mvp.t_login;

import android.app.Activity;
import android.util.Log;

import com.tiocloud.account.mvp.login.LoginPresenter;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.ConfigResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.httpclient.preferences.CookieUtils;
import com.watayouxiang.social.entities.ThirdInfoEntity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 12/28/20
 *     desc   :
 * </pre>
 */
public class TLoginPresenter extends TLoginContract.Presenter {

    public TLoginPresenter(TLoginContract.View view) {
        super(view);
    }

    @Override
    public void startTLogin(ThirdInfoEntity info, Activity activity) {
        String cookie = CookieUtils.getCookie();
        Log.d("===cookie==","=="+cookie);
        if (cookie != null) {
            startTLoginInternal(info, cookie, activity);
        } else {
            getToken(info, activity);
        }
    }

    // ====================================================================================
    // 获取token
    // ====================================================================================

    private void getToken(ThirdInfoEntity info, Activity activity) {
        getModel().reqConfig(new TioCallback<ConfigResp>() {
            @Override
            public void onTioSuccess(ConfigResp resp) {
                getTokenInternal(info, activity);
            }

            @Override
            public void onTioError(String msg) {
                boolean tioError = isTioError();
                if (tioError) {
                    getTokenInternal(info, activity);
                } else {
                    TioToast.showShort(msg);
                }
            }
        });
    }

    private void getTokenInternal(ThirdInfoEntity info, Activity activity) {
        String token = CookieUtils.getCookie();
        if (token != null) {
            startTLoginInternal(info, token, activity);
        } else {
            TioToast.showShort("获取token失败");
        }
    }

    // ====================================================================================
    // 三方登录
    // ====================================================================================

    private void startTLoginInternal(ThirdInfoEntity info, String token, Activity activity) {
        if (info == null) {
            TioToast.showShort("ThirdInfoEntity is null");
            return;
        }

        // 显示登录弹窗
        SingletonProgressDialog.show_unCancel(activity, "三方登录中…");

        if (ThirdInfoEntity.PLATFORM_QQ.equals(info.getPlatform())) {
            getModel().reqLoginQQ(info, token, mTLoginProxy);
        } else if (ThirdInfoEntity.PLATFORM_WX.equals(info.getPlatform())) {
            getModel().reqLoginWX(info, token, mTLoginProxy);
        } else {
            // 隐藏登录弹窗
            SingletonProgressDialog.dismiss();
        }
    }

    private final BaseModel.DataProxy<UserCurrResp> mTLoginProxy = new BaseModel.DataProxy<UserCurrResp>() {
        @Override
        public void onSuccess(UserCurrResp resp) {
            super.onSuccess(resp);
            // 隐藏登录弹窗
            SingletonProgressDialog.dismiss();
            // 登录
            LoginPresenter.login(resp);
        }

        @Override
        public void onFailure(String msg) {
            super.onFailure(msg);
            TioToast.showShort(msg);
            // 隐藏登录弹窗
            SingletonProgressDialog.dismiss();
        }
    };
}
