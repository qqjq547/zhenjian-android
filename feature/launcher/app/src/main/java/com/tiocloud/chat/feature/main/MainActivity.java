package com.tiocloud.chat.feature.main;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;

import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.utils2.SPUtilsNew;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.TioMainActivityBinding;
import com.tiocloud.chat.feature.main.adapter.MainTabPagerAdapter;
import com.tiocloud.chat.feature.main.model.MainTab;
import com.tiocloud.chat.feature.main.mvp.MainContract;
import com.tiocloud.chat.feature.main.mvp.MainPresenter;
import com.tiocloud.jpush.PushLauncher;
import com.watayouxiang.androidutils.page.TioActivity;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

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

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 再次打开，希望所处的页签索引
        int tabIndex = intent.getIntExtra(TioExtras.EXTRA_TAB_INDEX, -1);
        if (tabIndex != -1) {
            binding.viewPager.setCurrentItem(tabIndex);
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
        // 初始化 pager
        MainTabPagerAdapter adapter = new MainTabPagerAdapter(getSupportFragmentManager(), binding.viewPager, this);
        binding.viewPager.setCurrentItem(MainTab.CHAT.tabIndex, false);
        // 初始化 tab
        binding.tabStrip.setViewPager(binding.viewPager);
        binding.tabStrip.setOnTabClickListener(adapter);
        binding.tabStrip.setOnTabDoubleTapListener(adapter);
        binding.viewPager.setNoScroll(true);
    }

    @Override
    public void updateRedDot(int pageIndex, int count) {
        if (binding == null) return;
        binding.tabStrip.updateTab(pageIndex, count);

        // 设置角标数字
        if (pageIndex == MainTab.CHAT.tabIndex) {
            PushLauncher.getInstance().setBadgeNumber(count);
        }
    }
}
