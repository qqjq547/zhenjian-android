package com.tiocloud.newpay.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletPaperPacketDialogBinding;
import com.tiocloud.newpay.mvp.paydialog.PaperPacketView;
import com.tiocloud.newpay.mvp.paydialog.PayDialogContract;
import com.tiocloud.newpay.mvp.paydialog.PayDialogPresenter;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.widget.dialog2.BaseBindingDialog;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   : 发红包 - 钱包方式
 * </pre>
 */
public class PaperPacketDialog extends BaseBindingDialog<WalletPaperPacketDialogBinding> {

    @NonNull
    private final DialogVo dialogVo;
    private PayDialogContract.OnPayFinishListener onPayFinishListener;

    public PaperPacketDialog(@NonNull Context context, @NonNull DialogVo dialogVo) {
        super(context);
        this.dialogVo = dialogVo;
    }

    public PaperPacketDialog(Context context, PaperCardDialog.DialogVo dialogVo) {
        this(context, DialogVo.getInstance(dialogVo));
    }

    @NonNull
    @Override
    public Builder getBuilder() {
        return super.getBuilder()
                .setAnimationsResId(R.style.tio_dialog_anim)
                .setHeight(WindowManager.LayoutParams.WRAP_CONTENT)
                .setWidth(SizeUtils.dp2px(284))
                .setBackgroundDrawableResId(R.drawable.wallet_recharge_dialog_bg)
                .setCancelable(false)
                ;
    }

    @Override
    public int getLayoutId() {
        return R.layout.wallet_paper_packet_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetUI();

        presenter.reqWalletInfo();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    private void resetUI() {
        // 关闭按钮
        binding.ivCancelBtn.setOnClickListener(view -> dismiss());
        // 金额
        String amountYuan = StringUtils.null2Length0(MoneyUtils.fen2yuan(dialogVo.cny));
        binding.tvMoneyAmount.setText(amountYuan);
        // 支付方式
//        binding.rlWay.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View view) {
////                KeyboardUtils.hideSoftInput(binding.etPayPwd);
////                selectWay();
//            }
//        });
        String packetCnyYuan = StringUtils.null2Length0(MoneyUtils.fen2yuan(dialogVo.packetCny));
        binding.tvWayContent.setText(StringUtils.format("钱包 (¥%s)", packetCnyYuan));
        // 支付密码
        binding.etPayPwd.setOnTextFinishListener(str -> {
            KeyboardUtils.hideSoftInput(binding.etPayPwd);
            onInputPayPwdFinish(str);
            binding.etPayPwd.clearText();
        });
    }

    private void onInputPayPwdFinish(String payPwd) {

        presenter.reqPayRedPacket(null, null, payPwd, dialogVo.rid, "1");
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void selectWay() {
        ChooseBankCardDialog chooseBankCardDialog = new ChooseBankCardDialog(getContext(), true);
        chooseBankCardDialog.setOnItemClickListener(new ChooseBankCardDialog.OnItemClickListener() {
            @Override
            public void onItemClickText(String text) {

            }

            @Override
            public void onItemClick(ChooseBankCardDialog.CardListAdapter adapter, View view, int position) {
                ChooseBankCardDialog.CardModel model = adapter.getData().get(position);
                int type = model.getItemType();
                if (type == ChooseBankCardDialog.CardModel.ITEM_TYPE_CARD) {
                    // 银行卡方式
                    ChooseBankCardDialog.CardItem cardItem = model.cardItem;

                    chooseBankCardDialog.dismiss();
                    PaperPacketDialog.this.dismiss();

                    showPaperCardDialog(cardItem);
                } else if (type == ChooseBankCardDialog.CardModel.ITEM_TYPE_PACKET) {
                    // 钱包方式
                    ChooseBankCardDialog.PacketItem packetItem = model.packetItem;

                    dialogVo.packetCny = packetItem.originData.cny;
                    resetUI();

                    chooseBankCardDialog.dismiss();
                }
            }
        });
        chooseBankCardDialog.show();
    }

    // 银行卡支付方式弹窗
    private void showPaperCardDialog(ChooseBankCardDialog.CardItem cardItem) {
        PaperCardDialog paperCardDialog = new PaperCardDialog(getContext(), cardItem, dialogVo);
        paperCardDialog.setOnPayFinishListener(onPayFinishListener);
        paperCardDialog.show();
    }

    public void setOnPayFinishListener(PayDialogContract.OnPayFinishListener onPayFinishListener) {
        this.onPayFinishListener = onPayFinishListener;
    }

    // ====================================================================================
    // class
    // ====================================================================================

    private final PayDialogPresenter presenter = new PayDialogPresenter(new PaperPacketView() {
        @Override
        public void onPayGetWalletInfoResp(PayGetWalletInfoResp resp) {
            dialogVo.packetCny = resp.cny;
            resetUI();
        }

        @Override
        public void onPayRedPacketResp(PayRedPacketResp resp) {
            // 发红包确认
            if (onPayFinishListener != null) {
                onPayFinishListener.onOnPayFinish();
            }
        }
    });

    public static class DialogVo {
        // 总金额
        public String cny;
        // 红包id
        public String rid;

        // 钱包余额
        public String packetCny;

        public DialogVo(String cny, String rid) {
            this.cny = cny;
            this.rid = rid;
        }

        public static DialogVo getInstance(PaperCardDialog.DialogVo dialogVo) {
            return new DialogVo(dialogVo.cny, dialogVo.rid);
        }
    }
}
