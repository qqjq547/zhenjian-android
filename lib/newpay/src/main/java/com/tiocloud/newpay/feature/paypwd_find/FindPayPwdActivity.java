package com.tiocloud.newpay.feature.paypwd_find;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletFindPayPwdActivityBinding;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   : 忘记支付密码
 * </pre>
 */
public class FindPayPwdActivity extends BindingLightActivity<WalletFindPayPwdActivityBinding> {

    private String smsCode = null;

    public static void start(Context context) {
        Intent starter = new Intent(context, FindPayPwdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_find_pay_pwd_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.add(getSupportFragmentManager(), FindPayPwdFragmentOne.getInstance(), binding.clContainer.getId());
    }

    @Override
    public void onBackPressed() {
        new EasyOperDialog.Builder("是否放弃找回支付密码？")
                .setNegativeBtnTxt("否")
                .setPositiveBtnTxt("是")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        FindPayPwdActivity.super.onBackPressed();
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

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getSmsCode() {
        return smsCode;
    }
}
