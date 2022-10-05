package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;

import java.lang.reflect.Type;

public class RegisterNewflagReq extends BaseReq<Object> {
    @Override
    public String path() {
        return "/mytio/register/validParam.tio_x";
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}