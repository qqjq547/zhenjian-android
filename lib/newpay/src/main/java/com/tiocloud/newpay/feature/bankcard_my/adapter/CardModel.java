package com.tiocloud.newpay.feature.bankcard_my.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   :
 * </pre>
 */
public class CardModel implements MultiItemEntity {
    public static final int ITEM_TYPE_CARD = 1;
    public static final int ITEM_TYPE_ADD = 2;

    private final int itemType;
    private CardItem cardItem;
    private AddItem addItem;

    public CardModel(CardItem cardItem) {
        this.itemType = ITEM_TYPE_CARD;
        this.cardItem = cardItem;
    }

    public CardModel(AddItem addItem) {
        this.itemType = ITEM_TYPE_ADD;
        this.addItem = addItem;
    }

    public CardItem getCardItem() {
        return cardItem;
    }

    public AddItem getAddItem() {
        return addItem;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
