package com.tiocloud.newpay.feature.bankcard_my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletMyBankcardActivityBinding;
import com.tiocloud.newpay.feature.alipbind.AddAliPayCardActivity;
import com.tiocloud.newpay.feature.alipbind.BindAliPayActivity;
import com.tiocloud.newpay.feature.bankcard_add.AddBankCardActivity;
import com.tiocloud.newpay.feature.bankcard_my.adapter.AddItem;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardItem;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardListAdapter;
import com.tiocloud.newpay.feature.bankcard_my.adapter.CardModel;
import com.tiocloud.newpay.feature.bankcard_my.mvp.Contract;
import com.tiocloud.newpay.feature.bankcard_my.mvp.Presenter;
import com.tiocloud.newpay.feature.redpacket.RedPacketActivity;
import com.watayouxiang.androidutils.page.MvpLightActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.response.PayBankCardListResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   : 我的银行卡
 * </pre>
 */
public class MyBankCardActivity extends MvpLightActivity<Presenter, WalletMyBankcardActivityBinding> implements Contract.View {

    private CardListAdapter listAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, MyBankCardActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_my_bankcard_activity;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.init();
    }

    @Override
    public void onResume(int count) {
        super.onResume(count);
        if (count > 1) {
            presenter.reqCardListData();
        }
    }

    @Override
    public Presenter newPresenter() {
        return new Presenter(this);
    }

    @Override
    public void resetUI() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        //绑定支付宝
        binding.bgAliPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isGlobalSingleClick()) return;
                AddAliPayCardActivity.start(getActivity());

            }
        });
        listAdapter = new CardListAdapter(null);
        listAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                CardModel model = listAdapter.getData().get(position);
                int itemType = model.getItemType();
                if (itemType == CardModel.ITEM_TYPE_CARD) {
                    CardItem cardItem = model.getCardItem();
                    presenter.editBankCard(cardItem);
                    return true;
                }
                return false;
            }
        });
        listAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CardModel model = listAdapter.getData().get(position);
                int itemType = model.getItemType();
                if (itemType == CardModel.ITEM_TYPE_ADD) {
                    AddItem addItem = model.getAddItem();
                    presenter.addBankCard(addItem);
                }
            }
        });
        binding.recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void onBankCardListResp(PayBankCardListResp resp) {
        listAdapter.setNewData(presenter.resp2ListModel(resp));
    }

}
