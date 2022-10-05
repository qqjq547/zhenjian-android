package com.watayouxiang.qrcode.feature.qrcode_group;

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
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.qrcode.R;
import com.watayouxiang.qrcode.databinding.ActivityQrcodeGroupBinding;
import com.watayouxiang.qrcode.feature.qrcode_decoder.QRCodeDecoderActivity;
import com.watayouxiang.qrcode.feature.qrcode_group.mvp.Contract;
import com.watayouxiang.qrcode.feature.qrcode_group.mvp.Presenter;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/17
 *     desc   : 群二维码
 * </pre>
 */
public class GroupQRCodeActivity extends BindingActivity<ActivityQrcodeGroupBinding> implements Contract.View {

    private static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    private Presenter presenter;

    public static void start(Context context, String groupId) {
        Presenter.checkPermission(() -> {
            Intent starter = new Intent(context, GroupQRCodeActivity.class);
            starter.putExtra(KEY_GROUP_ID, groupId);
            context.startActivity(starter);
        });
    }

    private String getGroupId() {
        return getIntent().getStringExtra(KEY_GROUP_ID);
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
        return Color.parseColor("#498FF6");
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_qrcode_group;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        presenter.init(getGroupId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void resetUI() {
        binding.titleBar.getIvRight().setOnClickListener(view -> {
            if (ClickUtils.isViewSingleClick(view)) {
                QRCodeDecoderActivity.start(GroupQRCodeActivity.this);
            }
        });
        binding.ivAvatar.load_tioAvatar(null);
        binding.tvNick.setText("");
        binding.tvTip.setText("扫码加入群聊");
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
        binding.tvShare.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                presenter.showShareDialog(getActivity(), binding.clQrcode);
            }
        });
    }

    @Override
    public void onGroupInfoResp(GroupInfoResp resp) {
        GroupInfoResp.Group group = resp.group;
        if (group == null) return;
        binding.ivAvatar.load_tioAvatar(HttpCache.getResUrl(group.avatar));
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(group.avatar))
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
        binding.tvNick.setText(StringUtils.null2Length0(group.name));
    }

    @Override
    public void onGroupQRCodeGet(Bitmap result) {
        binding.ivMyQRCode.setImageBitmap(result);
    }
}
