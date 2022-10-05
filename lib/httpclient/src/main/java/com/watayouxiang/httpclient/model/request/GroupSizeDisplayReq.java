package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class GroupSizeDisplayReq extends BaseReq<Object> {
    private final String shownumflag;
    private final String groupid;

    public GroupSizeDisplayReq(String shownumflag1, String groupid) {
        this.shownumflag = shownumflag1;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/group/modifyShownumflag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("shownumflag", shownumflag)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}