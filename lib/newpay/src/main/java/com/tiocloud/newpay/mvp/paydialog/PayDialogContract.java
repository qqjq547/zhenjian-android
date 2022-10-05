package com.tiocloud.newpay.mvp.paydialog;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayCommissionResp;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayInitRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRechargeConfirmResp;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

public interface PayDialogContract {
    interface View extends BaseView {
        void onUserCurrResp(UserCurrResp resp);

        void onBankCardListResp(PayBankCardListResp resp);

        void onPayRechargeResp(PayRechargeResp resp);

        void onPayRechargeConfirmResp(PayRechargeConfirmResp resp);

        void onPayGetWalletInfoResp(PayGetWalletInfoResp resp);

        void onPayWithholdResp(PayWithholdResp resp);

        void onPayInitRedPacketResp(Integer resp);

        void onPayRedPacketResp(PayRedPacketResp resp);

        void onPayQuickRedPacketResp(PayQuickRedPacketResp resp);
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

        public abstract void reqUserCurr();

        public abstract void reqBankCardList();

        public abstract void reqPayRecharge(String fenAmount, String agrno);

        public abstract void reqPayRechargeConfirm(String smsCode, String merorderid, String rid);

        public abstract void reqWalletInfo();

        public abstract void reqPayWithhold(String fenAmount, String agrno, String payPwd,String alipayFlag);

        public abstract void reqInitRedPacket(String mode, String chatlinkid, String num, String remark, String singlecny, String cny);

        public abstract void reqPayRedPacket(String smscode, String merorderid, String paypwd, String rid, String paytype);

        public abstract void reqPayQuickRedPacket(String agrno, String rid);

        public abstract void reqPayCommission(String amount, TioCallback<PayCommissionResp> callback);
    }

    interface OnPayFinishListener {
        void onOnPayFinish();
    }
}
