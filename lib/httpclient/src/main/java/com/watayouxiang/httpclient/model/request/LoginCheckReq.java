package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.LoginResp;

import java.lang.reflect.Type;

/**
 * author : TaoWang
 * date : 2020-01-06
 * desc : 登录请求
 *
 * @see LoginResp
 */
public class LoginCheckReq extends BaseReq<Boolean> {

    private final String imei;

    private LoginCheckReq(String imei) {
        this.imei = imei;
    }

    public static LoginCheckReq getInstance(String imei) {
        return new LoginCheckReq(imei);
    }

    @Override
    public String path() {
        return "/mytio/oneClick/checkImeiExist.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("imei", imei);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Boolean>>() {
        }.getType();
    }
}
