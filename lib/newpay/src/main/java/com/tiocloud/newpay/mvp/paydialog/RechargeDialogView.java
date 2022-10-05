package com.tiocloud.newpay.mvp.paydialog;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CollectionUtils;
import com.tiocloud.newpay.mvp.paydialog.PayDialogContract;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayInitRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/16
 *     desc   :
 * </pre>
 */
public abstract class RechargeDialogView implements PayDialogContract.View {

    @Override
    public void onPayGetWalletInfoResp(PayGetWalletInfoResp resp) {

    }

    @Override
    public void onUserCurrResp(UserCurrResp resp) {

    }

    @Override
    public void onBankCardListResp(PayBankCardListResp resp) {
        PayBankCardListResp.Data data = null;
        if (!CollectionUtils.isEmpty(resp)) {
            data = resp.get(0);
        }
        onBankCardListResp_firstCard(data);
    }

    protected abstract void onBankCardListResp_firstCard(@Nullable PayBankCardListResp.Data data);

    @Override
    public void onPayWithholdResp(PayWithholdResp resp) {

    }

    @Override
    public void onPayInitRedPacketResp(Integer resp) {

    }

    @Override
    public void onPayRedPacketResp(PayRedPacketResp resp) {

    }

    @Override
    public void onPayQuickRedPacketResp(PayQuickRedPacketResp resp) {

    }
}
