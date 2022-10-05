package com.tiocloud.newpay.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletWithdrawDialogBinding;
import com.tiocloud.newpay.feature.withdraw_result.WithdrawResultActivity;
import com.tiocloud.newpay.mvp.paydialog.PayDialogPresenter;
import com.tiocloud.newpay.mvp.paydialog.WithdrawDialogView;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog2.BaseBindingDialog;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayCommissionResp;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/12
 *     desc   : 提现弹窗
 * </pre>
 */
public class WithdrawDialog extends BaseBindingDialog<WalletWithdrawDialogBinding> {
    @NonNull
    private final DialogVo dialogVo;
    private final Activity activity;
    private String alipayFlag;// // 1 是支付宝 2 不是
    private WithdrawDialog(@NonNull Activity activity, @NonNull DialogVo dialogVo) {
        super(activity);
        this.activity = activity;
        this.dialogVo = dialogVo;
    }

    public WithdrawDialog(@NonNull Activity activity, @NonNull String amountYuan) {
        this(activity, new DialogVo(amountYuan));
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
        return R.layout.wallet_withdraw_dialog;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetUI();

        if (TextUtils.isEmpty(dialogVo.bankCardLastFourNum)
                || TextUtils.isEmpty(dialogVo.bankName)
                || TextUtils.isEmpty(dialogVo.agrno)) {
            presenter.reqBankCardList();
        }

        String amountFen = MoneyUtils.yuan2fen(dialogVo.amountYuan);
        presenter.reqPayCommission(amountFen, new TioSuccessCallback<PayCommissionResp>() {
            @Override
            public void onTioSuccess(PayCommissionResp resp) {
                dialogVo.commission_rate = resp.rate;
                dialogVo.commission_withholdconst = resp.withholdconst;
                dialogVo.commission = resp.commission;

                resetUI();
            }
        });
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
        String amountYuan = StringUtils.null2Length0(MoneyUtils.formatYuan(dialogVo.amountYuan));
        binding.tvMoneyAmount.setText(amountYuan);

        // 服务费
        // -- 服务费
        String str01 = "";
        try {
//            // 客户端计算服务费
//            double withdrawFen = new BigDecimal(dialogVo.amountYuan).multiply(new BigDecimal(100)).doubleValue();
//            double rate = new BigDecimal(dialogVo.commission_rate).divide(new BigDecimal(1000)).doubleValue();
//            long constFen = dialogVo.commission_withholdconst;
//
//            BigDecimal _result = (new BigDecimal(withdrawFen)
//                    .multiply(new BigDecimal(rate))
//                    .add(new BigDecimal(constFen)))
//                    .divide(new BigDecimal(100));
//            String feeYuan = new DecimalFormat("0.00##").format(_result);
//            str01 = String.format(Locale.getDefault(), "服务费：¥%s", feeYuan);

            // 服务端计算服务费
            String commissionYuan = MoneyUtils.fen2yuan(dialogVo.commission);
            str01 = String.format(Locale.getDefault(), "服务费：¥%s", commissionYuan);
        } catch (Exception ignored) {
        }
        // -- 费率、固定手续费
        String str02 = "";
        try {
            double _rate = new BigDecimal(dialogVo.commission_rate).divide(new BigDecimal(10)).doubleValue();
            String rate = new DecimalFormat("#.####").format(_rate);
            rate = rate + "%";

            double _cons = new BigDecimal(dialogVo.commission_withholdconst).divide(new BigDecimal(100)).doubleValue();
            String cons = new DecimalFormat("#.####").format(_cons);

            str02 = String.format(Locale.getDefault(), " (费率%s+%s元)", rate, cons);
        } catch (Exception ignored) {
        }
        // -- 显示
        SpannableStringBuilder stringBuilder = new SpanUtils()
                .append(str01).setForegroundColor(Color.parseColor("#333333"))
                .append(str02).setForegroundColor(Color.parseColor("#9C9C9C"))
                .create();
        binding.tvFeeInfo.setText(stringBuilder);

        // 提现方式
        String bankName = StringUtils.null2Length0(dialogVo.bankName);
        String bankCardLastFourNum = StringUtils.null2Length0(dialogVo.bankCardLastFourNum);
        if (TextUtils.isEmpty(bankName) || TextUtils.isEmpty(bankCardLastFourNum)) {
            binding.tvWayContent.setText("");
        } else {
            binding.tvWayContent.setText(StringUtils.format("%s (%s)", bankName, bankCardLastFourNum));
        }

        binding.rlWay.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                KeyboardUtils.hideSoftInput(binding.etPayPwd);
                selectWay();
            }
        });
        Log.d("===提现==","=="+dialogVo.cardid);

        // 支付密码
        binding.etPayPwd.setOnTextFinishListener(this::onInputPayPwdFinish);
    }

    private void onInputPayPwdFinish(String payPwd) {
        String fenAmount = MoneyUtils.yuan2fen(dialogVo.amountYuan);
        if (TextUtils.isEmpty(fenAmount)
                || TextUtils.isEmpty(dialogVo.agrno)) {
            TioToast.showShort("未选择支付方式");
            return;
        }
        if (TextUtils.isEmpty(payPwd)) {
            TioToast.showShort("支付密码为空");
            return;
        }

        // public PayWithholdReq(String amount, String agrno, String paypwd, String phone) {
        presenter.reqPayWithhold(fenAmount, dialogVo.cardid+"", payPwd,alipayFlag);
    }

    private void selectWay() {
        ChooseBankCardDialog chooseBankCardDialog = new ChooseBankCardDialog(getContext());
        chooseBankCardDialog.setOnItemClickListener(new ChooseBankCardDialog.OnItemClickListener() {
            @Override
            public void onItemClickText(String text) {
                alipayFlag="1";
                binding.tvWayContent.setText(text);
                chooseBankCardDialog.dismiss();

            }

            @Override
            public void onItemClick(ChooseBankCardDialog.CardListAdapter adapter, View view, int position) {
                ChooseBankCardDialog.CardModel model = adapter.getData().get(position);
                if (model.getItemType() == ChooseBankCardDialog.CardModel.ITEM_TYPE_CARD) {
                    ChooseBankCardDialog.CardItem cardItem = model.cardItem;
                    PayBankCardListResp.Data originData = cardItem.originData;
                    alipayFlag="2";

                    dialogVo.bankName = originData.bankname;
                    dialogVo.bankCardLastFourNum = MoneyUtils.getBankCardLast4No(originData.cardno);
                    dialogVo.cardid = originData.id;
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

    public static class DialogVo {
        // 金额（单位：元）
        public String amountYuan;
        // 银行名称
        public String bankName;
        // 银行卡后四位
        public String bankCardLastFourNum;
        // 提现的银行卡签约号
        public String agrno;
        public int cardid;//银行卡id
        // 利率 (单位：千分)
        public long commission_rate;
        // 固定提现手续费（分）
        public long commission_withholdconst;
        // 计算好的手续费（分）
        public long commission;

        public DialogVo(String amountYuan) {
            this.amountYuan = amountYuan;
        }
    }

    private final PayDialogPresenter presenter = new PayDialogPresenter(new WithdrawDialogView() {
        @Override
        public void onPayWithholdResp(PayWithholdResp resp) {
            dismiss();
            WithdrawResultActivity.start(activity, resp.id, resp.reqid);
        }

        @Override
        protected void onBankCardListResp_firstCard(@Nullable PayBankCardListResp.Data data) {
            if (data != null) {
                dialogVo.bankName = data.bankname;
                dialogVo.bankCardLastFourNum = MoneyUtils.getBankCardLast4No(data.cardno);
                dialogVo.agrno = data.agrno;
                dialogVo.cardid=data.id;
                resetUI();
            }
        }
    });
}
