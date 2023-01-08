package com.watayouxiang.androidutils.widget.dialog.progress;

import android.content.Context;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ThreadUtils;

import java.lang.ref.WeakReference;

/**
 * author : TaoWang
 * date : 2020-01-06
 * desc : 单例tio全屏进度弹窗
 */
public class SingletonProgressDialog {
    private static WeakReference<TioProgressDialog> mDialogRef;

    private static TioProgressDialog getDialog() {
        return mDialogRef == null ? null : mDialogRef.get();
    }

    public static void dismiss() {
        TioProgressDialog dialog = getDialog();
        if (dialog != null) {
            try {
                dialog.dismiss();
            }catch (Exception e){

            }
            mDialogRef.clear();
        }
    }

    public static void show(@NonNull Context context, @Nullable String message,
                            boolean cancelable, boolean canceledOnTouchOutside,
                            @Nullable final DialogInterface.OnCancelListener onCancelListener,
                            boolean equalContextShow) {

        if (!ThreadUtils.isMainThread()) {
            ThreadUtils.runOnUiThread(() -> show(context, message, cancelable, canceledOnTouchOutside, onCancelListener, equalContextShow));
            return;
        }

        TioProgressDialog dialog = getDialog();

        if (dialog != null) {
            if (dialog.getContext() != context) {
                dismiss();
                dialog = null;
            } else {
                if (equalContextShow) {
                    dismiss();
                    dialog = null;
                }
            }
        }

        if (dialog == null) {
            dialog = new TioProgressDialog(message);
            mDialogRef = new WeakReference<>(dialog);
            try {
                dialog.show(context, cancelable, canceledOnTouchOutside, onCancelListener);
            }catch (Exception e){

            }
        }
    }

    public static void show_unCancel(Context context, String message) {
        show(context, message, false, false, null, false);
    }

    public static void show_cancelable(Context context, String message) {
        show(context, message, true, false, null, false);
    }

    public static void show_canceledOnTouchOutside(Context context, String message) {
        show(context, message, true, true, null, false);
    }
}
