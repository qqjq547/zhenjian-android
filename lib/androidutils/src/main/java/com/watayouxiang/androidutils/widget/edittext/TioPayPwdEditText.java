package com.watayouxiang.androidutils.widget.edittext;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.watayouxiang.androidutils.R;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/05
 *     desc   :
 * </pre>
 */
public class TioPayPwdEditText extends PayPwdEditText {
    public TioPayPwdEditText(Context context) {
        super(context);
        init(context);
    }

    public TioPayPwdEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TioPayPwdEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Object context) {
        initStyle(R.drawable.androidutils_pay_pwd_et_bg,
                1f, Color.parseColor("#D8D8D8"),
                6, Color.parseColor("#333333"), 24);
        showKeyboard();
    }
}
