package com.tiocloud.chat.feature.splash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.tiocloud.account.feature.login.LoginActivity;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.OssDataJsonBean;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.mvp.launcher.LauncherContract;
import com.tiocloud.chat.mvp.launcher.LauncherPresenter;
import com.tiocloud.chat.util.AESEncrypt;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.preferences.HttpPreferences;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import static android.graphics.Bitmap.createBitmap;

/**
 * author : TaoWang
 * date : 2019-12-31
 * desc : 欢迎页面
 */
public class SplashActivity extends TioActivity implements LauncherContract.View {

    @Nullable
    private LauncherPresenter presenter;
    private static final byte keyNum = 99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 问题：用android的installer安装打开闪屏页，按Home键回到首页，然后点击launcher的图标会再打开一个闪屏页。
        // 原因：installer安装方式打开闪屏页，系统会创建了新的Task中用于存放闪屏页实例，从而导致重复启动闪屏页面。
        // 解决办法：避免从桌面启动程序后，会重新实例化入口类的activity。
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) > 0) {
            finish();
            return;
        }
        setContentView(R.layout.tio_activity_welcome);

        presenter = new LauncherPresenter(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                presenter.init();
            }
        }).start();

        test();
    }
    public  void test() {
        String data="1234567890123456789012345678901234567891234567890";
        String fingerprint= "17f036040c6c9e39ea661f41c9f988b2";
//        String data="I/GOlKj4EUAEIX5/qwK09QX+uqQBOdZScLhCZP0FmYJkCgwqT6g3rcewieF7fDZHcuYJiNHQvg9NmVrfvEq9oQ==";
//        String fingerprint= "17f036040c6c9e39ea661f41c9f988b2";
//        String content=decrypt(data,fingerprint);
        try {
            String content= AESEncrypt.encrypt(data,fingerprint);
            Log.d("hjq",content);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
    @Override
    public void openLoginPage() {
        LoginActivity.start(this);
    }

    @Override
    public void openMainPage() {
        MainActivity.start(this);
    }
}
