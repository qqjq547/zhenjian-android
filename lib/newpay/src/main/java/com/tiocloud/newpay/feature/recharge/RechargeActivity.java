package com.tiocloud.newpay.feature.recharge;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletRechargeActivityBinding;
import com.tiocloud.newpay.dialog.ChooseBankCardDialog;
import com.tiocloud.newpay.feature.recharge.mvp.Contract;
import com.tiocloud.newpay.feature.recharge.mvp.Presenter;
import com.tiocloud.newpay.tools.MoneyInputFilter;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.listener.SimpleTextWatcher;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.imclient.TioIMClient;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/04
 *     desc   : 充值
 * </pre>
 */
public class RechargeActivity extends BindingLightActivity<WalletRechargeActivityBinding> implements Contract.View {

    public final ObservableField<String> amount = new ObservableField<>("");
    private Presenter presenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, RechargeActivity.class);
        context.startActivity(starter);
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_recharge_activity;
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
            TioIMClient.getInstance().getEventEngine().post(new ChooseBankCardDialog.RefreshDataEvent());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    public void clickRecharge(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        String rmbYuan = amount.get();

        try {
            long rmbFen = Long.parseLong(MoneyUtils.yuan2fen(rmbYuan));
            if (rmbFen == 0) {
                TioToast.showShort(StringUtils.format("金额不能为 %s 元", MoneyUtils.fen2yuan(rmbFen)));
                return;
            }
        } catch (Exception ignored) {
        }

        presenter.showRechargeDialog(rmbYuan);
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
        // 弹出键盘
        KeyboardUtils.showSoftInput(binding.etAmount);
    }

    @Override
    public void hideSoftInput() {
        KeyboardUtils.hideSoftInput(this);
    }
}
