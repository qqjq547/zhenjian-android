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
 *     time   : 4/15/21
 *     desc   : 意见反馈
 * </pre>
 */
public class AdviseReq extends BaseReq<String> {
    private final String reason;

    public AdviseReq(String reason) {
        this.reason = reason;
    }

    @Override
    public String path() {
        return "/mytio/sys/advise.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("reason", reason)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<String>>() {
        }.getType();
    }
}
