package com.tiocloud.chat.feature.session.p2p.fragment.msg;

import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.chat.util.TimeUtil;
import com.watayouxiang.androidutils.utils.HtmlUtils;
import com.watayouxiang.imclient.model.MsgTemplate;
import com.watayouxiang.imclient.model.body.wx.WxFriendMsgResp;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgType;

/**
 * author : TaoWang
 * date : 2020-02-11
 * desc :
 */
public class HistoryP2PMsg extends TioMsg {
    private final WxFriendMsgResp.DataBean item;
    private final String currUid;
    private final String currNick;
    private final String chatlinkid;

    public HistoryP2PMsg(WxFriendMsgResp.DataBean list, String currUid, String currNick, String chatlinkid) {
        this.item = list;
        this.currUid = currUid;
        this.currNick = currNick;
        this.chatlinkid = chatlinkid;
        setReadMsg(isReceiptInternal());
    }

    @Override
    public String getId() {
        return item.mid;
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
            case TEMPLATE:
                return TioMsgType.template;
            default:
                return TioMsgType.unknown;
        }
    }

    @Override
    public boolean isSendMsg() {
        return StringUtil.equals(String.valueOf(item.uid), currUid);
    }

    @Override
    public String getAvatar() {
        if (getMsgType() == TioMsgType.tip) {
            return null;
        }
        return StringUtil.nonNull(item.avatar);
    }

    @Override
    public String getName() {
        return item.nick;
    }

    @Override
    public boolean isShowName() {
        return false;
    }

    @Override
    public Long getTime() {
        return TimeUtil.dateString2Long(item.t);
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
            case TEMPLATE:
                return item.temp;
        }
        return null;
    }

    @Override
    public String getUid() {
        return String.valueOf(item.uid);
    }

    @Override
    public String getChatLinkId() {
        return chatlinkid;
    }

    private Boolean isReceiptInternal() {
        TioMsgType msgType = getMsgType();
        if (msgType == TioMsgType.tip || msgType == TioMsgType.redPaper) {
            return null;
        }
        return item.readflag == 1;
    }
}
