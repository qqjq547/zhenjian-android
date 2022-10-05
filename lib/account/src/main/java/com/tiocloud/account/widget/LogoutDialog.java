package com.tiocloud.account.widget;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.watayouxiang.androidutils.widget.dialog.oper.TioOperDialog;

/**
 * author : TaoWang
 * date : 2020-02-25
 * desc :
 */
public class LogoutDialog extends TioOperDialog {
    private final OnBtnListener onBtnListener;

    public LogoutDialog(OnBtnListener btnListener) {
        this.onBtnListener = btnListener;
    }

    @Override
    protected void initPositiveBtn(TextView tv_positiveBtn) {
        tv_positiveBtn.setText("退出");
        tv_positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnListener.onClickPositive(v, LogoutDialog.this);
            }
        });
    }

    @Override
    protected void initNegativeBtn(TextView tv_negativeBtn) {
        tv_negativeBtn.setText("取消");
        tv_negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnListener.onClickNegative(v, LogoutDialog.this);
            }
        });
    }

    @Override
    protected void initTitleView(TextView tv_title) {
        tv_title.setGravity(Gravity.CENTER);
        tv_title.setText("确定退出当前账号？");
    }

    public interface OnBtnListener {
        void onClickPositive(View view, LogoutDialog dialog);

        void onClickNegative(View view, LogoutDialog dialog);
    }
}
