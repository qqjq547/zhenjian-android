package com.tiocloud.newpay.feature.bankcard_my.mvp;

import com.tiocloud.newpay.feature.bankcard_my.adapter.AddItem;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardItem;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardModel;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;

import java.util.List;

public interface Contract {
    interface View extends BaseView {
        void resetUI();

        TioActivity getActivity();

        void onBankCardListResp(PayBankCardListResp resp);
    }

    abstract class Presenter extends BasePresenter<BaseModel, View> {
        public Presenter(View view, boolean registerEvent) {
            super(null, view, registerEvent);
        }

        public abstract void init();

        public abstract List<CardModel> getExampleData();

        public abstract void editBankCard(CardItem item);

        public abstract void addBankCard(AddItem item);

        public abstract void reqCardListData();

        public abstract List<CardModel> resp2ListModel(PayBankCardListResp resp);
    }
}
