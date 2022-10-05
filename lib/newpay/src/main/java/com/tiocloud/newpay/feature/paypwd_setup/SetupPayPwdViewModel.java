package com.tiocloud.newpay.feature.paypwd_setup;

import androidx.lifecycle.ViewModel;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.httpclient.model.request.SetPayPwdReq;
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
public class SetupPayPwdViewModel extends ViewModel {

    public void reqSetPayPwd(String pwd, SetupPayPwdFragmentTwo fragmentTwo) {

        new UserCurrReq().setCancelTag(fragmentTwo).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {

                new SetPayPwdReq(pwd, resp.loginname).setCancelTag(fragmentTwo).post(new TioSuccessCallback<Object>() {
                    @Override
                    public void onTioSuccess(Object o) {
                        fragmentTwo.showOkDialog();
                    }
                });

            }
        });

    }
}
