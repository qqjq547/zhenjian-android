package com.tiocloud.newpay.feature.paypwd_setup;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletSetupPayPwdFragmentTwoBinding;
import com.tiocloud.newpay.feature.wallet.WalletActivity;
import com.watayouxiang.androidutils.page.AppManager;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.confirm.EasyConfirmDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/05
 *     desc   :
 * </pre>
 */
public class SetupPayPwdFragmentTwo extends BindingFragment<WalletSetupPayPwdFragmentTwoBinding> {

    private static final String KEY_PWD_TEXT = "KEY_PWD_TEXT";
    private SetupPayPwdViewModel viewModel;

    public static SetupPayPwdFragmentTwo getInstance(String pwdText) {
        SetupPayPwdFragmentTwo fragmentTwo = new SetupPayPwdFragmentTwo();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PWD_TEXT, pwdText);
        fragmentTwo.setArguments(bundle);
        return fragmentTwo;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_setup_pay_pwd_fragment_two;
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

        String pwdTxt = getArguments().getString(KEY_PWD_TEXT, null);
        viewModel = newViewModel(SetupPayPwdViewModel.class);

        binding.payPwdEditText.setOnTextFinishListener(str -> {
            if (!StringUtils.equals(pwdTxt, str)) {
                TioToast.showShort("两次密码输入不一致，请重新输入");
                binding.payPwdEditText.clearText();
                return;
            }

            viewModel.reqSetPayPwd(str, SetupPayPwdFragmentTwo.this);
        });
    }

    public void showOkDialog() {
        new EasyConfirmDialog.Builder(getTioActivity())
                .setMessage("支付密码设置成功")
                .setOnConfirmListener((view, dialog) -> {
                    if(AppManager.getAppManager().hasActivity(WalletActivity.class)){
                        Log.d("hjq","hasActivity="+true);
                        finish();
                    }else {
                        Log.d("hjq","hasActivity="+false);
                        WalletActivity.start(getTioActivity());
                        finish();
                    }
                })
                .build()
                .show();
    }

}
