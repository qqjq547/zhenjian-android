package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

import java.io.File;
import java.lang.reflect.Type;

public class UserOnlineReq extends BaseReq<String> {

    @Override
    public String path() {
        return "/mytio/syn/online.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params();
    }



    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<UserCurrResp>>() {
        }.getType();
    }
}