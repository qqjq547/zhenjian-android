package com.tiocloud.account.dialognew;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.tiocloud.account.R;
import com.tiocloud.account.ScreenUtil;


public class SexSelectDialog extends Dialog {

    OnBtnListener onBtnListener;

    private TextView sex1,sex2,sex3;

    public SexSelectDialog(@NonNull Context context) {
        super(context, R.style.apply_friend_dialog);
        setContentView(R.layout.sex_select_dialog_new);

        sex1 = findViewById(R.id.sex1);
        sex2 = findViewById(R.id.sex2);
        sex3 = findViewById(R.id.sex3);

        findViewById(R.id.cancelBTn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnListener != null){
                    onBtnListener.onClickNegative(findViewById(R.id.iv_back), SexSelectDialog.this);
                }
            }
        });

        sex1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnListener != null){
                    onBtnListener.onClickPositive(v, 1, SexSelectDialog.this);
                }
            }
        });

        sex2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnListener != null){
                    onBtnListener.onClickPositive(v, 2, SexSelectDialog.this);
                }
            }
        });

        sex3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBtnListener != null){
                    onBtnListener.onClickPositive(v, 3, SexSelectDialog.this);
                }
            }
        });

        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams p = window.getAttributes();
        p.height = (int) (/*ScreenUtils.getScreenHeight() * 0.45*/ScreenUtil.dp2px(335));
        p.width = ScreenUtils.getScreenWidth();
        window.setAttributes(p);
        window.setWindowAnimations(R.style.main_menu_animStyle);
        this.onBtnListener = onBtnListener;

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onBtnListener != null){
                    onBtnListener.onClickNegative(null, SexSelectDialog.this);
                }
            }
        });
    }

    public SexSelectDialog setOnBtnListener(OnBtnListener onBtnListener){
        this.onBtnListener = onBtnListener;
        return this;
    }


    public interface OnBtnListener {
        void onClickPositive(View view, int sex, SexSelectDialog dialog);
        void onClickNegative(View view, SexSelectDialog dialog);
    }
}
