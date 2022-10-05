package com.tiocloud.newpay.feature.paypwd_verify;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.CheckPayPwdReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   :
 * </pre>
 */
public class VerifyPayPwdViewModel extends ViewModel {

    public void reqCheckPayPwd(String pwd, VerifyPayPwdFragment fragment) {
        new UserCurrReq().setCancelTag(fragment).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {

                new CheckPayPwdReq(pwd, resp.loginname).setCancelTag(fragment).post(new TioCallback<Object>() {
                    @Override
                    public void onTioSuccess(Object o) {
                        fragment.notifyVerifyPayPwdSuccess(pwd);
                        Log.d("===验证1111===","=onTioSuccess=="+pwd);
                    }

                    @Override
                    public void onTioError(String msg) {
                        Log.d("===验证1111===","=onTioError=="+msg);

                        fragment.notifyVerifyPayPwdError(msg);
                    }
                });

            }
        });
    }

}
