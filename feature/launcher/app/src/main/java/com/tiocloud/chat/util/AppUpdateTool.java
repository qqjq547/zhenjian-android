package com.tiocloud.chat.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.AppUtils;
import com.tiocloud.chat.R;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.appupdate.UpdateManager;
import com.watayouxiang.appupdate.entity.AppUpdate;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.SysVersionReq;
import com.watayouxiang.httpclient.model.response.SysVersionResp;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/18
 *     desc   :
 * </pre>
 */
public class AppUpdateTool {

    private FragmentActivity activity;
    // 是否为启动检测更新
    private static boolean CHECK_UPDATE_ON_LAUNCHER = false;
    // 普通检测更新次数
    private static int CHECK_UPDATE_NORMAL_COUNT = 0;

    public AppUpdateTool(FragmentActivity activity) {
        this.activity = activity;
    }

    public void checkUpdateNormal() {
        CHECK_UPDATE_NORMAL_COUNT++;
        if (CHECK_UPDATE_ON_LAUNCHER) {
            if (CHECK_UPDATE_NORMAL_COUNT == 1) {
                return;
            }
        }
        checkUpdateInternal();
    }

    public void checkUpdateOnLauncher() {
        CHECK_UPDATE_ON_LAUNCHER = true;
        checkUpdateInternal();
    }

    /**
     * 检测更新
     */
    private void checkUpdateInternal() {
        new SysVersionReq(AppUtils.getAppVersionName()).setCancelTag(AppUpdateTool.this).get(new TioCallback<SysVersionResp>() {
            @Override
            public void onTioSuccess(SysVersionResp sysVersionResp) {
                if (onCheckUpdateListener != null) {
                    onCheckUpdateListener.onCheckUpdateSuccess(sysVersionResp);
                }
                if (sysVersionResp.getUpdateflag() == 1) {
                    startUpdate(sysVersionResp);
                } else {
                    // 不需要更新
                    if (onNextStepListener != null) {
                        onNextStepListener.onNextStep();
                    }
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
                // 检测更新失败
                if (onNextStepListener != null) {
                    onNextStepListener.onNextStep();
                }
            }
        });
    }

    /**
     * 开始更新流程
     */
    private void startUpdate(SysVersionResp resp) {
        AppUpdate appUpdate;
        if (ModuleConfig.ENABLE_MANUAL_UPDATE) {
            appUpdate = new AppUpdate.Builder(HttpCache.getResUrl(resp.getUrl()))
                    .updateResourceId(R.layout.tio_dialog_update2)
                    .forceUpdate(resp.getForceflag() == 1)
                    .updateTitle(String.format(Locale.getDefault(), "发现新版本v%s", resp.getVersion()))
                    .updateInfo(resp.getContent())
                    .manualUpdateUrl("https://www.tiocloud.com/2/h5down.html")
                    .build();
        } else {
            appUpdate = new AppUpdate.Builder(HttpCache.getResUrl(resp.getUrl()))
                    .updateResourceId(R.layout.tio_dialog_update)
                    .forceUpdate(resp.getForceflag() == 1)
                    .updateTitle(String.format(Locale.getDefault(), "发现新版本v%s", resp.getVersion()))
                    .updateInfo(resp.getContent())
                    .build();
        }


        new UpdateManager(activity, appUpdate) {
            @Override
            public void onUpdateToast(@NonNull String msg) {
                super.onUpdateToast(msg);
                TioToast.showShort(msg);
            }

            @Override
            public void onClickCancelBtn() {
                super.onClickCancelBtn();
                // 取消更新
                if (onNextStepListener != null) {
                    onNextStepListener.onNextStep();
                }
            }
        }.startUpdate();
    }

    /**
     * 释放资源
     */
    public void release() {
        TioHttpClient.cancel(AppUpdateTool.this);
        activity = null;
    }

    // ====================================================================================
    // 检测更新监听
    // ====================================================================================

    private OnCheckUpdateListener onCheckUpdateListener = null;

    public void setOnCheckUpdateListener(OnCheckUpdateListener listener) {
        this.onCheckUpdateListener = listener;
    }

    public interface OnCheckUpdateListener {
        void onCheckUpdateSuccess(SysVersionResp resp);
    }

    // ====================================================================================
    // 下一步监听（检测更新失败/取消更新 时调用）
    // ====================================================================================

    private OnNextStepListener onNextStepListener = null;

    public void setOnNextStepListener(OnNextStepListener listener) {
        onNextStepListener = listener;
    }

    public interface OnNextStepListener {
        void onNextStep();
    }
}
