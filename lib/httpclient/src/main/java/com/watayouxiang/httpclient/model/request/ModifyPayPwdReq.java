package com.watayouxiang.httpclient.model.request;

import android.util.Log;

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
 *     desc   : 修改支付密码
 * </pre>
 */
public class ModifyPayPwdReq extends BaseReq<Object> {
    /**
     * 原始明文密码
     */
    private final String initPwd;
    /**
     * 支付密码-同登录密码加密
     */
    private final String newPwd;

    public ModifyPayPwdReq(String initPwd, String newPwd, String loginname) {
        this.initPwd = initPwd;
        this.newPwd = MD5Utils.getMd5("${" + loginname + "}" + newPwd);

    }

    @Override
    public String path() {
        return "/mytio/user/updatepaypwd.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("initPwd", initPwd)
                .append("newPwd", newPwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
