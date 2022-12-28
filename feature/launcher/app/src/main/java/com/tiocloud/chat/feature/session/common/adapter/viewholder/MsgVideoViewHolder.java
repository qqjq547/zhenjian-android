package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.FileUtils;
import com.watayouxiang.androidutils.feature.player.VideoPlayerActivity;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgVideo;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.FileOutputStream;

import static com.tiocloud.chat.util.AESEncrypt.getFileName;

/**
 * author : TaoWang
 * date : 2020/3/5
 * desc :
 */
public class MsgVideoViewHolder extends MsgBaseViewHolder {

    private TioImageView msgImageView;
    private String mVideoUrl;
    public String fingerprint;

    public MsgVideoViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.tio_msg_item_video;
    }

    @Override
    protected void inflateContent() {
        msgImageView = findViewById(R.id.msgImageView);
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        InnerMsgVideo data = (InnerMsgVideo) getMessage().getContentObj();
        if (data == null) return;
        mVideoUrl = data.url;
        Log.d("====视频==coverurl==","=="+data.coverurl);
        fingerprint=data.fingerprint;
//        secretkey=data.secretkey;

        Log.d("====视频==fingerprint==","=="+data.fingerprint);
//        Log.d("====视频==secretkey==","=="+data.secretkey);

        if(!TextUtils.isEmpty(fingerprint)){
            Glide.with(msgImageView.getContext())
                    .downloadOnly()
                    .load(HttpCache.TIO_RES_URL+data.coverurl)
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            Log.d("===下载失败=44==","===");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            try {
                                AESEncrypt.decryptFile(resource,FileUtils.bytePath, getFileName(data.coverurl),data.fingerprint);
                                Glide.with(msgImageView.getContext()).load(FileUtils.bytePath+ getFileName(data.coverurl)).listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        Log.d("===图片加载失败视频=4444==","===");
                                        boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ getFileName(data.coverurl));

                                        return false;
                                    }
                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        //加载成功之后删除本地解密的图片
//                                    Log.d("===图片加载视频===","==大大大大大大=");

                                        boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ getFileName(data.coverurl));
                                        return false;
                                    }}).into(msgImageView);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            return false;
                        }
                    })
                    .preload();
        }else {
            msgImageView.load_tioMsgPic(HttpCache.TIO_RES_URL+data.coverurl, 300, 300);

        }

    }

    @Override
    protected View.OnClickListener onContentClick() {
        return view -> {
            if (mVideoUrl != null) {
                VideoPlayerActivity.start(view.getContext(), mVideoUrl,fingerprint);
            }
        };
    }
}
