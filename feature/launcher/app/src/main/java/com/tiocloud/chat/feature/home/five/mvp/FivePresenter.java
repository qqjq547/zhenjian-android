package com.tiocloud.chat.feature.home.five.mvp;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tiocloud.chat.feature.home.five.adapter.FiveAdapter;
import com.tiocloud.chat.widget.TioRefreshView;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.request.FoundListResp;

import java.util.Objects;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public class FivePresenter extends FiveContract.Presenter {

    // ====================================================================================
    // eventBus
    // ====================================================================================

    public FivePresenter(FiveContract.View view) {
        super(view);
    }



    // ====================================================================================
    // ui
    // ====================================================================================

    private FiveAdapter adapter;
    private TioRefreshView refreshView;

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        adapter = new FiveAdapter(recyclerView);
        ListItemClickListener listener = new ListItemClickListener();
        adapter.setOnItemClickListener(listener);
    }

    private  final class ListItemClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            FoundListResp.Found found = ((FiveAdapter) adapter).getData().get(position);
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
