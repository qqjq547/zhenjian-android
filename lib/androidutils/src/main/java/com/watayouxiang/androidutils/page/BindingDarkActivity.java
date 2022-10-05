package com.watayouxiang.androidutils.page;

import androidx.databinding.ViewDataBinding;

import com.watayouxiang.androidutils.R;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/29
 *     desc   : 深色主题：默认背景色、蓝色状态栏
 * </pre>
 */
public abstract class BindingDarkActivity<T extends ViewDataBinding> extends BindingActivity<T> {
    @Override
    protected Boolean statusBar_lightMode() {
        return false;
    }

    @Override
    public Integer statusBar_color() {
        return getResources().getColor(R.color.blue_4c94ff);
    }
}
