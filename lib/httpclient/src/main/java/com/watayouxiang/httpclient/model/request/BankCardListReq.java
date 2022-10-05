package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.BankCardListResp;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;

import java.lang.reflect.Type;

public class BankCardListReq extends BaseReq<BankCardListResp> {
    private final String pageNumber;

    public BankCardListReq( String pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("searchKey", "")
                .append("pageNumber", pageNumber)
                .append("pageSize", "100")

                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<BankCardListResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/banklist.tio_x";
    }
}