package com.tiocloud.chat.feature.search.customservice.fragment.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.httpclient.model.response.CustServiceTeamListResp;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public class SearchCustServiceTeamAdapter extends BaseQuickAdapter<CustServiceTeamListResp.ListBean, BaseViewHolder> {

    private String keyWord;

    public SearchCustServiceTeamAdapter(RecyclerView recyclerView) {
        super(R.layout.tio_custservice_search_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(this);
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SearchCustServiceTeamAdapter userAdapter = (SearchCustServiceTeamAdapter) adapter;
                CustServiceTeamListResp.ListBean bean = userAdapter.getData().get(position);
                if (view.getId() == R.id.tv_addBtn) {
                    onClickAddBtn(bean, view);
                }
            }
        });
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchCustServiceTeamAdapter userAdapter = (SearchCustServiceTeamAdapter) adapter;
                CustServiceTeamListResp.ListBean bean = userAdapter.getData().get(position);
                onClickItem(bean);
            }
        });
        // 添加头部
        View view = new View(recyclerView.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12));
        view.setLayoutParams(params);
        addHeaderView(view);
    }

    protected void onClickItem(CustServiceTeamListResp.ListBean bean) {

    }

    @Override
    protected void convert(BaseViewHolder helper, CustServiceTeamListResp.ListBean item) {
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_addBtn = helper.getView(R.id.tv_addBtn);
        // 昵称
        tv_name.setText(KeywordUtil.matcherSearchTitle(
                Utils.getApp().getResources().getColor(R.color.blue_4c94ff),
                StringUtil.nonNull(item.getName()),
                keyWord));
        // 添加按钮
        helper.addOnClickListener(tv_addBtn.getId());
    }

    protected void onClickAddBtn(@NonNull CustServiceTeamListResp.ListBean bean, View view) {

    }

    public void setNewData(List<CustServiceTeamListResp.ListBean> list, String keyWord) {
        this.keyWord = keyWord;
        setNewData(list);
    }
}
