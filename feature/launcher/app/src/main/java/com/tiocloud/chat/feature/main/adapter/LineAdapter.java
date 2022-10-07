package com.tiocloud.chat.feature.main.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.watayouxiang.httpclient.model.request.FoundListResp;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends BaseAdapter {
   private List<FoundListResp.Found> dataList=new ArrayList<>();
    public LineAdapter(@Nullable List<FoundListResp.Found> data) {
       this.dataList=data;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int i) {
        return dataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_line,null);
        TextView tvName=root.findViewById(R.id.tv_name);
        tvName.setText(dataList.get(i).itemname);
        return root;
    }
}
