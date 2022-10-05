package com.tiocloud.account.mvp.account;

import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

public class AccountModel extends AccountContract.Model {
    public AccountModel() {
        super(false);
    }

    @Override
    public void reqCurrUser(TioCallback<UserCurrResp> callback) {
        new UserCurrReq().setCancelTag(this).get(callback);
    }
}
