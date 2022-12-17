package com.tiocloud.chat.feature.main.fragment;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.content.ContextCompat;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebChromeClient;
import com.just.agentweb.WebViewClient;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.found.FoundFragment;
import com.tiocloud.chat.feature.home.found.WebViewFragment;
import com.tiocloud.chat.feature.main.adapter.LineAdapter;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.widget.titlebar.HomeTitleBar;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.model.request.FoundListResp;

import java.util.ArrayList;
import java.util.List;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc : 群聊
 * <p>
 * tabData: {@link com.tiocloud.chat.feature.main.model.MainTab#}
 */
public class MainWebFragment extends MainTabFragment {

    private HomeTitleBar homeTitleBar;
    private WebView webView;
    private LinearLayout agenwebView;
    private AgentWeb agentWeb;
    private String url;
    private ListPopupWindow mListPop;
    private List<FoundListResp.Found> itemData=new ArrayList<>();
    private LineAdapter lineAdapter;
    private int currentPosition=0;


    @Override
    protected void onInit() {
        homeTitleBar = findViewById(R.id.homeTitleBar);
        setStatusBarCustom(findViewById(R.id.fl_statusBar));
//        homeTitleBar.setTitleCenter(getString(getTabData().titleId));
        homeTitleBar.setLeftClicListener(0,null);
        homeTitleBar.setRightClicListener(0,null);
        homeTitleBar.setSXClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadBt();
            }
        });
        homeTitleBar.setHTClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackBt();
            }
        });
        homeTitleBar.setCloselicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!itemData.isEmpty()){
                    showItem(currentPosition);
                }
            }
        });
        if (!itemData.isEmpty()){
            showItem(0);
        }
        homeTitleBar.getWebTitleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListPop.isShowing()) {
                    view.setSelected(false);
                    mListPop.dismiss();
                } else {
                    view.setSelected(true);
                    mListPop.show();
                }
            }
        });
        showRightBtn();
        initPopwindow();
        initWebView(getView());
    }

    /**
     * 显示右侧关闭按钮
     */
    private void showRightBtn(){
        homeTitleBar.setRightClicListener(R.drawable.sel_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeTitleBar.setRightClicListener(0,null);
            }
        });
        homeTitleBar.isShowWebBt(true);
    }

    /**
     * 隐藏右侧关闭按钮
     */
    private void hightRightBtn(){
        homeTitleBar.setRightClicListener(0,null);
        homeTitleBar.isShowWebBt(false);

    }


    @Override
    public void onPageShow(int count, boolean isInit) {
        super.onPageShow(count, isInit);
        setStatusBarLightMode(true);
    }

    public void setAppendTitle(int groupNum) {
        if (homeTitleBar != null) {
            homeTitleBar.setAppendTitle(String.valueOf(groupNum));
        }
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();
//        if (fragment != null) {
//            fragment.onRefresh();
//        }
    }

    /**
     * WebView 加载链接
     *
     * @param url
     */
    public void loadUrlFromWebView(String url) {
        agentWeb.getUrlLoader().loadUrl(url);
    }



    private <T extends BaseFragment> void showFragment(T t){
        if (t==null){
            return;
        }
        TioActivity tioActivity = (TioActivity) getActivity();
        if (tioActivity != null) {
            tioActivity.showFragment(t);
        }
    }

    private <T extends BaseFragment> void hideFragment(T t){
        if (t==null){
            return;
        }
        TioActivity tioActivity = (TioActivity) getActivity();
        if (tioActivity != null) {
            tioActivity.hideFragment(t);
        }
    }


    private <T extends BaseFragment> void replaceFragment(T t){
        if (t==null){
            return;
        }
        t.setContainerId(R.id.found_fragment_container);
        TioActivity tioActivity = (TioActivity) getActivity();
        if (tioActivity != null) {
            tioActivity.replaceFragment(t);
        }
    }


    private <T extends BaseFragment> void removeFragment(T t){
        if (t==null){
            return;
        }
        TioActivity tioActivity = (TioActivity) getActivity();
        if (tioActivity != null) {
            tioActivity.removeFragment(t);
        }
    }

    public void setItemData(List<FoundListResp.Found> itemData) {
        this.itemData = itemData;
        lineAdapter=new LineAdapter(itemData);
        mListPop.setAdapter(lineAdapter);
        if (isAdded()) {
            showItem(0);
        }
    }
    public void showItem(int position){
        if (position<itemData.size()){
            currentPosition=position;
            loadUrlFromWebView(itemData.get(position).linkedaddress);
            homeTitleBar.setWebTitle(itemData.get(position).itemname);
        }
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
        try {
            agentWeb.getWebLifeCycle().onDestroy();
        }catch (Exception e){
            e.printStackTrace();
        }
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
    private void initPopwindow(){
        mListPop = new ListPopupWindow(getActivity());
        mListPop.setWidth(FrameLayout.LayoutParams.WRAP_CONTENT);
        mListPop.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
        mListPop.setBackgroundDrawable(
                ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.white_tanchuang_bg
                )
        );
        lineAdapter=new LineAdapter(itemData);
        mListPop.setAdapter(lineAdapter);
        mListPop.setAnchorView(homeTitleBar);
        mListPop.setModal(true);
        mListPop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mListPop.dismiss();
                FoundListResp.Found sportItem = itemData.get(i);
                showItem(i);
            }
        });
        mListPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                homeTitleBar.getWebTitleView().setSelected(false);
            }
        });
    }
}
