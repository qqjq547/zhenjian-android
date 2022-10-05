package com.tiocloud.account.mvp.register;

import android.content.Context;
import android.text.TextUtils;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.account.feature.register.KeyboardUtil;
import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.AccountRegisterResp;
import com.watayouxiang.httpclient.model.response.PhoneRegisterBindEmailResp;
import com.watayouxiang.httpclient.model.response.PhoneRegisterResp;

public class RegisterPresenter extends RegisterContract.Presenter {

    private WtTimer wtTimer;

    public RegisterPresenter(RegisterContract.View view) {
        super(new RegisterModel(), view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (wtTimer != null) {
            wtTimer.stop();
        }
    }

    @Override
    public void phoneRegisterBindEmail(String code, String phone, String email, String pwd, boolean isAgreeProtocol) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            TioToast.showShort("手机验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            TioToast.showShort("邮箱不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            TioToast.showShort("邮箱账号密码不能为空");
            return;
        }
        if (!isAgreeProtocol) {
            TioToast.showShort("请先勾选同意用户服务协议和隐私政策");
            return;
        }
        getModel().reqRegisterBindEmail(code, phone, email, pwd, new TioCallback<PhoneRegisterBindEmailResp>() {
            @Override
            public void onTioSuccess(PhoneRegisterBindEmailResp phoneRegisterBindEmailResp) {
                // 存储登录名
                AccountSP.putLoginName(phone);
                // 回调
                getView().onRegisterBindEmailSuccess();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void phoneRegister(String loginName, String phone, String code, String friendinvitecode, String nick, String pwd, boolean isAgreeProtocol, QMUIRoundButton button) {
        if (TextUtils.isEmpty(loginName) ) {
            TioToast.showShort("账号不能为空");
            return;
        }

        if (loginName.startsWith("0")){
            TioToast.showShort("账号不能以0开头");
            return;
        }

        if (loginName.length() < 6){
            TioToast.showShort("账号太短了");
            return;
        }

        if (TextUtils.isEmpty(friendinvitecode)){
            TioToast.showShort("邀请码不能为空");
            return;
        }
//        if (TextUtils.isEmpty(phone)) {
//            TioToast.showShort("手机号不能为空");
//            return;
//        }

        if (TextUtils.isEmpty(nick)) {
            TioToast.showShort("昵称不能为空");
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            TioToast.showShort("密码不能为空");
            return;
        }

        if (!isAgreeProtocol) {
            TioToast.showShort("请先勾选同意用户服务协议和隐私政策");
            return;
        }
        getModel().postAccountRegister(loginName,phone, code, friendinvitecode, nick, pwd, new TioCallback<AccountRegisterResp>() {
            @Override
            public void onTioSuccess(AccountRegisterResp phoneRegisterResp) {
                // 存储登录名
                AccountSP.putLoginName(loginName);
                AccountSP.putKeyLoginPwd(pwd);

                // 回调
                getView().onPhoneRegisterSuccess();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
                KeyboardUtil.hideSoftInput(button);

            }
        });
    }



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
        getModel().reqSmsBeforeCheck("2", phone, new TioCallback<String>() {
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
        getModel().reqSendSms("2", phone, captchaVerification, new TioCallback<String>() {
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
}
