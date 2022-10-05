package com.tiocloud.newpay.feature.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.SizeUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletAccountActivityBinding;
import com.tiocloud.newpay.feature.account.mvp.Contract;
import com.tiocloud.newpay.feature.account.mvp.Presenter;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.httpclient.model.response.PayRealInfoResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/03
 *     desc   : 账户信息
 * </pre>
 */
public class AccountActivity extends BindingLightActivity<WalletAccountActivityBinding> implements Contract.View {

    public final ObservableField<String> name = new ObservableField<>("");
    public final ObservableField<String> idCard = new ObservableField<>("");
    public final ObservableField<String> phone = new ObservableField<>("");

    private Presenter presenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(this);
        presenter = new Presenter(this);
        presenter.init();
    }

    @Override
    public void onResume(int count) {
        super.onResume(count);
        if (count > 1) {
            presenter.getWalletInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected Integer background_color() {
        return Color.parseColor("#F8F8F8");
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_account_activity;
    }

    @Override
    public void resetUI() {
        // 新生支付，没有信息认证
        binding.tvNameMark.setVisibility(View.INVISIBLE);
        binding.tvPhoneMark.setVisibility(View.INVISIBLE);
        binding.tvNameMark.getLayoutParams().height = SizeUtils.dp2px(12);
        binding.tvPhoneMark.getLayoutParams().height = SizeUtils.dp2px(12);

        name.set("");
        idCard.set("");
        phone.set("");
    }

    @Override
    public void onPayRealInfoResp(PayRealInfoResp resp) {
        name.set(resp.name);
        idCard.set(resp.cardno);
        phone.set(MoneyUtils.getHiddenPhone(resp.mobile));
    }
}
