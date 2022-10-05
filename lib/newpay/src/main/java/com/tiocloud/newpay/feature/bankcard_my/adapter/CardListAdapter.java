package com.tiocloud.newpay.feature.bankcard_my.adapter;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.newpay.R;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   :
 * </pre>
 */
public class CardListAdapter extends BaseMultiItemQuickAdapter<CardModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public CardListAdapter(List<CardModel> data) {
        super(data);
        addItemType(CardModel.ITEM_TYPE_CARD, R.layout.wallet_my_bankcard_listitem);
        addItemType(CardModel.ITEM_TYPE_ADD, R.layout.wallet_my_bankcard_additem);
    }

    @Override
    protected void convert(BaseViewHolder helper, CardModel item) {
        int itemType = item.getItemType();
        if (itemType == CardModel.ITEM_TYPE_CARD) {
            CardItem cardItem = item.getCardItem();
            convertCard(helper, cardItem);
        } else if (itemType == CardModel.ITEM_TYPE_ADD) {
            AddItem addItem = item.getAddItem();
            convertAdd(helper, addItem);
        }
    }

    private void convertAdd(BaseViewHolder helper, AddItem item) {
        ImageView iv_addBtn = helper.getView(R.id.iv_addBtn);
        TextView tv_addTip = helper.getView(R.id.tv_addTip);
    }

    private void convertCard(BaseViewHolder helper, CardItem item) {
        TioImageView iv_cardBg = helper.getView(R.id.iv_cardBg);
        TioImageView iv_bankIcon = helper.getView(R.id.iv_bankIcon);
        TextView tv_bankName = helper.getView(R.id.tv_bankName);
        TextView tv_bankCardNo = helper.getView(R.id.tv_bankCardNo);
        CardView bg_center = helper.getView(R.id.bg_center);

        tv_bankName.setText(StringUtils.format("%s%s",
                StringUtils.null2Length0(item.getBankName()),
                StringUtils.null2Length0(item.getBankCardType())
        ));
        tv_bankCardNo.setText(StringUtils.null2Length0(item.getBankCardNo()));

//        if (TioNewPay.IS_DEBUG) {
//            iv_bankIcon.loadDrawableId(R.drawable.wallet_bankicon_jianshe);
//            iv_cardBg.loadDrawableId(R.drawable.wallet_bankbg_jianshe);
//        } else {
//            iv_bankIcon.loadUrlStatic(item.getBankLogo());
//            iv_cardBg.loadUrlStatic(item.getBankLogoBg());
//        }
        iv_bankIcon.loadUrlStatic(item.getBankLogo());
        iv_cardBg.loadUrlStatic(item.getBankLogoBg());

        String bankCardBgColor = item.getBankCardBgColor();
        if (bankCardBgColor != null) {
            bg_center.setCardBackgroundColor(Color.parseColor(bankCardBgColor));
        } else {
            bg_center.setCardBackgroundColor(Color.GRAY);
        }

    }

}
