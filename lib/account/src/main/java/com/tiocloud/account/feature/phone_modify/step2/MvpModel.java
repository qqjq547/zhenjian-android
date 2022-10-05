package com.tiocloud.account.feature.phone_modify.step2;

import android.text.TextUtils;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.BindNewPhoneReq;
import com.watayouxiang.httpclient.model.request.SmsBeforeCheckReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

class MvpModel extends MvpContract.Model {
    public MvpModel() {
        super(false);
    }

    @Override
    public void reqSmsBeforeCheck(String biztype, String phone, TioCallback<String> callback) {
        new SmsBeforeCheckReq(biztype, phone).setCancelTag(this).post(callback);
    }

    @Override
    public void reqSendSms(String biztype, String phone, String captchaVerification, TioCallback<String> callback) {
        new SmsSendReq(biztype, phone, captchaVerification).setCancelTag(this).post(callback);
    }

    @Override
    public void reqBindNewPhone(String code, String phone, String pwd, TioCallback<Object> callback) {
        new UserCurrReq().setCancelTag(this).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                String email = resp.email;
                if (TextUtils.isEmpty(email)) {
                    email = null;
                }
                new BindNewPhoneReq(code, phone, email, pwd).setCancelTag(MvpModel.this).post(callback);
            }
        });
    }

}
