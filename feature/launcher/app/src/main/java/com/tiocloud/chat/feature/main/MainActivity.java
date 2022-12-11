package com.tiocloud.chat.feature.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentPagerAdapter;

import com.blankj.utilcode.util.GsonUtils;
import com.google.gson.reflect.TypeToken;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.utils2.SPUtilsNew;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.TioMainActivityBinding;
import com.tiocloud.chat.feature.main.adapter.MainTabPagerAdapter;
import com.tiocloud.chat.feature.main.fragment.MainChatFragment;
import com.tiocloud.chat.feature.main.fragment.MainWebFragment;
import com.tiocloud.chat.feature.main.model.MainTab;
import com.tiocloud.chat.feature.main.mvp.MainContract;
import com.tiocloud.chat.feature.main.mvp.MainPresenter;
import com.tiocloud.jpush.PushLauncher;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.model.request.FoundListResp;
import com.watayouxiang.httpclient.model.response.internal.MainTabBean;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.httpclient.utils.PreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author : TaoWang
 * date : 2019-12-31
 * desc :
 */
public class MainActivity extends TioActivity implements MainContract.View {

    public MainPresenter presenter;
    private TioMainActivityBinding binding;
    private float fontSizeScale;
    public static void start(Context context) {
        MainActivity.start(context, null);
    }

    /**
     * @param tabIndex 再次打开，希望所处的页签索引
     */
    public static void start(Context context, int tabIndex) {
        Intent intent = new Intent();
        intent.putExtra(TioExtras.EXTRA_TAB_INDEX, tabIndex);
        MainActivity.start(context, intent);
    }
    public static void start(Context context,int tabIndex, String chatLinkId) {
        Intent intent = new Intent();
        intent.putExtra(TioExtras.EXTRA_TAB_INDEX, tabIndex);
        intent.putExtra(TioExtras.EXTRA_CHAT_LINK_ID, chatLinkId);
        MainActivity.start(context, intent);
    }

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.tio_main_activity);
        presenter = new MainPresenter(this);
        presenter.init();
//        int selectTab=PreferencesUtil.getInt("firstTab",0);
        binding.viewPager.setCurrentItem(MainTab.CHAT.getTabIndex());
