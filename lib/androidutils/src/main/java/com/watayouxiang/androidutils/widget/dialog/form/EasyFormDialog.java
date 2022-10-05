package com.watayouxiang.androidutils.widget.dialog.form;

import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2/8/21
 *     desc   : 表单弹窗
 * </pre>
 */
public class EasyFormDialog extends TioFormDialog {
    private final CharSequence titleTxt;
    private final String positiveBtnTxt;
    private final String negativeBtnTxt;
    private final OnBtnListener onBtnListener;
    private final int minLine;
    private final String hint;
    private final Integer maxLength;

    // 仅仅在show()之后才有值
    private EditText et_input;

    private EasyFormDialog(CharSequence titleTxt,
                           String positiveBtnTxt,
                           String negativeBtnTxt,
                           OnBtnListener onBtnListener,
                           int minLine,
                           String hint,
                           Integer maxLength) {
        this.titleTxt = titleTxt;
        this.positiveBtnTxt = positiveBtnTxt;
        this.negativeBtnTxt = negativeBtnTxt;
        this.onBtnListener = onBtnListener;
        this.minLine = minLine;
        this.hint = hint;
        this.maxLength = maxLength;
    }

    @Override
    protected void initPositiveBtn(final TextView tv_positiveBtn) {
        tv_positiveBtn.setText(positiveBtnTxt);
        tv_positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnListener != null)
                    onBtnListener.onClickPositive(tv_positiveBtn, EasyFormDialog.this, et_input.getText().toString());
            }
        });
    }

    @Override
    protected void initNegativeBtn(final TextView tv_negativeBtn) {
        tv_negativeBtn.setText(negativeBtnTxt);
        tv_negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnListener != null)
                    onBtnListener.onClickNegative(tv_negativeBtn, EasyFormDialog.this);
            }
        });
    }

    @Override
    protected void initInputView(EditText et_input) {
        this.et_input = et_input;
        et_input.setMinLines(minLine);
        et_input.setHint(hint);
        et_input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    @Override
    protected void initTitleView(TextView tv_title) {
        tv_title.setText(titleTxt);
    }

    public interface OnBtnListener {
        void onClickPositive(View view, EasyFormDialog dialog, String inputTxt);

        void onClickNegative(View view, EasyFormDialog dialog);
    }

    public static class Builder {
        private final CharSequence titleTxt;
        private String positiveBtnTxt = "确定";
        private String negativeBtnTxt = "取消";
        private OnBtnListener onBtnListener;
        private int minLine = 3;
        private String hint = "";
        private Integer maxLength = null;

        public Builder(CharSequence titleTxt) {
            this.titleTxt = titleTxt;
        }

        public Builder setPositiveBtnTxt(String positiveBtnTxt) {
            this.positiveBtnTxt = positiveBtnTxt;
            return this;
        }

        public Builder setNegativeBtnTxt(String negativeBtnTxt) {
            this.negativeBtnTxt = negativeBtnTxt;
            return this;
        }

        public Builder setOnBtnListener(OnBtnListener onBtnListener) {
            this.onBtnListener = onBtnListener;
            return this;
        }

        public Builder setMinLine(int minLine) {
            this.minLine = minLine;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public EasyFormDialog build() {
            return new EasyFormDialog(
                    titleTxt,
                    positiveBtnTxt,
                    negativeBtnTxt,
                    onBtnListener,
                    minLine,
                    hint,
                    maxLength);
        }
    }
}
