package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.CustServiceTeamListResp;

import java.lang.reflect.Type;

public class CustServiceSearchReq extends BaseReq<CustServiceTeamListResp> {
    private  int pageNumber=1;
    private  int pageSize=10;
    private String searchkey;

    public CustServiceSearchReq(String searchkey) {
        this.searchkey = searchkey;
    }
    public CustServiceSearchReq(String searchkey,int pageNumber) {
        this.searchkey = searchkey;
        this.pageNumber = pageNumber;
    }

    @Override
    public String path() {
        return "/mytio/custserviceteam/search.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("searchkey", searchkey);
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<CustServiceTeamListResp>>() {
        }.getType();
    }
}