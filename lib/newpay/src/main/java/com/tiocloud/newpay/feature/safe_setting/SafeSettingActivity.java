package com.tiocloud.newpay.feature.safe_setting;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;

import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletSafeSettingActivityBinding;
import com.tiocloud.newpay.feature.paypwd_find.FindPayPwdActivity;
import com.tiocloud.newpay.feature.paypwd_modify.ModifyPayPwdActivity;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/05
 *     desc   : 安全设置
 * </pre>
 */
public class SafeSettingActivity extends BindingActivity<WalletSafeSettingActivityBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, SafeSettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_safe_setting_activity;
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
    protected Boolean statusBar_lightMode() {
        return true;
    }

    // 修改支付密码
    public void onClick_modifyPayPwd(View view) {
        if (ClickUtils.isViewSingleClick(view)) {
            ModifyPayPwdActivity.start(getActivity());
        }
    }

    // 忘记支付密码
    public void onClick_findPayPwd(View view) {
        if (ClickUtils.isViewSingleClick(view)) {
            FindPayPwdActivity.start(getActivity());
        }
    }
}
