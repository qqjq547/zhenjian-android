package com.tiocloud.newpay.feature.withdraw;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletWithdrawActivityBinding;
import com.tiocloud.newpay.dialog.ChooseBankCardDialog;
import com.tiocloud.newpay.feature.withdraw.mvp.Contract;
import com.tiocloud.newpay.feature.withdraw.mvp.Presenter;
import com.tiocloud.newpay.feature.withdraw_record.WithdrawRecordActivity;
import com.tiocloud.newpay.tools.MoneyInputFilter;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.listener.SimpleTextWatcher;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.PayCommissionResp;
import com.watayouxiang.imclient.TioIMClient;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/04
 *     desc   : 提现
 * </pre>
 */
public class WithdrawActivity extends BindingLightActivity<WalletWithdrawActivityBinding> implements Contract.View {

    public final ObservableField<String> amount = new ObservableField<>("");
    public final ObservableField<String> remainingRmb = new ObservableField<>("");
    private Presenter presenter;

    private String mRemainingRmbYuan = null;
    private PayCommissionResp mPayCommissionResp = null;

    public static void start(Context context) {
        Intent starter = new Intent(context, WithdrawActivity.class);
        context.startActivity(starter);
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_withdraw_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.parseColor("#F8F8F8");
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
            presenter.reqPayCommission();
            TioIMClient.getInstance().getEventEngine().post(new ChooseBankCardDialog.RefreshDataEvent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    // 提现记录
    private void clickWithdrawRecord(View view) {
        if (ClickUtils.isViewSingleClick(view)) {
            hideSoftInput();
            WithdrawRecordActivity.start(this);
        }
    }

    // 全部提现
    public void clickWithdrawAll(View view) {
        presenter.getWalletInfo(true);
    }

    // 提现
    public void clickWithdraw(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;

        if (mRemainingRmbYuan == null) {
            TioToast.showShort("当前余额获取失败");
            return;
        }
        if (mPayCommissionResp == null) {
            TioToast.showShort("提现手续费获取失败");
            return;
        }
        String rmbYuan = amount.get();
        try {
            long rmbFen = Long.parseLong(MoneyUtils.yuan2fen(rmbYuan));
            long rmbFenMin = mPayCommissionResp.min;
            long rmbFenMax = mPayCommissionResp.max;
            long rmbFenRemaining = Long.parseLong(MoneyUtils.yuan2fen(mRemainingRmbYuan));

            if (rmbFen < rmbFenMin) {
                TioToast.showShort(StringUtils.format("不能小于限制最小金额 %s 元", MoneyUtils.fen2yuan(rmbFenMin)));
                return;
            }
            if (rmbFen > rmbFenMax) {
                TioToast.showShort(StringUtils.format("不能大于限制最大金额 %s 元", MoneyUtils.fen2yuan(rmbFenMax)));
                return;
            }
            if (rmbFen > rmbFenRemaining) {
                TioToast.showShort(StringUtils.format("不能大于当前余额 %s 元", MoneyUtils.fen2yuan(rmbFenRemaining)));
                return;
            }
        } catch (Exception ignored) {
        }
        presenter.showWithdrawDialog(rmbYuan);
    }

    public void clickClear(View view) {
        binding.etAmount.setText("");
    }

    @Override
    public void resetUI() {
        // init Clear btn
        binding.ivClear.setVisibility(View.GONE);
        // init EditText
        binding.etAmount.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                binding.ivClear.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
            }
        });
        binding.etAmount.setFilters(new InputFilter[]{new MoneyInputFilter(binding.etAmount)});
        // titleBar - rightBtn
        binding.titleBar.getTvRight().setOnClickListener(this::clickWithdrawRecord);
        // 当前金额
        remainingRmb.set("当前余额--元");
        // 弹出键盘
        KeyboardUtils.showSoftInput(binding.etAmount);
    }

    @Override
    public void onMoneyResp(String money) {
        mRemainingRmbYuan = money;
        remainingRmb.set(String.format(Locale.getDefault(), "当前余额%s元", money));
    }

    @Override
    public void onWithdrawAllResp(String money) {
        amount.set(money);

        binding.etAmount.postDelayed(() -> {
            int length = 0;
            Editable text = binding.etAmount.getText();
            if (text != null) {
                length = text.length();
            }
            binding.etAmount.setSelection(length);
        }, 100);
    }

    @Override
    public void hideSoftInput() {
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    public void onPayCommissionResp(PayCommissionResp resp) {
        mPayCommissionResp = resp;
    }
}
