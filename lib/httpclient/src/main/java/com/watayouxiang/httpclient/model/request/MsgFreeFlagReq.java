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
 *     time   : 2021/02/02
 *     desc   : 会话免打扰操作
 * </pre>
 */
public class MsgFreeFlagReq extends BaseReq<Object> {

    private final String touid;
    private final String groupid;
    /**
     * freeflag:1:开启免打扰，2：不开启
     */
    private final String freeflag;

    public MsgFreeFlagReq(String touid, String groupid, String freeflag) {
        this.touid = touid;
        this.groupid = groupid;
        this.freeflag = freeflag;
    }

    public static MsgFreeFlagReq getInstance_P2P(String touid, String freeflag) {
        return new MsgFreeFlagReq(touid, null, freeflag);
    }

    public static MsgFreeFlagReq getInstance_Group(String groupid, String freeflag) {
        return new MsgFreeFlagReq(null, groupid, freeflag);
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("touid", touid)
                .append("groupid", groupid)
                .append("freeflag", freeflag)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/chat/msgfreeflag.tio_x";
    }
}
