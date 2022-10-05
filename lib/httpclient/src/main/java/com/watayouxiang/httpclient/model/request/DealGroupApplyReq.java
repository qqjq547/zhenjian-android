package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/09
 *     desc   : 处理入群申请
 * </pre>
 */
public class DealGroupApplyReq extends BaseReq<Object> {
    private final String mid;
    private final String aid;

    public DealGroupApplyReq(String mid, String aid) {
        this.mid = mid;
        this.aid = aid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("aid", aid)
                .append("mid", mid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/chat/dealGroupApply.tio_x";
    }
}
