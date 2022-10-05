package com.tiocloud.chat.feature.session.common.mvp;

import androidx.annotation.NonNull;

import com.tiocloud.chat.feature.session.common.SessionActivity;
import com.tiocloud.chat.feature.session.common.panel.MsgInputPanel;
import com.tiocloud.chat.feature.session.common.proxy.MsgListProxy;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.request.UserCurrReq;

public interface SessionFragmentContract {
    interface View extends BaseView {
        @NonNull
        CharSequence getChatLinkId();

        @NonNull
        String getGroupId();
        @NonNull
        SessionActivity getSessionActivity();

        MsgInputPanel getInputPanel();

        MsgListProxy getMsgListProxy();

    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }
        public abstract UserCurrReq getUserCurrReq();

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

       public abstract void getCurUserInfo();

    }
}
