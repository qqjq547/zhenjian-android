package com.watayouxiang.androidutils.widget.dialog2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.tools.TioLogger;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/11
 *     desc   :
 * </pre>
 */
public class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.tio_dialog);

        setCancelable(getBuilder().cancelable);
        setCanceledOnTouchOutside(getBuilder().canceledOnTouchOutside);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TioLogger.d(getClass().getSimpleName() + " - onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        TioLogger.d(getClass().getSimpleName() + " - onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        TioLogger.d(getClass().getSimpleName() + " - onStop");
    }

    @Override
    public void show() {
        super.show();

        setGravity(getBuilder().gravity);
        setWidth(getBuilder().width);
        setHeight(getBuilder().height);

        if (getBuilder().backgroundDrawable != null) {
            setBackgroundDrawable(getBuilder().backgroundDrawable);
        } else if (getBuilder().backgroundDrawableResId != null) {
            setBackgroundDrawableResource(getBuilder().backgroundDrawableResId);
        }

        if (getBuilder().animationsResId != null) {
            setAnimation(getBuilder().animationsResId);
        }
    }

    @NonNull
    public Builder getBuilder() {
        return new Builder();
    }

    private void setAnimation(@StyleRes int resId) {
        Window window = getWindow();
        if (window != null) {
            window.setWindowAnimations(resId);
        }
    }

    /**
     * {@link Gravity#BOTTOM}
     * {@link Gravity#TOP}
     * {@link Gravity#CENTER}
     */
    private void setGravity(int gravity) {
        Window window = getWindow();
        if (window != null) {
            window.setGravity(gravity);
        }
    }

    /**
     * {@link WindowManager.LayoutParams#WRAP_CONTENT}
     * {@link WindowManager.LayoutParams#MATCH_PARENT}
     */
    private void setWidth(int width) {
        Window window = getWindow();
        if (window != null) {
            // attributes
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = width;
            window.setAttributes(attributes);
        }
    }

    /**
     * {@link WindowManager.LayoutParams#WRAP_CONTENT}
     * {@link WindowManager.LayoutParams#MATCH_PARENT}
     */
    private void setHeight(int height) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = height;
            window.setAttributes(attributes);
        }
    }

    private void setBackgroundDrawable(Drawable backgroundDrawable) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(backgroundDrawable);
        }
    }

    private void setBackgroundDrawableResource(@DrawableRes int backgroundDrawableResId) {
        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(backgroundDrawableResId);
        }
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
        super.setOnCancelListener(listener);
    }

    /**
     * 一般返回都是null
     *
     * @deprecated
     */
    @Nullable
    public Activity getActivity() {
        Context context = getContext();
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    public static class Builder {
        /* 初始化参数 */
        private boolean cancelable = true;// 返回键可以取消
        private boolean canceledOnTouchOutside = false;// 点击弹窗外部可取消

        /* 显示参数 */
        /**
         * {@link Gravity#BOTTOM}
         * {@link Gravity#TOP}
         * {@link Gravity#CENTER}
         */
        private int gravity = Gravity.CENTER;
        /**
         * {@link WindowManager.LayoutParams#WRAP_CONTENT}
         * {@link WindowManager.LayoutParams#MATCH_PARENT}
         */
        private int width = WindowManager.LayoutParams.WRAP_CONTENT;
        /**
         * {@link WindowManager.LayoutParams#WRAP_CONTENT}
         * {@link WindowManager.LayoutParams#MATCH_PARENT}
         */
        private int height = WindowManager.LayoutParams.WRAP_CONTENT;
        /**
         * {@link R.style.tio_dialog_anim}
         * {@link R.style.tio_bottom_dialog_anim}
         */
        @Nullable
        private Integer animationsResId = null;

        private Drawable backgroundDrawable = null;
        @DrawableRes
        private Integer backgroundDrawableResId = null;

        public Builder() {
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setAnimationsResId(@Nullable Integer animationsResId) {
            this.animationsResId = animationsResId;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder setBackgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder setBackgroundDrawableResId(Integer backgroundDrawableResId) {
            this.backgroundDrawableResId = backgroundDrawableResId;
            return this;
        }
    }

}
