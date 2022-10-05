package com.tiocloud.chat.widget.dialog.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.tiocloud.chat.R;
import com.tiocloud.chat.util.FileUtils;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UpdateAvatarReq;
import com.watayouxiang.androidutils.engine.EasyPhotosEngine;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.UrlUtil;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * author : TaoWang
 * date : 2020/3/3
 * desc : 头像修改弹窗
 */
public class AvatarDialog extends BaseDialog implements View.OnClickListener {

    private final static int REQ_CODE_IMAGE_GIF = 1333;// 选择图片、gif

    private View tv_takePhoto;
    private View tv_pickPhoto;
    private View tv_cancel;
    private final TioCallback<Void> mCallback;
    private Activity activity;
    private Uri mDestination;

    public AvatarDialog(final Context context, TioCallback<Void> callback) {
        super(context);
        mCallback = callback;
        setAnimation(R.style.tio_bottom_dialog_anim);
        setFullScreenWidth();
        setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(LayoutInflater.from(context).inflate(R.layout.tio_bottom_dialog_avatar, null));
        initViews();
    }

    private void initViews() {
        tv_takePhoto = findViewById(R.id.tv_takePhoto);
        tv_pickPhoto = findViewById(R.id.tv_pickPhoto);
        tv_cancel = findViewById(R.id.tv_cancel);

        tv_takePhoto.setOnClickListener(this);
        tv_pickPhoto.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        TioHttpClient.cancel(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            dismiss();
        } else if (v == tv_takePhoto) {
            // 拍照
            if (activity == null) return;
            EasyPhotos.createCamera(activity)
                    .setFileProviderAuthority("com.meita.zhenjian.chat.fileprovider")
                    .start(REQ_CODE_IMAGE_GIF);
            dismiss();
        } else if (v == tv_pickPhoto) {
            // 选择图片
            if (activity == null) return;
            EasyPhotos.createAlbum(activity, false, EasyPhotosEngine.getInstance())
                    .setFileProviderAuthority("com.meita.zhenjian.chat.fileprovider")
                    .setPuzzleMenu(false)
                    .setCleanMenu(false)
                    .setCount(1)
                    .setVideo(false)
                    .setGif(true)
                    .start(REQ_CODE_IMAGE_GIF);
            dismiss();
        }
    }

    // ====================================================================================
    // 头像上传
    // ====================================================================================

    public void setActivity(Activity activity) {
        this.activity = activity;
        mDestination = Uri.fromFile(new File(activity.getCacheDir(), "cropImage.jpeg"));

    }
    private String imagename;
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_IMAGE_GIF) {
            // 容错处理
            if (data == null) return;
            //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
            //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
            boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
            TioLogger.i(String.valueOf(resultPhotos));

            // 容错处理
            if (resultPhotos == null || resultPhotos.size() == 0) {
                return;
            }

            // 判断类型
//            Photo photo = resultPhotos.get(0);
//            if (UrlUtil.isImageSuffix(photo.path) || UrlUtil.isGifSuffix(photo.path)) {
//                // 上传图片
//                uploadAvatar(photo.path);
//            }

            // 判断类型
            Photo photo = resultPhotos.get(0);
            if (UrlUtil.isImageSuffix(photo.path) || UrlUtil.isGifSuffix(photo.path)) {
                UCrop.Options options = new UCrop.Options();
                // 修改标题栏颜色
                options.setToolbarColor(activity.getResources().getColor(com.tiocloud.account.R.color.white));
                options.setStatusBarColor(activity.getResources().getColor(com.tiocloud.account.R.color.white));
                options.setToolbarWidgetColor(activity.getResources().getColor(com.tiocloud.account.R.color.black));
                // 隐藏底部工具
                options.setHideBottomControls(true);
                // 图片格式
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // 设置图片压缩质量
                options.setCompressionQuality(100);
                // 上传图片
                UCrop.of(photo.uri, mDestination)
                        // 长宽比
                        .withAspectRatio(1, 1)
                        // 图片大小
                        .withMaxResultSize(512, 512)
                        // 配置参数
                        .withOptions(options)
                        .start(activity, UCrop.REQUEST_CROP);
                imagename=photo.name;
////                uploadAvatar(photo.path);
            }
        }else if (requestCode == UCrop.REQUEST_CROP){
//            try {
                String path = mDestination.getPath();
//                try {
//                    byte[] imagebyte=  FileUtils.toByteArray(path);
//                    byte[] imagebyte2=  FileUtils.encryptByte(imagebyte);
//                    FileUtils.byteArrayToFile(imagebyte2, FileUtils.bytePath+imagename);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }catch (Exception e){
//                Toast.makeText(activity, "无法剪切选择的图片", Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }
            uploadAvatar(path);

        }
    }

    private void uploadAvatar(String path) {
        UpdateAvatarReq req = new UpdateAvatarReq(path);
        req.setCancelTag(this);
        req.upload(mCallback);
    }
}
