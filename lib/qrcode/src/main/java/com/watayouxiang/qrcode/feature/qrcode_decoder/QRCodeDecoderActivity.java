package com.watayouxiang.qrcode.feature.qrcode_decoder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.qrcode.R;
import com.watayouxiang.qrcode.databinding.ActivityQrcodeDecoderBinding;
import com.watayouxiang.qrcode.feature.qrcode_my.MyQRCodeActivity;
import com.watayouxiang.qrcode.feature.qrcode_decoder.mvp.Contract;
import com.watayouxiang.qrcode.feature.qrcode_decoder.mvp.Presenter;

import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/08
 *     desc   : 二维码扫描页
 * </pre>
 */
public class QRCodeDecoderActivity extends BindingActivity<ActivityQrcodeDecoderBinding> implements Contract.View {

    private Presenter presenter;

    public static void start(Context context) {
        Presenter.checkPermission(() -> startInternal(context));
    }

    private static void startInternal(Context context) {
        Intent starter = new Intent(context, QRCodeDecoderActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.TRANSPARENT;
    }

    @Override
    protected Integer background_color() {
        return Color.BLACK;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_qrcode_decoder;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        presenter.init(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void resetUI() {
//        binding.titleBar.getTvRight().setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
//                presenter.chooseQRCodeFromGallery(getActivity());
//            }
//        });
        binding.titleBar.getTvRight().setVisibility(View.GONE);
        binding.xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.chooseQRCodeFromGallery(getActivity());

            }
        });

        binding.tvMyQRCode.setOnClickListener(view -> {
            if (ClickUtils.isViewSingleClick(view)) {
                MyQRCodeActivity.start(QRCodeDecoderActivity.this);
            }
        });
    }

    @Override
    public ZXingView getZXingView() {
        return binding.zxingview;
    }

    @Override
    public TextView getTvFlashlight() {
        return binding.tvFlashlight;
    }
}
