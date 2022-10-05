package com.tiocloud.newpay.feature.recharge.mvp;

import android.text.TextUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.tiocloud.newpay.dialog.RechargeDialog;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.EasyProgressDialog;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;

public class Presenter extends Contract.Presenter {

    private EasyProgressDialog progressDialog;

    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void init() {
        getView().resetUI();
    }

    @Override
    public void walletRecharge(String yuanAmount, String agrno) {
        String fenAmount = MoneyUtils.yuan2fen(yuanAmount);
        if (fenAmount == null) {
            TioToast.showShort("金额格式化错误");
            return;
        }
        if (TextUtils.isEmpty(agrno)) {
            TioToast.showShort("未选择银行卡");
            return;
        }

        getView().hideSoftInput();

        getModel().postRecharge(fenAmount, agrno, new BaseModel.DataProxy<PayRechargeResp>() {
            @Override
            public void onSuccess(PayRechargeResp payRechargeResp) {
                super.onSuccess(payRechargeResp);

                evokeRecharge$SDK(payRechargeResp);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                ToastUtils.showShort(msg);
            }
        });
    }

    private void evokeRecharge$SDK(PayRechargeResp resp) {
        String merchantId = resp.getMerchantId();
        String walletId = resp.getWalletId();
        String token = resp.getToken();

    }

    @Override
    public void showRechargeDialog(String yuanAmount) {
        getView().hideSoftInput();

        new RechargeDialog(getView().getActivity(),
                new RechargeDialog.RechargeVo(yuanAmount))
                .show();
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
}
