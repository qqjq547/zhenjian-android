package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRechargeConfirmResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/16
 *     desc   : 充值-确定下单
 * </pre>
 */
public class PayRechargeConfirmReq extends BaseReq<PayRechargeConfirmResp> {

    private final String smscode;
    /**
     * 商户订单号
     */
    private final String merorderid;
    /**
     * 预下单订单id
     */
    private final String rid;

    public PayRechargeConfirmReq(String smscode, String merorderid, String rid) {
        this.smscode = smscode;
        this.merorderid = merorderid;
        this.rid = rid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("smscode", smscode)
                .append("merorderid", merorderid)
                .append("rid", rid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayRechargeConfirmResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/rechargeconfirm.tio_x";
    }
}
