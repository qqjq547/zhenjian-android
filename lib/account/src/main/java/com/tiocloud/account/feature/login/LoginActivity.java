package com.tiocloud.account.feature.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.tiocloud.account.R;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.account.databinding.AccountLoginActivityBinding;
import com.tiocloud.account.feature.login_sms.SmsLoginActivity;
import com.tiocloud.account.feature.register.RegisterActivity;
import com.tiocloud.account.feature.retrieve_pwd.RetrievePwdActivity;
import com.tiocloud.account.mvp.login.LoginContract;
import com.tiocloud.account.mvp.login.LoginPresenter;
import com.tiocloud.account.widget.ThirdPartyLoginView;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/21
 *     desc   : 邮箱登录/手机号登录
 * </pre>
 */
public class LoginActivity extends BindingActivity<AccountLoginActivityBinding> implements LoginContract.View {

    public final ObservableField<String> txt_top_right = new ObservableField<>("");
    public final ObservableField<String> txt_account = new ObservableField<>("");
    public final ObservableField<String> txt_pwd = new ObservableField<>("");
    private LoginPresenter presenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, LoginActivity.class);
        if (!(context instanceof Activity)) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_login_activity;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.parseColor("#DBEAFF");
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected Boolean statusBar_lightMode() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(LoginActivity.this);
        presenter = new LoginPresenter(this);
        resetUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        resetUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ThirdPartyLoginView.onActivityResult(requestCode, resultCode, data);
    }

    private void resetUI() {
//        txt_top_right.set("账号注册");

        // 设置显示账号
        String account = AccountSP.getLoginName();
        if (account != null) {
            txt_account.set(account);
        }
       String  paw= AccountSP.getKeyLoginPwd();
        if (paw != null) {
            txt_pwd.set(paw);
        }
        // 验证码登录
//        if (!ModuleConfig.ENABLE_SMS_LOGIN) {
//            binding.tvCodeLogin.setVisibility(View.INVISIBLE);
//        }
    }

    // 注册
    public void onClick_topRight(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        RegisterActivity.start(getActivity());
        finish();
    }

    // 登录
    public void onClick_ok(View view) {
//        Log.d("--",null);
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.pwdLogin(txt_account.get(), txt_pwd.get(), getActivity());
    }

    // 验证码登录
    public void onClick_codeLogin(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        SmsLoginActivity.start(getActivity());
        finish();
    }

    // 忘记密码
    public void onClick_forgetPwd(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        RetrievePwdActivity.start(getActivity());
    }
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                finish();
                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
