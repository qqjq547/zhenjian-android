package com.tiocloud.newpay.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletChooseBankcardDialogBinding;
import com.tiocloud.newpay.feature.bankcard_add.AddBankCardActivity;
import com.tiocloud.newpay.mvp.paydialog.ChooseBankCardDialogView;
import com.tiocloud.newpay.mvp.paydialog.PayDialogPresenter;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.widget.dialog2.BaseBindingDialog;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;
import com.watayouxiang.httpclient.model.response.PayGetWalletInfoResp;
import com.watayouxiang.imclient.TioIMClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/11
 *     desc   : 选择银行卡
 * </pre>
 */
public class ChooseBankCardDialog extends BaseBindingDialog<WalletChooseBankcardDialogBinding> {

    @NonNull
    private final DialogVo dialogVo;
    private OnItemClickListener onItemClickListener;
    private PayDialogPresenter presenter;

    public ChooseBankCardDialog(@NonNull Context context, boolean showPacketItem) {
        this(context, new DialogVo(showPacketItem));
    }

    public ChooseBankCardDialog(@NonNull Context context) {
        this(context, new DialogVo());
    }

    public ChooseBankCardDialog(@NonNull Context context, @NonNull DialogVo dialogVo) {
        super(context);
        this.dialogVo = dialogVo;
    }

    @Override
    public int getLayoutId() {
        return R.layout.wallet_choose_bankcard_dialog;
    }

    @NonNull
    @Override
    public Builder getBuilder() {
        return super.getBuilder()
                .setGravity(Gravity.BOTTOM)
                .setWidth(WindowManager.LayoutParams.MATCH_PARENT)
                .setAnimationsResId(R.style.tio_bottom_dialog_anim)
                .setCancelable(false)
                ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PayDialogPresenter(chooseBankCardDialogView);
        TioIMClient.getInstance().getEventEngine().register(this);
        resetUI();
        refresh(false);
    }

    private void refresh(boolean force) {
        if (presenter == null) return;
        if (CollectionUtils.isEmpty(dialogVo.cardModels) || force) {
            if (force) {
                dialogVo.cardModels = null;
            }
            if (dialogVo.showPacketItem) {
                presenter.reqWalletInfo();
            } else {
                presenter.reqBankCardList();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        TioIMClient.getInstance().getEventEngine().unregister(this);
        if (presenter != null) {
            presenter.detachView();
        }
    }

    private void resetUI() {
        // 关闭弹窗
        binding.ivClose.setOnClickListener(view -> dismiss());
        // 添加新银行卡
        binding.rlBottomContainer.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                AddBankCardActivity.start(view.getContext());
            }
        });
        // 初始化列表
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.VERTICAL, false));
        CardListAdapter cardListAdapter = new CardListAdapter(dialogVo.cardModels);
        cardListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapter, view, position);
                }
            }
        });

