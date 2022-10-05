package com.tiocloud.newpay.feature.open;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.common.ModuleConfig;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.TioNewPay;
import com.tiocloud.newpay.databinding.WalletOpenWalletActivityBinding;
import com.tiocloud.newpay.feature.paypwd_setup.SetupPayPwdActivity;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.utils.BrowserUtils;
import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/30
 *     desc   : 钱包账户开通说明
 * </pre>
 */
public class OpenWalletActivity extends BindingLightActivity<WalletOpenWalletActivityBinding> {

    public final ObservableField<String> userName = new ObservableField<>("");
    public final ObservableField<String> userId = new ObservableField<>("");
    public final ObservableField<String> userPhone = new ObservableField<>("");
    public final ObservableField<Boolean> isCheckbox = new ObservableField<>(false);
    private OpenWalletViewModel viewModel;

    public static void start(Activity activity) {
        Intent starter = new Intent(activity, OpenWalletActivity.class);
        activity.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setView(this);
        binding.tvCheckbox.setSelected(isCheckbox.get());
//        binding.etUserPhone.setEnabled(false);

        viewModel = newViewModel(OpenWalletViewModel.class);
        viewModel.queryUserPhone(this);

        if (ModuleConfig.DEBUG) {
            binding.tvOpenSetupPayPwd.setVisibility(View.VISIBLE);
            binding.tvOpenSetupPayPwd.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    SetupPayPwdActivity.start(getActivity());
                }
            });
        } else {
            binding.tvOpenSetupPayPwd.setVisibility(View.GONE);
        }
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
        return R.layout.wallet_open_wallet_activity;
    }

    public void clickCheckBox(View view) {
        boolean selected = view.isSelected();
        isCheckbox.set(!selected);
        view.setSelected(isCheckbox.get());
    }

    public void clickServiceProtocol(View view) {
        BrowserUtils.openBrowserActivity(this, TioNewPay.PAY_EASE_SERVICE_AGREEMENT);
    }

    public void clickPrivateProtocol(View view) {
        BrowserUtils.openBrowserActivity(this, TioNewPay.PAY_EASE_PRIVACY_POLICY);
    }

    public void clickAgreeBtn(View view) {
        if (ClickUtils.isViewSingleClick(view))
            viewModel.reqOpenWallet(this);
    }

    public void onUserPhone(String phone) {
        userPhone.set(StringUtils.null2Length0(phone));
    }
}
