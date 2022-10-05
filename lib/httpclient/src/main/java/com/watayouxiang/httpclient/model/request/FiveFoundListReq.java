package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class FiveFoundListReq extends BaseReq<FiveFoundListResp> {
    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<FiveFoundListResp>>() {
        }.getType();
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap();
    }

    @Override
    public String path() {
        return "/mytio/findPage/index.tio_x";
    }
}