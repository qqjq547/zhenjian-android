package com.tiocloud.newpay.feature.paypwd_modify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletModifyPayPwdActivityBinding;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyModel;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyPayPwdFragment;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   : 修改支付密码
 * </pre>
 */
public class ModifyPayPwdActivity extends BindingLightActivity<WalletModifyPayPwdActivityBinding> {

    private String mInitPwd;

    public static void start(Context context) {
        Intent starter = new Intent(context, ModifyPayPwdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_modify_pay_pwd_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        VerifyPayPwdFragment verifyPayPwdFragment = VerifyPayPwdFragment.getInstance(VerifyModel.getInstance_modifyPayPwd());
        verifyPayPwdFragment.setOnVerifyPayPwdListener(new VerifyPayPwdFragment.OnVerifyPayPwdListener() {
            @Override
            public void onVerifyPayPwdSuccess(String payPwd) {
                mInitPwd = payPwd;
                FragmentUtils.replace(verifyPayPwdFragment, ModifyPayPwdFragmentTwo.getInstance());
            }
        });
        FragmentUtils.add(getSupportFragmentManager(), verifyPayPwdFragment, binding.clContainer.getId());
    }

    @Override
    public void onBackPressed() {
        new EasyOperDialog.Builder("是否放弃修改支付密码？")
                .setNegativeBtnTxt("否")
                .setPositiveBtnTxt("是")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        ModifyPayPwdActivity.super.onBackPressed();
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

    public String getInitPwd() {
        return mInitPwd;
    }
}
