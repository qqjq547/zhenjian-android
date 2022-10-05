package com.tiocloud.chat.feature.main.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;

import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.utils2.BrandUtil;
import com.tiocloud.chat.baseNewVersion.utils2.DensityUtils;
import com.tiocloud.chat.feature.home.chat.ChatFragment;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.feature.search.curr.SearchActivity;
import com.tiocloud.chat.widget.titlebar.HomeTitleBar;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.request.UserOnlineReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.client.IMState;
import com.watayouxiang.imclient.event.TioStateEvent;
import com.watayouxiang.androidutils.page.TioActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc : 会话
 * <p>
 * tabData: {@link com.tiocloud.chat.feature.main.model.MainTab#CHAT}
 */
public class MainChatFragment extends MainTabFragment {

    private HomeTitleBar homeTitleBar;
    private ChatFragment fragment;
    private RelativeLayout all_layout;
    @Override
    public boolean isRegisterEvent() {
        return true;
    }

    @Override
    protected void onInit() {
        setStatusBarCustom(findViewById(R.id.fl_statusBar));

        homeTitleBar = findViewById(R.id.homeTitleBar);
        homeTitleBar.setTitle("聊天");
        IMState imState = TioIMClient.getInstance().getState();
        boolean isConnect = imState == IMState.CONNECTED;
        setAppendTitle(isConnect ? null : "未连接");

        fragment = new ChatFragment();
        fragment.setContainerId(R.id.chat_fragment_container);

        TioActivity tioActivity = (TioActivity) getActivity();
        if (tioActivity != null) {
            tioActivity.replaceFragment(fragment);
        }
        findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(v.getContext());
            }
        });
//        all_layout=findViewById(R.id.all_layout);
//        if(BrandUtil.isBrandHuawei()){
//            Log.d("===华为==","是");
//        }
//        if(BrandUtil.isBrandOppo()){
//            Log.d("===oppo==","是");
//        }
//        if(BrandUtil.isBrandXiaoMi()){
//            Log.d("===小米==","是");
//
//        }
//        checkNotifySetting();
    }
    private View inflateCard;
    private PopupWindow popupWindow;
    private void dialogPermissionisOpen(){
        if(popupWindow!=null){
            if(popupWindow.isShowing()){
                return;
            }
        }
        inflateCard = View.inflate(getActivity(), R.layout.show_activity_permiss_layout, null);
        popupWindow= new PopupWindow(inflateCard, WindowManager.LayoutParams.MATCH_PARENT, DensityUtils.dp2px(getActivity(),200));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.anim_menu_bottombar);
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 1f;
        getActivity().getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        inflateCard.findViewById(R.id.tv_cancleBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        inflateCard.findViewById(R.id.tv_goOpenBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotiof();
                popupWindow.dismiss();
            }
        });
        if(popupWindow.isShowing()){
            return;
        }

        popupWindow.showAtLocation(findViewById(R.id.all_layout),
                Gravity.BOTTOM, 0, 0);
    }
    private void checkNotifySetting() {
        NotificationManagerCompat manager = NotificationManagerCompat.from(getActivity());
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        boolean isOpened = manager.areNotificationsEnabled();

        if (isOpened) {
            Log.d("==通知权限已经被打开==" , "\n手机型号:" + android.os.Build.MODEL +
                    "\nSDK版本:" + android.os.Build.VERSION.SDK +
                    "\n系统版本:" + android.os.Build.VERSION.RELEASE +
                    "\n软件包名:" );

        } else {
            Log.d("还没有开启通知权限，点击去开启","==");
//            Toast.makeText(getActivity(),"没有开启通知权限，点击去开启",Toast.LENGTH_SHORT).show();
            dialogPermissionisOpen();
        }
    }
    private void openNotiof(){
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            intent.putExtra(EXTRA_APP_PACKAGE, BaseConstants.applicationId);
            intent.putExtra(EXTRA_CHANNEL_ID, TioApplication.getInstanceKit().getApplicationInfo().uid);

            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", BaseConstants.applicationId);
            intent.putExtra("app_uid", TioApplication.getInstanceKit().getApplicationInfo().uid);

            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            Intent intent = new Intent();

            //下面这种方案是直接跳转到当前应用的设置界面。
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BaseConstants.applicationId, null);
            intent.setData(uri);
            startActivity(intent);
        }
    }


    // ====================================================================================
    // 通知 fragment 刷新数据
    // ====================================================================================

    @Override
    public void onPageShow(int count, boolean isInit) {
        super.onPageShow(count, isInit);
        setStatusBarLightMode(true);
    }

    @Override
    protected void onRefresh() {
        super.onRefresh();

    }

    @Override
    public void onTabDoubleTap() {
        super.onTabDoubleTap();

    }

    // ====================================================================================
    // 长连接状态
    // ====================================================================================

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTioStateEvent(TioStateEvent event) {
        switch (event.state) {
            case CONNECTING:
                //setAppendTitle("连接中...");
                break;
            case CONNECT:
                isConnect();
                setAppendTitle("在线");
                break;
            case ERROR:
            case DISCONNECT:
                setAppendTitle("未连接");
                break;
        }
    }
    private void  isConnect(){
        UserOnlineReq req = new UserOnlineReq();
        req.get(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String content) {
                Log.d("===连接===","==onTioSuccess==="+content);

            }

            @Override
            public void onTioError(String msg) {
                Log.d("===连接===","==onTioError==="+msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
    private void setAppendTitle(String append) {
        if (homeTitleBar != null) {
            homeTitleBar.setAppendTitle(append);
        }
    }
}
