package com.watayouxiang.androidutils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/05
 *     desc   :
 * </pre>
 */
public class NoScrollViewPager extends ViewPager {
    private boolean enableScroll = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void enableScroll(boolean enableScroll) {
        this.enableScroll = enableScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (!enableScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!enableScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
}
