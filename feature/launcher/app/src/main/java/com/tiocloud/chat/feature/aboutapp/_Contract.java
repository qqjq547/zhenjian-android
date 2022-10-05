package com.tiocloud.chat.feature.aboutapp;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;

interface _Contract {
    interface View extends BaseView {
        FragmentActivity getActivity();
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

        public abstract void checkAppUpdate();

        public abstract String getOutApkTime(Context context);
    }
}
