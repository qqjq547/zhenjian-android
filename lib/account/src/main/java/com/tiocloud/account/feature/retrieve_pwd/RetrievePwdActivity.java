package com.tiocloud.account.feature.retrieve_pwd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountRetrievePwdActivityBinding;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.BindingActivity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/24
 *     desc   : 找回密码
 * </pre>
 */
public class RetrievePwdActivity extends BindingActivity<AccountRetrievePwdActivityBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, RetrievePwdActivity.class);
        if (!(context instanceof Activity)) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_retrieve_pwd_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(this);
        resetUI();
    }

    private void resetUI() {
        showInputPhoneFragment();
    }

    @Override
    public <T extends BaseFragment> void replaceFragment(T fragment) {
        fragment.setContainerId(binding.flContainer.getId());
        super.replaceFragment(fragment);
    }

    public void showInputPhoneFragment() {
        replaceFragment(new InputPhoneFragment());
    }

    public void showSmsCodeFragment(String phone) {
        replaceFragment(SmsCodeFragment.getInstance(phone));
    }

    public void showResetPwdFragment(String code, String phone) {
        replaceFragment(ResetPwdFragment.getInstance(code, phone));
    }
}
