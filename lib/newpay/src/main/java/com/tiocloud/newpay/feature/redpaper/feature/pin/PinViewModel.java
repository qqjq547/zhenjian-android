package com.tiocloud.newpay.feature.redpaper.feature.pin;

import androidx.lifecycle.ViewModel;

import com.tiocloud.newpay.dialog.PaperPacketDialog;
import com.tiocloud.newpay.feature.redpaper.RedPaperActivity;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayInitRedPacketReq;
import com.watayouxiang.httpclient.model.request.PaySendRedPacketReq;
import com.watayouxiang.httpclient.model.response.PaySendRedPacketResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/09
 *     desc   :
 * </pre>
 */
public class PinViewModel extends ViewModel {

    public void postSendRedPacket(PinFragment fragment) {
        try {
            double _totalMoney = Double.parseDouble(fragment.money.get()) * 100;
            double _amount = Double.parseDouble(fragment.amount.get());
            if (_totalMoney < _amount * 1) {
                TioToast.showShort("这点儿红包不够他们分的");
                return;
            }
        } catch (Exception ignored) {
        }

        String packetType = "2";
        String amount = MoneyUtils.yuan2fen(fragment.money.get());
        RedPaperActivity activity = fragment.getRedPaperActivity();
        String chatlinkid = activity.getRedPaperVo().chatlinkid;
        String singleAmount = null;
        String packetCount = fragment.amount.get();
        String remark = fragment.getGiftTxt();

        PaySendRedPacketReq paySendRedPacketReq = new PaySendRedPacketReq(packetType, amount, chatlinkid, singleAmount, packetCount, remark);
        paySendRedPacketReq.setCancelTag(activity);
        paySendRedPacketReq.post(new TioCallback<PaySendRedPacketResp>() {
            @Override
            public void onTioSuccess(PaySendRedPacketResp paySendRedPacketResp) {
                evoke$SDK(paySendRedPacketResp, fragment);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void evoke$SDK(PaySendRedPacketResp resp, PinFragment fragment) {
        String merchantId = resp.getMerchantId();
        String walletId = resp.getWalletId();
        String token = resp.getToken();
    }

    public void showPaperDialog(PinFragment fragment) {
        try {
            double _totalMoney = Double.parseDouble(fragment.money.get()) * 100;
            double _amount = Double.parseDouble(fragment.amount.get());
            if (_totalMoney < _amount * 1) {
                TioToast.showShort("这点儿红包不够他们分的");
                return;
            }
        } catch (Exception ignored) {
        }

        String mode = "2";
        String chatlinkid = fragment.getRedPaperActivity().getRedPaperVo().chatlinkid;
        String num = fragment.amount.get();
        String remark = fragment.getGiftTxt();
        String singlecny = null;
        String cny = MoneyUtils.yuan2fen(fragment.money.get());

        /*
        // 1普通红包，2手气红包
        public String mode;
        // 会话id
        public String chatlinkid;
        // 红包数量: 一对一红包数量为1。普通群红包和拼手气红包 数量最大 100 个。
        public String num;
        // 备注，选填
        public String remark;
        // 单笔金额: 以分为单位普通红包
        public String singlecny;
        // 总金额
        public String cny;
         */

        fragment.getRedPaperActivity().showProgressDialog();
        new PayInitRedPacketReq(mode, chatlinkid, num, remark, singlecny, cny).setCancelTag(this).post(new TioSuccessCallback<Integer>() {
            @Override
            public void onTioSuccess(Integer payInitRedPacketResp) {
                fragment.getRedPaperActivity().dismissProgressDialog();

                PaperPacketDialog.DialogVo dialogVo = new PaperPacketDialog.DialogVo(cny, payInitRedPacketResp + "");
                PaperPacketDialog paperPacketDialog = new PaperPacketDialog(fragment.getRedPaperActivity(), dialogVo);
                paperPacketDialog.setOnPayFinishListener(() -> {
                    paperPacketDialog.dismiss();
                    fragment.finish();
                });
                paperPacketDialog.show();
            }

            @Override
            public void onTioError(String msg) {
                fragment.getRedPaperActivity().dismissProgressDialog();
                super.onTioError(msg);
            }
        });
    }
}
