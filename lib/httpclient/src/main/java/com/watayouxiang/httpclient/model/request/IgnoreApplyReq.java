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
 *     time   : 2021/02/08
 *     desc   :
 * </pre>
 */
public class IgnoreApplyReq extends BaseReq<Object> {
    private final String applyid;

    public IgnoreApplyReq(String applyid) {
        this.applyid = applyid;
    }

    @Override
    public String path() {
        return "/mytio/friend/ignoreApply.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("applyid", applyid);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
