package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.EmojiAllResp;

import java.lang.reflect.Type;

public class EmojiAllReq extends BaseReq<EmojiAllResp> {
    private final String searchKey;

    public EmojiAllReq(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public String path() {
        return "/mytio/emoji/all.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params().append("searchKey", searchKey);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<EmojiAllResp>>() {
        }.getType();
    }
}