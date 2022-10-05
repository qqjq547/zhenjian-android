package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class GroupVipClearReq extends BaseReq<Object> {
    private final String clearmemberflag;//  // 1 自动清理 2 不自动清理
    private final String groupid;

    public GroupVipClearReq(String clearmemberflag, String groupid) {
        this.clearmemberflag = clearmemberflag;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/group/modifyClearmemberflag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("clearmemberflag", clearmemberflag)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}