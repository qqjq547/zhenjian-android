package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.BindPhoneResp;

import java.lang.reflect.Type;

public class RevocationAllMessageReq extends BaseReq<String> {
    private String uids;
    /**
     * 群id
     */
    private String groupid;

    public RevocationAllMessageReq(String uids, String groupid) {
        this.uids = uids;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/chat/recallAllMsg.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("uid", uids)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<String>>() {
        }.getType();
    }
}