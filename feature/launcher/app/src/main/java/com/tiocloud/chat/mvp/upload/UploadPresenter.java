package com.tiocloud.chat.mvp.upload;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tiocloud.chat.baseNewVersion.utils2.DensityUtils;
import com.tiocloud.chat.baseNewVersion.utils2.ToastshowUtils;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.BitmapUtils;
import com.tiocloud.chat.util.FileUtil;
import com.tiocloud.chat.util.FileUtils;
import com.tiocloud.chat.util.PreferencesUtil;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.callback.TioFileCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.UploadFileReq;
import com.watayouxiang.httpclient.model.request.UploadImg2Req;
import com.watayouxiang.httpclient.model.request.UploadImgReq;
import com.watayouxiang.httpclient.model.request.UploadPic2Req;
import com.watayouxiang.httpclient.model.request.UploadVideo2Req;
import com.watayouxiang.httpclient.model.request.UploadVideoReq;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.httpclient.utils.MD5Utils;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxFaultItem;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Handler;

import static android.provider.MediaStore.Video.Thumbnails.MICRO_KIND;
import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static com.tiocloud.chat.util.AESEncrypt.getFileName;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;


public class UploadPresenter extends UploadContract.Presenter {
    public UploadPresenter(UploadContract.View view) {
        super(new UploadModel(false), view);
    }


