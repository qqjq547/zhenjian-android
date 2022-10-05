package com.tiocloud.chat.feature.session.common.adapter.viewholder;

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
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.browser.ShareBean;
import com.tiocloud.chat.feature.browser.TioShareBrowserActivity;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgTemplate;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/23
 *     desc   : 模版消息
 * </pre>
 */
public class MsgTempViewHolder extends MsgBaseViewHolder {

    private TioImageView hiv_img;
    private TextView tv_subtitle;
    private TextView tv_title;

    public MsgTempViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.tio_msg_item_temp;
    }

    @Override
    protected void inflateContent() {
        tv_title = findViewById(R.id.tv_title);
        tv_subtitle = findViewById(R.id.tv_subtitle);
        hiv_img = findViewById(R.id.hiv_img);
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        resetUI();
        InnerMsgTemplate template = (InnerMsgTemplate) getMessage().getContentObj();
        if (template == null) return;
        initUI(template);
    }

    private void initUI(InnerMsgTemplate template) {
        String title = StringUtils.null2Length0(template.title);
        String subtitle = StringUtils.null2Length0(template.subtitle);
        String img = HttpCache.getResUrl(template.img);
        String url = HttpCache.getResUrl(template.url);

        tv_title.setText(title);
        tv_subtitle.setText(subtitle);
        hiv_img.load_tioAvatar(img);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(img)
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
//                                    .into(hiv_img);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        getContentContainer().setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ShareBean shareBean = new ShareBean(title, subtitle, url, img);
                TioShareBrowserActivity.start(getActivity(), shareBean);
            }
        });
    }

    private void resetUI() {
        tv_title.setText("标题...");
        tv_subtitle.setText("副标题...");
        hiv_img.load_tioAvatar(null);
        getContentContainer().setOnClickListener(null);
    }
}
