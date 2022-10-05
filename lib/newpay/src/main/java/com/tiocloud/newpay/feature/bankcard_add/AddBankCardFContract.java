package com.tiocloud.newpay.feature.bankcard_add;

import androidx.fragment.app.FragmentActivity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.response.PayBindCardConfirmResp;
import com.watayouxiang.httpclient.model.response.PayBindCardResp;

public interface AddBankCardFContract {
    interface View extends BaseView {
        void resetUI();

        void onCodeTimerRunning(int second);

        void onCodeTimerStop();

        FragmentActivity getActivity();

        void showErrorDialog(String msg);


        void reqBindCardSuccess(PayBindCardResp o);

//        void reqBindCardSuccess(PayBindCardConfirmResp o);
    }

    abstract class Presenter extends BasePresenter<BaseModel, View> {
        public Presenter(View view, boolean registerEvent) {
            super(null, view, registerEvent);
        }

        public abstract void init();

        public abstract void reqSmsCode(String phone);

        public abstract void reqSmsCode(String cardNo, String creditCardExpiry, String creditCardTail, String name, String idNo, String phone,String cardtype,String bankcode);

        public abstract void reqSmsCode(String cardNo, String name, String idNo, String phone,String cardtype,String bankcode);

        public abstract void reqBindCard(String smsCode);
    }
}
