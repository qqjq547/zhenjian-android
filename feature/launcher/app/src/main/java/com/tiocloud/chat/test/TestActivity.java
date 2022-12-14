package com.tiocloud.chat.test;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.NotificationUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.tiocloud.account.feature.bind_phone.BindPhoneActivity;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.tiocloud.chat.R;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.chat.feature.account.login.LoginActivity;
import com.tiocloud.chat.test.activity.FilePickerTestActivity;
import com.tiocloud.chat.test.activity.HttpTestActivity;
import com.tiocloud.chat.test.activity.PermissionTestActivity;
import com.tiocloud.chat.test.activity.RecordTestActivity;
import com.tiocloud.chat.test.activity.TestWebRTCActivity;
import com.tiocloud.chat.test.activity.UITestActivity;
import com.tiocloud.chat.test.debug.DebugIcon;
import com.tiocloud.chat.util.CrashLogUtils;
import com.tiocloud.social.TioSocialDemoActivity;
import com.tiocloud.verification.TestVerificationActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.appupdate.TestAppUpdate;
import com.watayouxiang.audiorecord.tio.TioBellVibrate;
import com.watayouxiang.demoshell.ListActivity;
import com.watayouxiang.demoshell.ListData;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.ChatListReq;
import com.watayouxiang.httpclient.model.request.SysVersionReq;
import com.watayouxiang.httpclient.model.request.UpdateTokenReq;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.httpclient.model.response.SysVersionResp;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.prefernces.IMPreferences;
import com.watayouxiang.imclient.utils.LoggerUtils;
import com.watayouxiang.webrtclib.TioWebRTC;

public class TestActivity extends ListActivity {
    private int notifyId = 0;
    private boolean switchVideoSink = false;

    public static void start(Context context) {
        Intent starter = new Intent(context, TestActivity.class);
        if (!(context instanceof Activity)) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    @Override
    protected ListData getListData() {
        return new ListData()
                .addActivity(this, PermissionTestActivity.class)
                .addActivity(this, HttpTestActivity.class)
                .addActivity(this, UITestActivity.class)
                .addActivity(this, FilePickerTestActivity.class)
                .addActivity(this, RecordTestActivity.class)
                .addActivity(this, TioSocialDemoActivity.class)
                .addActivity(this, TestVerificationActivity.class)
                .addActivity(this, "?????????????????????", LoginActivity.class)
                .addActivity(this, BindPhoneActivity.class)
                .addSection("???????????????json??????")
                .addClick("???????????????json - ???", view -> LoggerUtils.setJsonFormat(true))
                .addClick("???????????????json - ???", view -> LoggerUtils.setJsonFormat(false))
                .addSection("upload file")
                .addClick("??????????????????", view -> {
                    int i = 1 / 0;
                })
                .addClick("????????????", view -> CrashLogUtils.getInstance().upload())
                .addSection("app update")
                .addClick("????????????", view -> TestAppUpdate.normalUpdate(TestActivity.this))
                .addClick("????????????", view -> TestAppUpdate.forceUpdate(TestActivity.this))
                .addClick("??????????????????", view -> {
                    SysVersionReq sysVersionReq = new SysVersionReq(AppUtils.getAppVersionName());
                    sysVersionReq.setCancelTag(TestActivity.this);
                    sysVersionReq.get(new TioCallback<SysVersionResp>() {
                        @Override
                        public void onTioSuccess(SysVersionResp sysVersionResp) {
                            TioToast.showShort(sysVersionResp.toString());
                        }

                        @Override
                        public void onTioError(String msg) {
                            TioToast.showShort(msg);
                        }
                    });
                })
                .addSection("IM test")
                .addClick("connect", view -> TioIMClient.getInstance().connect())
                .addClick("disconnect", view -> TioIMClient.getInstance().disconnect())
                .addClick("release", view -> TioIMClient.getInstance().release())
                .addClick("loop [disconnect-connect] 1000 times", v -> {
                    for (int i = 0; i < 1000; i++) {
                        TioIMClient.getInstance().disconnect();
                        TioIMClient.getInstance().connect();
                    }
                })
                .addClick("random [disconnect-connect] 10000 times", v -> {
                    for (int i = 0; i < 10000; i++) {
                        int random = (int) (Math.random() * 2 + 1);// [1, 2]
                        if (random == 1) {
                            TioIMClient.getInstance().disconnect();
                        } else {
                            TioIMClient.getInstance().connect();
                        }
                    }
                    TioIMClient.getInstance().connect();
                })
                .addSection("NotificationUtils")
                .addClick("notify", view -> NotificationUtils.notify(notifyId++, builder -> builder.setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("title")
                        .setContentText("text " + notifyId)
                        .setContentIntent(PendingIntent.getActivity(TestActivity.this, 0, getIntent(), PendingIntent.FLAG_UPDATE_CURRENT))
                        .setAutoCancel(true)))
                .addClick("cancelAll", view -> NotificationUtils.cancelAll())
                .addSection("BellTool")
                .addClick("start", view -> TioBellVibrate.getInstance().start(TioBellVibrate.Bell.MSG_NTF_GROUP))
                .addClick("stop", view -> TioBellVibrate.getInstance().stop())
                .addSection("WebRTC")
                .addClick("WebRTC?????????", view -> startActivity(new Intent(TestActivity.this, TestWebRTCActivity.class)))
                .addClick("????????????????????????", view -> TioWebRTC.getInstance().toggleVideoEnable())
                .addClick("?????????????????????????????????FIT/FULL???", view -> TioWebRTC.getInstance().switchLocalVideoScaling())
                .addClick("?????????????????????????????????FIT/FULL???", view -> TioWebRTC.getInstance().switchRemoteVideoScaling())
                .addClick("???????????????/????????????", view -> TioWebRTC.getInstance().toggleAudioDevice())
                .addClick("?????????????????????/???????????????", view -> TioWebRTC.getInstance().switchVideoSink(!switchVideoSink))
                .addSection("app environment")
                .addClick("??????????????????", view -> {
                    HttpPreferences.saveBaseUrl(TioConfig.BASE_URL);
                    IMPreferences.saveHandShakeKey(TioConfig.HAND_SHAKE_KEY);
                    LogoutPresenter.kickOut();
                    AppUtils.relaunchApp(true);
                })
                .addClick("??????????????????", view -> {
                    HttpPreferences.saveBaseUrl("https://tx.t-io.org");
                    IMPreferences.saveHandShakeKey("TesOt0T");
                    LogoutPresenter.kickOut();
                    AppUtils.relaunchApp(true);
                })
                .addSection("data setting")
                .addClick("????????????", view -> ShellUtils.execCmd("pm clear " + TestActivity.this.getPackageName(), false))
                .addClick("??????TioHttp??????", view -> TioHttpClient.clearCache())
                .addClick("launchAppDetailsSettings", view -> PermissionUtils.launchAppDetailsSettings())
                .addSection("http api")
                .addClick("updateTokenReq", view -> {
                    UpdateTokenReq updateTokenReq = new UpdateTokenReq();
                    updateTokenReq.get(new TioCallbackImpl<Void>() {
                    });
                })
                .addClick("chatListReq", view -> {
                    ChatListReq chatListReq = new ChatListReq();
                    chatListReq.get(new TioCallbackImpl<ChatListResp>() {
                    });
                })
                ;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        DebugIcon.setVisibility(false);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        DebugIcon.setVisibility(true);
    }
}