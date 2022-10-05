package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayBindCardConfirmResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   : 发起绑卡 - 确认
 * </pre>
 */
public class PayBindCardConfirmReq extends BaseReq<PayBindCardConfirmResp> {

    // 发起绑卡返回值id
    private final String bankcardid;
    // 短信验证码
    private final String smscode;
    // 发起绑卡返回值
    private final String merorderid;

    public PayBindCardConfirmReq(String bankcardid, String smscode, String merorderid) {
        this.bankcardid = bankcardid;
        this.smscode = smscode;
        this.merorderid = merorderid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("bankcardid", bankcardid)
                .append("smscode", smscode)
                .append("merorderid", merorderid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayBindCardConfirmResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/bindcardconfirm.tio_x";
    }
}
