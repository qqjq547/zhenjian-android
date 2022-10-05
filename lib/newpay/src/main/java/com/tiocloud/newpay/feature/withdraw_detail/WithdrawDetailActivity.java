package com.tiocloud.newpay.feature.withdraw_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletWithdrawDetailActivityBinding;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/01
 *     desc   : 提现详情页
 * </pre>
 */
public class WithdrawDetailActivity extends BindingLightActivity<WalletWithdrawDetailActivityBinding> {
    private static final String WITHDRAW_DETAIL_VO = "WITHDRAW_DETAIL_VO";

    public static void start(Context context, WithdrawDetailVo vo) {
        Intent starter = new Intent(context, WithdrawDetailActivity.class);
        starter.putExtra(WITHDRAW_DETAIL_VO, vo);
        context.startActivity(starter);
    }

    public WithdrawDetailVo getDetailVo() {
        return (WithdrawDetailVo) getIntent().getSerializableExtra(WITHDRAW_DETAIL_VO);
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_withdraw_detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WithdrawDetailVo vo = getDetailVo();
        if (vo == null) return;
        replaceFragment(new WithdrawDetailFragment());
    }

    @Override
    public <T extends BaseFragment> void replaceFragment(T fragment) {
        fragment.setContainerId(binding.flContainer.getId());
        super.replaceFragment(fragment);
    }
}
