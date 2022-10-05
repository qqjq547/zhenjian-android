package com.tiocloud.newpay.feature.recharge_result;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletRechargeResultActivityBinding;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.BindingLightActivity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/20
 *     desc   : 充值结果页
 * </pre>
 */
public class RechargeResultActivity extends BindingLightActivity<WalletRechargeResultActivityBinding> {
    private static final String RECHARGE_SERIAL_NUMBER = "RECHARGE_SERIAL_NUMBER";
    private static final String KEY_REQ_ID = "KEY_REQ_ID";
    private static final String KEY_R_ID = "KEY_R_ID";

    private RechargeResultViewModel viewModel;

    // 易支付
    public static void start(Context context, String serialnumber) {
        Intent starter = new Intent(context, RechargeResultActivity.class);
        starter.putExtra(RECHARGE_SERIAL_NUMBER, serialnumber);
        context.startActivity(starter);
    }

    /**
     * 新生支付
     *
     * @param rid   支付订单id
     * @param reqid 支付请求id，请注意，不是商户订单号
     */
    public static void start(Activity activity, String rid, String reqid) {
        Intent starter = new Intent(activity, RechargeResultActivity.class);
        starter.putExtra(KEY_R_ID, rid);
        starter.putExtra(KEY_REQ_ID, reqid);
        activity.startActivity(starter);
    }

    public String getSerialNumber() {
        return getIntent().getStringExtra(RECHARGE_SERIAL_NUMBER);
    }

    public String getRid() {
        return getIntent().getStringExtra(KEY_R_ID);
    }

    public String getReqId() {
        return getIntent().getStringExtra(KEY_REQ_ID);
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
        return R.layout.wallet_recharge_result_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = newViewModel(RechargeResultViewModel.class);
        String serialNumber = getSerialNumber();

        if (serialNumber != null) {
            viewModel.getRechargeQuery(serialNumber, this);
        } else if (getRid() != null && getReqId() != null) {
            viewModel.getRechargeQuery(getRid(), getReqId(), this);
        }
    }

    @Override
    public <T extends BaseFragment> void replaceFragment(T fragment) {
        fragment.setContainerId(binding.flContainer.getId());
        super.replaceFragment(fragment);
    }

    public RechargeResult getRechargeResult() {
        return viewModel.getRechargeResult();
    }
}
