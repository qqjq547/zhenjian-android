package com.tiocloud.newpay.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletRechargeDialogBinding;
import com.tiocloud.newpay.feature.recharge_result.RechargeResultActivity;
import com.tiocloud.newpay.mvp.paydialog.PayDialogPresenter;
import com.tiocloud.newpay.mvp.paydialog.RechargeDialogView;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.databinding.AndroidutilsDialogSmscodeEtBinding;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog2.BaseBindingDialog;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayRechargeConfirmResp;
import com.watayouxiang.httpclient.model.response.PayRechargeResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/10
 *     desc   : 充值弹窗
 * </pre>
 */
public class RechargeDialog extends BaseBindingDialog<WalletRechargeDialogBinding> {

    @NonNull
    private final RechargeVo rechargeVo;
    private final Activity activity;

    public RechargeDialog(@NonNull Activity activity, @NonNull RechargeVo rechargeVo) {
        super(activity);
        this.rechargeVo = rechargeVo;
        this.activity = activity;
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
        return R.layout.wallet_recharge_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetUI();

        if (TextUtils.isEmpty(rechargeVo.bankCardLastFourNum)
                || TextUtils.isEmpty(rechargeVo.bankName)
                || TextUtils.isEmpty(rechargeVo.agrno)
                || TextUtils.isEmpty(rechargeVo.phone)) {
            presenter.reqBankCardList();
        }
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
        // 手机号
        String phone = MoneyUtils.getHiddenPhone(rechargeVo.phone);
        binding.tvSmsCodeTip.setText(StringUtils.format("短信验证码将发送至：%s", phone));
        // 验证码输入框
        AndroidutilsDialogSmscodeEtBinding etBinding = binding.etSmsCode.getBinding();
        etBinding.tvReqCode.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                reqSmsCode();
            }
        });
        // 金额
        String amountYuan = StringUtils.null2Length0(MoneyUtils.formatYuan(rechargeVo.amountYuan));
        binding.tvMoneyAmount.setText(amountYuan);
        // 支付方式
        String bankName = StringUtils.null2Length0(rechargeVo.bankName);
        String bankCardLastFourNum = StringUtils.null2Length0(rechargeVo.bankCardLastFourNum);
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
        // 确定按钮
        binding.tvOk.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                clickOkBtn();
            }
        });
    }

    private void reqSmsCode() {
        String fenAmount = MoneyUtils.yuan2fen(rechargeVo.amountYuan);
        if (TextUtils.isEmpty(fenAmount)
                || TextUtils.isEmpty(rechargeVo.agrno)) {
            TioToast.showShort("未选择支付方式");
            return;
        }

        presenter.reqPayRecharge(fenAmount, rechargeVo.agrno);
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
        if (TextUtils.isEmpty(rechargeVo.rid)
                || TextUtils.isEmpty(rechargeVo.merorderid)) {
            TioToast.showShort("未发送短信验证码");
            return;
        }

        presenter.reqPayRechargeConfirm(smsCode, rechargeVo.merorderid, rechargeVo.rid);
    }

    private void selectPayWay() {
        ChooseBankCardDialog chooseBankCardDialog = new ChooseBankCardDialog(getContext());
        chooseBankCardDialog.setOnItemClickListener(new ChooseBankCardDialog.OnItemClickListener() {
            @Override
            public void onItemClickText(String text) {

            }

            @Override
            public void onItemClick(ChooseBankCardDialog.CardListAdapter adapter, View view, int position) {
                ChooseBankCardDialog.CardModel model = adapter.getData().get(position);
                if (model.getItemType() == ChooseBankCardDialog.CardModel.ITEM_TYPE_CARD) {
                    ChooseBankCardDialog.CardItem cardItem = model.cardItem;
                    PayBankCardListResp.Data originData = cardItem.originData;

                    rechargeVo.bankName = originData.bankname;
                    rechargeVo.bankCardLastFourNum = MoneyUtils.getBankCardLast4No(originData.cardno);
                    rechargeVo.phone = originData.phone;
                    rechargeVo.agrno = originData.agrno;
                    resetUI();

                    chooseBankCardDialog.dismiss();
                }
            }
        });
        chooseBankCardDialog.show();
    }

    // ====================================================================================
    // class
    // ====================================================================================

    public static class RechargeVo {
        // 金额（单位：元）
        public String amountYuan;
        // 银行名称
        public String bankName;
        // 银行卡后四位
        public String bankCardLastFourNum;
        // 手机号
        public String phone;
        // 充值的卡协议号
        public String agrno;

        /* --- 以下为内部使用 --- */

        // 预下单订单id
        private String rid;
        // 商户订单号
        private String merorderid;

        public RechargeVo(String amountYuan) {
            this.amountYuan = amountYuan;
        }
    }

    private final PayDialogPresenter presenter = new PayDialogPresenter(new RechargeDialogView() {
        @Override
        public void onPayRechargeResp(PayRechargeResp resp) {
            rechargeVo.rid = resp.id;
            rechargeVo.merorderid = resp.merorderid;

            startSmsCodeTimer();
        }

        @Override
        public void onPayRechargeConfirmResp(PayRechargeConfirmResp resp) {
            dismiss();
            RechargeResultActivity.start(activity, resp.id, resp.reqid);
        }

        @Override
        protected void onBankCardListResp_firstCard(@Nullable PayBankCardListResp.Data data) {
            if (data != null) {
                rechargeVo.phone = data.phone;
                rechargeVo.bankName = data.bankname;
                rechargeVo.bankCardLastFourNum = MoneyUtils.getBankCardLast4No(data.cardno);
                rechargeVo.agrno = data.agrno;

                resetUI();
            }
        }
    });
}
