package com.tiocloud.newpay.feature.bankcard_remove;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletRemoveBankcardActivityBinding;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyPayPwdFragment;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/09
 *     desc   : 解绑银行卡
 * </pre>
 */
public class RemoveBankCardActivity extends BindingLightActivity<WalletRemoveBankcardActivityBinding> {

    private static final String KEY_REMOVE_BANK_CARD_VO = "KEY_REMOVE_BANK_CARD_VO";

    private VerifyPayPwdFragment verifyPayPwdFragment;
    private RemoveBankCardViewModel viewModel;

    public static void start(Context context, RemoveBankCardVo vo) {
        Intent starter = new Intent(context, RemoveBankCardActivity.class);
        starter.putExtra(KEY_REMOVE_BANK_CARD_VO, vo);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_remove_bankcard_activity;
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
        viewModel = newViewModel(RemoveBankCardViewModel.class);
        verifyPayPwdFragment = viewModel.addFragment(this, binding.flContainer.getId());
    }

    public VerifyPayPwdFragment getVerifyPayPwdFragment() {
        return verifyPayPwdFragment;
    }

    public RemoveBankCardVo getRemoveBankCardVo() {
        return (RemoveBankCardVo) getIntent().getSerializableExtra(KEY_REMOVE_BANK_CARD_VO);
    }

    @Override
    public void onBackPressed() {
        new EasyOperDialog.Builder("是否退出解绑")
                .setNegativeBtnTxt("否")
                .setPositiveBtnTxt("是")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        RemoveBankCardActivity.super.onBackPressed();
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
