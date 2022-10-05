package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.EmojiCollectResp;

import java.lang.reflect.Type;

public class RemoveCollectEmojiReq extends BaseReq<String> {
    private String imgid;

    public RemoveCollectEmojiReq( String mids) {
        this.imgid = mids;
    }


    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<EmojiCollectResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/emoji/del.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("imgid", imgid);
    }
}