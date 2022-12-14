package com.tiocloud.chat;

import android.os.Handler;

import androidx.multidex.MultiDexApplication;

import com.fm.openinstall.OpenInstall;

public class TioApplication extends MultiDexApplication {

    public static TioApplication sApplication;

    public int chatmode;

    public static TioApplication getInstanceKit() {
        return sApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication=this;
        AppLauncher.getInstance().init(this);
        OpenInstall.init(this);
    }

}
