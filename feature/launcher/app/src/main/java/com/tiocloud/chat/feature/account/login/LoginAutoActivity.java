package com.tiocloud.chat.feature.account.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.DeviceUtils;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.account.mvp.login.LoginContract;
import com.tiocloud.account.mvp.login.LoginPresenter;
import com.tiocloud.chat.R;
import com.tiocloud.chat.databinding.ActivityLoginAutoBinding;
import com.tiocloud.chat.feature.account.pwd.RetrievePwdActivity;
import com.tiocloud.chat.feature.account.register.RegisterActivity;
import com.watayouxiang.androidutils.page.TioActivity;

/**
 * author : TaoWang
 * date : 2019-12-31
 * desc : 登录页面
 */
public class LoginAutoActivity extends TioActivity implements LoginContract.View {

    private LoginPresenter presenter;
    private ActivityLoginAutoBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginAutoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_auto);
        hideStatusBar();
        setStatusBarLightMode(false);
        presenter = new LoginPresenter(this);
        initViews();
        presenter.reqAutoLogin(DeviceUtils.getUniqueDeviceId(), getActivity());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        binding.unbind();
    }

    private void initViews() {

    }
}
