package com.watayouxiang.httpclient.model.request;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.ResetPwdResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/24
 *     desc   :
 * </pre>
 */
public class ResetPwdReq extends BaseReq<ResetPwdResp> {
    private final String code;
    private final String phone;
    /**
     * 手机加密密码
     */
    private final String phonepwd;
    /**
     * 邮箱加密密码
     */
    private String emailpwd;

    public ResetPwdReq(String code, String pwd, String phone, @Nullable String email) {
        this.code = code;
        this.phone = phone;
        this.phonepwd = MD5Utils.getMd5("${" + phone + "}" + pwd);
        if (!TextUtils.isEmpty(email)) {
            this.emailpwd = MD5Utils.getMd5("${" + email + "}" + pwd);
        }
    }

    @Override
    public String path() {
        return "/mytio/user/resetPwd.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("code", code)
                .append("phone", phone)
                .append("pwd", phonepwd)
                .append("emailpwd", emailpwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<ResetPwdResp>>() {
        }.getType();
    }
}
