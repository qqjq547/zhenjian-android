package com.tiocloud.chat.widget.dialog.tio;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.watayouxiang.androidutils.listener.OnSimpleCheckedChangeListener;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/17
 *     desc   :
 * </pre>
 */
public class DeleteSessionDialog extends BaseOperCheckDialog {
    private final OnClickListener onClickListener;
    private boolean isCheck = false;

    public DeleteSessionDialog(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    protected void initTitleView(TextView tv_title) {
        tv_title.setText("确定删除会话吗？");
    }

    @Override
    protected void initCheckBox(CheckBox checkBox) {
        checkBox.setText("同时删除聊天记录");
        checkBox.setClickable(true);
        checkBox.setChecked(isCheck);
        checkBox.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                DeleteSessionDialog.this.isCheck = isChecked;
            }
        });
    }

    @Override
    protected void initPositiveBtn(TextView tv_positiveBtn) {
        tv_positiveBtn.setText("确定");
        tv_positiveBtn.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onClickPositiveBtn(v, DeleteSessionDialog.this, isCheck);
            }
        });
    }

    @Override
    protected void initNegativeBtn(TextView tv_negativeBtn) {
        tv_negativeBtn.setText("取消");
        tv_negativeBtn.setOnClickListener(v -> dismiss());
    }

    public interface OnClickListener {
        void onClickPositiveBtn(View view, DeleteSessionDialog dialog, boolean isCheck);
    }
}
