package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRealInfoResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   :
 * </pre>
 */
public class PayRealInfoReq extends BaseReq<PayRealInfoResp> {

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayRealInfoResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/realinfo.tio_x";
    }
}
