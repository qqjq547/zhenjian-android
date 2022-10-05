package com.tiocloud.chat.feature.session.common.action.model;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.action.model.base.BaseRedPaperAction;
import com.tiocloud.commonwallet.TioWallet;

/**
 * author : TaoWang
 * date : 2020/3/5
 * desc :
 */
public class P2PRedPaperAction extends BaseRedPaperAction {
    public P2PRedPaperAction() {
        super(R.drawable.ic_red_paper_session_enable, R.string.red_paper);
    }

    @Override
    public void onClick() {
        TioWallet.getWallet().RedPaperActivity_startP2P(activity, chatLinkId);
    }
}
