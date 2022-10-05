package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021-03-16
 *     desc   : 找回支付密码
 * </pre>
 */
public class ResetPayPwdReq extends BaseReq<Object> {
    /**
     * 短信验证码-10
     */
    private final String code;
    /**
     * 支付密码-同登录密码加密
     */
    private final String paypwd;

    public ResetPayPwdReq(String code, String paypwd, String phone) {
        this.code = code;
        this.paypwd = MD5Utils.getMd5("${" + phone + "}" + paypwd);
    }

    @Override
    public String path() {
        return "/mytio/user/resetpaypwd.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("code", code)
                .append("paypwd", paypwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
