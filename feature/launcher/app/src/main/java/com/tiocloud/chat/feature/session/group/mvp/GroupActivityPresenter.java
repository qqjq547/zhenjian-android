package com.tiocloud.chat.feature.session.group.mvp;

import com.watayouxiang.httpclient.model.response.ActChatResp;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;
import com.watayouxiang.imclient.model.body.wx.WxGroupOperNtf;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-06
 * desc :
 */
public class GroupActivityPresenter extends GroupActivityContract.Presenter {

    private String mChatLinkId;
    private boolean isFirstCall = true;

    public GroupActivityPresenter(GroupActivityContract.View view) {
        super(new GroupActivityModel(), view, true);
    }

    // 群操作通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxGroupOperNtf(WxGroupOperNtf ntf) {
        WxGroupOperNtf.Oper oper = WxGroupOperNtf.Oper.valueOf(ntf.oper);
        if (oper == null) return;
        switch (oper) {
            case KICK_OUT_GROUP:
            case REJOIN_GROUP:
                getChatInfo();
                break;
        }
    }

    @Override
    public void init() {
        getView().initUI();
    }

    @Override
    public void activeGroup(String groupId) {
        getModel().actChat(groupId, new BaseModel.DataProxy<ActChatResp>() {
            @Override
            public void onSuccess(ActChatResp actChatResp) {
                enterGroup(actChatResp.chat.id);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort("激活会话失败");
                getView().getActivity().finish();
            }
        });
    }

    @Override
    public void enterGroup(String chatLinkId) {
        this.mChatLinkId = chatLinkId;
        getChatInfo(chatLinkId, true);
    }

    private void getChatInfo(String chatLinkId, boolean isInit) {
        getModel().getChatInfo(chatLinkId, new BaseModel.DataProxy<WxChatItemInfoResp>() {
            @Override
            public void onSuccess(WxChatItemInfoResp resp) {
                super.onSuccess(resp);
                String groupId = resp.data.bizid;
                // 初始化内容页
                if (isInit) {
                    getView().initFragment(chatLinkId, groupId);
                }
                // 设置 groupId
                getView().setGroupId(groupId);
                // 设置 ui
                getView().onChatInfoResp(resp);
            }
        });
    }

    @Override
    public void getChatInfo() {
        if (isFirstCall) {
            isFirstCall = false;
            return;
        }
        if (mChatLinkId != null) {
            getChatInfo(mChatLinkId, false);
        }
    }
}
