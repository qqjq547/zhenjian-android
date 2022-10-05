package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PhoneRegisterResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 12/23/20
 *     desc   : 手机注册
 * </pre>
 */
public class PhoneRegisterReq extends BaseReq<PhoneRegisterResp> {

    /**
     * 账号
     */
    private final String loginname;

    /**
     * 手机号
     */
    private final String phone;

    /**
     * 昵称
     */
    private final String nick;
    /**
     * 密码
     */
    private final String pwd;
    /**
     * 短信验证码
     */
    private final String code;

    /**
     * 邀请码
     */
    private final String friendinvitecode;

    /**
     * 是否同意协议
     */
    private final String agreement;

    private PhoneRegisterReq(String loginname, String phone, String nick, String pwd, String agreement, String code, String friendinvitecode) {
        this.loginname = loginname;
        this.phone = phone;
        this.nick = nick;
        this.pwd = MD5Utils.getMd5("${" + loginname + "}" + pwd);
        this.agreement = agreement;
        this.code = code;
        this.friendinvitecode = friendinvitecode;
    }


    public PhoneRegisterReq(String loginname, String phone, String nick, String pwd, String code, String friendinvitecode) {
        this(loginname, phone, nick, pwd, "on", code, friendinvitecode);
    }

    @Override
    public String path() {
        return "/mytio/register/2.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("loginname", loginname)
                .append("phone", phone)
                .append("nick", nick)
                .append("pwd", pwd)
                .append("code", code)
                .append("friendinvitecode", friendinvitecode)
                .append("agreement", agreement);
    }

    public String getLoginname() {
        return loginname;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PhoneRegisterResp>>() {
        }.getType();
    }
}
