package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

public class GroupSystemNotificationReq extends BaseReq<Object> {
    /**
     */
    private final String qunxitongtongzhi;//1 开启 2 关闭
    private final String groupid;

    public GroupSystemNotificationReq(String qunxitongtongzhi1, String groupid) {
        this.qunxitongtongzhi = qunxitongtongzhi1;
        this.groupid = groupid;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/group/modifySysnoticeflag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("sysnoticeflag", qunxitongtongzhi)
                .append("groupid", groupid)
                ;
    }
}