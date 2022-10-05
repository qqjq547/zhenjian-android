package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/17
 *     desc   : 充值-预下单(发短信业务)
 * </pre>
 */
public class PayRechargeReq extends BaseReq<PayRechargeResp> {
    /**
     * 充值金额，字符串，分
     */
    private final String amount;
    /**
     * 备注，选填
     */
    private String remark;
    /**
     * 充值的卡协议号（新生支付）
     */
    private String agrno;

    // 易支付
    public PayRechargeReq(String amount) {
        this.amount = amount;
    }

    // 新生支付
    public PayRechargeReq(String amount, String agrno) {
        this.amount = amount;
        this.agrno = agrno;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("amount", amount)
                .append("remark", remark)
                .append("agrno", agrno)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayRechargeResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/recharge.tio_x";
    }
}
