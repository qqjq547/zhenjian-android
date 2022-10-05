package com.tiocloud.chat.feature.main.fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lzy.okgo.cache.CacheMode;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.five.FiveFragment;
import com.tiocloud.chat.feature.home.five.WebFiveViewFragment;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.widget.titlebar.HomeTitleBar;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.FiveFoundListReq;
import com.watayouxiang.httpclient.model.request.FiveFoundListResp;
import com.watayouxiang.httpclient.model.request.FoundListReq;
import com.watayouxiang.httpclient.model.request.FoundListResp;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc :
 * <p>
 * tabData: {@link com.tiocloud.chat.feature.main.model.MainTab#FIVE}
 */
public class MainFiveFragment extends MainTabFragment {

    private HomeTitleBar homeTitleBar;
    private FiveFragment fragment;
    private WebFiveViewFragment webViewFragment;
    private String linkedaddress;
    @Override
    protected void onInit() {
        homeTitleBar = findViewById(R.id.homeTitleBar);

        setStatusBarCustom(findViewById(R.id.fl_statusBar));
        homeTitleBar.setTitle(getString(getTabData().titleId));
        homeTitleBar.setLeftClicListener(0,null);
        homeTitleBar.setRightClicListener(0,null);

        displayFoundList();
        FiveFoundListReq foundListReq = new FiveFoundListReq();
        foundListReq.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        foundListReq.setCancelTag(this);
        TioHttpClient.get(foundListReq, new TioCallback<FiveFoundListResp>() {
            @Override
            public void onTioSuccess(FiveFoundListResp listResp) {
//                Log.d("===获取主页==","=onTioSuccess=="+listResp.linkedaddress);
                linkedaddress=listResp.linkedaddress;
                if(!TextUtils.isEmpty(listResp.linkedaddress)){
                    loadUrlFromWebView(listResp.linkedaddress);
                }

            }

            @Override
            public void onTioError(String msg) {
//                Log.d("===获取主页==","==="+msg);
            }
        });
        homeTitleBar.setTitle("");
        homeTitleBar.setLeftClicListener(R.drawable.icon_refresh, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(linkedaddress)){
                    loadUrlFromWebView(linkedaddress);
                }
            }
        });
    }

    /**
     * 显示右侧关闭按钮
     */
    private void showRightBtn(){
        homeTitleBar.setRightClicListener(R.drawable.delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeTitleBar.setRightClicListener(0,null);
                displayFoundList();
            }
        });
    }

    /**
     * 隐藏右侧关闭按钮
     */
    private void hightRightBtn(){
        homeTitleBar.setRightClicListener(0,null);
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
        if (fragment != null) {
            fragment.onRefresh();
        }
    }

    /**
     * WebView 加载链接
     *
     * @param url
     */
    public void loadUrlFromWebView(String url) {
        removeFragment(fragment);
        webViewFragment = WebFiveViewFragment.getInstance(url);
        replaceFragment(webViewFragment);
//        showRightBtn();
    }

    public void displayFoundList(){
        removeFragment(webViewFragment);
        fragment = new FiveFragment();
        replaceFragment(fragment);
        hightRightBtn();
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
        t.setContainerId(R.id.found_fragment_containerfive);
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


}
