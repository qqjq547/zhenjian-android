package com.watayouxiang.androidutils.widget;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.ToastUtils;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc :
 */
public class TioToast {

    public static void showShort(@Nullable CharSequence msg) {
        ToastUtils.showShort(msg);
    }

    public static void showShort(@StringRes final int resId) {
        ToastUtils.showShort(resId);
    }

    public static void showLong(@Nullable CharSequence msg) {
        ToastUtils.showLong(msg);
    }

    public static void showLong(@StringRes final int resId) {
        ToastUtils.showLong(resId);
    }

}
