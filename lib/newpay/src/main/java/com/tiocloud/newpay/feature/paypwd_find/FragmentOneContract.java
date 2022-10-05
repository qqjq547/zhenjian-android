package com.tiocloud.newpay.feature.paypwd_find;

import android.app.Activity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

interface FragmentOneContract {
    interface View extends BaseView {
        void onUserCurrResp(UserCurrResp resp);

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
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void reqSendSms(String phone);
    }
}
