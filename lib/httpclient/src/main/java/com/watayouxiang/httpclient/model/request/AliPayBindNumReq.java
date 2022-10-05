package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayBindCardResp;

import java.lang.reflect.Type;

public class AliPayBindNumReq extends BaseReq<Object> {
    private final String alipay;
    private final String alipayname;
    public AliPayBindNumReq(String alipay, String alipayname ) {
        this.alipay = alipay;
        this.alipayname = alipayname;

    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("alipay", alipay)
                .append("alipayname", alipayname)
                ;
    }
    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/bindAliPay.tio_x";
    }
}