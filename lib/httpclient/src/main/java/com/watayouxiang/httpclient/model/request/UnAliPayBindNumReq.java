package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

public class UnAliPayBindNumReq extends BaseReq<Object> {
    private final String paypwd;

    public UnAliPayBindNumReq(String paypwd, String loginname) {
        this.paypwd = MD5Utils.getMd5("${" + loginname + "}" + paypwd);
    }


    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("paypwd", paypwd)
                ;
    }
    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/unbindAliPay.tio_x";
    }
}