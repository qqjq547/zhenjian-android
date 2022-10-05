package com.watayouxiang.androidutils.feature.screenshot_monitor;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/01
 *     desc   : 截屏监听器
 * </pre>
 */
public class ScreenShotMonitor extends ScreenShotMonitorAbs {

    // sql
    private final static String QUERY_ORDER_SQL = MediaStore.Images.ImageColumns.DATE_ADDED + " DESC LIMIT 1";
    // 读取媒体数据库时需要读取的列
    private static final String[] MEDIA_STORE_IMAGE = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.WIDTH
    };
    // 截屏关键词
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap", "Screenshot", "截屏"
    };
    // 上次截屏 path
    private String mLastScreenShotPath = null;

    public ScreenShotMonitor(Application context) {
        super(context);
    }

    @Override
    protected void acquireScreenShotFile(Uri contentUri) {
        Cursor cursor = null;

        try {
            cursor = mContentResolver.query(
                    contentUri,
                    MEDIA_STORE_IMAGE,
                    null,
                    null,
                    QUERY_ORDER_SQL
            );
            findScreenShotPathByCursor(cursor);
        } catch (Exception e) {
            if (e.getMessage() != null) {
                ScreenShotUtils.e(e.getMessage());
            } else {
                e.printStackTrace();
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private void findScreenShotPathByCursor(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        if (!cursor.moveToFirst()) {
            ScreenShotUtils.d("Cannot find newest image file");
            return;
        }

        // 获取 文件索引
        int imageColumn_data = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        int imageColumn_width = cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);

        String imagePath = cursor.getString(imageColumn_data);
        int imageWidth = cursor.getInt(imageColumn_width);

        // 这里只判断width，否则长截屏无法判断
        if (mScreenWidth != imageWidth) {
            return;
        }
        // imagePath 不能为空
        if (TextUtils.isEmpty(imagePath)) {
            return;
        }
        // 根据 "截屏关键词" 判断是否为 "截屏"
        String screenShotPath = null;
        String imagePathLowerCase = imagePath.toLowerCase();
        for (String keyword : KEYWORDS) {
            if (imagePathLowerCase.contains(keyword)) {
                screenShotPath = imagePath;
                break;
            }
        }
        // 不是 "截屏"
        if (screenShotPath == null) return;
        // 防止重复文件，多次回调
        if (screenShotPath.equals(mLastScreenShotPath)) {
            return;
        }
        mLastScreenShotPath = screenShotPath;

        // 信息回调
        ScreenShotUtils.d("screenShotPath: " + screenShotPath);
        if (mScreenShotCallback != null) {
            mScreenShotCallback.onScreenShot(screenShotPath);
        }
    }
}
