package com.tiocloud.account.mvp.t_bind_phone;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.utils.SpanUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.TBindPhoneResp;

public class TBindPhonePresenter extends TBindPhoneContract.Presenter {

    private WtTimer wtTimer;

    public TBindPhonePresenter(TBindPhoneContract.View view) {
        super(new TBindPhoneModel(), view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (wtTimer != null) {
            wtTimer.stop();
        }
    }

    @Override
    public void bindPhone(String code, String phone) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            TioToast.showShort("验证码不能为空");
            return;
        }
        getModel().reqTBindPhone(code, phone, new TioCallback<TBindPhoneResp>() {
            @Override
            public void onTioSuccess(TBindPhoneResp bindPhoneResp) {
                getView().onTBindPhoneSuccess();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // ====================================================================================
    // 发送短信验证码
    // ====================================================================================

    @Override
    public void reqSendSms(Activity context, String phone) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        smsBeforeCheck(phone, context);
    }

    // 验证手机号是否可用
    private void smsBeforeCheck(String phone, Activity context) {
        getModel().reqSmsBeforeCheck("8", phone, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String resp) {
                // 三方绑定返回：1：已注册的账号；2：未注册的账号
                if ("1".equals(resp)) {
                    // 显示三方绑定提示弹窗
                    showBindTipDialog(context, phone);
                } else if ("2".equals(resp)) {
                    // 显示滑块验证弹窗
                    showBlockPuzzleDialog(context, phone);
                } else {
                    TioToast.showShort("三方绑定返回：" + resp);
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // 显示三方绑定提示弹窗
    private void showBindTipDialog(Activity context, String phone) {
        SpannableStringBuilder title = SpanUtils
                .getBuilder("当前手机号已注册，如绑定至该手机则当前三方账号将清空，")
                .append("是否绑定该手机?").setBold()
                .create();
        new EasyOperDialog.Builder(title)
                .setPositiveBtnTxt("绑定该手机")
                .setNegativeBtnTxt("换其他手机")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                        // 显示滑块验证弹窗
                        showBlockPuzzleDialog(context, phone);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(context);
    }

    // 显示滑块验证弹窗
    private void showBlockPuzzleDialog(Context context, String phone) {
        TioBlockPuzzleDialog blockPuzzleDialog = new TioBlockPuzzleDialog(context);
        blockPuzzleDialog.setOnResultsListener(result -> realSendSms(result, phone));
        blockPuzzleDialog.show();
    }

    // 发送短信验证码
    private void realSendSms(String captchaVerification, String phone) {
        getModel().reqSendSms("8", phone, captchaVerification, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                getView().onSendSmsSuccess();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
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

    // ====================================================================================
    // 返回确认弹窗
    // ====================================================================================

    @Override
    public void showBackConfirmDialog(Activity activity) {
        new EasyOperDialog.Builder("确定要退出吗？")
                .setPositiveBtnTxt("退出")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        LogoutPresenter.kickOut();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(activity);
    }
}
