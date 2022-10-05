package com.tiocloud.newpay.mvp.smscode;

import android.text.TextUtils;

import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

public class SmsCodePresenter extends SmsCodeContract.Presenter {
    private WtTimer wtTimer;
    private String biztype;

    public SmsCodePresenter(SmsCodeContract.View view) {
        super(view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (wtTimer != null) {
            wtTimer.stop();
        }
    }

    @Override
    public void reqSmsCode(String phone, String biztype) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        if (biztype == null) {
            TioToast.showShort("biztype 不能为空");
            return;
        }
        this.biztype = biztype;
        showBlockPuzzleDialog(phone);
    }

    private void showBlockPuzzleDialog(String phone) {
        TioBlockPuzzleDialog blockPuzzleDialog = new TioBlockPuzzleDialog(getView().getActivity());
        blockPuzzleDialog.setOnResultsListener(result -> realSendSms(result, phone));
        blockPuzzleDialog.show();
    }

    private void realSendSms(String captchaVerification, String phone) {
        new SmsSendReq(biztype, phone, captchaVerification).setCancelTag(this).post(new TioSuccessCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                startCodeTimer(60);
            }
        });
    }

    @Override
    public void startCodeTimer(int initTime) {
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
}
