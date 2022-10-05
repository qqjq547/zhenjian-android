package com.tiocloud.chat.feature.search.user.fragment.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.UserSearchResp;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public class SearchUserListAdapter extends BaseQuickAdapter<UserSearchResp.ListBean, BaseViewHolder> {

    private String keyWord;

    public SearchUserListAdapter(RecyclerView recyclerView) {
        super(R.layout.tio_user_search_item);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(this);
        setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SearchUserListAdapter userAdapter = (SearchUserListAdapter) adapter;
                UserSearchResp.ListBean bean = userAdapter.getData().get(position);
                if (view.getId() == R.id.tv_addBtn) {
                    onClickAddBtn(bean, view);
                }
            }
        });
        setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SearchUserListAdapter userAdapter = (SearchUserListAdapter) adapter;
                UserSearchResp.ListBean bean = userAdapter.getData().get(position);
                onClickItem(bean);
            }
        });
        // 添加头部
        View view = new View(recyclerView.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12));
        view.setLayoutParams(params);
        addHeaderView(view);
    }

    protected void onClickItem(UserSearchResp.ListBean bean) {

    }

    @Override
    protected void convert(BaseViewHolder helper, UserSearchResp.ListBean item) {
        TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_addBtn = helper.getView(R.id.tv_addBtn);

        hiv_avatar.load_tioAvatar(item.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(item.avatar))
//                .listener(new RequestListener<File>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                        Log.d("===下载失败===","===");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                        byte[] bytes= FileUtils.File2byte(resource);
//                        try {
//                            byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                            Glide.with(getContext())
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(hiv_avatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        // 昵称
        tv_name.setText(KeywordUtil.matcherSearchTitle(
                Utils.getApp().getResources().getColor(R.color.blue_4c94ff),
                StringUtil.nonNull(item.nick),
                keyWord));
        // 添加按钮
        tv_addBtn.setEnabled(true);
        helper.addOnClickListener(tv_addBtn.getId());
    }

    protected void onClickAddBtn(@NonNull UserSearchResp.ListBean bean, View view) {

    }

    public void setNewData(List<UserSearchResp.ListBean> list, String keyWord) {
        this.keyWord = keyWord;
        setNewData(list);
    }
}
