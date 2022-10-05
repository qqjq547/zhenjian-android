package com.watayouxiang.httpclient.model.request;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.io.File;
import java.lang.reflect.Type;

public class UploadImgIsExistReq extends BaseReq<Object> {
    private final String chatlinkid;

    /**
     * 单文件上传
     */
    private final String imgPath;
    private String fingerprint;//密钥
    public int width;//图片宽度
    public int height;//图片高度
    private String coversize;
    private String coverurl;
    private String filefingerprint;

    public UploadImgIsExistReq(String chatlinkid, String coverurl,String imgPath, String fingerprint,String filefingerprint, int width, int height,String coversize) {
        this.chatlinkid = chatlinkid;
        this.coverurl=coverurl;
        this.imgPath = imgPath;
        this.fingerprint = fingerprint;
        this.width = width;
        this.height = height;
        this.coversize=coversize;
        this.filefingerprint = filefingerprint;

        Log.d("===图片已存在参数===","=="+toString() );

    }

    @Override
    public String toString() {
        return "UploadImgIsExistReq{" +
                "chatlinkid='" + chatlinkid + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", coversize='" + coversize + '\'' +
                ", coverurl='" + coverurl + '\'' +
                '}';
    }

    @Override
    public String path() {
        return "/mytio/chat/img2.tio_x";

    }

    @Override
    public TioMap<String, String> params() {

        return super.params()
                .append("chatlinkid",chatlinkid)
                .append("title", "")
                .append("width", width+"")
                .append("height", height+"")
                .append("coverurl", coverurl)
                .append("coverwidth", width/2+"")
                .append("coverheight", height/2+"")
                .append("coversize", coversize)
                .append("fingerprint", fingerprint)
                .append("filefingerprint", filefingerprint)

                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}