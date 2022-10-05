package com.tiocloud.chat;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.ProcessUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tiocloud.account.AccountBridge;
import com.tiocloud.account.BuildConfig;
import com.tiocloud.account.TioAccount;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.chat.feature.account.pwd.ModifyPwdActivity;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.tiocloud.chat.mvp.card.CardContract;
import com.tiocloud.chat.mvp.card.CardPresenter;
import com.tiocloud.chat.util.CrashLogUtils;
import com.tiocloud.jpush.PushLauncher;
import com.tiocloud.session.TioSession;
import com.tiocloud.webrtc.webrtc.CallActivity;
import com.tiocloud.webrtc.webrtc.data.CallNtf;
import com.watayouxiang.androidutils.AndroidUtils;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.dialog.confirm.SingletonConfirmDialog;
import com.watayouxiang.db.TioDBHelper;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.httpclient.utils.PreferencesUtil;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.DeviceType;
import com.watayouxiang.imclient.model.body.webrtc.WxCall02Ntf;
import com.watayouxiang.qrcode.QRCodeBridge;
import com.watayouxiang.qrcode.TioQRCode;
import com.watayouxiang.webrtclib.TioWebRTC;
import com.watayouxiang.webrtclib.listener.OnSimpleRTCListener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/18
 *     desc   :
 * </pre>
 */
public class AppLauncher {
    private static final AppLauncher LAUNCHER = new AppLauncher();

    private AppLauncher() {
    }

    public static AppLauncher getInstance() {
        return LAUNCHER;
    }

    public void init(Application app) {
        // 多进程APP，当非主进程时，无需初始化
        if (!ProcessUtils.isMainProcess()) {
            return;
        }
        // 配置 baseUrl
        if (!StringUtils.equals(TioConfig.BASE_URL, HttpCache.TIO_BASE_URL)) {
            HttpPreferences.saveBaseUrl(TioConfig.BASE_URL);
        }
        // 账号模块
        initAccountModule();
        // 会话模块
        initSessionModule();
        // 二维码模块
        initQRCodeModule();
        // 数据库
        TioDBHelper.init(app);
        // AndroidUtils
        AndroidUtils.init(app);
        // jpush
        PushLauncher.getInstance().init(app);
        PushLauncher.getInstance().setOnPushListener(context -> MainActivity.start(context, 0));
        // 踢出登录
        TioIMClient.getInstance().setKickOutListener(this::handleKickOut);
        TioHttpClient.getInstance().getRespInterceptor().setKickOutListener(this::handleKickOut);
        // WebRTC 来电监听
        setWebRtcListener();
        // 获取当前包名
        String packageName = app.getPackageName();
         //获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
         //设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(app);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(app, "fb1fa6f6d6", false, strategy);
        // 崩溃日志
        CrashLogUtils.getInstance().listener();
        CrashLogUtils.getInstance().upload();
        // debug
        initDebug();
    }
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    private void initDebug() {
        boolean openDebug = BuildConfig.DEBUG;
        try {
            if (openDebug) {
                Class.forName("com.tiocloud.chat.test.debug.DebugUtils");
            }
        } catch (ClassNotFoundException ignore) {
        }
        TioHttpClient.getInstance().setDebug(openDebug);
        TioIMClient.setDebug(openDebug);
        TioWebRTC.setDebug(openDebug);
        TioLogger.setIsLogEnable(openDebug);
        TioDBHelper.setDebug(openDebug);
    }

    private void initSessionModule() {
        TioSession.init(new TioSession.Bridge() {
            @Override
            public void startUserDetailActivity(Context context, String uid) {
                UserDetailActivity.start(context, uid);
            }
        });
    }

    private void initAccountModule() {
        TioAccount.init(new AccountBridge() {
            @Override
            public void startMainActivity(Context context) {
                MainActivity.start(context);
            }

            @Override
            public void startModifyPwdActivity(Context context) {
                ModifyPwdActivity.start(context);
            }
        });
    }

    private void setWebRtcListener() {
        TioWebRTC.getInstance().setOnGlobalRTCListener(new OnSimpleRTCListener() {
            @Override
            public void onCall(WxCall02Ntf call) {
                super.onCall(call);

                DeviceType fromDevice = DeviceType.from((byte) call.fromdevice);
                if (call.fromuid == call.touid && fromDevice == DeviceType.ANDROID) {
                    return;
                }

                // 如果App在后台，那么来电不给响应
                if (!AppUtils.isAppForeground()) {
                    return;
                }

                CallNtf callNtf = new CallNtf(call.fromuid, call.type);
                CallActivity.start(Utils.getApp(), callNtf);
            }
        });
    }

    private void handleKickOut(String msg) {
        LogoutPresenter.clearLoginInfo();

        Activity activity = ActivityUtils.getTopActivity();
        if (activity == null) {
            LogoutPresenter.openLoginCloseOthers();
            return;
        }

        new SingletonConfirmDialog.ShowHelper(activity)
                .setMessage("登录失效，请重新登录")
                .setCancelable(false)
                .setMessageGravity(Gravity.CENTER_HORIZONTAL)
                .setOnConfirmListener((view, dialog) -> {
                    dialog.dismiss();
                    LogoutPresenter.openLoginCloseOthers();
                })
                .show();
    }

    private void initQRCodeModule() {
        TioQRCode.init(new QRCodeBridge() {
            private final CardPresenter cardPresenter = new CardPresenter();

            @Override
            public void openP2PCard(Context context, String uid) {
                cardPresenter.openP2PCard(context, uid);
            }

            @Override
            public void openGroupCard(Context context, String groupId, String shareFromUid, OpenGroupCardListener listener) {
                cardPresenter.openGroupCard(context, groupId, shareFromUid, new CardContract.OpenGroupCardListener() {
                    @Override
                    public void onOpenGroupCardSuccess() {
                        listener.onOpenGroupCardSuccess();
                    }

                    @Override
                    public void onOpenGroupCardError() {
                        listener.onOpenGroupCardError();
                    }
                });
            }
        });
    }
}
