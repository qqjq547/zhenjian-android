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
 *     time   : 2021-03-15
 *     desc   : 设置支付密码
 * </pre>
 */
public class SetPayPwdReq extends BaseReq<Object> {
    /**
     * 加密密码
     */
    private final String paypwd;

    public SetPayPwdReq(String pwd, String phone) {
        this.paypwd = MD5Utils.getMd5("${" + phone + "}" + pwd);
    }

    @Override
    public String path() {
        return "/mytio/user/setpaypwd.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("paypwd", paypwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
