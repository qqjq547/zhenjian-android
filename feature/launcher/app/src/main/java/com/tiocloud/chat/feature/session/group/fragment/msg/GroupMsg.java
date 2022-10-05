package com.tiocloud.chat.feature.session.group.fragment.msg;

import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.HtmlUtils;
import com.watayouxiang.imclient.model.MsgTemplate;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgType;

/**
 * author : TaoWang
 * date : 2020-02-07
 * desc :
 */
public class GroupMsg extends TioMsg {

    private final WxGroupChatNtf item;
    private final String currUid;
    private final String currNick;

    public GroupMsg(WxGroupChatNtf item, String currUid, String currNick) {
        this.item = item;
        this.currUid = currUid;
        this.currNick = currNick;
    }

    @Override
    public String getId() {
        return String.valueOf(item.mid);
    }

    @Override
    public TioMsgType getMsgType() {
        if (item.sendbysys == 1) {
            return TioMsgType.tip;
        }
        InnerMsgType contentType = InnerMsgType.valueOf(item.ct);
        if (contentType == null) return TioMsgType.unknown;
        switch (contentType) {
            case VIDEO:
                return TioMsgType.video;
            case IMAGE:
                return TioMsgType.image;
            case AUDIO:
                return TioMsgType.audio;
            case TEXT:
                return TioMsgType.text;
            case FILE:
                return TioMsgType.file;
            case CARD:
                return TioMsgType.card;
            case BLOG:
                return TioMsgType.blog;
            case CALL_AUDIO:
            case CALL_VIDEO:
                return TioMsgType.call;
            case RED_PAPER:
                return TioMsgType.redPaper;
            case JOIN_GROUP_APPLY:
                return TioMsgType.joinGroupApply;
            case TEMPLATE:
                return TioMsgType.template;
            default:
                return TioMsgType.unknown;
        }
    }

    @Override
    public boolean isSendMsg() {
        return StringUtil.equals(currUid, String.valueOf(item.f));
    }

    @Override
    public String getAvatar() {
        TioMsgType msgType = getMsgType();
        if (msgType == TioMsgType.tip
                || msgType == TioMsgType.joinGroupApply) {
            return null;
        }
        return StringUtil.nonNull(item.avatar);
    }

    @Override
    public String getName() {
        return String.valueOf(item.nick);
    }

    @Override
    public boolean isShowName() {
        TioMsgType msgType = getMsgType();
        if (msgType == TioMsgType.tip// 提示消息不现实昵称
                || isSendMsg()// 发送的消息不现实昵称
                || msgType == TioMsgType.joinGroupApply// 入群申请消息不现实昵称
        ) {
            return false;
        }
        return true;
    }

    @Override
    public String getAitName() {
        return String.valueOf(item.nick);
    }

    @Override
    public Long getTime() {
        return item.t;
    }

    @Override
    public String getContent() {
        if (getMsgType() == TioMsgType.tip) {
            String tipMsg = MsgTemplate.getTipMsg(item.sysmsgkey, item.opernick, item.tonicks, currNick);
            if (tipMsg != null) {
                return tipMsg;
            }
        }
        return HtmlUtils.unescapeHtml(item.c);
    }

    @Override
    public Object getContentObj() {
        InnerMsgType contentType = InnerMsgType.valueOf(item.ct);
        if (contentType == null) return null;
        switch (contentType) {
            case CALL_VIDEO:
            case CALL_AUDIO:
                return item.call;
            case CARD:
                return item.cardc;
            case FILE:
                return item.fc;
            case TEXT:
                return item.c;
            case IMAGE:
                return item.ic;
            case VIDEO:
                return item.vc;
            case BLOG:
                return null;
            case AUDIO:
                return item.ac;
            case RED_PAPER:
                return item.red;
            case JOIN_GROUP_APPLY:
                return item.apply;
            case TEMPLATE:
                return item.temp;
        }
        return null;
    }

    @Override
    public String getUid() {
        return String.valueOf(item.f);
    }

    @Override
    public String getChatLinkId() {
        return item.chatlinkid;
    }
}
