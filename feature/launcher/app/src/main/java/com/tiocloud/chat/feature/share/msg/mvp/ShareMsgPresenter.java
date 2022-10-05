package com.tiocloud.chat.feature.share.msg.mvp;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.feature.share.msg.model.MsgForwardEntity;
import com.tiocloud.chat.feature.share.msg.model.MsgForwardFrom;
import com.tiocloud.chat.feature.share.msg.model.MsgForwardTo;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.androidutils.widget.TioToast;

public class ShareMsgPresenter extends ShareMsgContract.Presenter {
    public ShareMsgPresenter(ShareMsgContract.View view) {
        super(new ShareMsgModel(), view, false);
    }

    @Override
    public void initUI() {
        getView().bindContentView();
        getView().initEditText();
        getView().initFragmentContainers();
        getView().showRecentPage();
    }

    @Override
    public void onEtTextChanged(CharSequence s) {
        if (StringUtils.isEmpty(s)) {
            getView().showRecentPage();
        } else {
            getView().showSearchResultPage(s.toString());
        }
    }

    @Override
    public void forwardMsg(MsgForwardTo to) {
        MsgForwardFrom from = new MsgForwardFrom(getView().getChatLinkId(), getView().getMids());
        MsgForwardEntity entity = new MsgForwardEntity(to, from);
        getView().showMsgForwardDialog(entity);
    }

    @Override
    public void reqMsgForward(String chatlinkid, String uids, String groupids, String mids) {
        getModel().getShareCardReq(chatlinkid, uids, groupids, mids).post(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort("转发成功");
                getView().getActivity().finish();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }
}
