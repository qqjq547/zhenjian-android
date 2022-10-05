package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class CheckFriendInviteCodeReq extends BaseReq<String> {
    private final String friendInviteCode;

    public CheckFriendInviteCodeReq(String friendInviteCode) {
        this.friendInviteCode = friendInviteCode;
    }

    @Override
    public String path() {
        return "/mytio/register/checkFriendInviteCode.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("friendInviteCode", friendInviteCode);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<String>>() {
        }.getType();
    }
}