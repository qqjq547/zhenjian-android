package com.watayouxiang.androidutils.feature.browser.mvp;

public class Presenter extends Contract.Presenter {
    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void init() {
        getView().resetUI();
    }
}
