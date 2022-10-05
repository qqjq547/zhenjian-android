package com.tiocloud.chat.feature.session.p2p.fragment.mvp;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioErrorMsg;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioNotFriendMsg;
import com.tiocloud.chat.feature.session.p2p.P2PSessionActivity;
import com.tiocloud.chat.feature.session.p2p.fragment.msg.P2PMsg;
import com.tiocloud.chat.preferences.TioMemory;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.ReadAckReq;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxFaultItem;
import com.watayouxiang.imclient.model.body.wx.WxFriendChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;
import com.watayouxiang.imclient.model.body.wx.WxFriendMsgResp;
import com.watayouxiang.imclient.model.body.wx.WxHandshakeResp;
import com.watayouxiang.imclient.model.body.wx.WxSessionOperReq;
import com.watayouxiang.imclient.model.body.wx.WxUserOperNtf;
import com.watayouxiang.imclient.packet.TioPacketBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-02-11
 * desc :
 */
public class P2PFragmentPresenter extends P2PFragmentContract.Presenter {

    private final String currUid = String.valueOf(TioDBPreferences.getCurrUid());
    private final String currNick = CurrUserTableCrud.curr_getNick();
    private String mNextStartMid;

    public P2PFragmentPresenter(P2PFragmentContract.View view) {
        super(new P2PFragmentModel(), view, true);
        // 进入房间
        TioIMClient.getInstance().sendPacket(TioPacketBuilder.getWxSessionOperReq(WxSessionOperReq.in(getView().getChatLinkId())));
        TioMemory.setInChatLinkId(getView().getChatLinkId());
    }

    @Override
    public void detachView() {
        super.detachView();
        // 离开房间
        TioIMClient.getInstance().sendPacket(TioPacketBuilder.getWxSessionOperReq(WxSessionOperReq.out()));
        TioMemory.setInChatLinkId(null);
    }

    // ====================================================================================
    // event
    // ====================================================================================

    // 私聊通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxFriendChatNtf(WxFriendChatNtf wxFriendChatNtf) {
        if (!getView().getChatLinkId().equals(wxFriendChatNtf.chatlinkid)) return;
        getView().getMsgListProxy().sendMsg(new P2PMsg(wxFriendChatNtf, currUid, currNick));
    }

    // 异常通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxFriendErrorNtf(WxFriendErrorNtf ntf) {
        if (!StringUtils.equals(ntf.chatlinkid, getView().getChatLinkId())) return;
        if (ntf.getCode() == WxFriendErrorNtf.Code.NO_LINK) {
            String toUid = getToUid();
            if (toUid == null) {
                TioToast.showShort("好友id获取失败");
            } else {
                // 好友删除了你
                // 提示你还不是ta的好友，发送好友验证
                getView().getMsgListProxy().sendMsg(new TioNotFriendMsg(ntf, toUid));
            }
        } else {
            // 统一返回首页
            //MainActivity.start(getView().getActivity());

            // 4/15/21: 发送错误提示消息
            getView().getMsgListProxy().sendMsg(new TioErrorMsg(ntf));
        }
    }

    // 用户操作通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserOperNtf(WxUserOperNtf ntf) {
        if (!StringUtils.equals(ntf.chatlinkid, getView().getChatLinkId())) return;
        if (ntf.oper == 9) {
            // 撤回消息
            getView().getMsgListProxy().deleteMsg(Long.parseLong(ntf.operbizdata));
        } else if (ntf.oper == 10) {
            // 删除消息
            getView().getMsgListProxy().deleteMsg(Long.parseLong(ntf.operbizdata));
        } else if (ntf.oper == 7) {
            // 已读通知
            getView().getMsgListProxy().readAllMsg();
            // 会话已读ack
            new ReadAckReq(getView().getChatLinkId()).get(new TioCallbackImpl<>());
        } else if (ntf.oper == 8) {
            // 清空聊天消息
            refresh();
        }
    }

    // 握手响应
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxHandshakeResp(WxHandshakeResp wxHandshakeResp) {
        refresh();
    }




    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    public void init() {
        // 初始化列表
        getView().initList();
        // 加载数据
        refresh();
    }

    @Override
    public void refresh() {
        getMsgList(null);
    }

    @Override
    public void loadMore() {
        if (mNextStartMid != null) {
            getMsgList(mNextStartMid);
        }
    }

    private void getMsgList(String startMid) {
        getModel().getTioP2PMsgList(getView().getChatLinkId(), startMid, new BaseModel.DataProxy<TioP2PMsgList>() {
            @Override
            public void onSuccess(TioP2PMsgList msgList) {
                WxFriendMsgResp msgResp = msgList.msgResp;
                List<TioMsg> tioMsgList = msgList.tioMsgList;

                // 获取下一页 mid
                List<WxFriendMsgResp.DataBean> data = msgResp.data;
                if (data.size() > 0) {
                    mNextStartMid = data.get(data.size() - 1).mid;
                }

                // 如果是刷新，则先清除旧数据
                if (startMid == null) {
                    getView().getMsgListProxy().clearList();
                }

                // 设置新数据
                if (msgResp.lastPage || tioMsgList.size() == 0) {
                    getView().getMsgListProxy().fetchMoreEnd(tioMsgList);
                } else {
                    getView().getMsgListProxy().fetchMoreComplete(tioMsgList);
                }

                // 如果是第一页，则需要定位到底部
                if (startMid == null) {
                    getView().getMsgListProxy().scrollToBottom();
                }
            }
        });
    }

    // ====================================================================================
    // private
    // ====================================================================================

    private String getToUid() {
        String toUid = null;
        P2PSessionActivity activity = getView().getP2PSessionActivity();
        if (activity != null) {
            toUid = activity.getUid();
        }
        return toUid;
    }
}
