package com.tiocloud.chat.feature.forbidden;

import android.content.Context;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/05
 *     desc   :
 * </pre>
 */
public class SilentTimeSingleChoiceDialog {
    private final QMUIDialog qmuiDialog;
    private OnDialogCallback onDialogCallback;

    public SilentTimeSingleChoiceDialog(Context context) {
        String[] items = new String[]{"10分钟", "1小时", "24小时", "长期禁言"};

        qmuiDialog = new QMUIDialog.CheckableDialogBuilder(context)
                .setSkinManager(QMUISkinManager.defaultInstance(context))
                .addItems(items, (dialog, which) -> {
                    if (onDialogCallback != null) {
                        onDialogCallback.onClickItem(getSecondTime(which), SilentTimeSingleChoiceDialog.this);
                    }
                })
                .setTitle("禁言时长")
                //.setCheckedIndex(0)
                //.addAction("取消", (dialog, index) -> dialog.dismiss())
                .create(com.qmuiteam.qmui.R.style.QMUI_Dialog);
    }

    private long getSecondTime(int which) {
        switch (which) {
            case 0:
                return 10 * 60;
            case 1:
                return 60 * 60;
            case 2:
                return 24 * 60 * 60;
            case 3:
                return Long.MAX_VALUE;
        }
        return -1;
    }

    public void dismiss() {
        qmuiDialog.dismiss();
    }

    public void show() {
        qmuiDialog.show();
    }

    public SilentTimeSingleChoiceDialog setOnDialogCallback(OnDialogCallback callback) {
        this.onDialogCallback = callback;
        return this;
    }

    public interface OnDialogCallback {
        void onClickItem(long secondTime, SilentTimeSingleChoiceDialog dialog);
    }
}
