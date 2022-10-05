package com.tiocloud.newpay.feature.bankcard_my.mvp;

import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.feature.bankcard_add.AddBankCardActivity;
import com.tiocloud.newpay.feature.bankcard_my.adapter.AddItem;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardItem;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardModel;
import com.tiocloud.newpay.feature.bankcard_remove.RemoveBankCardActivity;
import com.tiocloud.newpay.feature.bankcard_remove.RemoveBankCardVo;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.request.PayBankCardListReq;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.util.ArrayList;
import java.util.List;

public class Presenter extends Contract.Presenter {
    public Presenter(Contract.View view) {
        super(view, false);
    }

    @Override
    public void init() {
        getView().resetUI();
        reqCardListData();
    }

    @Override
    public void reqCardListData() {
        new PayBankCardListReq().setCancelTag(this).get(new TioSuccessCallback<PayBankCardListResp>() {
            @Override
            public void onTioSuccess(PayBankCardListResp payBankCardListResp) {
//                Log.d("=======解绑===","卡"+payBankCardListResp.toString());
                getView().onBankCardListResp(payBankCardListResp);
            }
        });
    }

    @Override
    public List<CardModel> resp2ListModel(PayBankCardListResp resp) {
        if (resp == null) resp = new PayBankCardListResp();

        ArrayList<CardModel> models = new ArrayList<>(resp.size() + 1);
        models.add(new CardModel(new AddItem()));

        for (PayBankCardListResp.Data data : resp) {
            CardItem cardItem = new CardItem();
            cardItem.setBankName(data.bankname);
            cardItem.setBankCardNo(data.cardno);
            cardItem.setBankCardBgColor(data.backcolor);
            cardItem.setBankLogo(HttpCache.getResUrl(data.banklogo));
            cardItem.setBankLogoBg(HttpCache.getResUrl(data.bankwatermark));
            cardItem.setOriginData(data);

            models.add(new CardModel(cardItem));
        }
        return models;
    }

    @Override
    public List<CardModel> getExampleData() {
        CardItem cardItem = new CardItem();
        cardItem.setBankName("建设银行");
        cardItem.setBankCardType("储蓄卡");
        cardItem.setBankCardNo("**** **** **** 2178");
        cardItem.setBankCardBgColor("#1EA2FB");

        return CollectionUtils.newArrayListNotNull(
                new CardModel(new AddItem()),
                new CardModel(cardItem),
                new CardModel(cardItem)
        );
    }

    @Override
    public void editBankCard(CardItem item) {
        // data
        TioActivity activity = getView().getActivity();

        String bankName = StringUtils.null2Length0(item.getBankName());

        String bankCardNo = null;
        String bankCardNo_ = item.getBankCardNo();
        if (bankCardNo_ != null && bankCardNo_.length() >= 4) {
            bankCardNo = bankCardNo_.substring(bankCardNo_.length() - 4);
        }

        // build
        SpannableStringBuilder stringBuilder = new SpanUtils()
                .append(StringUtils.format("%s（%s）", bankName, bankCardNo))
                .setFontSize(16, true)
                .setForegroundColor(ColorUtils.getColor(R.color.gray_333333))
//                .append("储蓄卡")
//                .setFontSize(13, true)
//                .setForegroundColor(ColorUtils.getColor(R.color.gray_666666))
                .create();

        new QMUIBottomSheet.BottomListSheetBuilder(activity)
                .addItem("解除绑定")
                .setGravityCenter(true)
                .setAddCancelBtn(true)
                .setRadius(SizeUtils.dp2px(19))
                .setAllowDrag(true)
                .setTitle(stringBuilder)
                .setSkinManager(QMUISkinManager.defaultInstance(activity))
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        if (StringUtils.equals(tag, "解除绑定")) {
                            removeBankCard(item.getOriginData());
                        }
                        dialog.dismiss();
                    }
                })
                .build()
                .show();
    }

    private void removeBankCard(PayBankCardListResp.Data originData) {
        if (!ClickUtils.isGlobalSingleClick()) return;

        RemoveBankCardVo removeBankCardVo = new RemoveBankCardVo(originData.id + "", originData.agrno);
        RemoveBankCardActivity.start(getView().getActivity(), removeBankCardVo);
    }

    @Override
    public void addBankCard(AddItem item) {
        if (!ClickUtils.isGlobalSingleClick()) return;
        AddBankCardActivity.start(getView().getActivity());
    }
}
