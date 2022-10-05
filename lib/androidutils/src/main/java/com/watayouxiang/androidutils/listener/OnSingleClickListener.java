package com.watayouxiang.androidutils.listener;

import android.view.View;

import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/17
 *     desc   :
 * </pre>
 */
public abstract class OnSingleClickListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        boolean viewSingleClick = ClickUtils.isViewSingleClick(view);
        if (viewSingleClick) {
            onSingleClick(view);
        }
    }

    public abstract void onSingleClick(View view);
}
