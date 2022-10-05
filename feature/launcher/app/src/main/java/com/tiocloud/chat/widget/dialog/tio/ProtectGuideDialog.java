package com.tiocloud.chat.widget.dialog.tio;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.tiocloud.chat.R;
import com.watayouxiang.androidutils.feature.browser.TioBrowserActivity;
import com.tiocloud.account.TioWebUrl;
import com.tiocloud.chat.preferences.TioPreferences;
import com.watayouxiang.androidutils.widget.dialog.TioDialog;
import com.watayouxiang.httpclient.preferences.HttpCache;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/13
 *     desc   :
 * </pre>
 */
public class ProtectGuideDialog extends TioDialog {
    @NonNull
    private Activity mContext;
    @NonNull
    private OnConfirmListener onConfirmListener;
    private boolean agreeProtectGuide = TioPreferences.getAgreeProtectGuide();

    public ProtectGuideDialog(@NonNull Activity context, @NonNull OnConfirmListener onConfirmListener) {
        this.mContext = context;
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    protected int getDialogContentId() {
        return R.layout.tio_dialog_protect_guide;
    }

    @Override
    protected void initDialogContentView() {
        super.initDialogContentView();
        View tv_userProtocol = findViewById(R.id.tv_userProtocol);
        tv_userProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = HttpCache.getBaseUrl() + TioWebUrl.TIO_USER_PROTOCOL;
                TioBrowserActivity.start(mContext, url);
            }
        });

        View tv_privatePolicy = findViewById(R.id.tv_privatePolicy);
        tv_privatePolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = HttpCache.getBaseUrl() + TioWebUrl.TIO_PRIVATE_POLICY;
                TioBrowserActivity.start(mContext, url);
            }
        });

        View tv_negativeBtn = findViewById(R.id.tv_negativeBtn);
        tv_negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 存储状态
                TioPreferences.saveAgreeProtectGuide(false);
                // 关闭页面
                mContext.finish();
                // 关闭弹窗
                dismiss();
            }
        });

        View tv_positiveBtn = findViewById(R.id.tv_positiveBtn);
        tv_positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 存储状态
                TioPreferences.saveAgreeProtectGuide(true);
                // 关闭弹窗
                dismiss();
                // 回调
                onConfirmListener.onConfirm();
            }
        });
    }

    /**
     * 检测是否同意
     */
    public void checkConfirm() {
        // 没有同意才显示
        if (!agreeProtectGuide) {
            super.show_unCancel(mContext);
        } else {
            onConfirmListener.onConfirm();
        }
    }

    public interface OnConfirmListener {
        /**
         * 同意回调
         */
        void onConfirm();
    }
}
