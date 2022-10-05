package com.watayouxiang.httpclient.model.request;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.io.File;
import java.lang.reflect.Type;

public class ImageIScheckFileExistReq extends BaseReq<Object> {
    private  String type;// 1 图片 2 音频 3 视频 4 文件
    private String fingerprint;//密钥
    private String filefingerprint;
    public ImageIScheckFileExistReq( String type, String fingerprint) {
        this.type = type;
        this.fingerprint = fingerprint;

        Log.d("===文件是否存在===","=="+toString() );

    }

    @Override
    public String toString() {
        return "ImageIScheckFileExistReq{" +
                "type='" + type + '\'' +
                ", fingerprint='" + fingerprint + '\'' +

                '}';
    }

    @Override
    public String path() {
        return "/mytio/chat/checkFileExist.tio_x";
    }

    @Override
    public TioMap<String, String> params() {

        return super.params()
                .append("type", type)
                .append("fingerprint", fingerprint);

    }


    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}