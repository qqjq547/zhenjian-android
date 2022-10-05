package com.tiocloud.chat.feature.group.at;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.AtGroupUserListResp;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/07/20
 *     desc   :
 * </pre>
 */
class AtAdapter extends BaseQuickAdapter<AtGroupUserListResp.List, BaseViewHolder> {
    @Nullable
    private String mSearchKey;

    public AtAdapter(RecyclerView recyclerView) {
        super(R.layout.item_at);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        bindToRecyclerView(recyclerView);
    }

    @Override
    protected void convert(BaseViewHolder helper, AtGroupUserListResp.List item) {
        // 头像
        TioImageView tiv_avatar = helper.getView(R.id.tiv_avatar);
        tiv_avatar.load_tioAvatar(item.avatar);
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
//                            HelperGlide.loadHead(getContext(),imagebyte2, tiv_avatar);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        // 备注名、昵称
        TextView tv_title = helper.getView(R.id.tv_title);
        TextView tv_subTitle = helper.getView(R.id.tv_subTitle);
        String remarkname = item.remarkname;
        String nick = item.srcnick;
        if (!TextUtils.isEmpty(remarkname) && !TextUtils.isEmpty(nick)) {
            // 显示 备注名、昵称
            tv_title.setText(KeywordUtil.matcherSearchTitle(
                    Utils.getApp().getResources().getColor(R.color.blue_4c94ff),
                    remarkname,
                    mSearchKey));
            tv_subTitle.setVisibility(View.VISIBLE);
            tv_subTitle.setText(KeywordUtil.matcherSearchTitle(
                    Utils.getApp().getResources().getColor(R.color.blue_4c94ff),
                    String.format("昵称：%s", nick),
                    mSearchKey));
        } else {
            // 显示 昵称
            tv_title.setText(KeywordUtil.matcherSearchTitle(
                    Utils.getApp().getResources().getColor(R.color.blue_4c94ff),
                    StringUtil.nonNull(nick),
                    mSearchKey));
            tv_subTitle.setVisibility(View.GONE);
        }
    }

    public void setNewData(@Nullable List<AtGroupUserListResp.List> data, @Nullable String searchkey) {
        mSearchKey = searchkey;
        setNewData(data);
    }
}
