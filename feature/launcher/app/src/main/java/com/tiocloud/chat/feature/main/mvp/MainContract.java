package com.tiocloud.chat.feature.main.mvp;

import android.app.Activity;

import com.tiocloud.chat.feature.main.MainActivity;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.request.FoundListResp;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public interface MainContract {
    interface View extends BaseView {
        Activity getActivity();

        MainActivity getMainActivity();

        void initViews();

        void updateRedDot();

        void setFoundList(List<FoundListResp.Found> data);
    }

    abstract class Model extends BaseModel {
        public abstract void requestFoundList(DataProxy<FoundListResp> proxy);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void checkAppUpdate();

        public abstract void clearAllNotifications();

    }
}
