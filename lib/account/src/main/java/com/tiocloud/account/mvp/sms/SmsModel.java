package com.tiocloud.account.mvp.sms;

import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.SmsBeforeCheckReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

/**
 * author : TaoWang
 * date : 2021-04-16
 * desc :
 */
public class SmsModel extends SmsContract.Model {

    @Override
    public void reqSendSms(String biztype, String phone, String captchaVerification, TioCallback<String> callback) {
        new SmsSendReq(biztype, phone, captchaVerification).setCancelTag(this).post(callback);
    }

    @Override
    public void reqSmsBeforeCheck(String biztype, String phone, TioCallback<String> callback) {
        new SmsBeforeCheckReq(biztype, phone).setCancelTag(this).post(callback);
    }
}
