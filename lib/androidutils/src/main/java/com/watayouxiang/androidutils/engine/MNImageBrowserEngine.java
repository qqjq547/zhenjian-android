package com.watayouxiang.androidutils.engine;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.maning.imagebrowserlibrary.ImageEngine;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * author : TaoWang
 * date : 2020/4/9
 * desc : 搭配 https://github.com/maning0303/MNImageBrowser
 */
class MNImageBrowserEngine implements ImageEngine {
    private List<ImageAesFingerprinBean> imgUrlsfingerprint;

    public MNImageBrowserEngine(List<ImageAesFingerprinBean> imgUrlsfingerprint) {
        this.imgUrlsfingerprint=imgUrlsfingerprint;
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, final View progressView) {
        progressView.setVisibility(View.VISIBLE);
//        Glide.with(context).load(url)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        // 隐藏进度View,必须设置setCustomProgressViewLayoutID
//                        progressView.setVisibility(View.GONE);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        // 隐藏进度View,必须设置setCustomProgressViewLayoutID
//                        progressView.setVisibility(View.GONE);
//                        return false;
//                    }
//                })
//                .into(imageView);

        int im_file_encrypt= PreferencesUtil.getInt("im_file_encrypt",0);
        if(im_file_encrypt==1){
            for (int i=0;i<imgUrlsfingerprint.size();i++){
                if(url.contains(imgUrlsfingerprint.get(i).getImgUrls())){
                    if(!TextUtils.isEmpty(imgUrlsfingerprint.get(i).getFingerprint())){
                    Log.d("===下载=3333==",i+"------"+imgUrlsfingerprint.size()+"==="+imgUrlsfingerprint.get(i).getFingerprint());
                        String fingerprint=imgUrlsfingerprint.get(i).getFingerprint();
                        Glide.with(context)
                                .downloadOnly()
                                .load(url)
                                .listener(new RequestListener<File>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                        Log.d("===下载失败===","===");
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                                        try {
                                            Log.d("===下载成功==111=",fingerprint+"==="+resource.getName());

                                            AESEncrypt.decryptFile(resource,FileUtils.bytePath, AESEncrypt.getFileName(url),fingerprint);
                                            Glide.with(context).load(FileUtils.bytePath+  AESEncrypt.getFileName(url)).listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    Log.d("===图片加载失败==11=","==="+fingerprint);
                                                    boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ AESEncrypt.getFileName(url));
                                                    progressView.setVisibility(View.GONE);
                                                    return false;
                                                }
                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    //加载成功之后删除本地解密的图片
                                                    boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ AESEncrypt.getFileName(url));
                                                    Log.d("===图片加载成功=11==",isRemove+"===");
                                                    progressView.setVisibility(View.GONE);
                                                    return false;
                                                }}).into(imageView);

                                        }catch (Exception e){

                                        }


                                        return false;
                                    }
                                })
                                .preload();

                    }else {
                        Glide.with(context).load(url).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("===图片加载失败==33=", "");

                                progressView.setVisibility(View.GONE);
                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("===图片加载成功=33==","===");
                                progressView.setVisibility(View.GONE);
                                return false;
                            }}).into(imageView);
                    }
                }
            }

        }else {
                    Glide.with(context).load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // 隐藏进度View,必须设置setCustomProgressViewLayoutID
                        progressView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        // 隐藏进度View,必须设置setCustomProgressViewLayoutID
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
        }


//        Glide.with(context)
//                .downloadOnly()
//                .load(url)
//                .listener(new RequestListener<File>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                        Log.d("===下载失败===","===");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                        Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                        byte[] bytes= FileUtils.File2byte(resource);
//                        try {
//                            byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                            Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                            imageView.setImageBitmap(bitmap);
//                            Glide.with(context).load(imagebyte2).listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    progressView.setVisibility(View.GONE);
//
//                                    return false;
//                                }
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    progressView.setVisibility(View.GONE);
//                                    return false;
//                                }
//                            }).into(imageView);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
    }
}
