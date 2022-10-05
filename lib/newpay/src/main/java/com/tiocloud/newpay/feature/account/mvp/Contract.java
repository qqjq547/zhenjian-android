package com.tiocloud.newpay.feature.account.mvp;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayRealInfoResp;

public interface Contract {
    interface View extends BaseView {
        void resetUI();

        void finish();

        void onPayRealInfoResp(PayRealInfoResp resp);
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }

        public abstract void getWalletInfo(DataProxy<PayGetWalletInfoResp> proxy);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void getWalletInfo();
    }
}
