package com.tiocloud.account.mvp.login;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.tiocloud.account.TioAccount;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.verification.widget.TioBlockPuzzleDialog;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.androidutils.utils.ViewUtil;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public class LoginPresenter extends LoginContract.Presenter {

    public LoginPresenter(LoginContract.View view) {
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
    // 登录
    // ====================================================================================

    @Override
    public void pwdLogin(TextView accountView, TextView pwdView, Activity activity) {
        String account = ViewUtil.getText(accountView);
        String pwd = ViewUtil.getText(pwdView);
        pwdLogin(account, pwd, activity);
    }

    @Override
    public void pwdLogin(String account, String pwd, Activity activity) {
        // 验证参数
        if (TextUtils.isEmpty(account)) {
            TioToast.showShort("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            TioToast.showShort("密码不能为空");
            return;
        }

        // 开启登录流程
        SingletonProgressDialog.show_unCancel(activity, "登录中…");

        getModel().reqPwdLogin(account, pwd, new BaseModel.DataProxy<UserCurrResp>() {
            @Override
            public void onSuccess(UserCurrResp resp) {
                AccountSP.putKeyLoginPwd(pwd);
                // 登录成功
                login(resp, account);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort(msg+"");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SingletonProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void codeLogin(String account, String code, Activity activity) {
        // 验证参数
        if (TextUtils.isEmpty(account)) {
            TioToast.showShort("账号不能为空");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            TioToast.showShort("验证码不能为空");
            return;
        }

        // 开启登录流程
        SingletonProgressDialog.show_unCancel(activity, "登录中…");

        getModel().reqCodeLogin(account, code, new BaseModel.DataProxy<UserCurrResp>() {
            @Override
            public void onSuccess(UserCurrResp resp) {
                // 登录成功
                login(resp, account);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort(msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                SingletonProgressDialog.dismiss();
            }
        });
    }

    // ====================================================================================
    // 发送短信验证码
    // ====================================================================================

    private LoginContract.OnSendSmsListener mOnSendSmsListener;

    @Override
    public void sendSms(Context context, String phone, LoginContract.OnSendSmsListener listener) {
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号不能为空");
            return;
        }
        this.mOnSendSmsListener = listener;
        smsBeforeCheck(phone, context);
    }

    // 验证手机号是否可用
    private void smsBeforeCheck(String phone, Context context) {
        getModel().reqSmsBeforeCheck("3", phone, new TioCallback<String>() {
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
        getModel().reqSendSms("3", phone, captchaVerification, new TioCallback<String>() {
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
    public void startCodeTimer(LoginContract.OnTimerListener listener) {
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

    // ====================================================================================
    // 登录
    // ====================================================================================

    public static void login(UserCurrResp currInfo) {
        String loginName = AccountSP.getLoginName();
        login(currInfo, loginName);
    }

    public static void login(UserCurrResp currInfo, String account) {
        // 存储登录名
        AccountSP.putLoginName(account);
        // 存储用户信息
        CurrUserTableCrud.insert(currInfo);
        // 打开主页
        TioAccount.getBridge().startMainActivity(Utils.getApp());
        // 关闭其他页面
        ActivityUtils.finishAllActivities();
    }
}
