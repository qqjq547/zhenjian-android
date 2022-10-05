package com.tiocloud.session.feature.session_info_p2p.mvp;

import android.app.Activity;
import android.content.Context;
import android.widget.CompoundButton;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;

public interface Contract {
    interface View extends BaseView {
        String getChatLinkId();

        void onChatInfoResp(WxChatItemInfoResp.DataBean resp);

        void resetUI();

        Activity getActivity();
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }

        public abstract void getChatInfo(String chatLinkId, DataProxy<WxChatItemInfoResp> proxy);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void toggleTopChatSwitch(boolean isChecked, CompoundButton compoundButton);

        public abstract void toggleDNDSwitch(String uid, boolean isChecked, CompoundButton compoundButton);

        public abstract void showReportDialog();

        public abstract void showClearChatRecordDialog();
    }
}
