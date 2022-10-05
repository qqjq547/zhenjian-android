package com.watayouxiang.androidutils.page;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.watayouxiang.androidutils.mvp.BasePresenter;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/28
 *     desc   :
 * </pre>
 */
public abstract class MvpActivity<P extends BasePresenter<?, ?>, T extends ViewDataBinding> extends BindingActivity<T> {

    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // mvp
        presenter = newPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    // ====================================================================================
    // mvp
    // ====================================================================================

    public abstract P newPresenter();

    @NonNull
    public P getPresenter() {
        return presenter;
    }
}
