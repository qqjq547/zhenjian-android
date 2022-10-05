package com.watayouxiang.androidutils.widget.edittext;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ywl on 2016/7/10.
 * <p>
 * 修改自：https://github.com/wanliyang1990/PayPwdEditText
 */
public class PayPwdEditText extends RelativeLayout {

    private final Context context;
    private EditText editText; //文本编辑框
    private TextView[] textViewArr; //文本数组
    private OnTextFinishListener onTextFinishListener;

    public PayPwdEditText(Context context) {
        this(context, null);
    }

    public PayPwdEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PayPwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    // ====================================================================================
    // init
    // ====================================================================================

    /**
     * @param bgDrawable     背景drawable
     * @param splitLineWidth 分割线宽度
     * @param splitLineColor 分割线颜色
     * @param pwdLength      密码长度
     * @param pwdColor       密码字体颜色
     * @param pwdSize        密码字体大小
     */
    public void initStyle(int bgDrawable, float splitLineWidth, int splitLineColor, int pwdLength, int pwdColor, int pwdSize) {
        initEdit(bgDrawable, pwdLength);
        initShowInput(bgDrawable, pwdLength, splitLineWidth, splitLineColor, pwdColor, pwdSize);
    }

    private void initEdit(int bgDrawable, final int pwdLength) {
        editText = new EditText(context);
        editText.setBackgroundResource(bgDrawable);
        editText.setCursorVisible(false);
        editText.setTextSize(0);
        editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(pwdLength)});
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Editable editable = editText.getText();
                Selection.setSelection(editable, editable.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 根据输入字符，显示密码个数
                if (textViewArr != null) {
                    int txtLength = s.length();
                    int tvLength = textViewArr.length;
                    for (int i = 0; i < tvLength; i++) {
                        String ch = "";
                        if (i < txtLength) {
                            ch = String.valueOf(s.charAt(i));
                        }
                        textViewArr[i].setText(ch);
                    }
                }

                // 输入完成回调
                if (s.length() == pwdLength) {
                    if (onTextFinishListener != null) {
                        onTextFinishListener.onFinish(s.toString().trim());
                    }
                }
            }
        });
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        addView(editText, lp);

    }

    private void initShowInput(int bgDrawable, int pwdLength, float splitLineWidth, int splitLineColor, int pwdColor, int pwdSize) {
        //-------- 线性布局容器
        LinearLayout llContainer = new LinearLayout(context);
        llContainer.setBackgroundResource(bgDrawable);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        llContainer.setLayoutParams(layoutParams);
        llContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(llContainer);

        //-------- 初始化 "文本" 和 "分割线"
        // 显示文本参数
        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT);
        tvParams.weight = 1;
        tvParams.gravity = Gravity.CENTER;

        // 分割线参数
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(dip2px(context, splitLineWidth), LayoutParams.MATCH_PARENT);

        textViewArr = new TextView[pwdLength];

        for (int i = 0; i < textViewArr.length; i++) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(pwdSize);
            textView.setTextColor(pwdColor);
            textView.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_CLASS_NUMBER);

            textViewArr[i] = textView;
            llContainer.addView(textView, tvParams);

            if (i < textViewArr.length - 1) {
                View view = new View(context);
                view.setBackgroundColor(splitLineColor);
                llContainer.addView(view, lineParams);
            }
        }
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // ====================================================================================
    // public
    // ====================================================================================

    public void setShowPwd(boolean showPwd) {
        for (TextView textView : textViewArr) {
            if (showPwd) {
                textView.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                textView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }

    public void setInputType(int type) {
        for (TextView textView : textViewArr) {
            textView.setInputType(type);
        }
    }

    public void clearText() {
        editText.setText("");
        for (TextView textView : textViewArr) {
            textView.setText("");
        }
    }

    public void setOnTextFinishListener(OnTextFinishListener onTextFinishListener) {
        this.onTextFinishListener = onTextFinishListener;
    }

    public String getPwdText() {
        if (editText != null) {
            return editText.getText().toString().trim();
        }
        return "";
    }

    public void showKeyboard() {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                editText.requestFocus();
                editText.setFocusable(true);
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            }
        }, 100);
    }

    public interface OnTextFinishListener {
        void onFinish(String str);
    }

}
