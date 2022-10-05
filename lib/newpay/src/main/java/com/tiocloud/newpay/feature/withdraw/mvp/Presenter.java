package com.tiocloud.newpay.feature.withdraw.mvp;

import android.text.TextUtils;

import com.tiocloud.newpay.dialog.WithdrawDialog;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.EasyProgressDialog;
import com.watayouxiang.httpclient.model.request.PayCommissionReq;
import com.watayouxiang.httpclient.model.response.PayCommissionResp;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;

public class Presenter extends Contract.Presenter {

    private EasyProgressDialog progressDialog;

    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void init() {
        getView().resetUI();
        getWalletInfo();
        reqPayCommission();
    }

    @Override
    public void reqPayCommission() {
        new PayCommissionReq().setCancelTag(this).get(new TioSuccessCallback<PayCommissionResp>() {
            @Override
            public void onTioSuccess(PayCommissionResp payCommissionResp) {
                getView().onPayCommissionResp(payCommissionResp);
            }
        });
    }

    @Override
    public void getWalletInfo(boolean withdrawAll) {
        getModel().getWalletInfo(new BaseModel.DataProxy<PayGetWalletInfoResp>() {
            @Override
            public void onSuccess(PayGetWalletInfoResp resp) {
                super.onSuccess(resp);
                String amountYuan = MoneyUtils.fen2yuan(resp.cny);
                if (amountYuan == null) return;

                getView().onMoneyResp(amountYuan);
                if (withdrawAll) {
                    getView().onWithdrawAllResp(amountYuan);
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

    @Override
    public void getWalletInfo() {
        getWalletInfo(false);
    }

    @Override
    public void postWithdraw(String yuanAmount) {
        String fenAmount = MoneyUtils.yuan2fen(yuanAmount);
        if (fenAmount == null) {
            TioToast.showShort("金额转换错误");
            return;
        }

        getView().hideSoftInput();

        getModel().postWithhold(new BaseModel.DataProxy<PayWithholdResp>() {
            @Override
            public void onSuccess(PayWithholdResp payWithholdResp) {
                super.onSuccess(payWithholdResp);
                postWithhold$SDK(payWithholdResp, fenAmount);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        }, fenAmount);
    }

    private void postWithhold$SDK(PayWithholdResp resp, String points) {
        String merchantId = resp.getMerchantId();
        String walletId = resp.getWalletId();
        String token = resp.getToken();
        String serialNumber = resp.getSerialnumber();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new EasyProgressDialog.Builder(getView().getActivity())
                    .setMessage("处理中")
                    .build();
        }
        progressDialog.show();
    }

    @Override
    public void showWithdrawDialog(String yuanAmount) {
        if (TextUtils.isEmpty(yuanAmount)) {
            TioToast.showShort("金额为空");
            return;
        }

        WithdrawDialog withdrawDialog = new WithdrawDialog(getView().getActivity(), yuanAmount);
        withdrawDialog.show();
    }
}
