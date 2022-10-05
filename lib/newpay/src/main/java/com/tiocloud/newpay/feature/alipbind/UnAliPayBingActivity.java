package com.tiocloud.newpay.feature.alipbind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletAddBankcardActivityBinding;
import com.tiocloud.newpay.feature.bankcard_add.AddBankCardFragment;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyModel;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyPayPwdFragment;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.CheckPayPwdReq;
import com.watayouxiang.httpclient.model.request.UnAliPayBindNumReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     time   : 2021/03/09
 *     desc   : 解绑绑支付宝
 * </pre>
 */
public class UnAliPayBingActivity extends BindingLightActivity<WalletAddBankcardActivityBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, UnAliPayBingActivity.class);
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
                new UserCurrReq().setCancelTag(this).get(new TioSuccessCallback<UserCurrResp>() {
                    @Override
                    public void onTioSuccess(UserCurrResp resp) {
                        new UnAliPayBindNumReq(payPwd, resp.loginname).setCancelTag(this).post(new TioCallback<Object>() {
                            @Override
                            public void onTioSuccess(Object o) {
                                Log.d("===解除绑定==",payPwd+"=="+o.toString());

                                setResult(200, null);
                                finish();
                            }

                            @Override
                            public void onTioError(String msg) {
                                Log.d("===解除绑定==",payPwd+"=onTioError="+msg);

                                TioToast.showShort(""+msg);

                            }
                        });


                    }
                });

            }
        });

        FragmentUtils.add(getSupportFragmentManager(), verifyPayPwdFragment, binding.container.getId());
    }

    @Override
    public void onBackPressed() {
        new EasyOperDialog.Builder("是否退出解绑")
                .setNegativeBtnTxt("否")
                .setPositiveBtnTxt("是")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        UnAliPayBingActivity.super.onBackPressed();
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
