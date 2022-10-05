package com.tiocloud.account.feature.phone_modify.step3;

class MvpPresenter extends MvpContract.Presenter {
    public MvpPresenter(MvpContract.View view) {
        super(new MvpModel(), view, false);
    }

    @Override
    public void init() {
        getView().resetUI();
    }
}
