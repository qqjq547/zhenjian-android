package com.tiocloud.chat.feature.user.applylist.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ResourceUtils;
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
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.ApplyListResp;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-02-24
 * desc :
 */
public class ApplyListAdapter extends BaseQuickAdapter<ApplyListResp.Data, BaseViewHolder> {

    public ApplyListAdapter() {
        super(R.layout.tio_apply_list_item, null);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ApplyListResp.Data item = getData().get(position);
                onClickItem(item, position);
            }
        });
        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                ApplyListResp.Data item = getData().get(position);
                if (view.getId() == R.id.tv_agreeBtn) {
                    if (item.status == 2) {
                        onClickAgreeBtn(item, position, view);
                    }
                } else if (view.getId() == R.id.tv_ignoreBtn) {
                    onClickIgnoreBtn(item, position, view);
                }
            }
        });
    }

    protected void onClickIgnoreBtn(ApplyListResp.Data item, int position, View view) {

    }

    protected void onClickItem(@NonNull ApplyListResp.Data item, int position) {

    }

    protected void onClickAgreeBtn(@NonNull ApplyListResp.Data item, int position, View view) {

    }

    @Override
    protected void convert(BaseViewHolder helper, ApplyListResp.Data item) {
        TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_subtitle = helper.getView(R.id.tv_subtitle);
        TextView tv_agreeBtn = helper.getView(R.id.tv_agreeBtn);
        TextView tv_ignoreBtn = helper.getView(R.id.tv_ignoreBtn);

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
//                            HelperGlide.loadHead(getContext(),imagebyte2,hiv_avatar);
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
        tv_name.setText(StringUtil.nonNull(item.nick));
        tv_subtitle.setText(StringUtil.nonNull(item.greet));

        // 同意按钮
        tv_agreeBtn.setEnabled(true);
        helper.addOnClickListener(tv_agreeBtn.getId());
        if (item.status == 2) {// 申请中
            tv_agreeBtn.setBackground(ResourceUtils.getDrawable(R.drawable.sel_add_friend_btn));
            tv_agreeBtn.setTextColor(Utils.getApp().getResources().getColorStateList(R.color.sel_add_friend_txt));
            tv_agreeBtn.setText(R.string.agree);
        } else if (item.status == 1) {// 申请通过
            tv_agreeBtn.setBackground(new ColorDrawable(Color.TRANSPARENT));
            tv_agreeBtn.setTextColor(Utils.getApp().getResources().getColor(R.color.gray_888888));
            tv_agreeBtn.setText(R.string.added);
        } else if (item.status == 3) {// 已忽略
            tv_agreeBtn.setBackground(new ColorDrawable(Color.TRANSPARENT));
            tv_agreeBtn.setTextColor(Utils.getApp().getResources().getColor(R.color.gray_888888));
            tv_agreeBtn.setText(R.string.already_ignore);
        } else {
            tv_agreeBtn.setBackground(new ColorDrawable(Color.TRANSPARENT));
            tv_agreeBtn.setTextColor(Utils.getApp().getResources().getColor(R.color.gray_888888));
            tv_agreeBtn.setText(R.string.empty);
        }

        // 忽略按钮
        tv_ignoreBtn.setEnabled(true);
        helper.addOnClickListener(tv_ignoreBtn.getId());
        if (item.status == 2) {// 申请中
            tv_ignoreBtn.setVisibility(View.VISIBLE);
        } else {
            tv_ignoreBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 标记添加成功
     */
    public void flagAgree(int position) {
        getData().get(position).status = 1;
        notifyItemChanged(position + getHeaderLayoutCount());
    }

    /**
     * 标记忽略
     */
    public void flagIgnoreByPosition(int position) {
        getData().get(position).status = 3;
        refreshNotifyItemChanged(position);
    }
}
