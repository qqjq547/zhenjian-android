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
 *     time   : 2/8/21
 *     desc   : 申请入群
 * </pre>
 */
public class JoinGroupApplyReq extends BaseReq<Object> {
    private final String uids;
    private final String groupid;
    private final String applymsg;

    public JoinGroupApplyReq(String uids, String groupid, String applymsg) {
        this.uids = uids;
        this.groupid = groupid;
        this.applymsg = applymsg;
    }

    @Override
    public String path() {
        return "/mytio/chat/joinGroupApply.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("uids", uids)
                .append("groupid", groupid)
                .append("applymsg", applymsg)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }
}
