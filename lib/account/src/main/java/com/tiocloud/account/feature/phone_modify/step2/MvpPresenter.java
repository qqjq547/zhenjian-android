package com.tiocloud.account.feature.phone_modify.step2;

import android.content.Context;
import android.text.TextUtils;

import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;

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
        getModel().reqSmsBeforeCheck("7", phone, new TioCallback<String>() {
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
        getModel().reqSendSms("7", phone, captchaVerification, new TioCallback<String>() {
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
    // 绑定新手机
    // ====================================================================================

    @Override
    public void bindNewPhone(String phoneNew, String code, String pwdOld) {
        getModel().reqBindNewPhone(code, phoneNew, pwdOld, new TioSuccessCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                getView().onBindNewPhoneSuccess();
            }
        });
    }
}
