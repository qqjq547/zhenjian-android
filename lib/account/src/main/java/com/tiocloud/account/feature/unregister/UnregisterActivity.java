package com.tiocloud.account.feature.unregister;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountUnregisterActivityBinding;
import com.watayouxiang.androidutils.page.BindingLightActivity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/04/15
 *     desc   : 注销账号
 * </pre>
 */
public class UnregisterActivity extends BindingLightActivity<AccountUnregisterActivityBinding> {

    public static void start(Context context) {
        Intent starter = new Intent(context, UnregisterActivity.class);
        context.startActivity(starter);
    }

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
        return R.layout.account_unregister_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.add(getSupportFragmentManager(), ReadItemFragment.getInstance(), binding.flContainer.getId());
    }
}
