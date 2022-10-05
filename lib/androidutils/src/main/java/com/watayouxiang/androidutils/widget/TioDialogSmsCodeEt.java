package com.watayouxiang.androidutils.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ColorUtils;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.databinding.AndroidutilsDialogSmscodeEtBinding;
import com.watayouxiang.androidutils.tools.WtTimer;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/12
 *     desc   :
 * </pre>
 */
public class TioDialogSmsCodeEt extends RelativeLayout {

    private WtTimer wtTimer;
    private final AndroidutilsDialogSmscodeEtBinding binding;

    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);

    public TioDialogSmsCodeEt(Context context) {
        this(context, null);
    }

    public TioDialogSmsCodeEt(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TioDialogSmsCodeEt(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.androidutils_dialog_smscode_et, this, true);
    }

    public AndroidutilsDialogSmscodeEtBinding getBinding() {
        return binding;
    }

    public void startCodeTimer(int initTime) {
        if (wtTimer == null) {
            wtTimer = new WtTimer();
        }
        wtTimer.start((count, timer) -> {
            if (count < initTime) {
                onCodeTimerRunning(initTime - count);
            } else {
                timer.stop();
                onCodeTimerStop();
            }
        }, true, 0, 1000);
    }

    private void onCodeTimerRunning(int second) {
        isStartTimer.set(true);
        binding.tvReqCode.setEnabled(false);
        binding.tvReqCode.setTextColor(ColorUtils.getColor(R.color.gray_cccccc));
        binding.tvReqCode.setText(String.format(Locale.getDefault(), "已发送(%ds)", second));
    }

    private void onCodeTimerStop() {
        isStartTimer.set(false);
        binding.tvReqCode.setEnabled(true);
        binding.tvReqCode.setTextColor(ColorUtils.getColor(R.color.blue_4c94ff));
        binding.tvReqCode.setText("获取验证码");
    }
}
