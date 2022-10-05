package com.tiocloud.account.feature.phone_modify.step1;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.callback.TioCallback;

interface MvpContract {
    interface View extends BaseView {
        void resetUI();

        void onCodeTimerRunning(int second);

        void onCodeTimerStop();

        void onCheckCodeOk();

        void onPhoneResp(String phone);
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }

        public abstract void reqSmsBeforeCheck(String biztype, String phone, TioCallback<String> callback);

        public abstract void reqSendSms(String biztype, String phone, String captchaVerification, TioCallback<String> callback);

        public abstract void reqSmsCheck(String biztype, String mobile, String code, TioCallback<String> callback);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void reqSendSms(Context context, String phone);

        public abstract void checkCode(String phone, String code);
    }
}
