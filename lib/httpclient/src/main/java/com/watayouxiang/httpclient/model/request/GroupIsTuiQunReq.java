package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class GroupIsTuiQunReq extends BaseReq<Object> {
    private final String leaveflag;//  // 1 允许 2 不允许
    private final String groupid;

    public GroupIsTuiQunReq(String clearmemberflag, String groupid) {
        this.leaveflag = clearmemberflag;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/group/modifyLeaveflag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("leaveflag", leaveflag)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}