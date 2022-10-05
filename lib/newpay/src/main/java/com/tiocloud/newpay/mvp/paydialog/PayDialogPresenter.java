package com.tiocloud.newpay.mvp.paydialog;

import android.util.Log;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayBankCardListReq;
import com.watayouxiang.httpclient.model.request.PayCommissionReq;
import com.watayouxiang.httpclient.model.request.PayGetWalletInfoReq;
import com.watayouxiang.httpclient.model.request.PayInitRedPacketReq;
import com.watayouxiang.httpclient.model.request.PayQuickRedPacketReq;
import com.watayouxiang.httpclient.model.request.PayRechargeConfirmReq;
import com.watayouxiang.httpclient.model.request.PayRechargeReq;
import com.watayouxiang.httpclient.model.request.PayRedPacketReq;
import com.watayouxiang.httpclient.model.request.PayWithholdReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayCommissionResp;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRechargeConfirmResp;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

public class PayDialogPresenter extends PayDialogContract.Presenter {
    public PayDialogPresenter(PayDialogContract.View view) {
        super(null, view, false);
    }

    @Override
    public void reqUserCurr() {
        new UserCurrReq().setCancelTag(this).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                getView().onUserCurrResp(resp);
            }
        });
    }

    @Override
    public void reqBankCardList() {
        new PayBankCardListReq().setCancelTag(this).get(new TioSuccessCallback<PayBankCardListResp>() {
            @Override
            public void onTioSuccess(PayBankCardListResp resp) {
//                Log.d("=======解绑===","22卡"+resp.toString());

                getView().onBankCardListResp(resp);
            }
        });
    }

    @Override
    public void reqPayRecharge(String fenAmount, String agrno) {
        new PayRechargeReq(fenAmount, agrno).setCancelTag(this).post(new TioSuccessCallback<PayRechargeResp>() {
            @Override
            public void onTioSuccess(PayRechargeResp payRechargeResp) {
                getView().onPayRechargeResp(payRechargeResp);
            }
        });
    }

    @Override
    public void reqPayRechargeConfirm(String smsCode, String merorderid, String rid) {
        new PayRechargeConfirmReq(smsCode, merorderid, rid).setCancelTag(this).post(new TioSuccessCallback<PayRechargeConfirmResp>() {
            @Override
            public void onTioSuccess(PayRechargeConfirmResp payRechargeConfirmResp) {
                getView().onPayRechargeConfirmResp(payRechargeConfirmResp);
            }
        });
    }

    @Override
    public void reqWalletInfo() {
        new PayGetWalletInfoReq().setCancelTag(this).get(new TioSuccessCallback<PayGetWalletInfoResp>() {
            @Override
            public void onTioSuccess(PayGetWalletInfoResp resp) {
                getView().onPayGetWalletInfoResp(resp);
            }
        });
    }

    @Override
    public void reqPayWithhold(String fenAmount, String cardid, String payPwd,String alipayFlag) {
        PayDialogPresenter tag = this;
        new UserCurrReq().setCancelTag(tag).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                Log.d("===提现==","=="+fenAmount+"=="+ cardid+"=="+ payPwd+"=="+ resp.loginname+"=="+alipayFlag);
                // public PayWithholdReq(String amount, String agrno, String paypwd, String phone) {
                new PayWithholdReq(fenAmount, cardid, payPwd, resp.loginname,alipayFlag).setCancelTag(tag).post(new TioSuccessCallback<PayWithholdResp>() {
                    @Override
                    public void onTioSuccess(PayWithholdResp payWithholdResp) {
                        getView().onPayWithholdResp(payWithholdResp);
                    }

                    @Override
                    public void onTioError(String msg) {
                        super.onTioError(msg);
                        Log.d("====onTioError==","=="+msg);
                    }
                });

            }
        });
    }

    @Override
    public void reqInitRedPacket(String mode, String chatlinkid, String num, String remark, String singlecny, String cny) {
        new PayInitRedPacketReq(mode, chatlinkid, num, remark, singlecny, cny).setCancelTag(this).post(new TioSuccessCallback<Integer>() {
            @Override
            public void onTioSuccess(Integer payInitRedPacketResp) {
                getView().onPayInitRedPacketResp(payInitRedPacketResp);
            }
        });
    }

    @Override
    public void reqPayRedPacket(String smscode, String merorderid, String paypwd, String rid, String paytype) {
        PayDialogPresenter tag = this;
        new UserCurrReq().setCancelTag(tag).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {

                new PayRedPacketReq(smscode, merorderid, paypwd, rid, paytype, resp.loginname).setCancelTag(tag).post(new TioSuccessCallback<PayRedPacketResp>() {
                    @Override
                    public void onTioSuccess(PayRedPacketResp payInitRedPacketResp) {
                        getView().onPayRedPacketResp(payInitRedPacketResp);
                    }
                });

            }
        });
    }

    @Override
    public void reqPayQuickRedPacket(String agrno, String rid) {
        new PayQuickRedPacketReq(agrno, rid).setCancelTag(this).post(new TioSuccessCallback<PayQuickRedPacketResp>() {
            @Override
            public void onTioSuccess(PayQuickRedPacketResp payInitRedPacketResp) {
                getView().onPayQuickRedPacketResp(payInitRedPacketResp);
            }
        });
    }

    @Override
    public void reqPayCommission(String amount, TioCallback<PayCommissionResp> callback) {
        new PayCommissionReq(amount).setCancelTag(this).get(callback);
    }
}
