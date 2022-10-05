package com.tiocloud.chat.feature.home.found.mvp;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiocloud.chat.feature.home.found.adapter.FoundAdapter;
import com.tiocloud.chat.widget.TioRefreshView;
import com.watayouxiang.httpclient.model.request.FoundListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;

import java.util.Objects;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public class FoundPresenter extends FoundContract.Presenter {

    // ====================================================================================
    // eventBus
    // ====================================================================================

    public FoundPresenter(FoundContract.View view) {
        super(view);
    }



    // ====================================================================================
    // ui
    // ====================================================================================

    private FoundAdapter adapter;
    private TioRefreshView refreshView;

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        adapter = new FoundAdapter(recyclerView);
        ListItemClickListener listener = new ListItemClickListener();
        adapter.setOnItemClickListener(listener);
    }

    private  final class ListItemClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            FoundListResp.Found found = ((FoundAdapter) adapter).getData().get(position);
            Objects.requireNonNull(getView().getMainGroupFragment()).loadUrlFromWebView(found.linkedaddress);
        }

    }

    @Override
    public void initRefreshView(TioRefreshView refreshView) {
        this.refreshView = refreshView;
        refreshView.setOnRefreshListener(this::load);
        refreshView.setEnabled(true);
    }

    // ====================================================================================
    // data
    // ====================================================================================

    @Override
    public void load() {
        if (adapter == null) return;
        if (refreshView == null) return;

        getModel().requestFoundList(new BaseModel.DataProxy<FoundListResp>() {
            @Override
            public void onSuccess(FoundListResp founds) {
                // 数据
                adapter.setNewData(founds);
                refreshView.setRefreshing(false);
            }

            @Override
            public void onFailure(String msg) {
                TioToast.showShort(msg);
                refreshView.setRefreshing(false);
            }
        });
    }
}
