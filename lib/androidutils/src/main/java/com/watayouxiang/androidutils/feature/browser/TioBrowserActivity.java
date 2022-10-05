package com.watayouxiang.androidutils.feature.browser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ResourceUtils;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.databinding.TioActivityBrowserBinding;
import com.watayouxiang.androidutils.feature.browser.mvp.Contract;
import com.watayouxiang.androidutils.feature.browser.mvp.Presenter;
import com.watayouxiang.androidutils.page.MvpLightActivity;
import com.watayouxiang.demoshell.webview.TListener;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/13
 *     desc   : 浏览器页
 * </pre>
 */
public class TioBrowserActivity extends MvpLightActivity<Presenter, TioActivityBrowserBinding> implements Contract.View {

    public static final String KEY_URL = "KEY_URL";

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, TioBrowserActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }

    public String getUrl() {
        return getIntent().getStringExtra(KEY_URL);
    }

    @Override
    public Presenter newPresenter() {
        return new Presenter(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.tio_activity_browser;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding.browser != null) {
            binding.browser.releaseRes();
        }
    }

    @Override
    public void onBackPressed() {
        if (binding.browser.canGoBack()) {
            binding.browser.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void resetUI() {
        initTitleBar();
        //设置字体默认缩放大小为100%
        binding.browser.getSettings().setTextZoom(100);
        //设置监听
        setWebListener();
        //加载网页
        String url = getUrl();
        if (url != null) {
            binding.browser.loadUrl(url);
        }
    }

    private void initTitleBar() {
        binding.titleBar.getIvBack().setOnClickListener(view -> finish());
        // 右侧按钮
        View.OnClickListener ivRightOnClickListener = getIvRightOnClickListener();
        if (ivRightOnClickListener != null) {
            ImageView ivRight = binding.titleBar.getIvRight();
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageDrawable(ResourceUtils.getDrawable(R.drawable.androidutils_ic_more));
            ivRight.setOnClickListener(ivRightOnClickListener);
        }
    }

    public View.OnClickListener getIvRightOnClickListener() {
        return null;
    }

    private void setWebListener() {
        binding.browser.setListener(new TListener() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                binding.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                binding.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                binding.titleBar.setTitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                binding.progressBar.setProgress(newProgress);
            }
        });
    }

}