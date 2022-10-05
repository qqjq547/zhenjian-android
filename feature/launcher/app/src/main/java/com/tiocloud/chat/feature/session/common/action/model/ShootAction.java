package com.tiocloud.chat.feature.session.common.action.model;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.DensityUtils;
import com.tiocloud.chat.feature.session.common.action.model.base.BaseUploadAction;
import com.tiocloud.chat.feature.session.common.model.RequestCode;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.BitmapUtils;
import com.tiocloud.chat.util.FileUtils;
import com.tiocloud.chat.util.PreferencesUtil;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.ImageIScheckFileExistReq;
import com.watayouxiang.httpclient.model.request.UploadImgIsExistReq;
import com.watayouxiang.httpclient.utils.MD5Utils;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.androidutils.tools.TioLogger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * author : TaoWang
 * date : 2020/3/5
 * desc : 拍照
 */
public class ShootAction extends BaseUploadAction {
    public ShootAction() {
        super(R.drawable.ic_shoot_session, R.string.shoot);
    }

    @Override
    public void onClick() {
        EasyPhotos.createCamera(fragment)
                .setFileProviderAuthority("com.meita.zhenjian.chat.fileprovider")
                .start(RequestCode.TAKE_PHOTO);

        // 取消 "App进入后台时，自动断开连接"
        TioIMClient.getInstance().setAutoDisconnectOnAppBackground(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode.TAKE_PHOTO) {

            // 恢复 "App进入后台时，自动断开连接"
            TioIMClient.getInstance().setAutoDisconnectOnAppBackground(true);

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
            // 上传图片
            Photo photo = resultPhotos.get(0);
            int im_burst_transfer_file= PreferencesUtil.getInt("im_burst_transfer_file",0);
            int im_file_encrypt= PreferencesUtil.getInt("im_file_encrypt",0);
            String fingerprint = null;

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
                String finalFingerprint = fingerprint;
                Log.d("===上传图片==",photo.name+"=="+photo.path);
                if(im_file_encrypt==1){
                    if(im_burst_transfer_file==1){
                        ImageIScheckFileExistReq img2Req=new ImageIScheckFileExistReq( "1",finalFingerprint);
                        img2Req.setCancelTag(this);
                        TioHttpClient.post(this, img2Req, new TioCallback<Object>() {
                            @Override
                            public void onTioSuccess(Object s) {
                                String json = new Gson().toJson(s);//info是object类型
                                Log.d("====文件是否存在==","=图片加密===妙传=="+s);

                                if(json.equals("true")){
                                    UploadImgIsExistReq img3Req=new UploadImgIsExistReq(fragment.getChatLinkId(),"", FileUtils.bytePath+photo.name,finalFingerprint, finalFingerprint,photo.width,photo.height,photo.size+"");
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
                                    AESEncrypt.encryptFile(new File(photo.path),FileUtils.bytePath,photo.name, finalFingerprint);
//                                    Bitmap bitmap= FileUtils.getImageThumbnail(photo.path, DensityUtils.dip2px(getActivity(),200), DensityUtils.dip2px(getActivity(),200));
                                    Bitmap bitmap= FileUtils.getImageThumbnail(photo.path, photo.width, photo.height);
                                    Bitmap bitmap3= BitmapUtils.compressBitmap(bitmap,DensityUtils.dip2px(getActivity(),70),DensityUtils.dip2px(getActivity(),70));
                                    File  coverurl = ImageUtils.save2Album(bitmap3, Bitmap.CompressFormat.PNG);
                                    Log.d("===suolue加密，不存在==",FileUtils.bytePath+coverurl.getName());
                                    AESEncrypt.encryptFile(coverurl,FileUtils.bytePath,coverurl.getName(), finalFingerprint);
                                    getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), FileUtils.bytePath+coverurl.getName(),FileUtils.bytePath+photo.name,finalFingerprint,photo.width,photo.height,photo.size+"",null,null);
                                    boolean isRemove=AESEncrypt.deleteFile(coverurl);
                                }
                            }
                            @Override
                            public void onTioError(String msg) {
                                Log.e("====文件是否存在==","=onTioError="+msg);
                            }
                        });
                    }else {
                        AESEncrypt.encryptFile(new File(photo.path),FileUtils.bytePath,photo.name, finalFingerprint);
//                        Bitmap bitmap= FileUtils.getImageThumbnail(photo.path, DensityUtils.dip2px(getActivity(),200), DensityUtils.dip2px(getActivity(),200));
                        Bitmap bitmap= FileUtils.getImageThumbnail(photo.path, photo.width, photo.height);
                        Bitmap bitmap3= BitmapUtils.compressBitmap(bitmap,DensityUtils.dip2px(getActivity(),70),DensityUtils.dip2px(getActivity(),70));

                        File  coverurl = ImageUtils.save2Album(bitmap3, Bitmap.CompressFormat.PNG);
                        Log.d("===suolue加密==","图片加密===不妙传"+FileUtils.bytePath+coverurl.getName());
                        AESEncrypt.encryptFile(coverurl,FileUtils.bytePath,coverurl.getName(), finalFingerprint);
                        getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), "",FileUtils.bytePath+photo.name,finalFingerprint,photo.width,photo.height,photo.size+"",null,null);
                        boolean isRemove=AESEncrypt.deleteFile(coverurl);

                    }
                }else {
                    if(im_burst_transfer_file==1){//是否妙传
                        ImageIScheckFileExistReq img2Req=new ImageIScheckFileExistReq( "1",finalFingerprint);
                        img2Req.setCancelTag(this);
                        TioHttpClient.post(this, img2Req, new TioCallback<Object>() {
                            @Override
                            public void onTioSuccess(Object s) {
                                String json = new Gson().toJson(s);//info是object类型
                                Log.d("====文件是否存在==","=图片不加密===妙传=="+s);
                                if(json.equals("true")){
                                    Log.d("====文件是否存在==","=图片="+json);
                                    UploadImgIsExistReq img3Req=new UploadImgIsExistReq(fragment.getChatLinkId(),"", "", "",finalFingerprint,photo.width,photo.height,photo.size+"");
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
                                    getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), "",photo.path,"",photo.width,photo.height,photo.size+"",null,null);
                                }
                            }
                            @Override
                            public void onTioError(String msg) {
                                Log.e("====文件是否存在==","=onTioError="+msg);
                            }
                        });
                    }else {
                        getUploadPresenter().uploadImg2(false,fragment.getChatLinkId(), "",photo.path,"",photo.width,photo.height,photo.size+"",null,null);
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }



//            getUploadPresenter().uploadImg(fragment.getChatLinkId(), photo.path);

        }
    }
}