//        PreferencesUtil.saveInt("firstTab",0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 再次打开，希望所处的页签索引
        int tabIndex = intent.getIntExtra(TioExtras.EXTRA_TAB_INDEX, -1);
        if (tabIndex != -1) {
            binding.viewPager.setCurrentItem(tabIndex);
        }
        String chatLinkId=intent.getStringExtra(TioExtras.EXTRA_CHAT_LINK_ID);
        if (!TextUtils.isEmpty(chatLinkId)){
            ((MainChatFragment)((FragmentPagerAdapter)binding.viewPager.getAdapter()).getItem(MainTab.CHAT.getTabIndex())).enterSession(chatLinkId);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 检测更新
        presenter.checkAppUpdate();
        // 清除所有通知
        presenter.clearAllNotifications();
    }
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        Configuration config = res.getConfiguration();
        if(fontSizeScale>0.5){
            config.fontScale= fontSizeScale;//1 设置正常字体大小的倍数
        }
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        binding.unbind();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // 不关闭应用，返回主界面
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
//                finish();
                startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public MainActivity getMainActivity() {
        return this;
    }

    @Override
    public void initViews() {
        fontSizeScale = (float) SPUtilsNew.get(this, BaseConstants.SP_FontScale, 0.0f);

    }
    public void setTabViewPager(MainTab[] mainTabs){
        // 初始化 pager
        MainTabPagerAdapter adapter = new MainTabPagerAdapter(getSupportFragmentManager(), binding.viewPager, this,mainTabs);
//        binding.viewPager.setCurrentItem(MainTab.VIDEO.tabIndex, false);
        // 初始化 tab
        binding.tabStrip.setViewPager(binding.viewPager);
        binding.tabStrip.setOnTabClickListener(adapter);
        binding.tabStrip.setOnTabDoubleTapListener(adapter);
        binding.viewPager.setNoScroll(true);
    }

    private int msgUnRead=0;
    private int applyUnRead=0;
    public void updateMsgUnReadCount(int count) {
        msgUnRead=count;
        updateRedDot();
    }
    public void updateApplyUnReadCount(int count) {
        applyUnRead=count;
        updateRedDot();
    }
    @Override
    public void updateRedDot(){
        if (binding == null) return;
        int count=msgUnRead+applyUnRead;
        binding.tabStrip.updateTab(MainTab.CHAT.getTabIndex(), count);
        PushLauncher.getInstance().setBadgeNumber(count);
    }

    @Override
    public void setFoundList(List<FoundListResp.Found> data) {
        List<MainTabBean> mainTabBeans= GsonUtils.fromJson(PreferencesUtil.getString("app_find_page_base_list",""),new TypeToken<ArrayList<MainTabBean>>(){}.getType());
        List<FoundListResp.Found> tabList=new ArrayList<>();
        for (FoundListResp.Found item:data) {
            //有icon图标，并且没有链接的，就是首页底部tab
            if(compareTabData(mainTabBeans,item)){
                tabList.add(item);
            }else {
                if (tabList.size()>0) {
                    tabList.get(tabList.size() - 1).items.add(item);
                }
            }
        }
        MainTab[] mainTabs=new MainTab[tabList.size()+2];
        for (int i = 0; i < tabList.size(); i++) {
            FoundListResp.Found found=tabList.get(i);
            String[] iconUrls=new String[]{found.icon,found.selecticon};
            mainTabs[i]=new MainTab(MainWebFragment.class,found.itemname,iconUrls,R.drawable.tio_main_home_selector,R.layout.tio_main_web_fragment);
            mainTabs[i].setTabIndex(i);
        }
        mainTabs[tabList.size()]=MainTab.CHAT;
        MainTab.CHAT.setTabIndex(tabList.size());
        mainTabs[tabList.size()+1]=MainTab.USER;
        MainTab.USER.setTabIndex(tabList.size()+1);
        setTabViewPager(mainTabs);
        for (int i = 0; i < tabList.size(); i++) {
            ((MainWebFragment)((FragmentPagerAdapter)binding.viewPager.getAdapter()).getItem(i)).setItemData(tabList.get(i).items);
        }
        binding.viewPager.post(new Runnable() {
            @Override
            public void run() {
                binding.viewPager.setCurrentItem(MainTab.CHAT.getTabIndex());
            }
        });
//        binding.tabStrip.onPageSelected(MainTab.CHAT.getTabIndex());
//        MainWebFragment videoFragment=(MainWebFragment)((FragmentPagerAdapter)binding.viewPager.getAdapter()).getItem(MainTab.VIDEO.tabIndex);
//        MainWebFragment beautyFragment=(MainWebFragment)((FragmentPagerAdapter)binding.viewPager.getAdapter()).getItem(MainTab.BEAUTY.tabIndex);
//        MainWebFragment goldFragment=(MainWebFragment)((FragmentPagerAdapter)binding.viewPager.getAdapter()).getItem(MainTab.GOLD.tabIndex);
//        videoFragment.setItemData(videoList);
//        beautyFragment.setItemData(beautyList);
//        goldFragment.setItemData(goldList);
    }
    private Boolean compareTabData(List<MainTabBean> mainTabBeans,FoundListResp.Found item){
        if (mainTabBeans==null||mainTabBeans.size()==0)
            return false;
        for (MainTabBean mainTabBean: mainTabBeans) {
            if (item.id==mainTabBean.id){
                item.itemname=mainTabBean.itemname;
                item.icon=mainTabBean.icon;
                item.selecticon=mainTabBean.selecticon;
                return true;
            }
        }
        return false;
    }
}
