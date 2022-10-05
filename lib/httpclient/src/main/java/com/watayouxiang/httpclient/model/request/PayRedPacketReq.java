package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/18
 *     desc   : 红包支付 - 确认接口
 * </pre>
 */
public class PayRedPacketReq extends BaseReq<PayRedPacketResp> {

    // 快捷支付确认短信验证码
    private final String smscode;
    // 快捷支付确认订单
    private final String merorderid;
    // 支付密码
    private final String paypwd;
    // 红包id
    private final String rid;
    // 支付类型：1：余额；2：快捷支付
    private final String paytype;

    public PayRedPacketReq(String smscode, String merorderid, String paypwd, String rid, String paytype, String loginname) {
        this.smscode = smscode;
        this.merorderid = merorderid;
        this.paypwd = MD5Utils.getMd5("${" + loginname + "}" + paypwd);
        this.rid = rid;
        this.paytype = paytype;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("smscode", smscode)
                .append("merorderid", merorderid)
                .append("paypwd", paypwd)
                .append("rid", rid)
                .append("paytype", paytype)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayRedPacketResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/payredpacket.tio_x";
    }
}
