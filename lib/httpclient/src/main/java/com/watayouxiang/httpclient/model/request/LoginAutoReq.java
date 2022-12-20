package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.LoginResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * author : TaoWang
 * date : 2020-01-06
 * desc : 登录请求
 *
 * @see LoginResp
 */
public class LoginAutoReq extends BaseReq<LoginResp> {

    private final String imei;
    private final String inviteCode;
    private final String registerType="3";

    private LoginAutoReq(String imei, String inviteCode) {
        this.imei = imei;
        this.inviteCode = inviteCode;

    }

    public static LoginAutoReq getInstance(String imei, String inviteCode) {
        return new LoginAutoReq(imei, inviteCode);
    }

    @Override
    public String path() {
        return "/mytio/oneClick/autoLoginByImei.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("imei", imei)
                .append("registerType", "2")
                .append("inviteCode", inviteCode);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<LoginResp>>() {
        }.getType();
    }
}
