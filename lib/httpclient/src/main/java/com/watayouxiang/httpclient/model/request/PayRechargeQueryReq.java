package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRechargeQueryResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   :
 * </pre>
 */
public class PayRechargeQueryReq extends BaseReq<PayRechargeQueryResp> {
    private String serialnumber;
    private String rid;
    private String reqid;

    // 易支付
    public PayRechargeQueryReq(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    // 新生支付
    public PayRechargeQueryReq(String rid, String reqid) {
        this.rid = rid;
        this.reqid = reqid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("serialnumber", serialnumber)
                .append("rid", rid)
                .append("reqid", reqid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayRechargeQueryResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/rechargeQuery.tio_x";
    }
}
