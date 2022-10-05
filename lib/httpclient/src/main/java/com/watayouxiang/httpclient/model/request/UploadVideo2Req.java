package com.watayouxiang.httpclient.model.request;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.io.File;
import java.lang.reflect.Type;

/**
 * author : TaoWang
 * date : 2020/3/9
 * desc :
 */
public class UploadVideo2Req extends BaseReq<Object> {
    private String chatlinkid;
    /**
     * 单文件上传
     */
    private String videoPath;
    private String coverurl,fingerprint;
    private int width,height;
    private String duration;
    private String filefingerprint;

    public UploadVideo2Req(String chatlinkid, String videoPath,int width,int height,String coverurl,String duration,String fingerprint,String filefingerprint) {
        this.chatlinkid = chatlinkid;
        this.videoPath = videoPath;
        this.width = width;
        this.height = height;
        this.coverurl = coverurl;
        this.duration = duration;
        this.filefingerprint = filefingerprint;

        this.fingerprint = fingerprint;
        Log.d("===视频参数==11=","=="+toString() );

    }

    @Override
    public String toString() {
        return "UploadVideo2Req{" +
                "chatlinkid='" + chatlinkid + '\'' +
                ", videoPath='" + videoPath + '\'' +
                ", coverurl='" + coverurl + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", duration='" + duration + '\'' +
                ", filefingerprint='" + filefingerprint + '\'' +
                '}';
    }

    @Override
    public String path() {
        return "/mytio/chat/video2.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
//        {
//            "uploadFile": "文件test.mp4", // 文件
//                "chatlinkid": 0, // 聊天会话id
//                "title": "", // 标题,默认为文件名
//                "width": "", // 视频的宽
//                "height": "", // 视频的高
//                "seconds": "", // 时长（秒）
//                "formatedseconds": "", // 格式化的时长1小时20分钟 36秒
//                "fps": "", // 每秒的帧数
//                "framecount": "", // 总帧数
//                "status": "", // 状态：1：不可公开浏览；2：可公开浏览；3：禁止在任何场景显示
//                "coverurl": "", // 封面url
//                "coverwidth": "", // 封面的宽
//                "coverheight": "", // 封面的高
//                "coversize": "", // 封面尺寸，单位：字节
//                "fingerprint": "" // 指纹，md5(文件前1024字节 + 文件长度)
//        }
        return super.params()
                .append("chatlinkid", chatlinkid)
                .append("title", "")
                .append("width", width+"")
                .append("height", height+"")
                .append("seconds", duration)
                .append("formatedseconds", "")
                .append("fps", "")
                .append("framecount", "")
                .append("coverwidth","200")
                .append("coverheight", "200")
                .append("fingerprint", fingerprint)
                .append("filefingerprint", filefingerprint)

                .append("secretkey", fingerprint)
                .append("secretiv", "")

                ;
    }

    @Override
    public TioMap<String, File> files() {
        return super.files()
                .append("uploadFile", new File(videoPath))
                .append("coverUploadFile", new File(coverurl))
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
