package com.tiocloud.session.feature.join_group_apply_info.mvp;

import android.app.Activity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.response.GroupApplyInfoResp;

public interface Contract {
    interface View extends BaseView {
        void resetUI();

        Activity getActivity();

        long getAid();

        String getMid();

        void onApplyInfoResp(GroupApplyInfoResp resp);

        void finish();

        void onAgreeSuccess(String mid, int aid);
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void agreeApply(GroupApplyInfoResp.ApplyBean apply);
    }
}
