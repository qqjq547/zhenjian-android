package com.tiocloud.chat.feature.session.common.action.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.DensityUtils;
import com.tiocloud.chat.baseNewVersion.utils2.ToastshowUtils;
import com.tiocloud.chat.feature.session.common.action.model.base.BaseUploadAction;
import com.tiocloud.chat.feature.session.common.model.RequestCode;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.AesUtil;
import com.tiocloud.chat.util.BitmapUtils;
import com.tiocloud.chat.util.FileUtils;
import com.tiocloud.chat.util.PreferencesUtil;
import com.watayouxiang.androidutils.engine.EasyPhotosEngine;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.UrlUtil;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.ImageIScheckFileExistReq;
import com.watayouxiang.httpclient.model.request.UploadImg2Req;
import com.watayouxiang.httpclient.model.request.UploadImgIsExistReq;
import com.watayouxiang.httpclient.model.request.UploadPic2Req;
import com.watayouxiang.httpclient.model.request.UploadVideo2NewReq;
import com.watayouxiang.httpclient.model.request.UploadVideo2Req;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import static android.provider.MediaStore.Video.Thumbnails.MINI_KIND;
import static com.tiocloud.chat.util.AESEncrypt.getFileName;

/**
 * author : TaoWang
 * date : 2019-12-30
 * desc : 图片操作
 */
public class ImageAction extends BaseUploadAction {

    public ImageAction() {
        super(R.drawable.ic_photo_album_session, R.string.photo_album);
    }

