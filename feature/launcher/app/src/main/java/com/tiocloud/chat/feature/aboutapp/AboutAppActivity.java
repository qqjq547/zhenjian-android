package com.tiocloud.chat.feature.aboutapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;
import com.tiocloud.account.TioWebUrl;
import com.tiocloud.chat.R;
import com.tiocloud.chat.databinding.TioAboutappActivityBinding;
import com.watayouxiang.androidutils.feature.browser.TioBrowserActivity;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.MvpLightActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.imclient.utils.DeviceUtils;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/04/15
 *     desc   :
 * </pre>
 */
public class AboutAppActivity extends MvpLightActivity<_Presenter, TioAboutappActivityBinding> implements _Contract.View {

    public static void start(Context context) {
        Intent starter = new Intent(context, AboutAppActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.tio_aboutapp_activity;
    }

    @Override
    public _Presenter newPresenter() {
        return new _Presenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding.rlVersion.setOnClickListener(v -> presenter.checkAppUpdate());
        binding.rlServiceItem.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                TioBrowserActivity.start(getActivity(), TioHttpClient.getBaseUrl() + TioWebUrl.TIO_USER_PROTOCOL);
            }
        });
        binding.rlPrivateItem.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                TioBrowserActivity.start(getActivity(), TioHttpClient.getBaseUrl() + TioWebUrl.TIO_PRIVATE_POLICY);
            }
        });
        binding.tvAppInfo.setOnLongClickListener(view -> {
            String outApkTime = presenter.getOutApkTime(view.getContext());
            if (!TextUtils.isEmpty(outApkTime)) {
                TioToast.showShort("打包时间：" + outApkTime);
                return true;
            }
            return false;
        });
        binding.tvAppInfo.setText(String.format(Locale.getDefault(), "%s v %s (公测版)",
                AppUtils.getAppName(), DeviceUtils.getAppVersion(this)
        ));
    }
}
