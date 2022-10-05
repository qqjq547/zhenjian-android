package com.watayouxiang.androidutils.widget.dialog.form;

import android.widget.EditText;
import android.widget.TextView;

import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.widget.dialog.TioDialog;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc : 表单弹窗
 */
public abstract class TioFormDialog extends TioDialog {

    @Override
    protected int getDialogContentId() {
        return R.layout.tio_form_dialog;
    }

    @Override
    protected void initDialogContentView() {
        super.initDialogContentView();
        TextView tv_title = findViewById(R.id.tv_title);
        EditText et_input = findViewById(R.id.et_input);
        TextView tv_negativeBtn = findViewById(R.id.tv_negativeBtn);
        TextView tv_positiveBtn = findViewById(R.id.tv_positiveBtn);

        initTitleView(tv_title);
        initInputView(et_input);
        initNegativeBtn(tv_negativeBtn);
        initPositiveBtn(tv_positiveBtn);
    }

    protected abstract void initPositiveBtn(TextView tv_positiveBtn);

    protected abstract void initNegativeBtn(TextView tv_negativeBtn);

    protected abstract void initInputView(EditText et_input);

    protected abstract void initTitleView(TextView tv_title);

}
