package com.tiocloud.chat.feature.session.common.adapter.msg;

import androidx.annotation.NonNull;

import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/04/15
 *     desc   :
 * </pre>
 */
public class TioErrorMsg extends TioMsg {
    @NonNull
    private final WxFriendErrorNtf ntf;

    public TioErrorMsg(@NonNull WxFriendErrorNtf ntf) {
        this.ntf = ntf;
    }

    @Override
    public String getId() {
        return ntf.mid;
    }

    @Override
    public TioMsgType getMsgType() {
        return TioMsgType.tip;
    }

    @Override
    public Long getTime() {
        return ntf.t;
    }

    @Override
    public String getContent() {
        return ntf.msg;
    }

    @Override
    public String getUid() {
        return String.valueOf(ntf.uid);
    }

    @Override
    public String getChatLinkId() {
        return ntf.chatlinkid;
    }
}
