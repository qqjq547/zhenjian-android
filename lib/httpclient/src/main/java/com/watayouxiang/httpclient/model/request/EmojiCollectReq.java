package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.EmojiCollectResp;

import java.lang.reflect.Type;

public class EmojiCollectReq extends BaseReq<String> {
    private String chatmode;//消息的类型 1 私聊 2 群聊
    private String mids;


    public EmojiCollectReq(String chatmode, String mids) {
        this.chatmode = chatmode;
        this.mids = mids;
    }
    public EmojiCollectReq() {
    }

    public static EmojiCollectReq complaint(String chatmode, String mids) {
        EmojiCollectReq msgOperReq = new EmojiCollectReq();
        msgOperReq.chatmode = chatmode;
        msgOperReq.mids = mids;
        return msgOperReq;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<EmojiCollectResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/emoji/collect.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("chatmode", chatmode)
                .append("mid", mids);
    }
}