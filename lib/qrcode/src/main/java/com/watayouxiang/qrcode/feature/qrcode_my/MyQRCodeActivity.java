package com.watayouxiang.qrcode.feature.qrcode_my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.androidutils.page.easy.EasyActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.qrcode.R;
import com.watayouxiang.qrcode.databinding.ActivityQrcodeMyBinding;
import com.watayouxiang.qrcode.feature.qrcode_decoder.QRCodeDecoderActivity;
import com.watayouxiang.qrcode.feature.qrcode_my.mvp.Contract;
import com.watayouxiang.qrcode.feature.qrcode_my.mvp.Presenter;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/09
 *     desc   : 我的二维码
 * </pre>
 */
public class MyQRCodeActivity extends EasyActivity<ActivityQrcodeMyBinding> implements Contract.View {

    private Presenter presenter;

    public static void start(Context context) {
        Presenter.checkPermission(() -> {
            Intent starter = new Intent(context, MyQRCodeActivity.class);
            context.startActivity(starter);
        });
    }

    @Override
    protected View getStatusBarHolder() {
//        return binding.statusBar;
        return null;
    }

    @Override
    protected Integer getStatusBarColor() {
//        return Color.parseColor("#DBEAFF");
        return Color.WHITE;
    }

    @Override
    protected Integer getBackgroundColor() {
        return Color.WHITE;
    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_qrcode_my;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTransparent(this);
        presenter = new Presenter(this);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void resetUI() {
        binding.titleBar.getIvRight().setOnClickListener(view -> {
            if (ClickUtils.isViewSingleClick(view)) {
                QRCodeDecoderActivity.start(MyQRCodeActivity.this);
            }
        });
        binding.ivAvatar.load_tioAvatar(null);
        binding.tvNick.setText("");
        binding.tvTip.setText("使用城市客栈APP扫一扫，加我为好友");
        binding.tvDownload.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                File png = presenter.saveQRCode2Album(binding.clQrcode);
                if (png != null) {
                    TioToast.showShort("已成功保存至相册");
                } else {
                    TioToast.showShort("下载失败");
                }
            }
        });
    }

    @Override
    public void onUserCurrResp(UserCurrResp resp) {
        binding.ivAvatar.load_tioAvatar(HttpCache.getResUrl(resp.avatar));
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(resp.avatar))
//                .listener(new RequestListener<File>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                        Log.d("===下载失败===","===");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                        byte[] bytes= FileUtils.File2byte(resource);
//                        try {
//                            byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                            Glide.with(getContext())
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(binding.ivAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        binding.tvNick.setText(StringUtils.null2Length0(resp.nick));
        binding.tvZhanghao.setText("账号："+resp.loginname);

    }

    @Override
    public void onMyQRCodeGet(Bitmap result) {
        binding.ivMyQRCode.setImageBitmap(result);
    }
}
