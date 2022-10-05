package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.GroupApplyInfoResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/09
 *     desc   : 入群申请信息
 * </pre>
 */
public class GroupApplyInfoReq extends BaseReq<GroupApplyInfoResp> {
    private final String aid;

    public GroupApplyInfoReq(String aid) {
        this.aid = aid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("aid", aid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<GroupApplyInfoResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/chat/groupApplyInfo.tio_x";
    }
}
