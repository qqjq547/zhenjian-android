package com.tiocloud.newpay.mvp.smscode;

import android.app.Activity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;

public interface SmsCodeContract {
    interface View extends BaseView {
        void onCodeTimerRunning(int second);

        void onCodeTimerStop();

        Activity getActivity();
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(View view, boolean registerEvent) {
            super(null, view, registerEvent);
        }

        public abstract void reqSmsCode(String phone, String biztype);

        public abstract void startCodeTimer(int initTime);
    }
}
