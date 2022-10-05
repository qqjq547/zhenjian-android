package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.response.ApplyListResp;

import java.lang.reflect.Type;

public class RegisterphoneflagReq extends BaseReq<String> {
    @Override
    public String path() {
        return "/mytio/register/phoneflag.tio_x";
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<String>>() {
        }.getType();
    }
}