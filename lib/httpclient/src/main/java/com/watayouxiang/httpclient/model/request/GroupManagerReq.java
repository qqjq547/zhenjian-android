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
 *     time   : 2021/01/06
 *     desc   : 群管理员操作
 * </pre>
 */
public class GroupManagerReq extends BaseReq<Object> {
    private final String uid;
    private final String groupid;
    /**
     * 角色：2：普通成员；3：管理员
     */
    private final String grouprole;

    public GroupManagerReq(String uid, String groupid, String grouprole) {
        this.uid = uid;
        this.groupid = groupid;
        this.grouprole = grouprole;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("uid", uid)
                .append("groupid", groupid)
                .append("grouprole", grouprole)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/group/manager.tio_x";
    }
}
