package com.tiocloud.chat.feature.main.adapter;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Viewpager 页面切换动画，只支持3.0以上版本
 * <p/>
 * [-∞，-1]完全不可见
 * [-1,  0]从不可见到完全可见
 * [0,1]从完全可见到不可见
 * [1,∞]完全不可见
 * <p/>
 */
class FadeInOutPageTransformer implements ViewPager.PageTransformer {
    @SuppressLint("NewApi")
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1) {//页码完全不可见
            page.setAlpha(0);
        } else if (position < 0) {
            page.setAlpha(1 + position);
        } else if (position < 1) {
            page.setAlpha(1 - position);
        } else {
            page.setAlpha(0);
        }
    }
}
