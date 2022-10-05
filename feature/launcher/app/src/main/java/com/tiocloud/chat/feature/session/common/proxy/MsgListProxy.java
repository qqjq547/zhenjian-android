package com.tiocloud.chat.feature.session.common.proxy;

import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.watayouxiang.androidutils.recyclerview.BaseFetchLoadAdapter;
import com.watayouxiang.imclient.model.body.wx.WxFaultItem;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/04
 *     desc   :
 * </pre>
 */
public interface MsgListProxy {
    void sendMsg(TioMsg message);

    void deleteMsg(long mid);

    void clearList();

    void fetchMoreEnd(List<TioMsg> msgList);

    void fetchMoreComplete(List<TioMsg> msgList);

    void readAllMsg();

    void scrollToBottom();

    void scrollToBottomDelayed();

    void setOnFetchMoreListener(BaseFetchLoadAdapter.RequestFetchMoreListener requestFetchMoreListener);

    void handleApplyMsg(String mid);

    void handleFaultMsg(TioMsg tioMsg);
}
