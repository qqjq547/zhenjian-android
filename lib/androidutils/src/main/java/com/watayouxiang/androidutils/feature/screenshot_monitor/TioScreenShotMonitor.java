package com.watayouxiang.androidutils.feature.screenshot_monitor;

import android.app.Application;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/01
 *     desc   : 截屏监听器
 * </pre>
 */
public class TioScreenShotMonitor extends ScreenShotMonitor {

    private static TioScreenShotMonitor mInstance = null;

    public static TioScreenShotMonitor getInstance() {
        if (mInstance == null) {
            mInstance = new TioScreenShotMonitor(Utils.getApp());
        }
        return mInstance;
    }

    private TioScreenShotMonitor(Application context) {
        super(context);
    }

    // 1、获取权限后才能使用
    public void requestPermissions(Runnable runnable) {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale((activity2, shouldRequest) -> shouldRequest.again(true))
                .callback((isAllGranted, granted, deniedForever, denied) -> {
                    if (isAllGranted) {
                        runnable.run();
                    } else if (!deniedForever.isEmpty()) {
                        ToastUtils.showShort("存储权限被禁用，请打开权限！");
                        PermissionUtils.launchAppDetailsSettings();
                    }
                })
                .request();
    }

}
