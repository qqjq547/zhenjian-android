package com.tiocloud.newpay.feature.paypwd_find;

import android.app.Activity;

import com.tiocloud.newpay.mvp.smscode.SmsCodeContract;
import com.tiocloud.newpay.mvp.smscode.SmsCodePresenter;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

class FragmentOnePresenter extends FragmentOneContract.Presenter {
    private SmsCodePresenter smsCodePresenter;

    public FragmentOnePresenter(FragmentOneContract.View view) {
        super(null, view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (smsCodePresenter != null) {
            smsCodePresenter.detachView();
        }
    }

    @Override
    public void init() {
        new UserCurrReq().setCancelTag(this).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                getView().onUserCurrResp(resp);
            }
        });
    }

    @Override
    public void reqSendSms(String phone) {
        if (smsCodePresenter == null) {
            smsCodePresenter = new SmsCodePresenter(new SmsCodeContract.View() {
                @Override
                public void onCodeTimerRunning(int second) {
                    getView().onCodeTimerRunning(second);
                }

                @Override
                public void onCodeTimerStop() {
                    getView().onCodeTimerStop();
                }

                @Override
                public Activity getActivity() {
                    return getView().getActivity();
                }
            });
        }
        smsCodePresenter.reqSmsCode(phone, "10");
    }
}
