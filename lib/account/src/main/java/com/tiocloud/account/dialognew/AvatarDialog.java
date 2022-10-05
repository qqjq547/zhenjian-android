package com.tiocloud.account.dialognew;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.tiocloud.account.NewGlideEngine;
import com.tiocloud.account.R;
import com.watayouxiang.androidutils.engine.EasyPhotosEngine;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.UrlUtil;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UpdateAvatarReq;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;

/**
 * author : TaoWang
 * date : 2020/3/3
 * desc : 头像修改弹窗
 */
public class AvatarDialog extends BaseDialog implements View.OnClickListener {

    private final static int REQ_CODE_IMAGE_GIF = 1334;// 选择图片、gif

    private View tv_takePhoto;
    private View tv_pickPhoto;
    private View tv_cancel;
    private Activity activity;

    public AvatarDialog(final Context context) {
        super(context);
        setAnimation(R.style.tio_bottom_dialog_anim);
        setFullScreenWidth();
        setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(LayoutInflater.from(context).inflate(R.layout.tio_bottom_dialog_avatar_new, null));
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
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQ_CODE_IMAGE_GIF) {
//            Log.d("=====路径=00===","==dddddd");
//
//            // 容错处理
//            if (data == null) return;
//            //返回对象集合：如果你需要了解图片的宽、高、大小、用户是否选中原图选项等信息，可以用这个
//            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
//            //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
////            boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);
//            Log.d("=====路径=00===","=="+resultPhotos.size());
//            // 容错处理
//            if (resultPhotos == null || resultPhotos.size() == 0) {
//                return;
//            }
//            // 判断类型
//            Photo photo = resultPhotos.get(0);
//            if (UrlUtil.isImageSuffix(photo.path) || UrlUtil.isGifSuffix(photo.path)) {
//                // 上传图片
////                uploadAvatar(photo.path);
//                String path=photo.path;
//                Log.d("=====路径=11===","=="+path);
//                mCallback.onTioSuccess(path);
//
//            }
//        }
//    }



}
