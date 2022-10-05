package com.watayouxiang.androidutils.page;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.watayouxiang.androidutils.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * author : TaoWang
 * date : 2020-02-06
 * desc : Activity 栈管理
 */
public abstract class TioActivity extends BaseActivity {

    @Override
    public TioActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);

        // 设置window背景色
        Integer backgroundColor = background_color();
        if (backgroundColor != null) {
            setBackgroundColor(backgroundColor);
        }

    }

    // ====================================================================================
    // background
    // ====================================================================================

    @ColorInt
    protected Integer background_color() {
        return Color.parseColor("#F4F5F6");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
    /**
     * 解密图片
     */
    public static void decrypt(File inFile, File outFile, int keyNum) {
        encrypt(inFile, outFile, keyNum);
    }

    /**
     * 加密图片
     */
    public static void encrypt(File inFile, File outFile, int keyNum) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(inFile));
            bos = new BufferedOutputStream(new FileOutputStream(outFile));
            int b;
            while ((b = bis.read()) != -1) {
                bos.write(b ^ keyNum);
            }
        } catch (IOException e) {
            Log.e("=====图片加密错误!==", e.getLocalizedMessage());
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                Log.e("=====图片加密关闭资源错误!==", e.getLocalizedMessage());

            }
        }
    }

}
