package com.tiocloud.chat.widget.dialog.tio;

import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.watayouxiang.androidutils.widget.dialog.form.TioFormDialog;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class CreateGroupDialog extends TioFormDialog {
    private final OnBtnListener onBtnListener;
    private TextView et_input;

    public CreateGroupDialog(OnBtnListener onBtnListener) {
        this.onBtnListener = onBtnListener;
    }

    @Override
    protected void initPositiveBtn(TextView tv_positiveBtn) {
        tv_positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnListener.onClickPositive(v, et_input.getText().toString(), CreateGroupDialog.this);
            }
        });
    }

    @Override
    protected void initNegativeBtn(TextView tv_negativeBtn) {
        tv_negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnListener.onClickNegative(v, CreateGroupDialog.this);
            }
        });
    }

    @Override
    protected void initInputView(EditText et_input) {
        this.et_input = et_input;
        et_input.setHint("设置名称 (非必填, 30字内)");
        et_input.setMinLines(1);
        et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
        et_input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    }

    @Override
    protected void initTitleView(TextView tv_title) {
        tv_title.setText("群聊名称");
    }

    public abstract static class OnBtnListener {
        public abstract void onClickPositive(View view, String submitTxt, CreateGroupDialog dialog);

        public void onClickNegative(View view, CreateGroupDialog dialog) {
            dialog.dismiss();
        }
    }
}
