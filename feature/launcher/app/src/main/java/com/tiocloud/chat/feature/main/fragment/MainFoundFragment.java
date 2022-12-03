package com.tiocloud.chat.feature.main.fragment;

import android.view.View;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.found.FoundFragment;
import com.tiocloud.chat.feature.home.found.WebViewFragment;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.widget.titlebar.HomeTitleBar;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.TioActivity;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc : 群聊
 * <p>
 * tabData: {@link com.tiocloud.chat.feature.main.model.MainTab#FOUND}
 */
public class MainFoundFragment extends MainTabFragment {

    private HomeTitleBar homeTitleBar;
    private FoundFragment fragment;
    private WebViewFragment webViewFragment;

    @Override
    protected void onInit() {
        homeTitleBar = findViewById(R.id.homeTitleBar);

        setStatusBarCustom(findViewById(R.id.fl_statusBar));
        homeTitleBar.setTitleCenter(getTabData().title);
        homeTitleBar.setLeftClicListener(0,null);
        homeTitleBar.setRightClicListener(0,null);
        homeTitleBar.setSXClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewFragment.reloadBt();
            }
        });
        homeTitleBar.setHTClicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewFragment.goBackBt();

            }
        });
        displayFoundList();

    }

    /**
     * 显示右侧关闭按钮
     */
    private void showRightBtn(){
        homeTitleBar.setRightClicListener(R.drawable.sel_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeTitleBar.setRightClicListener(0,null);
                displayFoundList();
            }
        });
        homeTitleBar.setCloselicListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFoundList();
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
        webViewFragment = WebViewFragment.getInstance(url);
        replaceFragment(webViewFragment);
        showRightBtn();
    }

    public void displayFoundList(){
        removeFragment(webViewFragment);
        fragment = new FoundFragment();
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


}
