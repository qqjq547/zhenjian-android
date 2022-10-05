package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.AccountRegisterResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.io.File;
import java.lang.reflect.Type;

public class AccountNewRegisterReq extends BaseReq<Void> {
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
    private  String code;

    /**
     * 邀请码
     */
    private final String friendinvitecode;

    /**
     * 是否同意协议
     */
    private  String agreement;
    private String sex;
    private String avatarFile;

    public AccountNewRegisterReq(String loginname, String phone, String nick, String pwd,String codenew, String friendinvitecode,String sex,String avatarFile) {
        this.loginname = loginname;
        this.phone = phone;
        this.nick = nick;
        this.code=codenew;
        this.pwd = MD5Utils.getMd5("${" + loginname + "}" + pwd);
        this.friendinvitecode = friendinvitecode;
        this.sex = sex;
        this.avatarFile = avatarFile;
    }



    @Override
    public String path() {
        return "/mytio/register/3.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("loginname", loginname)
                .append("phone", phone)
                .append("nick", nick)
                .append("pwd", pwd)
                .append("code", code)
                .append("sex",sex)
                .append("friendinvitecode", friendinvitecode)
                .append("agreement", "on");
    }

    @Override
    public TioMap<String, File> files() {
        return super.files().append("avatarFile", new File(avatarFile));
    }



    public String getLoginname() {
        return loginname;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Void>>() {
        }.getType();
    }
}