package com.tiocloud.chat.widget.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.group.create.CreateGroupActivity;
import com.tiocloud.chat.feature.search.user.SearchUserActivity;
import com.tiocloud.chat.util.ScreenUtil;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.qrcode.feature.qrcode_decoder.QRCodeDecoderActivity;

/**
 * author : TaoWang
 * date : 2020-02-19
 * desc :
 */
public class HomePopupWindow extends BasePopupWindow {

    public HomePopupWindow(View anchor) {
        super(anchor);
    }

    @Override
    protected int getViewLayout() {
        return ModuleConfig.ENABLE_QR_CODE ? R.layout.tio_home_popup2 : R.layout.tio_home_popup;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.ll_create_group).setOnClickListener(view -> {
            CreateGroupActivity.start(getActivity());
            dismiss();
        });
        findViewById(R.id.ll_add_friend).setOnClickListener(view -> {
            SearchUserActivity.start(getActivity());
            dismiss();
        });
        if (ModuleConfig.ENABLE_QR_CODE) {
            findViewById(R.id.ll_qrcode_decoder).setOnClickListener(view -> {
                QRCodeDecoderActivity.start(getActivity());
                dismiss();
            });
        }
    }

    @Override
    public void show() {
        // show animator
        ObjectAnimator animator = ObjectAnimator.ofFloat(getAnchor(), "rotation", 0, 45);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
        // show window
        HomePopupWindow.super.showAsDropDown(getAnchor(), -ScreenUtil.dp2px(94), ScreenUtil.dp2px(8));
        // bg alpha
        bgAlpha(0.8f);
    }

    @Override
    public void dismiss() {
        // dismiss animator
        ObjectAnimator animator = ObjectAnimator.ofFloat(getAnchor(), "rotation", 45, 0);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
        // dismiss window
        HomePopupWindow.super.dismiss();
        // bg alpha
        bgAlpha(1.0f);
    }
}
