package com.tiocloud.account.feature.phone_modify.step1;

import android.content.Context;
import android.text.TextUtils;

import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

class MvpPresenter extends MvpContract.Presenter {
    public MvpPresenter(MvpContract.View view) {
        super(new MvpModel(), view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (wtTimer != null) {
            wtTimer.stop();
        }
    }

    @Override
    public void init() {
        getView().resetUI();

        new UserCurrReq().setCancelTag(this).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                String phone = resp.phone;
                getView().onPhoneResp(phone);
            }
        });
    }

    // ====================================================================================
    // 发送验证码
    // ====================================================================================

    private WtTimer wtTimer;

    @Override
    public void reqSendSms(Context context, String phone) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        smsBeforeCheck(phone, context);
    }

    // 验证手机号是否可用
    private void smsBeforeCheck(String phone, Context context) {
        getModel().reqSmsBeforeCheck("5", phone, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String smsBeforeCheckResp) {
                showBlockPuzzleDialog(context, phone);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // 显示滑块验证弹窗
    private void showBlockPuzzleDialog(Context context, String phone) {
        TioBlockPuzzleDialog blockPuzzleDialog = new TioBlockPuzzleDialog(context);
        blockPuzzleDialog.setOnResultsListener(result -> realSendSms(result, phone));
        blockPuzzleDialog.show();
    }

    // 发送短信验证码
    private void realSendSms(String captchaVerification, String phone) {
        getModel().reqSendSms("5", phone, captchaVerification, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                startCodeTimer(60);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // 开始倒计时
    private void startCodeTimer(int initTime) {
        if (wtTimer == null) {
            wtTimer = new WtTimer();
        }
        wtTimer.start((count, timer) -> {
            if (count < initTime) {
                getView().onCodeTimerRunning(initTime - count);
            } else {
                timer.stop();
                getView().onCodeTimerStop();
            }
        }, true, 0, 1000);
    }

    // ====================================================================================
    // 验证code
    // ====================================================================================

    @Override
    public void checkCode(String phone, String code) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            TioToast.showShort("验证码不能为空");
            return;
        }
        getModel().reqSmsCheck("5", phone, code, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                getView().onCheckCodeOk();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }
}