//        binding.rlAliZhifu.setVisibility(View.VISIBLE);
        binding.tvTitleTX.setText("选择银行卡");
        binding.rlAliZhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClickText("支付宝");
                }
            }
        });
        recyclerView.setAdapter(cardListAdapter);
    }

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    // ====================================================================================
    // 数据列表
    // ====================================================================================

    public static class CardListAdapter extends BaseMultiItemQuickAdapter<CardModel, BaseViewHolder> {
        /**
         * Same as QuickAdapter#QuickAdapter(Context,int) but with
         * some initialization data.
         *
         * @param data A new list is created out of this one to avoid mutable list
         */
        public CardListAdapter(List<CardModel> data) {
            super(data);
            addItemType(CardModel.ITEM_TYPE_CARD, R.layout.wallet_bankcard_dialog_listitem);
            addItemType(CardModel.ITEM_TYPE_PACKET, R.layout.wallet_bankcard_dialog_packetitem);
        }

        @Override
        protected void convert(BaseViewHolder helper, CardModel model) {
            int itemType = model.getItemType();
            if (itemType == CardModel.ITEM_TYPE_CARD) {
                TioImageView iv_bankIcon = helper.getView(R.id.iv_bankIcon);
                TextView tv_title = helper.getView(R.id.tv_title);
                CardItem item = model.cardItem;

                iv_bankIcon.loadUrlStatic(item.bankIconUrl);

                String bankName = StringUtils.null2Length0(item.bankName);
                String bankCardType = StringUtils.null2Length0(item.bankCardType);
                if (!TextUtils.isEmpty(bankCardType)) {
                    bankCardType = " " + bankCardType;
                }
                String bankCardLastFourNum = StringUtils.null2Length0(item.bankCardLastFourNum);
                tv_title.setText(StringUtils.format("%s%s (%s)", bankName, bankCardType, bankCardLastFourNum));
            } else if (itemType == CardModel.ITEM_TYPE_PACKET) {
                TextView tv_title = helper.getView(R.id.tv_title);
                PacketItem item = model.packetItem;

                tv_title.setText(StringUtils.format("钱包 (¥%s)", item.moneyYuan));
            }
        }
    }

    public static abstract class OnItemClickListener implements BaseQuickAdapter.OnItemClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            this.onItemClick((CardListAdapter) adapter, view, position);

        }
        public abstract void onItemClickText(String text);

        public abstract void onItemClick(CardListAdapter adapter, View view, int position);
    }

    public static class CardModel implements MultiItemEntity {
        public static final int ITEM_TYPE_CARD = 1;
        public static final int ITEM_TYPE_PACKET = 2;

        private final int itemType;
        public CardItem cardItem;
        public PacketItem packetItem;

        public CardModel(CardItem cardItem) {
            this.itemType = ITEM_TYPE_CARD;
            this.cardItem = cardItem;
        }

        public CardModel(PacketItem packetItem) {
            this.itemType = ITEM_TYPE_PACKET;
            this.packetItem = packetItem;
        }

        @Override
        public int getItemType() {
            return itemType;
        }
    }

    public static class CardItem {
        public String bankIconUrl;
        public String bankName;
        public String bankCardType;
        public String bankCardLastFourNum;
        public PayBankCardListResp.Data originData;

        public CardItem setBankIconUrl(String bankIconUrl) {
            this.bankIconUrl = bankIconUrl;
            return this;
        }

        public CardItem setBankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public CardItem setBankCardType(String bankCardType) {
            this.bankCardType = bankCardType;
            return this;
        }

        public CardItem setBankCardLastFourNum(String bankCardLastFourNum) {
            this.bankCardLastFourNum = bankCardLastFourNum;
            return this;
        }

        public CardItem setOriginData(PayBankCardListResp.Data data) {
            this.originData = data;
            return this;
        }
    }

    public static class PacketItem {
        public String moneyYuan;
        public PayGetWalletInfoResp originData;

        public PacketItem setMoneyYuan(String moneyYuan) {
            this.moneyYuan = moneyYuan;
            return this;
        }

        public PacketItem setOriginData(PayGetWalletInfoResp resp) {
            this.originData = resp;
            return this;
        }
    }

    // ====================================================================================
    // 对话框数据模型
    // ====================================================================================

    public static class DialogVo {
        public boolean showPacketItem = false;
        public List<CardModel> cardModels = null;

        public DialogVo(boolean showPacketItem) {
            this.showPacketItem = showPacketItem;
        }

        public DialogVo() {
        }
    }

    private final ChooseBankCardDialogView chooseBankCardDialogView = new ChooseBankCardDialogView() {
        @Override
        protected void onPayGetWalletInfoResp(@NonNull ArrayList<CardModel> cardModels) {
            dialogVo.cardModels = cardModels;
            presenter.reqBankCardList();
        }

        @Override
        protected void onBankCardListResp(@NonNull ArrayList<CardModel> cardModels) {
            if (dialogVo.cardModels != null) {
                dialogVo.cardModels.addAll(cardModels);
            } else {
                dialogVo.cardModels = cardModels;
            }
            resetUI();
        }
    };

    // ====================================================================================
    // 重新刷新数据的事件
    // ====================================================================================

    public static class RefreshDataEvent {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshDataEvent(RefreshDataEvent event) {
        refresh(true);
    }

}
