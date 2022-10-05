package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/02
 *     desc   :
 * </pre>
 */
public class ScreenshotReq extends BaseReq<Object> {
    /**
     * 聊天模型：1：私聊；2：群聊
     */
    private final String chatmode;
    /**
     * 群id或者好友uid
     */
    private final String bizid;

    public ScreenshotReq(String chatmode, String bizid) {
        this.chatmode = chatmode;
        this.bizid = bizid;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/sys/screenshot.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("chatmode", chatmode)
                .append("bizid", bizid)
                ;
    }
}
