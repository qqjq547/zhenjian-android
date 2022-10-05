package com.tiocloud.newpay.mvp.paydialog;

import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRechargeConfirmResp;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/17
 *     desc   :
 * </pre>
 */
public abstract class PaperPacketView implements PayDialogContract.View {
    @Override
    public void onUserCurrResp(UserCurrResp resp) {

    }

    @Override
    public void onBankCardListResp(PayBankCardListResp resp) {

    }

    @Override
    public void onPayRechargeResp(PayRechargeResp resp) {

    }

    @Override
    public void onPayRechargeConfirmResp(PayRechargeConfirmResp resp) {

    }

    @Override
    public void onPayWithholdResp(PayWithholdResp resp) {

    }

    @Override
    public void onPayQuickRedPacketResp(PayQuickRedPacketResp resp) {

    }

    @Override
    public void onPayInitRedPacketResp(Integer resp) {

    }
}
