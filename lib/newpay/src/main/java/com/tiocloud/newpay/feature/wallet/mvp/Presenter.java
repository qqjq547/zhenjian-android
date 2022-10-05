package com.tiocloud.newpay.feature.wallet.mvp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tiocloud.newpay.feature.bankcard_my.MyBankCardActivity;
import com.tiocloud.newpay.feature.open.OpenWalletActivity;
import com.tiocloud.newpay.feature.paypwd_setup.SetupPayPwdActivity;
import com.tiocloud.newpay.feature.safe_setting.SafeSettingActivity;
import com.tiocloud.newpay.feature.wallet.WalletActivity;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayGetClientTokenReq;
import com.watayouxiang.httpclient.model.request.PayOpenFlagReq;
import com.watayouxiang.httpclient.model.request.PayOpenReq;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayOpenFlagResp;
import com.watayouxiang.httpclient.model.response.PayOpenResp;

public class Presenter extends Contract.Presenter {

    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    public static void openWalletActivity(Activity activity) {
        PayOpenFlagReq openFlagReq = new PayOpenFlagReq();
        openFlagReq.setCancelTag(activity);
        openFlagReq.get(new TioCallback<PayOpenFlagResp>() {
            @Override
            public void onTioSuccess(PayOpenFlagResp payOpenFlagResp) {
                int openflag = payOpenFlagResp.getOpenflag();
                int paypwdflag = payOpenFlagResp.paypwdflag;
                if (openflag == 1) {
                    // 已经开户
                    // 是否设置钱包支付密码
                    if (paypwdflag == 1) {
                        Intent starter = new Intent(activity, WalletActivity.class);
                        activity.startActivity(starter);
                    } else {
                        SetupPayPwdActivity.start(activity);
                    }
                } else {
                    // 未开户
//                    OpenWalletActivity.start(activity);
                    PayOpenReq payOpenReq = new PayOpenReq("0", "0", "0");
                    payOpenReq.setCancelTag(activity);
                    payOpenReq.post(new TioCallback<PayOpenResp>() {
                        @Override
                        public void onTioSuccess(PayOpenResp payOpenResp) {
//                            Intent starter = new Intent(activity, WalletActivity.class);
//                            activity.startActivity(starter);
                            SetupPayPwdActivity.start(activity);

                        }

                        @Override
                        public void onTioError(String msg) {
                            ToastUtils.showShort(msg);
                        }
                    });
                }
            }

            @Override
            public void onTioError(String msg) {
                ToastUtils.showShort(msg);
            }
        });
    }

    @Override
    public void init() {
        getView().resetUI();
        initWalletPay();
        getWalletInfo();
    }

    private void initWalletPay() {

    }

    @Override
    public void query$ShowMoney() {
        getWalletInfo(true);
    }

    @Override
    public void getWalletInfo() {
        getWalletInfo(false);
    }

    private void getWalletInfo(boolean mustShowMoney) {
        getModel().getWalletInfo(new BaseModel.DataProxy<PayGetWalletInfoResp>() {
            @Override
            public void onSuccess(PayGetWalletInfoResp resp) {
                super.onSuccess(resp);
                if (mustShowMoney || getView().isMoneyVisibility()) {
                    String format = MoneyUtils.fen2yuan(resp.cny);
//                    Log.d("===余额==",format+"===");
                    if (format != null) {
                        getView().showMoney(format);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                ToastUtils.showShort(msg);
            }
        });
    }

    @Override
    public void getClientToken(@PayGetClientTokenReq.BizType String bizType) {
        if (StringUtils.equals(bizType, PayGetClientTokenReq.ACCESS_SAFETY)) {
            SafeSettingActivity.start(getView().getActivity());
        } else if (StringUtils.equals(bizType, PayGetClientTokenReq.ACCESS_CARDlIST)) {
            MyBankCardActivity.start(getView().getActivity());
        }
    }
}
