package com.watayouxiang.androidutils.feature.screenshot_monitor;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/01
 *     desc   : 截屏内容观察者
 * </pre>
 */
class ScreenShotContentObserver extends ContentObserver {

    private final Uri mContentUri;
    private final ContentChangeCallback mCaptureCallback;

    public ScreenShotContentObserver(Uri contentUri, ContentChangeCallback changeCallback, Handler handler) {
        super(handler);
        mCaptureCallback = changeCallback;
        mContentUri = contentUri;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        // 触发了截屏 注意这里会多次回调 要优化下
        if (mCaptureCallback != null) {
            mCaptureCallback.onContentChange(mContentUri);
        }
    }

    public interface ContentChangeCallback {
        void onContentChange(Uri contentUri);
    }
}