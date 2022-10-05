package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class CheckCardKefuXiaoZuReq extends BaseReq<Object> {
    private final String teamid;

    public CheckCardKefuXiaoZuReq(String teamid) {
        this.teamid = teamid;
    }

    @Override
    public String path() {
        return "/mytio/custserviceteam/addCustTeamFriend.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("teamid", teamid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}