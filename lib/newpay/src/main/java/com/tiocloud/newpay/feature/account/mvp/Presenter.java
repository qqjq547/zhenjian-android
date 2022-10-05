package com.tiocloud.newpay.feature.account.mvp;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.httpclient.model.request.PayRealInfoReq;
import com.watayouxiang.httpclient.model.response.PayRealInfoResp;

public class Presenter extends Contract.Presenter {
    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void init() {
        getWalletInfo();
        getView().resetUI();
    }

    @Override
    public void getWalletInfo() {
        new PayRealInfoReq().setCancelTag(this).get(new TioSuccessCallback<PayRealInfoResp>() {
            @Override
            public void onTioSuccess(PayRealInfoResp resp) {
                getView().onPayRealInfoResp(resp);
            }
        });
    }
}
