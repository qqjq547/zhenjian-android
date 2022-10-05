package com.tiocloud.account.feature.register;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tiocloud.account.R;


public class CodeCountDownTimer extends CountDownTimer {
    private TextView bt;//声明一个Button
    public Context context;
    //构造方法里面添加Button
    public CodeCountDownTimer(Context context, long millisInFuture, long countDownInterval, TextView mButton) {
        super(millisInFuture, countDownInterval);
        this.bt = mButton;
        this.context=context;
    }

    @Override
    public void onTick(long l) {
        bt.setClickable(false);
        bt.setText(l / 1000 + "s后重试");//60s后显示变化的内容
        bt.setTextColor(ContextCompat.getColor(context, R.color.gray_999999));
        bt.setTextSize(18);
//        bt.setBackgroundResource(R.color.white);//点击后按钮的颜色
//        bt.setBackgroundResource(R.drawable.getcode_btn_bg);//点击后按钮的颜色


    }

    @Override
    public void onFinish() {
        bt.setText("重新获取");//60s显示的文字
        //设置可点击
        bt.setClickable(true);
        bt.setTextSize(18);
        bt.setTextColor(ContextCompat.getColor(context, R.color.gray_999999));
//        bt.setBackgroundResource(R.color.white);//60s过后按钮恢复的颜色
//        bt.setBackgroundResource(R.drawable.getcode_btn_bg);//点击后按钮的颜色

    }
}