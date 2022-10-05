package com.tiocloud.chat.feature.home.five;

import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.watayouxiang.androidutils.page.BaseFragment;

public class WebFiveViewFragment extends BaseFragment {

    private static String TAG_URL = "tag_url";

    public static WebFiveViewFragment getInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG_URL, url);
        WebFiveViewFragment webViewFragment = new WebFiveViewFragment();
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tio_webview_fragment, container, false);
        initWebView(view);
        String url = getArguments().getString(TAG_URL);
        Log.e("url", url);
        loadUrlFromWebView(url);
        return view;
    }


    /**
     * WebView
     */
    private void initWebView(View view) {
        webView = view.findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 不管证书是否有风险，都给与展示；不写该方法会出现空白页面
            }
        });
    }
    public void reloadUrl(){
        webView.reload();
    }

    /**
     * WebView 加载链接
     *
     * @param url
     */
    public void loadUrlFromWebView(String url) {
        if (webView == null) {
            return;
        }
        webView.loadUrl(url);
    }
}
