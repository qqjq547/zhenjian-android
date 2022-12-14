package com.tiocloud.chat.feature.main.fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.lzy.okgo.cache.CacheMode;
import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.utils2.BrandUtil;
import com.tiocloud.chat.baseNewVersion.utils2.DensityUtils;
import com.tiocloud.chat.event.UpdateFriendApplyNumEvent;
import com.tiocloud.chat.feature.home.chat.ChatFragment;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.feature.main.adapter.LineAdapter;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.feature.main.model.MainTab;
import com.tiocloud.chat.feature.search.curr.SearchActivity;
import com.tiocloud.chat.feature.session.group.GroupSessionActivity;
import com.tiocloud.chat.feature.session.p2p.P2PSessionActivity;
import com.tiocloud.chat.widget.titlebar.HomeTitleBar;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.ApplyDataReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.request.UserOnlineReq;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.client.IMState;
import com.watayouxiang.imclient.event.TioStateEvent;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.app.Notification.EXTRA_CHANNEL_ID;
import static android.provider.Settings.EXTRA_APP_PACKAGE;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc : ??????
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
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setStatusBarCustom(findViewById(R.id.fl_statusBar));

        homeTitleBar = findViewById(R.id.homeTitleBar);
        homeTitleBar.setTitle("??????");
        IMState imState = TioIMClient.getInstance().getState();
        boolean isConnect = imState == IMState.CONNECTED;
        setAppendTitle(isConnect ? null : "?????????");

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
        homeTitleBar.getFriendBtnView().setVisibility(View.VISIBLE);
        homeTitleBar.getFriendBtnView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),FriendActivity.class));
            }
        });
//        all_layout=findViewById(R.id.all_layout);
//        if(BrandUtil.isBrandHuawei()){
//            Log.d("===??????==","???");
//        }
//        if(BrandUtil.isBrandOppo()){
//            Log.d("===oppo==","???");
//        }
//        if(BrandUtil.isBrandXiaoMi()){
//            Log.d("===??????==","???");
//
//        }
//        checkNotifySetting();
        requestApplyNum();
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
        // areNotificationsEnabled??????????????????????????????????????????API 19?????????19??????????????????????????????????????????true?????????????????????????????????????????????
        boolean isOpened = manager.areNotificationsEnabled();

        if (isOpened) {
            Log.d("==???????????????????????????==" , "\n????????????:" + android.os.Build.MODEL +
                    "\nSDK??????:" + android.os.Build.VERSION.SDK +
                    "\n????????????:" + android.os.Build.VERSION.RELEASE +
                    "\n????????????:" );

        } else {
            Log.d("?????????????????????????????????????????????","==");
//            Toast.makeText(getActivity(),"??????????????????????????????????????????",Toast.LENGTH_SHORT).show();
            dialogPermissionisOpen();
        }
    }
    private void openNotiof(){
        try {
            // ??????isOpened?????????????????????????????????????????????AppInfo??????????????????App????????????
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //????????????????????? API 26, ???8.0??????8.0??????????????????
            intent.putExtra(EXTRA_APP_PACKAGE, BaseConstants.applicationId);
            intent.putExtra(EXTRA_CHANNEL_ID, TioApplication.getInstanceKit().getApplicationInfo().uid);

            //????????????????????? API21??????25?????? 5.0??????7.1 ???????????????????????????
            intent.putExtra("app_package", BaseConstants.applicationId);
            intent.putExtra("app_uid", TioApplication.getInstanceKit().getApplicationInfo().uid);

            // ??????6 -MIUI9.6-8.0.0??????????????????????????????????????????????????????"????????????????????????"??????????????????????????????????????????????????????????????????I'm not ok!!!
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            // ?????????????????????????????????????????????????????????3??????OC105 API25
            Intent intent = new Intent();

            //??????????????????????????????????????????????????????????????????
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", BaseConstants.applicationId, null);
            intent.setData(uri);
            startActivity(intent);
        }
    }


    // ====================================================================================
    // ?????? fragment ????????????
    // ====================================================================================

    @Override
    public void onPageShow(int count, boolean isInit) {
        super.onPageShow(count, isInit);
        setStatusBarLightMode(true);
        requestApplyNum();
    }

    @Override
    public void onResume() {
        super.onResume();
        requestApplyNum();
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
    // ???????????????
    // ====================================================================================

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTioStateEvent(TioStateEvent event) {
        switch (event.state) {
            case CONNECTING:
                //setAppendTitle("?????????...");
                break;
            case CONNECT:
                isConnect();
                setAppendTitle("??????");
                break;
            case ERROR:
            case DISCONNECT:
                setAppendTitle("?????????");
                break;
        }
    }
    private void  isConnect(){
        UserOnlineReq req = new UserOnlineReq();
        req.get(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String content) {
                Log.d("===??????===","==onTioSuccess==="+content);

            }

            @Override
            public void onTioError(String msg) {
                Log.d("===??????===","==onTioError==="+msg);
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
    public void setApplyNum(Integer integer){
        if (integer>0){
            homeTitleBar.getTipTextView().setUnread(integer);
            homeTitleBar.getTipTextView().setVisibility(View.VISIBLE);
        }else {
            homeTitleBar.getTipTextView().setVisibility(View.GONE);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserSysNtf(WxUserSysNtf ntf) {
        if (ntf.code == 30) {// ??????????????????
            requestApplyNum();
        }
    }
    // ?????????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserSysNtf(UpdateFriendApplyNumEvent event) {
        requestApplyNum();
    }
    /**
     * ?????????????????????
     */
    private void requestApplyNum() {
        ApplyDataReq applyDataReq = new ApplyDataReq();
        applyDataReq.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        applyDataReq.setCancelTag(this);
        applyDataReq.get(new TioCallback<Integer>() {
            @Override
            public void onTioSuccess(Integer integer) {
                if (integer>0){
                    homeTitleBar.getTipTextView().setUnread(integer);
                    homeTitleBar.getTipTextView().setVisibility(View.VISIBLE);
                }else {
                    homeTitleBar.getTipTextView().setVisibility(View.GONE);
                }
                if(getActivity() instanceof MainActivity){
                    ((MainActivity)getActivity()).updateApplyUnReadCount(integer);
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
    public void enterSession(String chatlinkid){
        if (TextUtils.isEmpty(chatlinkid)||fragment==null||!fragment.isAdded()){
            MainActivity.start(getActivity(),MainTab.CHAT.getTabIndex());
            return;
        }
        List<ChatListResp.List> chatList=fragment.getChatList();
        if (chatList.isEmpty()){
            MainActivity.start(getActivity(),MainTab.CHAT.getTabIndex());
            return;
        }
        ChatListResp.List chatItem=null;
        for (ChatListResp.List list: chatList) {
            if (TextUtils.equals(chatlinkid,list.id)){
                chatItem=list;
                break;
            }
        }
        if (chatItem==null){
            MainActivity.start(getActivity(),MainTab.CHAT.getTabIndex());
        }else {
            TioApplication.getInstanceKit().chatmode = chatItem.chatmode;
//            Log.d("===???????????????" , item.id);
            ChatListResp.List finalChatItem = chatItem;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (TioIMClient.getInstance().isHandshake()) {
                        switch (finalChatItem.chatmode) {
                            case 1:// ??????
                                // ???????????????
                                P2PSessionActivity.enter(getActivity(), finalChatItem.id);
                                break;
                            case 2:// ??????
                                // ???????????????
                                GroupSessionActivity.enter(getActivity(), finalChatItem.id);
                                break;
                        }
                    }
                }
            },500);
        }
    }
}
