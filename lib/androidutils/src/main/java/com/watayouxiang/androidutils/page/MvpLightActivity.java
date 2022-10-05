package com.watayouxiang.androidutils.page;

import android.graphics.Color;

import androidx.databinding.ViewDataBinding;

import com.watayouxiang.androidutils.mvp.BasePresenter;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/29
 *     desc   : 浅色主题：默认背景色、白色状态栏
 * </pre>
 */
public abstract class MvpLightActivity<P extends BasePresenter<?, ?>, T extends ViewDataBinding> extends MvpActivity<P, T> {
    @Override
    protected Boolean statusBar_lightMode() {
        return true;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.WHITE;
    }
}
