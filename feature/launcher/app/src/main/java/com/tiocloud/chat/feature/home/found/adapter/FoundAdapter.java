package com.tiocloud.chat.feature.home.found.adapter;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.request.FoundListResp;

import java.util.List;

/**
 * author : TaoWang
 * date : 2020-01-23
 * desc :
 */
public class FoundAdapter extends BaseQuickAdapter<FoundListResp.Found, BaseViewHolder> {

    @Override
    public void setNewData(@Nullable List<FoundListResp.Found> data) {
        super.setNewData(data);
    }

    public FoundAdapter(RecyclerView recyclerView) {
        super(R.layout.tio_found_list_item);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, FoundListResp.Found item) {
        TioImageView logo = helper.getView(R.id.img_logo);
        TextView title = helper.getView(R.id.tv_title);

        // logo
        logo.load_tioFoundPic(item.icon);
        // title
        title.setText(StringUtil.nonNull(item.itemname));

    }

}
