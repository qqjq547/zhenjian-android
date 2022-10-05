package com.tiocloud.newpay.feature.bankcard_add;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.tiocloud.newpay.mvp.smscode.SmsCodeContract;
import com.tiocloud.newpay.mvp.smscode.SmsCodePresenter;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayBindCardConfirmReq;
import com.watayouxiang.httpclient.model.request.PayBindCardReq;
import com.watayouxiang.httpclient.model.response.PayBindCardConfirmResp;
import com.watayouxiang.httpclient.model.response.PayBindCardResp;

public class AddBankCardFPresenter extends AddBankCardFContract.Presenter {

    private SmsCodePresenter smsCodePresenter;
    private PayBindCardResp mPayBindCardResp;

    public AddBankCardFPresenter(AddBankCardFContract.View view) {
        super(view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (smsCodePresenter != null) {
            smsCodePresenter.detachView();
        }
    }

    @Override
    public void init() {
        getView().resetUI();
    }

    @Override
    public void reqSmsCode(String phone) {
        getSmsCodePresenter().reqSmsCode(phone, null);
    }

    private SmsCodePresenter getSmsCodePresenter() {
        if (smsCodePresenter == null) {
            smsCodePresenter = new SmsCodePresenter(new SmsCodeContract.View() {
                @Override
                public void onCodeTimerRunning(int second) {
                    getView().onCodeTimerRunning(second);
                }

                @Override
                public void onCodeTimerStop() {
                    getView().onCodeTimerStop();
                }

                @Override
                public Activity getActivity() {
                    return getView().getActivity();
                }
            });
        }
        return smsCodePresenter;
    }

    @Override
    public void reqSmsCode(String cardNo, String creditCardExpiry, String creditCardTail, String name, String idNo, String phone,String cardtype,String bankname) {
        if (TextUtils.isEmpty(bankname)) {
            TioToast.showShort("请输入银行名称");
            return;
        }
        if (TextUtils.isEmpty(cardNo)) {
            TioToast.showShort("卡号码为空");
            return;
        }
        if (cardNo.length()<7) {
            TioToast.showShort("请输入正确的银行卡号");
            return;
        }
        if (TextUtils.isEmpty(creditCardExpiry)) {
            TioToast.showShort("信用卡有效期为空");
            return;
        }
        if (TextUtils.isEmpty(creditCardTail)) {
            TioToast.showShort("信用卡背后三位为空");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            TioToast.showShort("持卡人为空");
            return;
        }
        if (TextUtils.isEmpty(idNo)) {
            TioToast.showShort("身份证为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号为空");
            return;
        }

        //public PayBindCardReq(String cvv2, String availabledate, String name, String mobile, String cardno, String bankcardno) {
        new PayBindCardReq(creditCardExpiry, creditCardTail, name, phone, idNo, cardNo,cardtype,bankname).setCancelTag(this).post(new TioCallback<PayBindCardResp>() {
            @Override
            public void onTioSuccess(PayBindCardResp o) {
                mPayBindCardResp = o;
//                getSmsCodePresenter().startCodeTimer(60);
                Log.d("===绑卡==","=="+o.toString());
                getView().reqBindCardSuccess(o);

            }

            @Override
            public void onTioError(String msg) {
                getView().showErrorDialog(msg);
            }
        });
    }

    @Override
    public void reqSmsCode(String cardNo, String name, String idNo, String phone,String cardtype,String bankcode) {
        if (TextUtils.isEmpty(bankcode)) {
            TioToast.showShort("请选择银行卡");
            return;
        }
        if (TextUtils.isEmpty(cardNo)) {
            TioToast.showShort("卡号码为空");
            return;
        }
        if (cardNo.length()<7) {
            TioToast.showShort("请输入正确的银行卡号");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            TioToast.showShort("持卡人为空");
            return;
        }
        if (TextUtils.isEmpty(idNo)) {
            TioToast.showShort("身份证为空");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            TioToast.showShort("手机号为空");
            return;
        }
        //public PayBindCardReq(String name, String mobile, String cardno, String bankcardno) {
        new PayBindCardReq(name, phone, idNo, cardNo,cardtype,bankcode).setCancelTag(this).post(new TioCallback<PayBindCardResp>() {
            @Override
            public void onTioSuccess(PayBindCardResp o) {
                mPayBindCardResp = o;
                getView().reqBindCardSuccess(o);

//                getSmsCodePresenter().startCodeTimer(60);
            }

            @Override
            public void onTioError(String msg) {
                getView().showErrorDialog(msg);
            }
        });
    }

    @Override
    public void reqBindCard(String smsCode) {
        if (mPayBindCardResp == null) {
            TioToast.showShort("未获取短信验证码");
            return;
        }
        if (TextUtils.isEmpty(smsCode)) {
            TioToast.showShort("短信验证码为空");
            return;
        }

        // public PayBindCardConfirmReq(String bankcardid, String smscode, String merorderid) {
        new PayBindCardConfirmReq(mPayBindCardResp.id+"", smsCode, mPayBindCardResp.merorderid).setCancelTag(this).post(new TioCallback<PayBindCardConfirmResp>() {
            @Override
            public void onTioSuccess(PayBindCardConfirmResp o) {
//                getView().reqBindCardSuccess(o);
            }

            @Override
            public void onTioError(String msg) {
                getView().showErrorDialog(msg);
            }
        });
    }
}
