package com.tiocloud.newpay.feature.withdraw_record;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.CollectionUtils;
import com.tiocloud.newpay.feature.withdraw_detail.WithdrawDetailActivity;
import com.tiocloud.newpay.feature.withdraw_detail.WithdrawDetailVo;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.response.PayWithholdListResp;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletWithdrawRecordActivityBinding;
import com.tiocloud.newpay.feature.withdraw_record.adapter.ListAdapter;
import com.tiocloud.newpay.feature.withdraw_record.adapter.ListModel;
import com.tiocloud.newpay.feature.withdraw_record.mvp.Contract;
import com.tiocloud.newpay.feature.withdraw_record.mvp.Presenter;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   : 提现记录
 * </pre>
 */
public class WithdrawRecordActivity extends BindingLightActivity<WalletWithdrawRecordActivityBinding> implements Contract.View {

    private Presenter presenter;
    private ListAdapter listAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, WithdrawRecordActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @NonNull
    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_withdraw_record_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(this);
        presenter = new Presenter(this);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void resetUI() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListAdapter(null);
        listAdapter.setOnLoadMoreListener(() -> presenter.loadMoreData(), binding.recyclerView);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            PayWithholdListResp.ListBean original = listAdapter.getData().get(position).getOriginal();
            if (ClickUtils.isViewSingleClick(view)) {
                WithdrawDetailVo vo = WithdrawDetailVo.getInstance(original);
                WithdrawDetailActivity.start(getActivity(), vo);
            }
        });
        binding.recyclerView.setAdapter(listAdapter);
        binding.swipeRefreshLayout.setOnRefreshListener(() -> presenter.refreshData());
    }

    @Override
    public void setRefreshData(boolean ok, List<ListModel> models) {
        if (ok) {
            listAdapter.setNewData(models);
            if (CollectionUtils.isEmpty(models)) {
                View notDataView = getLayoutInflater().inflate(R.layout.wallet_withdraw_record_list_empty, (ViewGroup) binding.recyclerView.getParent(), false);
                listAdapter.setEmptyView(notDataView);
            }
        }
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setLoadMoreData(boolean ok, boolean lastPage, List<ListModel> models) {
        if (ok) {
            if (models != null) {
                listAdapter.addData(models);
            }
            if (lastPage) {
                listAdapter.loadMoreEnd();
            } else {
                listAdapter.loadMoreComplete();
            }
        } else {
            listAdapter.loadMoreFail();
        }
    }
}
