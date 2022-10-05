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
 *     time   : 2021/02/22
 *     desc   :
 * </pre>
 */
public class ModifyFriendFlagReq extends BaseReq<Object> {
    /**
     * 群内互加好友开关：1开启，2关闭
     */
    private final String friendflag;
    private final String groupid;

    public ModifyFriendFlagReq(String friendflag, String groupid) {
        this.friendflag = friendflag;
        this.groupid = groupid;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/group/modifyFriendFlag.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("friendflag", friendflag)
                .append("groupid", groupid)
                ;
    }
}
