package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRedStatusResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/24
 *     desc   :
 * </pre>
 */
public class PayRedStatusReq extends BaseReq<PayRedStatusResp> {
    /**
     * 红包的订单号（易支付）
     */
    private final String serialnumber;
    /**
     * 红包id（新生支付）
     */
    private String rid;

    public PayRedStatusReq(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    private PayRedStatusReq(String serialnumber, String rid) {
        this.serialnumber = serialnumber;
        this.rid = rid;
    }

    public static PayRedStatusReq getInstance(String rid) {
        return new PayRedStatusReq(null, rid);
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
        return new TypeToken<BaseResp<PayRedStatusResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/redStatus.tio_x";
    }
}
