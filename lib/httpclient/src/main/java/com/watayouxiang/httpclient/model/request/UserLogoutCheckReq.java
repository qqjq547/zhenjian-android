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
 *     desc   : 注销验证
 * </pre>
 */
public class UserLogoutCheckReq extends BaseReq<Object> {
    private final String uid;

    public UserLogoutCheckReq(String uid) {
        this.uid = uid;
    }

    @Override
    public String path() {
        return "/mytio/user/logoutcheck.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("uid", uid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
