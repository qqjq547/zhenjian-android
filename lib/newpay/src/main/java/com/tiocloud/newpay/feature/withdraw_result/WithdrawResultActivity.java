package com.tiocloud.newpay.feature.withdraw_result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletWithdrawResultActivityBinding;
import com.tiocloud.newpay.feature.withdraw_record.WithdrawRecordActivity;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/20
 *     desc   : 提现结果页
 * </pre>
 */
public class WithdrawResultActivity extends BindingLightActivity<WalletWithdrawResultActivityBinding> {
    private static final String KEY_SERIAL_NUMBER = "KEY_SERIAL_NUMBER";
    private static final String KEY_REQ_ID = "KEY_REQ_ID";
    private static final String KEY_W_ID = "KEY_W_ID";

    private WithdrawResultViewModel viewModel;

    // 易支付
    public static void start(Context context, String serialnumber) {
        Intent starter = new Intent(context, WithdrawResultActivity.class);
        starter.putExtra(KEY_SERIAL_NUMBER, serialnumber);
        context.startActivity(starter);
    }

    /**
     * 新生支付
     *
     * @param wid   订单id
     * @param reqid 请求id，请注意，不是商户订单号
     */
    public static void start(Activity activity, String wid, String reqid) {
        Intent starter = new Intent(activity, WithdrawResultActivity.class);
        starter.putExtra(KEY_W_ID, wid);
        starter.putExtra(KEY_REQ_ID, reqid);
        activity.startActivity(starter);
    }

    public WithdrawResult getWithdrawResult() {
        return viewModel.getWithdrawResult();
    }

    public String getSerialNumber() {
        return getIntent().getStringExtra(KEY_SERIAL_NUMBER);
    }

    public String getWid() {
        return getIntent().getStringExtra(KEY_W_ID);
    }

    public String getReqId() {
        return getIntent().getStringExtra(KEY_REQ_ID);
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
        return R.layout.wallet_withdraw_result_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // titleBar - rightBtn
        binding.titleBar.getTvRight().setOnClickListener(this::clickWithdrawRecord);
        viewModel = newViewModel(WithdrawResultViewModel.class);

        String serialNumber = getSerialNumber();
        String reqId = getReqId();
        String wid = getWid();

        if (serialNumber != null) {
            viewModel.getWithholdQuery(serialNumber, this);
        } else if (wid != null && reqId != null) {
            viewModel.getWithholdQuery(wid, reqId, this);
        }
    }

    // 提现记录
    private void clickWithdrawRecord(View view) {
        if (ClickUtils.isViewSingleClick(view))
            WithdrawRecordActivity.start(this);
    }

    @Override
    public <T extends BaseFragment> void replaceFragment(T fragment) {
        fragment.setContainerId(binding.flContainer.getId());
        super.replaceFragment(fragment);
    }
}
