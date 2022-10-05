package com.tiocloud.chat.feature.group.silent;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/06
 *     desc   :
 * </pre>
 */
class ListAdapter extends BaseMultiItemQuickAdapter<ListModel, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ListAdapter(List<ListModel> data) {
        super(data);
        addItemType(ListModel.ITEM_TYPE_NORMAL, R.layout.tio_silent_list_item_normal);
    }

    @Override
    public void setNewData(@Nullable List<ListModel> data) {
        super.setNewData(data);
        // 空白页
        RecyclerView recyclerView = getRecyclerView();
        if (recyclerView != null) {
            View notDataView = LayoutInflater.from(recyclerView.getContext()).inflate(R.layout.tio_list_empty, recyclerView, false);
            setEmptyView(notDataView);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, ListModel item) {
        int itemType = item.getItemType();
        switch (itemType) {
            case ListModel.ITEM_TYPE_NORMAL:
                convertNormal(helper, item.getNormalItem());
                break;
        }
    }

    private void convertNormal(BaseViewHolder helper, ListNormalItem item) {
        ImageView iv_delete = helper.getView(R.id.iv_delete);
        TioImageView iv_avatar = helper.getView(R.id.iv_avatar);
        TextView tv_title = helper.getView(R.id.tv_title);
        TextView tv_subtitle = helper.getView(R.id.tv_subtitle);
        TextView tv_tag = helper.getView(R.id.tv_tag);

        iv_delete.setVisibility(ListNormalItem.isShowDelete() ? View.VISIBLE : View.GONE);
        iv_avatar.load_tioAvatar(item.getAvatar());
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(item.getAvatar()))
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
//                            HelperGlide.loadHead(getContext(),imagebyte2, iv_avatar);
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
        tv_title.setText(item.getTitle());
        tv_tag.setText(item.getTagTxt());

        String subtitle = item.getSubtitle();
        if (StringUtils.isEmpty(subtitle)) {
            tv_subtitle.setVisibility(View.GONE);
        } else {
            tv_subtitle.setVisibility(View.VISIBLE);
            tv_subtitle.setText(subtitle);
        }

        helper.addOnClickListener(iv_delete.getId());
    }
}
