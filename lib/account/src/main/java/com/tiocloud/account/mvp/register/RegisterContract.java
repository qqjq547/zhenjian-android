package com.tiocloud.account.mvp.register;

import android.content.Context;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.AccountRegisterResp;
import com.watayouxiang.httpclient.model.response.PhoneRegisterBindEmailResp;
import com.watayouxiang.httpclient.model.response.PhoneRegisterResp;

public interface RegisterContract {
    interface View extends BaseView {
        void onPhoneRegisterSuccess();

        void onSendSmsSuccess();

        void onCodeTimerRunning(int second);

        void onCodeTimerStop();

        void onRegisterBindEmailSuccess();
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }

        public abstract void postPhoneRegister(String loginName,String phone, String code,String friendinvitecode, String nick, String pwd, TioCallback<PhoneRegisterResp> callback);

        public abstract void postAccountRegister(String loginName,String phone, String code,String friendinvitecode, String nick, String pwd, TioCallback<AccountRegisterResp> callback);

        public abstract void reqSmsBeforeCheck(String biztype, String phone, TioCallback<String> callback);

        public abstract void reqSendSms(String biztype, String phone, String captchaVerification, TioCallback<String> callback);

        public abstract void reqRegisterBindEmail(String code, String phone, String email, String pwd, TioCallback<PhoneRegisterBindEmailResp> callback);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void phoneRegisterBindEmail(String code, String phone, String email, String pwd, boolean isAgreeProtocol);

        public abstract void phoneRegister(String loginName, String phone, String code, String friendinvitecode, String nick, String pwd, boolean isAgreeProtocol, QMUIRoundButton button);

        public abstract void reqSendSms(Context context, String phone);

        public abstract void startCodeTimer(int second);
    }
}
