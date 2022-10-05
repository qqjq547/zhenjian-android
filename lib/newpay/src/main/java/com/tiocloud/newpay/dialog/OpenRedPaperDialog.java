package com.tiocloud.newpay.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.ResourceUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletOpenRedpaperDialogBinding;
import com.tiocloud.newpay.feature.paperdetail.PaperDetailActivity;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.widget.dialog2.BaseBindingDialog;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/11
 *     desc   : 打开后的红包
 * </pre>
 */
public class OpenRedPaperDialog extends BaseBindingDialog<WalletOpenRedpaperDialogBinding> {

    public static final int NONE = 1;
    public static final int OVERDUE = 2;

    public final ObservableField<String> fromInfo = new ObservableField<>("小生的红包");
    public final ObservableField<String> gift = new ObservableField<>("哎呀，红包被一抢而光了");

    private final int dialogType;
    private final RedPaperDialog.RedPaperVo redPaperVo;

    public OpenRedPaperDialog(@NonNull Context context, @DialogType int dialogType, RedPaperDialog.RedPaperVo redPaperVo) {
        super(context);
        this.dialogType = dialogType;
        this.redPaperVo = redPaperVo;
    }

    @NonNull
    @Override
    public Builder getBuilder() {
        return super.getBuilder()
                .setAnimationsResId(R.style.tio_dialog_anim)
                ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setView(this);

        setDialogType(dialogType);
    }

    @Override
    public int getLayoutId() {
        return R.layout.wallet_open_redpaper_dialog;
    }


    public void clickCancel(View v) {
        dismiss();
    }

    @SuppressLint("SetTextI18n")
    private void setDialogType(@DialogType int dialogType) {
        if (redPaperVo == null) return;

        binding.ivAvatar.loadUrlStatic_asCircle_border(redPaperVo.avatar, Color.parseColor("#FFF9AD55"), 1);
        fromInfo.set(String.format(Locale.getDefault(), "%s的红包", redPaperVo.name));

        if (dialogType == NONE) {
            binding.tvShowDetail.setText("看看大家的人品");
            Drawable drawable = ResourceUtils.getDrawable(R.drawable.wallet_arrow_right_yellow);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            binding.tvShowDetail.setCompoundDrawables(null, null, drawable, null);
            binding.tvShowDetail.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    PaperDetailActivity.start(view.getContext(), redPaperVo.serialNumber);
                    dismiss();
                }
            });

            gift.set("哎呀，红包被一抢而光了");
        } else if (dialogType == OVERDUE) {
            binding.tvShowDetail.setText("超过24小时未领取将过期");
            binding.tvShowDetail.setCompoundDrawables(null, null, null, null);
            binding.tvShowDetail.setOnClickListener(null);

            gift.set("可惜，当前红包过期了");
        }
    }

    @IntDef({NONE, OVERDUE})
    public @interface DialogType {
    }
}
