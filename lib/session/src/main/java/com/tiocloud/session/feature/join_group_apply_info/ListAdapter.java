package com.tiocloud.session.feature.join_group_apply_info;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.session.R;
import com.tiocloud.session.TioSession;
import com.tiocloud.session.feature.session_info_p2p.P2PSessionInfoActivity;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.GroupApplyInfoResp;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/09
 *     desc   :
 * </pre>
 */
class ListAdapter extends BaseQuickAdapter<GroupApplyInfoResp.ItemsBean, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    public ListAdapter() {
        super(R.layout.session_join_group_apply_info_item);
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupApplyInfoResp.ItemsBean item) {
        TioImageView iv_avatar = helper.getView(R.id.iv_avatar);
        TextView tv_nick = helper.getView(R.id.tv_nick);

        iv_avatar.load_tioAvatar(item.getAvatar());
//        Glide.with(iv_avatar.getContext())
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
//                            Glide.with(iv_avatar.getContext())
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(iv_avatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        tv_nick.setText(StringUtils.null2Length0(item.getNick()));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GroupApplyInfoResp.ItemsBean bean = getData().get(position);
        int uid = bean.getUid();
        if (mContext != null && uid != 0) {
            TioSession.getBridge().startUserDetailActivity(mContext, uid + "");
        }
    }
}
