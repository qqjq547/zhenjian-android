package com.tiocloud.newpay.feature.wallet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SpanUtils;
import com.tiocloud.common.ModuleConfig;
import com.tiocloud.newpay.feature.account.AccountActivity;
import com.tiocloud.newpay.feature.bill.BillActivity;
import com.tiocloud.newpay.feature.recharge.RechargeActivity;
import com.watayouxiang.androidutils.page.BindingDarkActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.request.PayGetClientTokenReq;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletWalletActivityBinding;
import com.tiocloud.newpay.feature.redpacket.RedPacketActivity;
import com.tiocloud.newpay.feature.wallet.mvp.Contract;
import com.tiocloud.newpay.feature.wallet.mvp.Presenter;
import com.tiocloud.newpay.feature.withdraw.WithdrawActivity;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/28
 *     desc   : 钱包主页
 * </pre>
 */
public class WalletActivity extends BindingDarkActivity<WalletWalletActivityBinding> implements Contract.View {

    private Presenter presenter;

    public static void start(Activity activity) {
        Presenter.openWalletActivity(activity);
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_wallet_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setView(this);
        presenter = new Presenter(this);
        presenter.init();
        if (ModuleConfig.ENABLE_RECHARGE){
            binding.tvRecharge.setVisibility(View.VISIBLE);
        }else {
            binding.tvRecharge.setVisibility(View.GONE);
        }
        if (ModuleConfig.ENABLE_WITHDRAW){
            binding.tvWithdraw.setVisibility(View.VISIBLE);
        }else {
            binding.tvWithdraw.setVisibility(View.GONE);
        }
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

    // 金额显隐
    public void clickEye(View v) {
        boolean moneyVisibility = isMoneyVisibility();

        if (moneyVisibility) {
            // 变成不可见
            hideMoney();
        } else {
            // 变成可见
            presenter.query$ShowMoney();
        }
    }

    // 提现
    public void clickWithdraw(View v) {
        if (ClickUtils.isViewSingleClick(v))
            WithdrawActivity.start(this);
    }

    // 充值
    public void clickRecharge(View v) {
        if (ClickUtils.isViewSingleClick(v))
            RechargeActivity.start(this);
    }

    // 账号信息
    public void clickAccountDetail(View v) {
        if (ClickUtils.isViewSingleClick(v))
            AccountActivity.start(this);
    }

    // 钱包明细
    public void clickWalletDetail(View v) {
        if (ClickUtils.isViewSingleClick(v))
            BillActivity.start(this);
    }

    // 红包记录
    public void clickRedPaperRecord(View v) {
        if (ClickUtils.isViewSingleClick(v))
            RedPacketActivity.start(this);
    }

    // 银行卡---支付方式
    public void clickBank(View v) {
        if (ClickUtils.isGlobalSingleClick())
            presenter.getClientToken(PayGetClientTokenReq.ACCESS_CARDlIST);
    }

    // 安全设置
    public void clickSafe(View v) {
        if (ClickUtils.isGlobalSingleClick())
            presenter.getClientToken(PayGetClientTokenReq.ACCESS_SAFETY);
    }

    // 帮助中心
    public void clickHelp(View v) {
        // TODO: 11/18/20
    }

    @Override
    public void resetUI() {
        // 底部文字
        binding.tvBottomInfo.setText(String.format(Locale.getDefault(), "本服务由%s提供", "新生支付"));
        // 余额显示隐藏处理
//        hideMoney();
        // 变成可见
        presenter.query$ShowMoney();
        // 隐藏帮助中心
        binding.rlHelp.setVisibility(View.GONE);
    }

    @Override
    public boolean isMoneyVisibility() {
        return binding.tvMoneyEye.isSelected();
    }

    @Override
    public void hideMoney() {
        binding.tvMoneyEye.setSelected(false);
        SpanUtils.with(binding.tvMoney)
                .append("******").setFontSize(26, true)
                .create();
    }

    @Override
    public void showMoney(String money) {
        binding.tvMoneyEye.setSelected(true);
        SpanUtils.with(binding.tvMoney)
                .append("¥ ").setFontSize(16, true)
                .append(money).setFontSize(26, true)
                .create();
    }
}
