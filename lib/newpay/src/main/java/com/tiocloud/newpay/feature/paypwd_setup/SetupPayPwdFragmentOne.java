package com.tiocloud.newpay.feature.paypwd_setup;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletSetupPayPwdFragmentOneBinding;
import com.watayouxiang.androidutils.page.BindingFragment;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/05
 *     desc   :
 * </pre>
 */
public class SetupPayPwdFragmentOne extends BindingFragment<WalletSetupPayPwdFragmentOneBinding> {

    public static SetupPayPwdFragmentOne getInstance() {
        return new SetupPayPwdFragmentOne();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_setup_pay_pwd_fragment_one;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.WHITE;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        binding.payPwdEditText.setOnTextFinishListener(str -> {
            String pwdText = binding.payPwdEditText.getPwdText();
            FragmentUtils.replace(this, SetupPayPwdFragmentTwo.getInstance(pwdText));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
