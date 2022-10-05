package com.tiocloud.chat.feature.session.group.fragment.mvp;

import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioErrorMsg;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.group.fragment.msg.GroupMsg;
import com.tiocloud.chat.preferences.TioMemory;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;
import com.watayouxiang.imclient.model.body.wx.WxGroupChatNtf;
import com.watayouxiang.imclient.model.body.wx.WxGroupMsgResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupOperNtf;
import com.watayouxiang.imclient.model.body.wx.WxHandshakeResp;
import com.watayouxiang.imclient.model.body.wx.WxSessionOperReq;
import com.watayouxiang.imclient.model.body.wx.WxUserOperNtf;
import com.watayouxiang.imclient.packet.TioPacketBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-02-09
 * desc :
 */
public class GroupFragmentPresenter extends GroupFragmentContract.Presenter {

    private final String currUid = String.valueOf(TioDBPreferences.getCurrUid());
    private final String currNick = CurrUserTableCrud.curr_getNick();
    private String mNextStartMid;

    public GroupFragmentPresenter(GroupFragmentContract.View view) {
        super(new GroupFragmentModel(), view, true);
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

    // 用户操作通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserOperNtf(WxUserOperNtf ntf) {
        if (!getView().getChatLinkId().equals(ntf.chatlinkid)) return;
        if (ntf.oper == 8) {// 清空聊天消息通知
            refresh();
        }
    }

    // 群操作通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxGroupOperNtf(WxGroupOperNtf ntf) {
        WxGroupOperNtf.Oper oper = WxGroupOperNtf.Oper.valueOf(ntf.oper);
        if (oper == null) return;
        switch (oper) {
            case BACK_MSG:
            case DEL_MSG:
                getView().getMsgListProxy().deleteMsg(Long.parseLong(ntf.bizdata));
                break;
            case EWMOVE_GROUP://收到通知后 删除指定好友的所有本地聊天记录 备注：收到此通知时数据库聊天记录已经清空，可手动删除当前会话聊天记录 也可以刷新当前会话的聊天记录

                refresh();

                break;
        }
    }

    // 群聊消息通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxGroupChatNtf(WxGroupChatNtf wxGroupChatNtf) {
        if (!getView().getChatLinkId().equals(wxGroupChatNtf.chatlinkid)) return;
        getView().getMsgListProxy().sendMsg(new GroupMsg(wxGroupChatNtf, currUid, currNick));

    }

    // 异常通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxFriendErrorNtf(WxFriendErrorNtf ntf) {
        if (!StringUtils.equals(ntf.chatlinkid, getView().getChatLinkId())) return;

        //TioToast.showShort(ntf.msg);

        // 4/15/21: 发送错误提示消息
        getView().getMsgListProxy().sendMsg(new TioErrorMsg(ntf));
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
        // 初始化ui
        getView().resetUI();
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
        getModel().getTioGroupMsgList(getView().getChatLinkId(), startMid, new BaseModel.DataProxy<TioGroupMsgList>() {
            @Override
            public void onSuccess(TioGroupMsgList msgList) {
                WxGroupMsgResp msgResp = msgList.msgResp;
                List<TioMsg> tioMsgList = msgList.tioMsgList;

                // 获取下一页 mid
                List<WxGroupMsgResp.DataBean> data = msgResp.data;
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

}
