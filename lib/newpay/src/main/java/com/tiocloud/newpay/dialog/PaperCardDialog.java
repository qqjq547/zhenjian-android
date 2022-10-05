package com.tiocloud.newpay.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletPaperCardDialogBinding;
import com.tiocloud.newpay.mvp.paydialog.PaperCardView;
import com.tiocloud.newpay.mvp.paydialog.PayDialogContract;
import com.tiocloud.newpay.mvp.paydialog.PayDialogPresenter;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.databinding.AndroidutilsDialogSmscodeEtBinding;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog2.BaseBindingDialog;
import com.watayouxiang.httpclient.model.response.PayQuickRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRedPacketResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/10
 *     desc   : 发红包 - 银行卡方式
 * </pre>
 */
public class PaperCardDialog extends BaseBindingDialog<WalletPaperCardDialogBinding> {

    @NonNull
    private DialogVo dialogVo;
    private PayDialogContract.OnPayFinishListener onPayFinishListener;
    private PayQuickRedPacketResp mPayQuickRedPacketResp;

    public PaperCardDialog(@NonNull Context context, @NonNull DialogVo dialogVo) {
        super(context);
        this.dialogVo = dialogVo;
    }

    public PaperCardDialog(Context context, ChooseBankCardDialog.CardItem cardItem, PaperPacketDialog.DialogVo dialogVo) {
        this(context, DialogVo.update(cardItem, dialogVo));
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
        return R.layout.wallet_paper_card_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetUI();
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
        // 验证码输入框
        AndroidutilsDialogSmscodeEtBinding etBinding = binding.etSmsCode.getBinding();
        etBinding.tvReqCode.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                reqSmsCode();
            }
        });
        // 金额
        String amountYuan = StringUtils.null2Length0(MoneyUtils.fen2yuan(dialogVo.cny));
        binding.tvMoneyAmount.setText(amountYuan);
        // 支付方式
        String bankName = StringUtils.null2Length0(dialogVo.bankName);
        String bankCardLastFourNum = StringUtils.null2Length0(dialogVo.bankCardLastFourNum);
        if (TextUtils.isEmpty(bankName) || TextUtils.isEmpty(bankCardLastFourNum)) {
            binding.tvPayWayContent.setText("");
        } else {
            binding.tvPayWayContent.setText(StringUtils.format("%s (%s)", bankName, bankCardLastFourNum));
        }
        binding.rlPayWay.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                KeyboardUtils.hideSoftInput(etBinding.etInput);
                selectPayWay();
            }
        });
        // 手机号
        String phone = MoneyUtils.getHiddenPhone(dialogVo.phone);
        binding.tvSmsCodeTip.setText(StringUtils.format("短信验证码将发送至：%s", phone));
        // 确定按钮
        binding.tvOk.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                clickOkBtn();
            }
        });
    }

    private void reqSmsCode() {
        presenter.reqPayQuickRedPacket(dialogVo.agrno, dialogVo.rid);
    }

    private void startSmsCodeTimer() {
        binding.etSmsCode.startCodeTimer(60);
    }

    private void clickOkBtn() {
        String smsCode = binding.etSmsCode.getBinding().etInput.getText().toString().trim();
        if (TextUtils.isEmpty(smsCode)) {
            TioToast.showShort("验证码为空");
            return;
        }

        if (mPayQuickRedPacketResp == null) {
            TioToast.showShort("未发送短信验证码");
            return;
        }
        String rid = mPayQuickRedPacketResp.id;
        String merorderid = mPayQuickRedPacketResp.merorderid;

        presenter.reqPayRedPacket(smsCode, merorderid, null, rid, "2");
    }

    private void selectPayWay() {
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

                    dialogVo = DialogVo.update(cardItem, dialogVo);
                    resetUI();

                    chooseBankCardDialog.dismiss();
                } else if (type == ChooseBankCardDialog.CardModel.ITEM_TYPE_PACKET) {
                    // 钱包方式
                    ChooseBankCardDialog.PacketItem packetItem = model.packetItem;

                    chooseBankCardDialog.dismiss();
                    PaperCardDialog.this.dismiss();

                    showPaperPacketDialog(packetItem);
                }
            }
        });
        chooseBankCardDialog.show();
    }

    private void showPaperPacketDialog(ChooseBankCardDialog.PacketItem packetItem) {
        PaperPacketDialog paperPacketDialog = new PaperPacketDialog(getContext(), dialogVo);
        paperPacketDialog.setOnPayFinishListener(onPayFinishListener);
        paperPacketDialog.show();
    }

    public void setOnPayFinishListener(PayDialogContract.OnPayFinishListener onPayFinishListener) {
        this.onPayFinishListener = onPayFinishListener;
    }

    // ====================================================================================
    // class
    // ====================================================================================

    private final PayDialogPresenter presenter = new PayDialogPresenter(new PaperCardView() {
        @Override
        public void onPayQuickRedPacketResp(PayQuickRedPacketResp resp) {
            mPayQuickRedPacketResp = resp;
            // 红包支付-快捷支付-发短信
            startSmsCodeTimer();
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
        /* --- UI 参数 --- */

        // 银行名称
        public String bankName;
        // 银行卡后四位
        public String bankCardLastFourNum;
        // 手机号
        public String phone;
        // 快捷支付的协议号
        public String agrno;

        /* --- 请求参数 --- */

        // 总金额
        public String cny;
        // 红包id
        public String rid;

        public DialogVo(String bankName, String bankCardLastFourNum, String phone, String agrno, String cny, String rid) {
            this.bankName = bankName;
            this.bankCardLastFourNum = bankCardLastFourNum;
            this.phone = phone;
            this.cny = cny;
            this.agrno = agrno;
            this.rid = rid;
        }

        public static DialogVo update(ChooseBankCardDialog.CardItem cardItem, PaperPacketDialog.DialogVo dialogVo) {
            String bankName = cardItem.bankName;
            String bankCardLastFourNum = cardItem.bankCardLastFourNum;
            String phone = cardItem.originData.phone;
            String agrno = cardItem.originData.agrno;
            return new DialogVo(bankName, bankCardLastFourNum, phone, agrno, dialogVo.cny, dialogVo.rid);
        }

        public static DialogVo update(ChooseBankCardDialog.CardItem cardItem, DialogVo dialogVo) {
            String bankName = cardItem.bankName;
            String bankCardLastFourNum = cardItem.bankCardLastFourNum;
            String phone = cardItem.originData.phone;
            String agrno = cardItem.originData.agrno;
            return new DialogVo(bankName, bankCardLastFourNum, phone, agrno, dialogVo.cny, dialogVo.rid);
        }
    }
}
