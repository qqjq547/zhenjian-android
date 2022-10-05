package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/18
 *     desc   : 红包支付-快捷支付-发短信
 * </pre>
 */
public class PayQuickRedPacketReq extends BaseReq<PayQuickRedPacketResp> {

    // 快捷支付的协议号
    private final String agrno;
    // 红包id
    private final String rid;

    public PayQuickRedPacketReq(String agrno, String rid) {
        this.agrno = agrno;
        this.rid = rid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("agrno", agrno)
                .append("rid", rid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayQuickRedPacketResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/quickredpacket.tio_x";
    }
}
