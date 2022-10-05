package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.GetAliPayDataResp;
import com.watayouxiang.httpclient.model.response.PayOpenFlagResp;

import java.lang.reflect.Type;

/**
 * 获取支付宝绑定信息
 */
public class AliPayGetDataReq extends BaseReq<GetAliPayDataResp> {

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap();
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<GetAliPayDataResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/aliPayInfo.tio_x";
    }
}