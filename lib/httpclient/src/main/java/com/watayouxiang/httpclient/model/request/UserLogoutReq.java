package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 4/16/21
 *     desc   : 注销
 * </pre>
 */
public class UserLogoutReq extends BaseReq<Object> {
    private final String uid;
    private final String code;

    public UserLogoutReq(String uid, String code) {
        this.uid = uid;
        this.code = code;
    }

    @Override
    public String path() {
        return "/mytio/user/logout.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("uid", uid)
                .append("code", code)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
