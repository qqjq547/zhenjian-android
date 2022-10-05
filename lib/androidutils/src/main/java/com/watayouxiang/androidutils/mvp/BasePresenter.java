package com.watayouxiang.androidutils.mvp;

import androidx.annotation.Nullable;

import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.imclient.TioIMClient;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public abstract class BasePresenter<Model extends BaseModel, View extends BaseView> {
    private final Model model;
    private final View view;
    private final boolean registerEvent;

    public BasePresenter(@Nullable Model model, @Nullable View view, boolean registerEvent) {
        this.model = model;
        this.view = view;
        this.registerEvent = registerEvent;
        if (registerEvent) {
            TioIMClient.getInstance().getEventEngine().register(this);
        }
    }

    public BasePresenter(@Nullable Model model, @Nullable View view) {
        this(model, view, false);
    }

    public View getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

    public void detachView() {
        if (model != null) {
            model.detachModel();
        }
        TioHttpClient.cancel(this);
        if (registerEvent) {
            TioIMClient.getInstance().getEventEngine().unregister(this);
        }
    }
}
