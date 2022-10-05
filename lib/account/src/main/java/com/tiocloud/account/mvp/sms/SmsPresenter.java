package com.tiocloud.account.mvp.sms;

import android.content.Context;
import android.text.TextUtils;

import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * author : TaoWang
 * date : 2021-04-16
 * desc :
 */
public class SmsPresenter extends SmsContract.Presenter {

    public SmsPresenter(SmsContract.View view) {
        super(view);
    }

    @Override
    public void detachView() {
        mOnSendSmsListener = null;
        if (wtTimer != null) {
            wtTimer.stop();
        }
        super.detachView();
    }

    // ====================================================================================
    // 发送短信验证码
    // ====================================================================================

    private SmsContract.OnSendSmsListener mOnSendSmsListener;
    private String mBiztype;

    @Override
    public void sendSms(Context context, String phone, String biztype, SmsContract.OnSendSmsListener listener) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        this.mOnSendSmsListener = listener;
        this.mBiztype = biztype;
        smsBeforeCheck(phone, context);
    }

    // 验证手机号是否可用
    private void smsBeforeCheck(String phone, Context context) {
        getModel().reqSmsBeforeCheck(mBiztype, phone, new TioCallback<String>() {
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
        getModel().reqSendSms(mBiztype, phone, captchaVerification, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                if (mOnSendSmsListener != null) {
                    mOnSendSmsListener.onSendSmsSuccess();
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // ====================================================================================
    // 倒计时
    // ====================================================================================

    private WtTimer wtTimer;
    private final int INIT_TIME = 60;

    @Override
    public void startCodeTimer(SmsContract.OnTimerListener listener) {
        // 开始倒计时
        if (wtTimer == null) {
            wtTimer = new WtTimer();
        }
        wtTimer.start((count, timer) -> {
            if (count < INIT_TIME) {
                if (listener != null) {
                    listener.OnTimerRunning(INIT_TIME - count);
                }
            } else {
                timer.stop();
                if (listener != null) {
                    listener.OnTimerStop();
                }
            }
        }, true, 0, 1000);
    }

    @Override
    public void getCurrUserInfo(TioCallback<UserCurrResp> callback) {
        new UserCurrReq().setCancelTag(this).get(callback);
    }
}
