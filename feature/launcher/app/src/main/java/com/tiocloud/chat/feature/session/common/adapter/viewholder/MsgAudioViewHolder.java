package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import static com.tiocloud.chat.util.AESEncrypt.getFileName;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.GsonUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.FileUtils;
import com.watayouxiang.audiorecord.TioAudioBubbleUtils;
import com.watayouxiang.audiorecord.TioAudioPlayer;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgAudio;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/08/05
 *     desc   :
 * </pre>
 */
public class MsgAudioViewHolder extends MsgBaseViewHolder {
    private FrameLayout container;
    private InnerMsgAudio audio;
    private TioImageView image;
    private TextView tv;

    private int audioImageId;

    public MsgAudioViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.tio_msg_item_audio;
    }

    @Override
    protected void inflateContent() {
        container = findViewById(R.id.fl_container);
        TioImageView image_left = findViewById(R.id.image_left);
        TioImageView image_right = findViewById(R.id.image_right);
        TextView tv_left = findViewById(R.id.tv_left);
        TextView tv_right = findViewById(R.id.tv_right);
        LinearLayout ll_left = findViewById(R.id.ll_left);
        LinearLayout ll_right = findViewById(R.id.ll_right);

        if (getMessage().isSendMsg()) {
            ll_left.setVisibility(View.GONE);
            ll_right.setVisibility(View.VISIBLE);
            image = image_right;
            tv = tv_right;
            audioImageId = R.drawable.audio_ownvoice;
        } else {
            ll_right.setVisibility(View.GONE);
            ll_left.setVisibility(View.VISIBLE);
            image = image_left;
            tv = tv_left;
            audioImageId = R.drawable.audio_voice;
        }
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        Log.d("hjq", "InnerMsgAudio="+GsonUtils.toJson(getMessage()));
        audio = (InnerMsgAudio) getMessage().getContentObj();
        if (audio == null) return;

        // 语音时长
        tv.setText(String.format(Locale.getDefault(), "%d''", audio.seconds));
        // 气泡长度
        TioAudioBubbleUtils.setAudioBubbleWidth(container, audio.seconds);
        if(!TextUtils.isEmpty(audio.fingerprint)) {
            Glide.with(tv.getContext())
                    .downloadOnly()
                    .load(HttpCache.TIO_RES_URL+audio.url)
                    .listener(new RequestListener<File>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                            Log.d("===下载失败=44==","===");
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                            try {
                                AESEncrypt.decryptFile(resource, FileUtils.bytePath, getFileName(audio.url),audio.fingerprint);
                                // 初始化播放器
                                TioAudioPlayer.getInstance().init(onPlayerListener, getMessage().getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            return false;
                        }
                    })
                    .preload();
        }else {
            // 初始化播放器
            TioAudioPlayer.getInstance().init(onPlayerListener, getMessage().getId());
        }
    }

    @Override
    protected boolean isShowContentBg() {
        return true;
    }

    @Override
    protected View.OnClickListener onContentClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio == null) return;
                String resUrl = TextUtils.isEmpty(audio.fingerprint)?
                        HttpCache.getResUrl(audio.url) :  FileUtils.bytePath+ getFileName(audio.url);
                if (resUrl == null) return;
                TioAudioPlayer.getInstance().toggle(onPlayerListener, resUrl, getMessage().getId());
            }
        };
    }

    private final TioAudioPlayer.OnPlayerListener onPlayerListener = new TioAudioPlayer.OnPlayerListener() {
        @Override
        public void onWtPlayerStart() {
            image.loadDrawableId(audioImageId, true);
        }

        @Override
        public void onWtPlayerStop() {
            image.loadDrawableId(audioImageId, false);
        }
    };
}
