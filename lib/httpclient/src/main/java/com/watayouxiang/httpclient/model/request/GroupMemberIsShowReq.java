package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class GroupMemberIsShowReq extends BaseReq<Object> {
    private final String groupmembershowflag;// // 1 显示 2 不显示
    private final String groupid;

    public GroupMemberIsShowReq(String groupmembershowflag, String groupid) {
        this.groupmembershowflag = groupmembershowflag;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/group/modifyGroupmembershowflag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("groupmembershowflag", groupmembershowflag)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}