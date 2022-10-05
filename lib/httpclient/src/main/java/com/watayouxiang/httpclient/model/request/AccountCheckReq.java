package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class AccountCheckReq extends BaseReq<Object> {

    private final String loginname;

    public AccountCheckReq(String loginname) {
        this.loginname = loginname;

    }

    @Override
    public String path() {
        return "/mytio/register/checkLoginName.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("loginname", loginname);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}