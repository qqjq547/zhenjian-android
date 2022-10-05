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
 * author : TaoWang
 * date : 2020/3/13
 * desc : 修改密码
 * <p>
 * 兼容老版本的"修改密码"
 */
public class UpdatePwdReq extends BaseReq<Void> {
    /**
     * 原始密码-明文
     */
    private final String initPwd;
    /**
     * 新的手机号密码-加密
     */
    private final String newPwd;
    /**
     * 新的邮箱密码-又邮箱必传-加密
     */
    private String emailpwd;

    public UpdatePwdReq(String initPwd, String newPwd, String phone, @Nullable String email) {
        this.initPwd = initPwd;
        this.newPwd = MD5Utils.getMd5("${" + phone + "}" + newPwd);
        if (!TextUtils.isEmpty(email)) {
            this.emailpwd = MD5Utils.getMd5("${" + email + "}" + newPwd);
        }
    }

    @Override
    public String path() {
        return "/mytio/user/updatePwd.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("initPwd", initPwd)
                .append("newPwd", newPwd)
                .append("emailpwd", emailpwd)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Void>>() {
        }.getType();
    }
}
