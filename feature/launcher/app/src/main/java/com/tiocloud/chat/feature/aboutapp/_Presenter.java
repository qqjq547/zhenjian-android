package com.tiocloud.chat.feature.aboutapp;

import android.content.Context;
import android.content.pm.PackageManager;

import com.tiocloud.chat.util.AppUpdateTool;
import com.watayouxiang.androidutils.widget.TioToast;

class _Presenter extends _Contract.Presenter {
    private AppUpdateTool appUpdateTool;

    public _Presenter(_Contract.View view) {
        super(new _Model(), view, false);
    }

    @Override
    public void checkAppUpdate() {
        if (appUpdateTool == null) {
            appUpdateTool = new AppUpdateTool(getView().getActivity());
            appUpdateTool.setOnCheckUpdateListener(resp -> {
                if (resp.getUpdateflag() == 2) {
                    TioToast.showShort("当前已是最新版本");
                }
            });
        }
        appUpdateTool.checkUpdateNormal();
    }

    @Override
    public String getOutApkTime(Context context) {
        try {
            return context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString("OUT_APK_TIME");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (appUpdateTool != null) {
            appUpdateTool.release();
        }
    }
}
