package com.watayouxiang.httpclient.model.request;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class UploadImg2Req extends BaseReq<Object> {

    /**
     * 单文件上传
     */
    private final String imgPath;
    private String fingerprint;//密钥
    public int width;//图片宽度
    public int height;//图片高度

    @Override
    public String toString() {
        return "UploadImg2Req{" +
                "imgPath='" + imgPath + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public UploadImg2Req( String imgPath, String fingerprint, int width, int height) {
        this.imgPath = imgPath;
        this.fingerprint = fingerprint;
        this.width = width;
        this.height = height;
        Log.d("===缩略图参数===","=="+toString() );

    }

    @Override
    public String path() {
        return "/mytio/chat/uploadImg2.tio_x";

    }

    @Override
    public TioMap<String, String> params() {

        return super.params()
                .append("title", "")
                .append("width", width/2+"")
                .append("height", height/2+"")
                .append("fingerprint", fingerprint)
                ;
    }

    @Override
    public TioMap<String, File> files() {
        return super.files()
                .append("uploadFile",new File(imgPath))
                ;

    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}