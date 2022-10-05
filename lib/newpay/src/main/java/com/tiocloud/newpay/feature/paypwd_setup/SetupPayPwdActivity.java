package com.tiocloud.newpay.feature.paypwd_setup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletSetupPayPwdActivityBinding;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/04
 *     desc   : 设置支付密码
 * </pre>
 */
public class SetupPayPwdActivity extends BindingLightActivity<WalletSetupPayPwdActivityBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, SetupPayPwdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_setup_pay_pwd_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.add(getSupportFragmentManager(), SetupPayPwdFragmentOne.getInstance(), binding.clContainer.getId());
    }

    @Override
    public void onBackPressed() {
        new EasyOperDialog.Builder("是否放弃设置支付密码？")
                .setNegativeBtnTxt("否")
                .setPositiveBtnTxt("是")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        SetupPayPwdActivity.super.onBackPressed();
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
