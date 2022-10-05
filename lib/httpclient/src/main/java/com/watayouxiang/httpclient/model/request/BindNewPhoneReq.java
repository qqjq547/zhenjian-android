package com.watayouxiang.httpclient.model.request;

import android.text.TextUtils;

import androidx.annotation.Nullable;

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
 *     time   : 2021/01/20
 *     desc   : 更换新手机号
 * </pre>
 */
public class BindNewPhoneReq extends BaseReq<Object> {
    /**
     * 验证码
     */
    private final String code;
    /**
     * 新手机号
     */
    private final String phone;
    /**
     * 手机密码,必填手机加密密码
     */
    private final String phonepwd;
    /**
     * 邮箱密码,验证密码时必填
     */
    private String emailpwd;

    public BindNewPhoneReq(String code, String phone, @Nullable String email, String pwd) {
        this.code = code;
        this.phone = phone;
        this.phonepwd = MD5Utils.getMd5("${" + phone + "}" + pwd);
        if (!TextUtils.isEmpty(email)) {
            this.emailpwd = MD5Utils.getMd5("${" + email + "}" + pwd);
        }
    }

    @Override
    public String path() {
        return "/mytio/user/bindnewphone.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("code", code)
                .append("phone", phone)
                .append("phonepwd", phonepwd)
                .append("emailpwd", emailpwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
