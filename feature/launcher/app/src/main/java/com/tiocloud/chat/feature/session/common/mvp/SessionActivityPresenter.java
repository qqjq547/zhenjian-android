package com.tiocloud.chat.feature.session.common.mvp;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.tiocloud.chat.feature.session.common.model.SessionType;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.androidutils.feature.screenshot_monitor.TioScreenShotMonitor;
import com.watayouxiang.androidutils.widget.WatermarkDrawable;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.ScreenshotReq;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.imclient.prefernces.IMPreferences;

import java.util.ArrayList;
import java.util.List;

public class SessionActivityPresenter extends SessionActivityContract.Presenter {
    public SessionActivityPresenter(SessionActivityContract.View view) {
        super(new SessionActivityModel(), view, false);
    }

    @Override
    public void initBackgroundDrawable() {
        if (!ModuleConfig.ENABLE_WATERMARK) return;

        List<String> labels = new ArrayList<>(2);
        long currUid = HttpPreferences.getCurrUid();
        if (currUid != -1) {
            labels.add(currUid + "");
        }
        String ip = IMPreferences.getIp();
        if (ip != null) {
            labels.add(ip);
        }
        String nowString = TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy/MM/dd HH:mm:ss"));
        labels.add(nowString);
        if (!CollectionUtils.isEmpty(labels)) {
            getView().setBackgroundDrawable(new WatermarkDrawable.Builder(labels).build());
        }
    }

    // ====================================================================================
    // 截屏
    // ====================================================================================

    @Override
    public void registerScreenShotListener() {
        // 告知服务器
        TioScreenShotMonitor.getInstance().setScreenShotCallback(this::reportScreenShot);
        TioScreenShotMonitor.getInstance().registerScreenShotListener();
    }

    private void reportScreenShot(String imagePath) {
        String chatmode = null;
        String bizid = null;
        SessionType sessionType = getView().getSessionType();
        if (sessionType == SessionType.GROUP) {
            chatmode = "2";
            bizid = getView().getGroupId();
        } else if (sessionType == SessionType.P2P) {
            chatmode = "1";
            bizid = getView().getUid();
        }

        if (chatmode == null || bizid == null) return;

        new ScreenshotReq(chatmode, bizid).setCancelTag(SessionActivityPresenter.this).get(new TioCallbackImpl<>());
    }

    @Override
    public void unregisterScreenShotListener() {
        TioScreenShotMonitor.getInstance().unregisterScreenShotListener();
    }

    // ====================================================================================
    // 权限
    // ====================================================================================

    public static void checkPermission(Runnable runnable) {
        // 截屏权限
        TioScreenShotMonitor.getInstance().requestPermissions(runnable);
    }
}
