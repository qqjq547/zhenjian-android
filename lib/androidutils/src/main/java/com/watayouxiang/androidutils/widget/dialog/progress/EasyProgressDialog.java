package com.watayouxiang.androidutils.widget.dialog.progress;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/12
 *     desc   :
 * </pre>
 */
public class EasyProgressDialog extends TioProgressDialog {
    private final Builder builder;

    private EasyProgressDialog(Builder builder) {
        super(builder.message);
        this.builder = builder;
    }

    public void show() {
        super.show(builder.context, builder.cancelable, builder.canceledOnTouchOutside, builder.onCancelListener);
    }

    public static class Builder {
        /* 构造参数 */
        @Nullable
        private CharSequence message;

        /* 显示参数 */
        @NonNull
        private final Context context;
        private boolean cancelable = false;
        private boolean canceledOnTouchOutside = false;
        @Nullable
        private DialogInterface.OnCancelListener onCancelListener;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

        public Builder setMessage(@Nullable CharSequence message) {
            this.message = message;
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

        public Builder setOnCancelListener(@Nullable DialogInterface.OnCancelListener onCancelListener) {
            this.onCancelListener = onCancelListener;
            return this;
        }

        public EasyProgressDialog build() {
            return new EasyProgressDialog(this);
        }
    }
}