    @Override
    public void onClick() {
        EasyPhotos.createAlbum(fragment, false, EasyPhotosEngine.getInstance())
                .setFileProviderAuthority("com.meita.zhenjian.fileprovider")
                .setPuzzleMenu(false)
                .setCleanMenu(false)
                .setCount(9)
                .setVideo(true)
                .setGif(true)
                .start(RequestCode.PICK_IMAGE_GIF_VIDEO);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.PICK_IMAGE_GIF_VIDEO) {
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
            if (fragment == null || fragment.getChatLinkId() == null) {
                return;
            }
            String fingerprint = null;


            // // im_burst_transfer_file 是否开启聊天文件快速传输 1 开启 2 关闭
            //    "im_file_encrypt": 2,     // 是否开启聊天文件加密 1 开启 2 关闭
            int im_burst_transfer_file= PreferencesUtil.getInt("im_burst_transfer_file",0);
            int im_file_encrypt= PreferencesUtil.getInt("im_file_encrypt",0);
            Log.d("===上传图片==","=im_burst_transfer_file=="+im_burst_transfer_file+"====im_file_encrypt=="+im_file_encrypt);
            for (Photo photo : resultPhotos) {
//                String  fingerprint2="7997b7be6eefc973662db19cc0b62d37";
//                AESEncrypt.decryptFile(new File(photo.path),FileUtils.bytePath, getFileName(photo.path),fingerprint2);
                Log.d("===图片回调==","=="+photo.path);
                // 判断类型
                if (UrlUtil.isImageSuffix(photo.path) || UrlUtil.isGifSuffix(photo.path)) {
                    try {
                        byte[]   imagebyte = FileUtils.toByteArray(photo.path);
                        String imagezi= FileUtils.base64Byte2String(imagebyte);
//                       String textjiequ=imagezi.substring(0,512);
//                            Log.d("===字节==",imagebyte.length+"==="+"=="+imagezi);
                        fingerprint = MD5Utils.getMd5(imagezi + new File(photo.path).length());
//                         Log.d("====fingerprint==",textjiequ.length()+"===="+fingerprint);
                        //异或加密
//                        byte[] imagebyte2=  FileUtils.encryptByte(imagebyte);
//                         FileUtils.byteArrayToFile(imagebyte2, FileUtils.bytePath+"two_"+photo.name);
//                        String ass = Arrays.stream(imagebyte).boxed().map(Object::toString).collect(Collectors.joining(","));
//                        Log.d("===上传图片==",fingerprint+"----"+photo.name+"=="+photo.path);
                        if(im_file_encrypt==1){
                            if(im_burst_transfer_file==1){
                                ImageIScheckFileExistReq img2Req=new ImageIScheckFileExistReq( "1",fingerprint);
                                img2Req.setCancelTag(this);
                                String finalFingerprint1 = fingerprint;
                                TioHttpClient.post(this, img2Req, new TioCallback<Object>() {
                                    @Override
                                    public void onTioSuccess(Object s) {
                                        String json = new Gson().toJson(s);//info是object类型
                                        Log.d("====文件是否存在==",json+"------"+finalFingerprint1+"===图片加密===妙传=="+s);
                                        if(json.equals("true")){
                                            UploadImgIsExistReq img3Req=new UploadImgIsExistReq(fragment.getChatLinkId(),"", FileUtils.bytePath+photo.name,finalFingerprint1, finalFingerprint1,photo.width,photo.height,photo.size+"");
                                            img3Req.setCancelTag(this);
                                            TioHttpClient.post(this, img3Req, new TioCallback<Object>(){

                                                @Override
                                                public void onTioSuccess(Object s) {
                                                    Log.d("====文件存在==","=onTioSuccess="+s.toString());

                                                }

                                                @Override
                                                public void onTioError(String msg) {
                                                    ToastUtils.showShort(msg);
                                                    Log.e("====文件存在==","=onTioError="+msg);

                                                }
                                            });
                                        }else {
                                            AESEncrypt.encryptFile(new File(photo.path),FileUtils.bytePath,photo.name, finalFingerprint1);
                                            Bitmap bitmap= FileUtils.getImageThumbnail(photo.path, photo.width, photo.height);
//                                            Bitmap bitmap2=BitmapUtils.getThumbnail(bitmap,DensityUtils.dip2px(getActivity(),100),DensityUtils.dip2px(getActivity(),100));
                                            Bitmap bitmap3= BitmapUtils.compressBitmap(bitmap,DensityUtils.dip2px(getActivity(),70),DensityUtils.dip2px(getActivity(),70));
                                            File  coverurl = ImageUtils.save2Album(bitmap3, Bitmap.CompressFormat.PNG);
                                            if(coverurl!=null&&coverurl.getName()!=null){
                                                Log.d("===缩略图加密，不存在==",FileUtils.bytePath+coverurl.getName());
                                                AESEncrypt.encryptFile(coverurl,FileUtils.bytePath,coverurl.getName(), finalFingerprint1);
                                            }
                                            getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), FileUtils.bytePath+coverurl.getName(),FileUtils.bytePath+photo.name,finalFingerprint1,photo.width,photo.height,photo.size+"",null,null);
                                            AESEncrypt.deleteFile(coverurl);
                                        }
                                    }
                                    @Override
                                    public void onTioError(String msg) {
                                        Log.e("====文件是否存在==","=onTioError="+msg);
                                    }
                                });
                            }else {
                                AESEncrypt.encryptFile(new File(photo.path),FileUtils.bytePath,photo.name, fingerprint);
                                Bitmap bitmap= FileUtils.getImageThumbnail(photo.path,photo.width, photo.height);
                                Bitmap bitmap2= BitmapUtils.compressBitmap(bitmap,DensityUtils.dip2px(getActivity(),70),DensityUtils.dip2px(getActivity(),70));
                                File  coverurl = ImageUtils.save2Album(bitmap2, Bitmap.CompressFormat.PNG);
//                                Log.d("===缩略图加密==","图片加密===不妙传"+FileUtils.bytePath+coverurl.getName());
                                if(coverurl!=null&&coverurl.getName()!=null){
                                    AESEncrypt.encryptFile(coverurl,FileUtils.bytePath,coverurl.getName(), fingerprint);
                                }
                                getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), FileUtils.bytePath+coverurl.getName(),FileUtils.bytePath+photo.name,fingerprint,photo.width,photo.height,photo.size+"",null,null);
                                AESEncrypt.deleteFile(coverurl);

                            }
                        }else {
                            if(im_burst_transfer_file==1){//是否妙传
                                ImageIScheckFileExistReq img2Req=new ImageIScheckFileExistReq( "1",fingerprint);
                                img2Req.setCancelTag(this);
                                String finalFingerprint2 = fingerprint;
                                TioHttpClient.post(this, img2Req, new TioCallback<Object>() {
                                    @Override
                                    public void onTioSuccess(Object s) {
                                        String json = new Gson().toJson(s);//info是object类型
                                        Log.d("====文件是否存在==","=图片不加密===妙传=="+s);
                                        if(json.equals("true")){
                                            Log.d("====文件是否存在==","=1111=="+s);

                                            UploadImgIsExistReq img3Req=new UploadImgIsExistReq(fragment.getChatLinkId(),"", photo.path, "",finalFingerprint2,photo.width,photo.height,photo.size+"");
                                            img3Req.setCancelTag(this);
                                            TioHttpClient.post(this, img3Req, new TioCallback<Object>(){
                                                @Override
                                                public void onTioSuccess(Object s) {
                                                    Log.d("====文件存在==","=onTioSuccess="+s.toString());
                                                }

                                                @Override
                                                public void onTioError(String msg) {
                                                    ToastUtils.showShort(msg);
                                                    Log.e("====文件存在==","=onTioError="+msg);

                                                }
                                            });
                                        }else {
                                            Log.d("====文件是否存在==","=1111=="+s);

                                            getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), "",photo.path,finalFingerprint2,photo.width,photo.height,photo.size+"",null,null);
                                        }
                                    }
                                    @Override
                                    public void onTioError(String msg) {
                                        Log.e("====文件是否存在==","=onTioError="+msg);
                                    }
                                });
                            }else {
                                getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), "",photo.path,fingerprint,photo.width,photo.height,photo.size+"",null,null);
                            }


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    // 上传图片
//                    getUploadPresenter().uploadImg(fragment.getChatLinkId(), FileUtils.bytePath+photo.name);

                } else {
                    try {
                        byte[]    imagebyte = FileUtils.toByteArray(photo.path);
                        String imagezi= FileUtils.base64Byte2String(imagebyte);
                        if(imagezi.length()<=1024*256){
                            fingerprint = MD5Utils.getMd5(imagezi + new File(photo.path).length());
                        }else {
                            String textjiequ=imagezi.substring(0,1024*256);
                            fingerprint = MD5Utils.getMd5(textjiequ + new File(photo.path).length());
                        }
                        FileUtils.getFirstframe(photo.path);

                        //是否加密
                        if(im_file_encrypt==1){
                            Log.d("===字节==",fingerprint.length()+"==="+"=="+fingerprint);
//                         Log.d("====fingerprint==",textjiequ.length()+"===="+fingerprint);
                            ImageIScheckFileExistReq img2Req=new ImageIScheckFileExistReq( "3",fingerprint);
                            img2Req.setCancelTag(this);
                            AESEncrypt.encryptFile(new File(photo.path),FileUtils.bytePath,photo.name, fingerprint);
                            //加密，判断是否妙传
                            if(im_burst_transfer_file==1){
                                //加密，是妙传，就再判断文件是否存在，如果存在传指纹， 如果不是直接传文件
                                String finalFingerprint = fingerprint;
                                TioHttpClient.post(this, img2Req, new TioCallback<Object>() {
                                    @Override
                                    public void onTioSuccess(Object s) {
                                        String json = new Gson().toJson(s);//info是object类型
                                        Log.d("====文件是否存在==","=视频加密===妙传=="+s);

                                        if(json.equals("true")){//存在直接传指纹
                                            UploadVideo2NewReq req = new UploadVideo2NewReq(fragment.getChatLinkId(), FileUtils.bytePath+photo.name,photo.width,photo.height,"",photo.duration+"", finalFingerprint,finalFingerprint);
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
                                                    boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+photo.name);
                                                    Log.d("=====视频上传完成: " ,isRemove+"==="+s);

                                                    ToastshowUtils.showToastSafe("上传完成100%");

                                                }

                                                @Override
                                                public void onTioError(String msg) {
                                                    TioLogger.i("=====上传出错: " + msg);
                                                    boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+photo.name);
                                                    TioToast.showShort(msg);
                                                }
                                            });
                                        }else {//不存在，传文件
                                            AESEncrypt.encryptFile(new File(FileUtils.bytePath+"videoImage.jpg"),FileUtils.bytePath,"videoImage1.jpg", finalFingerprint);
                                            getUploadPresenter().uploadImg2(true,fragment.getChatLinkId(),"", FileUtils.bytePath+photo.name, finalFingerprint,photo.width,photo.height,photo.size+"",photo.duration+"",photo.path);
                                        }
                                    }
                                    @Override
                                    public void onTioError(String msg) {
                                        Log.e("====文件是否存在==","=onTioError="+msg);
                                    }
                                });
                            }else {//加密，不是妙传 直接传文件
                                AESEncrypt.encryptFile(new File(FileUtils.bytePath+"videoImage.jpg"),FileUtils.bytePath,"videoImage1.jpg", fingerprint);
                                getUploadPresenter().uploadImg2(true,fragment.getChatLinkId(), "",FileUtils.bytePath+photo.name,fingerprint,photo.width,photo.height,photo.size+"",photo.duration+"",photo.path);
                            }
                        }else {
                            if(im_burst_transfer_file==1){
                                //不加密 妙传 判断文件是否穿在
                                ImageIScheckFileExistReq img2Req=new ImageIScheckFileExistReq( "3",fingerprint);
                                img2Req.setCancelTag(this);
                                String finalFingerprint3 = fingerprint;
                                TioHttpClient.post(this, img2Req, new TioCallback<Object>() {
                                    @Override
                                    public void onTioSuccess(Object s) {
                                        String json = new Gson().toJson(s);//info是object类型
                                        Log.d("====文件是否存在==","==视频不加密===妙传=="+s);
                                        if(json.equals("true")){//存在直接传指纹
                                            UploadVideo2NewReq req = new UploadVideo2NewReq(fragment.getChatLinkId(), photo.path,photo.width,photo.height,"",photo.duration+"", "",finalFingerprint3);
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
                                                    ToastshowUtils.showToastSafe("上传完成100%");

                                                }

                                                @Override
                                                public void onTioError(String msg) {
                                                    TioLogger.i("=====上传出错: " + msg);
                                                    TioToast.showShort(msg);
                                                }
                                            });
                                        }else {//不存在，传文件
//                                            getUploadPresenter().uploadVideo(fragment.getChatLinkId(), photo.path);
                                            getUploadPresenter().uploadImg2(true,fragment.getChatLinkId(),"", photo.path, finalFingerprint3,photo.width,photo.height,photo.size+"",photo.duration+"",photo.path);

                                        }
                                    }
                                    @Override
                                    public void onTioError(String msg) {
                                        Log.e("====文件是否存在==","=onTioError="+msg);
                                    }
                                });

                            }else {
                                //不加密 不妙传
//                                getUploadPresenter().uploadVideo(fragment.getChatLinkId(), photo.path);
                                getUploadPresenter().uploadImg2(true,fragment.getChatLinkId(),"", photo.path,"",photo.width,photo.height,photo.size+"",photo.duration+"",photo.path);

                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // 上传图
                    // 上传视频
                }
            }
        }
    }

}
