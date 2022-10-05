package com.tiocloud.account.mvp.account;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

public interface AccountContract {
    interface View extends BaseView {
        void onUserCurrResp(UserCurrResp resp);
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }

        public abstract void reqCurrUser(TioCallback<UserCurrResp> callback);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }
    }
}