    @Override
    public void uploadImg(String chatlinkid, String filePath) {
        UploadImgReq req = new UploadImgReq(chatlinkid, filePath);
        req.setCancelTag(this);
        TioHttpClient.upload(req, new TioCallback<String>() {
            @Override
            public void onStart(Request<BaseResp<String>, ? extends Request> request) {
                super.onStart(request);
                TioLogger.i("正在上传中...");
//                showUploadDialog();
                postWxFaultItem(chatlinkid,filePath, 0);
            }

            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                TioLogger.i("uploadProgress: " + progress);
                postWxFaultItem(chatlinkid,filePath, 50);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideUploadDialog();
            }

            @Override
            public void onTioSuccess(String s) {
                TioLogger.i("上传完成: " + s);
                postWxFaultItem(chatlinkid,filePath, 100);
            }

            @Override
            public void onTioError(String msg) {
                TioLogger.i("上传出错: " + msg);
                TioToast.showShort(msg);
            }
        });
    }

    /**
     * 上传图片AES
     */
    private void uploadAesImage(String chatlinkid, File coverurl,String filePath,String fingerprint,String filefingerprint,int width,int height,String size){
        UploadPic2Req img2Req=new UploadPic2Req(chatlinkid,coverurl, filePath,fingerprint,filefingerprint,width,height,size);
        img2Req.setCancelTag(this);
        int im_file_encrypt= PreferencesUtil.getInt("im_file_encrypt",0);
        TioHttpClient.upload(img2Req, new TioCallback<Object>() {
            @Override
            public void onStart(Request<BaseResp<Object>, ? extends Request> request) {
                super.onStart(request);
                TioLogger.i("===正在IMage上传中...");
//                showUploadDialog();
                postWxFaultItem(chatlinkid,filePath, 0);
            }

            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                TioLogger.i("uploadProgress: " + progress);
                postWxFaultItem(chatlinkid,filePath, 50);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideUploadDialog();
            }

            @Override
            public void onTioSuccess(Object s) {
                String json = new Gson().toJson(s);
//                TioLogger.i("===上传完成: " +json);
                try {
                    if(im_file_encrypt==1){
                        boolean isRemove=AESEncrypt.deleteFile(filePath);
                        boolean isRemove2=AESEncrypt.deleteFile(coverurl);
                        Log.d("===上传图片完成: ==" ,isRemove2+"==");
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }


                postWxFaultItem(chatlinkid,filePath, 100);
            }

            @Override
            public void onTioError(String msg) {
                if(im_file_encrypt==1){
                    boolean isRemove=AESEncrypt.deleteFile(filePath);
                    boolean isRemove2=AESEncrypt.deleteFile(coverurl);
                }
                TioLogger.i("===上传出错: " + msg);

                TioToast.showShort(msg);
            }
        });
    }

    /**
     * 上传缩略图
     * @param isVideo
     * @param chatlinkid
     * @param filePath
     * @param fingerprint
     * @param width
     * @param height
     */
    @Override
    public void uploadImg2(boolean isVideo,String chatlinkid,String coverurlnew, String filePath,String fingerprint,int width,int height,String size,String duration,String VideoPath) {
        // // im_burst_transfer_file 是否开启聊天文件快速传输 1 开启 2 关闭
        //    "im_file_encrypt": 2,     // 是否开启聊天文件加密 1 开启 2 关闭
        int im_burst_transfer_file= PreferencesUtil.getInt("im_burst_transfer_file",0);
        int im_file_encrypt= PreferencesUtil.getInt("im_file_encrypt",0);
        if(isVideo){
//            UploadImg2Req img2Req=new UploadImg2Req( FileUtils.bytePath+"videoImage.jpg",fingerprint,600,600);
//            img2Req.setCancelTag(this);
//            TioHttpClient.upload(img2Req, new TioCallback<Object>() {
//                @Override
//                public void onStart(Request<BaseResp<Object>, ? extends Request> request) {
//                    super.onStart(request);
//                    TioLogger.i("===正在上传缩略图中...");
//                }
//
//                @Override
//                public void uploadProgress(Progress progress) {
//                    super.uploadProgress(progress);
//                    TioLogger.i("uploadProgress: " + progress);
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    hideUploadDialog();
//                }
//
//                @Override
//                public void onTioSuccess(Object s) {
//                    String json = new Gson().toJson(s);//info是object类型
//                     JSONObject jsonObject = new JSONObject(json);//把json 转换成JSONObject
//                String url=jsonObject.getString("url");
//                Log.d("===上传缩略图完成: ==" ,"=="+url);
//
//                }
//
//                @Override
//                public void onTioError(String msg) {
//                    boolean isRemove=AESEncrypt.deleteFile(filePath);
//                    AESEncrypt.deleteFile(FileUtils.bytePath+"videoImage.jpg");
//
//                    Log.d("===视频缩略图上传出错== " ,isRemove+"==="+msg);
//                    TioToast.showShort(msg);
//                }
//            });
//            AESEncrypt.deleteFile(FileUtils.bytePath+"videoImage.jpg");
            try {
                UploadVideo2Req req = null;
                if(im_file_encrypt==1){
                    if(im_burst_transfer_file==1){
                        req = new UploadVideo2Req(chatlinkid, filePath,width,height,FileUtils.bytePath+"videoImage1.jpg",duration,fingerprint,fingerprint);
                    }else {
                        req = new UploadVideo2Req(chatlinkid, filePath,width,height,FileUtils.bytePath+"videoImage1.jpg",duration,fingerprint,"");
                    }
                }else {
                    if(im_burst_transfer_file==1){
                        req = new UploadVideo2Req(chatlinkid, filePath,width,height,FileUtils.bytePath+"videoImage.jpg",duration,"",fingerprint);

                    }else {
                        req = new UploadVideo2Req(chatlinkid, filePath,width,height,FileUtils.bytePath+"videoImage.jpg",duration,"","");
                    }

                }

                req.setCancelTag(this);
                TioHttpClient.upload(req, new TioCallback<Object>() {
                    @Override
                    public void onStart(Request<BaseResp<Object>, ? extends Request> request) {
                        super.onStart(request);
                        TioLogger.i("正在上传中...");
                    }

                    @Override
                    public void uploadProgress(Progress progress) {
                        super.uploadProgress(progress);
                        int progressnum= (int) (progress.fraction*100);
                        Log.d("===上传视频===",progressnum+"==="+progress);

                        ToastshowUtils.showToastSafe("上传中"+progressnum+"%");

                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                    }

                    @Override
                    public void onTioSuccess(Object s) {
                        if(im_file_encrypt==1){
                            boolean isRemove=AESEncrypt.deleteFile(filePath);
                            Log.d("=====视频上传完成: " ,isRemove+"==="+s);

                        }
                        ToastshowUtils.showToastSafe("上传完成100%");

                    }

                    @Override
                    public void onTioError(String msg) {
                        TioLogger.i("=====上传出错: " + msg);
                        if(im_file_encrypt==1){
                            boolean isRemove=AESEncrypt.deleteFile(filePath);
                        }
                        TioToast.showShort(msg);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }


        }else {
            long sizeImage = 0;
            long sizeImage2 = 0;
            if(im_file_encrypt==1){
                if (new File(coverurlnew).exists()) {
                    FileInputStream fis = null;
                    FileInputStream fis2 = null;
                    try {
                        fis = new FileInputStream(new File(coverurlnew));
                        sizeImage = fis.available();
                        fis2 = new FileInputStream(new File(filePath));
                        sizeImage2 = fis2.available();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d("====size=1111=","==="+sizeImage+"==="+sizeImage2);
                if(im_burst_transfer_file==1){
                    if(sizeImage>sizeImage2){
                        Log.d("====size=1111=","===大于");
                        uploadAesImage(chatlinkid, new File(filePath),filePath,fingerprint,fingerprint,width,height,size);
                    }else {
                        Log.d("====size=1111=","===小于");
                        uploadAesImage(chatlinkid, new File(coverurlnew),filePath,fingerprint,fingerprint,width,height,size);
                    }
                }else {
                    if(sizeImage>sizeImage2){
                        uploadAesImage(chatlinkid, new File(filePath),filePath,fingerprint,"",width,height,size);
                    }else {
                        uploadAesImage(chatlinkid, new File(coverurlnew),filePath,fingerprint,"",width,height,size);
                    }
                }
            }else {
                Bitmap bitmap= FileUtils.getImageThumbnail(filePath,width, height);
                Bitmap bitmap2= BitmapUtils.compressBitmap(bitmap,DensityUtils.dip2px(getView().getActivity(),70),DensityUtils.dip2px(getView().getActivity(),70));
                File  coverurl = ImageUtils.save2Album(bitmap2, Bitmap.CompressFormat.PNG);
                if (coverurl.exists()) {
                    FileInputStream fis = null;
                    FileInputStream fis2 = null;
                    try {
                        fis = new FileInputStream(coverurl);
                        sizeImage = fis.available();
                        fis2 = new FileInputStream(new File(filePath));
                        sizeImage2 = fis2.available();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Log.d("====size=222=","==="+sizeImage+"==="+sizeImage2);
                if(im_burst_transfer_file==1){
                    if(sizeImage>sizeImage2){
                        uploadAesImage(chatlinkid, new File(filePath),filePath,"",fingerprint,width,height,size);
                    }else {
                        uploadAesImage(chatlinkid, coverurl,filePath,"",fingerprint,width,height,size);
                    }
                }else {
                    if(sizeImage>sizeImage2){
                        uploadAesImage(chatlinkid,new File(filePath),filePath,"","",width,height,size);

                    }else {
                        uploadAesImage(chatlinkid,coverurl,filePath,"","",width,height,size);
                    }
                }
            }
        }
    }
    @Override
    public void uploadVideo(String chatlinkid, String filePath) {
        UploadVideoReq req = new UploadVideoReq(chatlinkid, filePath);
        req.setCancelTag(this);
        //上传视频
        TioHttpClient.upload(req, new TioCallback<String>() {
            @Override
            public void onStart(Request<BaseResp<String>, ? extends Request> request) {
                super.onStart(request);
                TioLogger.i("正在上传中...");
//                showUploadDialog("0");
            }
            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                int progressnum= (int) (progress.fraction*100);
                Log.d("===上传视频===",progressnum+"==="+progress);
                ToastshowUtils.showToastSafe("上传中"+progressnum+"%");
            }
            @Override
            public void onFinish() {
                super.onFinish();
//                hideUploadDialog();
//                ToastshowUtils.showToastSafe("上传完成100%");
            }

            @Override
            public void onTioSuccess(String s) {
//                boolean isRemove=AESEncrypt.deleteFile(filePath);
//                TioLogger.i(isRemove+"==上传完成: " + s);
                ToastshowUtils.showToastSafe("上传完成100%");
            }
            @Override
            public void onTioError(String msg) {
                TioLogger.i("上传出错: " + msg);
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void uploadFile(String chatlinkid, String filePath) {
        UploadFileReq req = new UploadFileReq(chatlinkid, filePath);
        req.setCancelTag(this);
        TioHttpClient.upload(req, new TioCallback<String>() {
            @Override
            public void onStart(Request<BaseResp<String>, ? extends Request> request) {
                super.onStart(request);
                TioLogger.i("正在上传中...");
                showUploadDialog();
            }
            @Override
            public void uploadProgress(Progress progress) {
                super.uploadProgress(progress);
                TioLogger.i("uploadProgress: " + progress);
            }
            @Override
            public void onFinish() {
                super.onFinish();
                hideUploadDialog();
            }
            @Override
            public void onTioSuccess(String s) {
                TioLogger.i("上传完成: " + s);
            }
            @Override
            public void onTioError(String msg) {
                TioLogger.i("上传出错: " + msg);
                TioToast.showShort(msg);
            }
        });
    }

    private void showUploadDialog() {
        Activity activity = getView().getActivity();
        if (activity != null) {
            SingletonProgressDialog.show_unCancel(activity, "上传中...");
        }
    }
    private void showUploadDialog(String progress) {
        Activity activity = getView().getActivity();
        if (activity != null) {
            SingletonProgressDialog.show_unCancel(activity, "上传中..."+progress+"%");
        }
    }
    private void hideUploadDialog() {
        SingletonProgressDialog.dismiss();
    }

    /**
     * 上传过程中，做一条模拟数据展示界面
     */
    private void postWxFaultItem(String chatlinkid, String filepath, int progress) {
        WxFaultItem wxFaultItem = new WxFaultItem();
        wxFaultItem.chatlinkid = chatlinkid;
        wxFaultItem.url = filepath;
        wxFaultItem.progress = progress;
        TioIMClient.getInstance().getEventEngine().post(wxFaultItem);
    }

}
