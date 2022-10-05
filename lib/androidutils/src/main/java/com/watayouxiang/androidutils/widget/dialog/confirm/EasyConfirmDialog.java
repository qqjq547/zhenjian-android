package com.watayouxiang.androidutils.widget.dialog.confirm;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020-03-08
 *     desc   :
 * </pre>
 */
public class EasyConfirmDialog extends TioConfirmDialog {

    private final Builder builder;

    private EasyConfirmDialog(Builder builder) {
        super(builder.message, builder.messageGravity, builder.onConfirmListener);
        this.builder = builder;
    }

    public void show() {
        super.show(builder.context, builder.cancelable, builder.canceledOnTouchOutside, builder.onCancelListener);
    }

    public static class Builder {
        /* 构造参数 */
        @Nullable
        private CharSequence message;
        @Nullable
        private OnConfirmListener onConfirmListener = null;
        private int messageGravity = Gravity.CENTER;

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

        public Builder setOnConfirmListener(@Nullable OnConfirmListener onConfirmListener) {
            this.onConfirmListener = onConfirmListener;
            return this;
        }

        public Builder setMessageGravity(int messageGravity) {
            this.messageGravity = messageGravity;
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

        public EasyConfirmDialog build() {
            return new EasyConfirmDialog(this);
        }
    }
}
