package com.tiocloud.account.mvp.account;

import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

public class AccountPresenter extends AccountContract.Presenter {
    public AccountPresenter(AccountContract.View view) {
        super(new AccountModel(), view, false);
    }

    public void init() {
        getModel().reqCurrUser(new TioCallback<UserCurrResp>(){
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                getView().onUserCurrResp(resp);
            }

            @Override
            public void onTioError(String s) {
                TioToast.showShort(s);
            }
        });
    }
}
