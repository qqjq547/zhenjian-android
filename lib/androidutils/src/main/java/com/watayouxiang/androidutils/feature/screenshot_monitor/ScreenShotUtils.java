package com.watayouxiang.androidutils.feature.screenshot_monitor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ScreenShotUtils {

    // ====================================================================================
    // private
    // ====================================================================================

    private static final String TAG = "ScreenShotMonitor";

    static void e(String msg) {
        Log.e(TAG, msg);
    }

    static void d(String msg) {
        Log.d(TAG, msg);
    }

    static int getScreenWidth(Context context) {
        try {
            Point point = new Point();
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            defaultDisplay.getRealSize(point);
            return point.x;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ====================================================================================
    // public
    // ====================================================================================

    public static Bitmap compressBitmap(String imagePath, long sizeLimit) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        return compressBitmap(bitmap, sizeLimit);
    }

    public static Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);

        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().length / 1024 > sizeLimit) {
            // 清空baos
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }

        return BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
    }
}
