package com.tiocloud.account.feature.phone_modify.step1;

import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.SmsBeforeCheckReq;
import com.watayouxiang.httpclient.model.request.SmsCheckReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

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
    public void reqSmsCheck(String biztype, String mobile, String code, TioCallback<String> callback) {
        new SmsCheckReq(biztype, mobile, code).setCancelTag(this).post(callback);
    }
}
