package com.tiocloud.chat.feature.home.found;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.found.mvp.FoundPresenter;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.TioActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class WebViewFragment extends BaseFragment {

    private static String TAG_URL = "tag_url";

    public static WebViewFragment getInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(TAG_URL, url);
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    private WebView webView;
    private LinearLayout agenwebView;
    private AgentWeb agentWeb;
    private String url;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tio_webview_fragment, container, false);
         url = getArguments().getString(TAG_URL);
        initWebView(view);
        return view;
    }

    private WebChromeClient mWebChromeClient=new WebChromeClient(){

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

    };
    private WebViewClient mWebViewClient=new com.just.agentweb.WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

    };
    /**
     * WebView
     */
    private void initWebView(View view) {
        agenwebView=view.findViewById(R.id.agenwebView);

        agentWeb= AgentWeb.with(this)
                .setAgentWebParent(agenwebView, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(url);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                agentWeb.getIndicatorController().offerIndicator().setProgress(100);
            }
        },2000);
//        webView = view.findViewById(R.id.webView2);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setSavePassword(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        webSettings.setUseWideViewPort(true);
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                handler.proceed(); // 不管证书是否有风险，都给与展示；不写该方法会出现空白页面
//            }
//            @Override
//            public void onLoadResource(WebView view, String url) {
//                Log.e("===getUrl===","WebView3:"+view.getUrl());
//                super.onLoadResource(view, url);
//            }
//        });
//        webView.loadUrl(url);

    }
    @Override
    public void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    public void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
    public void goBackBt(){
//        if (webView == null) {
//            return;
//        }
//        webView.goBack();
        if (agentWeb == null) {
            return;
        }
        agentWeb.back();
    }
    public void reloadBt(){
//        if (webView == null) {
//            return;
//        }
//        webView.reload();
        if (agentWeb == null) {
            return;
        }
        agentWeb.getUrlLoader().reload();
    }

    /**
     * WebView 加载链接
     *
     * @param url
     */
    public void loadUrl2FromWebView(String url) {
//        if (webView == null) {
//            return;
//        }
//        webView.loadUrl(url);
        if (agentWeb == null) {
            return;
        }
        agentWeb.getUrlLoader().loadUrl(url);

    }
}
