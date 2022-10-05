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
 *     desc   : 邀请审核开关
 * </pre>
 */
public class ModifyReviewReq extends BaseReq<Object> {
    /**
     * 1 开启邀请审核；2 关闭邀请审核
     */
    private final String mode;
    private final String groupid;

    public ModifyReviewReq(String mode, String groupid) {
        this.mode = mode;
        this.groupid = groupid;
    }

    @Override
    public String path() {
        return "/mytio/group/modifyReview.tio_x";
    }

    @Override
    public TioMap<String, String> params() {
        return super.params()
                .append("mode", mode)
                .append("groupid", groupid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Object>>() {
        }.getType();
    }

}
