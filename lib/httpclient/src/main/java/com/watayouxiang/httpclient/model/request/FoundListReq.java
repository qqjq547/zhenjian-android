package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;


import java.lang.reflect.Type;
import java.util.List;

public class FoundListReq extends BaseReq<FoundListResp> {

    /**
     * 搜索字段
     */
    private final String searchKey;

    public FoundListReq(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<FoundListResp>>() {
        }.getType();
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("searchKey", searchKey);
    }

    @Override
    public String path() {
        return "/mytio/findPage/listAll";
    }

}
