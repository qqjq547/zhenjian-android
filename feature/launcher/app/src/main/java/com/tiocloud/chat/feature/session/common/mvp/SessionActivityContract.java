package com.tiocloud.chat.feature.session.common.mvp;

import android.graphics.drawable.Drawable;

import androidx.annotation.Nullable;

import com.tiocloud.chat.feature.session.common.model.SessionType;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;

public interface SessionActivityContract {
    interface View extends BaseView {
        void setBackgroundDrawable(Drawable drawable);

        SessionType getSessionType();

        @Nullable
        String getGroupId();

        @Nullable
        String getUid();
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

        public abstract void initBackgroundDrawable();

        public abstract void registerScreenShotListener();

        public abstract void unregisterScreenShotListener();
    }
}
