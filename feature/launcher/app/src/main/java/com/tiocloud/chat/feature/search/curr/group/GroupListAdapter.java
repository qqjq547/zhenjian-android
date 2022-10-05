package com.tiocloud.chat.feature.search.curr.group;

import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.tiocloud.chat.R;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-02-14
 * desc :
 */
public class GroupListAdapter extends BaseQuickAdapter<MailListResp.Group, BaseViewHolder> {

    private final String keyWord;

    public GroupListAdapter(@Nullable List<MailListResp.Group> data, String keyWord) {
        super(R.layout.tio_search_group_item, data);
        this.keyWord = keyWord;
    }

    @Override
    protected void convert(BaseViewHolder helper, MailListResp.Group item) {
        setItemData(helper, item, keyWord);
    }

    public static void setItemData(BaseViewHolder helper, MailListResp.Group item, String keyWord) {
        TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_subtitle = helper.getView(R.id.tv_subtitle);

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
        tv_name.setText(KeywordUtil.matcherSearchTitle(
                Color.parseColor("#56D89A"),
                StringUtil.nonNull(item.name),
                keyWord));

        tv_subtitle.setText(KeywordUtil.matcherSearchTitle(
                Color.parseColor("#56D89A"),
                String.format(Locale.getDefault(), "%d人", item.joinnum),
                keyWord));
    }
}
