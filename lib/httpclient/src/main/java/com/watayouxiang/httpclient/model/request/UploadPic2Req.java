package com.watayouxiang.httpclient.model.request;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.io.File;
import java.lang.reflect.Type;

public class UploadPic2Req extends BaseReq<Object> {
    private final String chatlinkid;

    /**
     * 单文件上传
     */
    private final String imgPath;
    private String fingerprint;//密钥
    public int width;//图片宽度
    public int height;//图片高度
    private String coversize;
    private File coverurl;
    private String filefingerprint;
    public UploadPic2Req(String chatlinkid, File coverurl,String imgPath, String fingerprint, String filefingerprint,int width, int height,String coversize) {
        this.chatlinkid = chatlinkid;
        this.coverurl=coverurl;
        this.imgPath = imgPath;
        this.fingerprint = fingerprint;
        this.filefingerprint = filefingerprint;
        this.width = width;
        this.height = height;
        this.coversize=coversize;
        Log.d("===图片参数===","=999="+toString() );

    }

    @Override
    public String toString() {
        return "UploadPic2Req{" +
                "chatlinkid='" + chatlinkid + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", filefingerprint='" + filefingerprint + '\'' +
                ", secretkey='" + filefingerprint + '\'' +
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
                .append("coverwidth","200")
                .append("coverheight", "200")
                .append("fingerprint", fingerprint)
                .append("filefingerprint", filefingerprint)
                .append("secretkey", fingerprint)
                .append("secretiv", "");
    }

    @Override
    public TioMap<String, File> files() {
        return super.files()
                .append("uploadFile", new File(imgPath))
                .append("coverUploadFile",coverurl)
                ;

    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}