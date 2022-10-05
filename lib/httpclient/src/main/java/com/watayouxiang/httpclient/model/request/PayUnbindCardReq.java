package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayUnbindCardResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   : 解绑
 * </pre>
 */
public class PayUnbindCardReq extends BaseReq<PayUnbindCardResp> {

    // 卡列表返回值id
    private final String bankcardid;
    // 协议绑卡agrno
    private final String agrno;
    // 加密同登录
    private final String paypwd;

    public PayUnbindCardReq(String bankcardid, String agrno, String paypwd, String loginname) {
        this.bankcardid = bankcardid;
        this.agrno = agrno;
        this.paypwd = MD5Utils.getMd5("${" + loginname + "}" + paypwd);
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("bankcardid", bankcardid)
                .append("agrno", agrno)
                .append("paypwd", paypwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayUnbindCardResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/unbindcard.tio_x";
    }
}
