package com.tiocloud.newpay.feature.paypwd_find;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletFindPayPwdFragmentTwoBinding;
import com.watayouxiang.androidutils.page.BindingFragment;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   :
 * </pre>
 */
public class FindPayPwdFragmentTwo extends BindingFragment<WalletFindPayPwdFragmentTwoBinding> {

    public static FindPayPwdFragmentTwo getInstance() {
        FindPayPwdFragmentTwo fragmentTwo = new FindPayPwdFragmentTwo();
        Bundle bundle = new Bundle();
        fragmentTwo.setArguments(bundle);
        return fragmentTwo;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_find_pay_pwd_fragment_two;
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
            FragmentUtils.replace(this, FindPayPwdFragmentThree.getInstance(str));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
