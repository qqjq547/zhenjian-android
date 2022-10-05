package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRedInfoResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/25
 *     desc   :
 * </pre>
 */
public class PayRedInfoReq extends BaseReq<PayRedInfoResp> {
    /**
     * 红包的订单号（易支付）
     */
    private final String serialnumber;
    /**
     * 红包id（新生支付）
     */
    private String rid;

    // 易支付
    public PayRedInfoReq(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    private PayRedInfoReq(String serialnumber, String rid) {
        this.serialnumber = serialnumber;
        this.rid = rid;
    }

    // 新生支付
    public static PayRedInfoReq getInstance(String rid) {
        return new PayRedInfoReq(null, rid);
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("serialnumber", serialnumber)
                .append("rid", rid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayRedInfoResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/redInfo.tio_x";
    }
}
