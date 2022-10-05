package com.tiocloud.newpay.feature.alipbind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletAddBankcardActivityBinding;
import com.tiocloud.newpay.feature.bankcard_add.AddBankCardFragment;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyModel;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyPayPwdFragment;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     time   : 2021/03/09
 *     desc   : 添加绑定支付宝
 * </pre>
 */
public class AddAliPayCardActivity extends BindingLightActivity<WalletAddBankcardActivityBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, AddAliPayCardActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_add_bankcard_activity;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding.titleBar.setTitle("支付宝绑定");
        VerifyPayPwdFragment verifyPayPwdFragment = VerifyPayPwdFragment.getInstance(VerifyModel.getInstance_addBankCard());
        verifyPayPwdFragment.setOnVerifyPayPwdListener(new VerifyPayPwdFragment.OnVerifyPayPwdListener() {
            @Override
            public void onVerifyPayPwdSuccess(String payPwd) {
                BindAliPayActivity.start(getActivity(),payPwd);
                finish();
            }
        });

        FragmentUtils.add(getSupportFragmentManager(), verifyPayPwdFragment, binding.container.getId());
    }

    @Override
    public void onBackPressed() {
        new EasyOperDialog.Builder("是否退出绑定")
                .setNegativeBtnTxt("否")
                .setPositiveBtnTxt("是")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        AddAliPayCardActivity.super.onBackPressed();
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getActivity());
    }

}
