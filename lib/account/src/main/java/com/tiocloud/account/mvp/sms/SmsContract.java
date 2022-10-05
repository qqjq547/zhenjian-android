package com.tiocloud.account.mvp.sms;

import android.content.Context;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public interface SmsContract {

    abstract class Model extends BaseModel {
        public abstract void reqSendSms(String biztype, String phone, String captchaVerification, TioCallback<String> callback);

        public abstract void reqSmsBeforeCheck(String biztype, String phone, TioCallback<String> callback);
    }

    interface View extends BaseView {

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(View view) {
            super(new SmsModel(), view);
        }

        public abstract void sendSms(Context context, String phone, String biztype, OnSendSmsListener listener);

        public abstract void startCodeTimer(OnTimerListener listener);

        public abstract void getCurrUserInfo(TioCallback<UserCurrResp> callback);
    }

    interface OnSendSmsListener {
        void onSendSmsSuccess();
    }

    interface OnTimerListener {
        void OnTimerRunning(int second);

        void OnTimerStop();
    }

}
