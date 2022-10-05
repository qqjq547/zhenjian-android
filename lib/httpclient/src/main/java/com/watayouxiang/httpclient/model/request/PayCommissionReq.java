package com.watayouxiang.httpclient.model.request;

import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayCommissionResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/11/17
 *     desc   : 提现手续费
 * </pre>
 */
public class PayCommissionReq extends BaseReq<PayCommissionResp> {

    /**
     * 提现金额（分）
     */
    private final String amount;

    public PayCommissionReq(@Nullable String amount) {
        this.amount = amount;
    }

    public PayCommissionReq() {
        this(null);
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("amount", amount)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayCommissionResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/commission.tio_x";
    }
}
