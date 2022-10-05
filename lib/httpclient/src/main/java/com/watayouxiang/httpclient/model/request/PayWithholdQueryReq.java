package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayWithholdQueryResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   :
 * </pre>
 */
public class PayWithholdQueryReq extends BaseReq<PayWithholdQueryResp> {

    private String serialnumber;
    // 订单id
    private String wid;
    // 请求id
    private String reqid;

    // 易支付
    public PayWithholdQueryReq(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    // 新生支付
    public PayWithholdQueryReq(String wid, String reqid) {
        this.wid = wid;
        this.reqid = reqid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("serialnumber", serialnumber)
                .append("wid", wid)
                .append("reqid", reqid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayWithholdQueryResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/withholdQuery.tio_x";
    }
}
