package com.tiocloud.account.feature.phone_modify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiocloud.account.BuildConfig;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountModifyActivityBinding;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.watayouxiang.androidutils.page.BindingLightActivity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/19
 *     desc   : 修改手机号
 * </pre>
 */
public class ModifyPhoneActivity extends BindingLightActivity<AccountModifyActivityBinding> implements MvpContract.View {

    private MvpPresenter presenter;
    private PagerAdapter pagerAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, ModifyPhoneActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_modify_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MvpPresenter(this);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void resetUI() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        binding.viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.viewPager.enableScroll(BuildConfig.DEBUG);
    }

    @Override
    public void onBackPressed() {
        int currentItem = binding.viewPager.getCurrentItem();
        if (currentItem == pagerAdapter.getCount() - 1) {
            // 最后一页
            LogoutPresenter.kickOut();
        } else if (currentItem > 0 && currentItem < pagerAdapter.getCount() - 1) {
            // 非最后一页
            binding.viewPager.setCurrentItem(currentItem - 1);
        } else {
            super.onBackPressed();
        }
    }

    public void nextFragment() {
        int currentItem = binding.viewPager.getCurrentItem();
        if (currentItem < pagerAdapter.getCount() - 1) {
            binding.viewPager.setCurrentItem(currentItem + 1);
        }
    }
}
