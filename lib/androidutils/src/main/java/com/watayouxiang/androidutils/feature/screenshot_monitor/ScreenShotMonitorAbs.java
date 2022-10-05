package com.watayouxiang.androidutils.feature.screenshot_monitor;

import android.app.Application;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Images.Media;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/01
 *     desc   : 截屏监听器
 * </pre>
 */
public abstract class ScreenShotMonitorAbs implements ScreenShotContentObserver.ContentChangeCallback {

    // 内容观察者
    protected final ContentResolver mContentResolver;
    private final ScreenShotContentObserver mInternalFileContentObserver;
    private final ScreenShotContentObserver mExternalFileContentObserver;
    private final Uri[] mContentUris = {Media.INTERNAL_CONTENT_URI, Media.EXTERNAL_CONTENT_URI};
    // 截屏事件回调
    protected ScreenShotCallback mScreenShotCallback;
    // 上下文
    protected Application mContext;
    // 开始监听的时间
    protected long mStartListenTime;
    // 屏幕宽度
    protected final int mScreenWidth;

    public ScreenShotMonitorAbs(Application context) {
        mContext = context;

        // 屏幕宽度
        mScreenWidth = ScreenShotUtils.getScreenWidth(context);
        if (mScreenWidth == -1) {
            ScreenShotUtils.e("屏幕宽度获取失败");
        }

        // 内部外部媒体文件的监听
        mContentResolver = mContext.getContentResolver();
        Handler handler = new Handler(Looper.getMainLooper());
        mInternalFileContentObserver = new ScreenShotContentObserver(mContentUris[0], this, handler);
        mExternalFileContentObserver = new ScreenShotContentObserver(mContentUris[1], this, handler);
    }

    public void registerScreenShotListener() {
        // 记录开始监听的时间 算是一个图片是否是截屏的一个指标
        mStartListenTime = System.currentTimeMillis();

        // 注意 第二个boolean 参数 要设置为true 不然有些机型由于多媒体文件层级不同 导致变化监听不到 所以设置后代文件夹发生了文件改变也要进行通知
        mContentResolver.registerContentObserver(mContentUris[0], true, mInternalFileContentObserver);
        mContentResolver.registerContentObserver(mContentUris[1], true, mExternalFileContentObserver);
    }

    public void unregisterScreenShotListener() {
        mContentResolver.unregisterContentObserver(mInternalFileContentObserver);
        mContentResolver.unregisterContentObserver(mExternalFileContentObserver);
    }

    public void setScreenShotCallback(ScreenShotCallback screenShotCallback) {
        mScreenShotCallback = screenShotCallback;
    }

    @Override
    public void onContentChange(Uri contentUri) {
        // 获取截屏文件
        acquireScreenShotFile(contentUri);
    }

    protected abstract void acquireScreenShotFile(Uri contentUri);
}
