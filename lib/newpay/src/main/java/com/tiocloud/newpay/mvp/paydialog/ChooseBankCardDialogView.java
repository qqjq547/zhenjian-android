package com.tiocloud.newpay.mvp.paydialog;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.CollectionUtils;
import com.tiocloud.newpay.dialog.ChooseBankCardDialog;
import com.tiocloud.newpay.mvp.paydialog.PayDialogContract;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayInitRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRechargeConfirmResp;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.util.ArrayList;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/16
 *     desc   :
 * </pre>
 */
public abstract class ChooseBankCardDialogView implements PayDialogContract.View {
    @Override
    public void onUserCurrResp(UserCurrResp resp) {

    }

    @Override
    public void onPayRechargeResp(PayRechargeResp resp) {

    }

    @Override
    public void onPayRechargeConfirmResp(PayRechargeConfirmResp resp) {

    }

    @Override
    public void onPayGetWalletInfoResp(PayGetWalletInfoResp resp) {
        ArrayList<ChooseBankCardDialog.CardModel> cardModels = new ArrayList<>();

        if (resp != null) {
            ChooseBankCardDialog.PacketItem packetItem = new ChooseBankCardDialog.PacketItem()
                    .setMoneyYuan(MoneyUtils.fen2yuan(resp.cny))
                    .setOriginData(resp);
            cardModels.add(new ChooseBankCardDialog.CardModel(packetItem));
        }

        onPayGetWalletInfoResp(cardModels);
    }

    protected abstract void onPayGetWalletInfoResp(@NonNull ArrayList<ChooseBankCardDialog.CardModel> cardModels);

    @Override
    public void onBankCardListResp(PayBankCardListResp resp) {
        ArrayList<ChooseBankCardDialog.CardModel> cardModels = new ArrayList<>();

        if (!CollectionUtils.isEmpty(resp)) {
            for (PayBankCardListResp.Data data : resp) {

                ChooseBankCardDialog.CardItem cardItem = new ChooseBankCardDialog.CardItem()
                        .setBankName(data.bankname)
                        .setBankIconUrl(HttpCache.getResUrl(data.banklogo))
                        .setBankCardLastFourNum(MoneyUtils.getBankCardLast4No(data.cardno))
                        .setOriginData(data);

                cardModels.add(new ChooseBankCardDialog.CardModel(cardItem));
            }
        }

        onBankCardListResp(cardModels);
    }

    protected abstract void onBankCardListResp(@NonNull ArrayList<ChooseBankCardDialog.CardModel> cardModels);

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
