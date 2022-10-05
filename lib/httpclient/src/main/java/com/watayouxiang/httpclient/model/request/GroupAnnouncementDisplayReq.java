package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class GroupAnnouncementDisplayReq extends BaseReq<Object> {
    private final String goinshownoticeflag;
    private final String groupid;

    public GroupAnnouncementDisplayReq(String goinshownoticeflag, String groupid) {
        this.goinshownoticeflag = goinshownoticeflag;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/group/modifyGoinshownoticeflag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("goinshownoticeflag", goinshownoticeflag)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}