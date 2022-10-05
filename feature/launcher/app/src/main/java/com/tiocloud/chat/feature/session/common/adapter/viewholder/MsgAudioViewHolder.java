package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.watayouxiang.audiorecord.TioAudioBubbleUtils;
import com.watayouxiang.audiorecord.TioAudioPlayer;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgAudio;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

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
        audio = (InnerMsgAudio) getMessage().getContentObj();
        if (audio == null) return;

        // 语音时长
        tv.setText(String.format(Locale.getDefault(), "%d''", audio.seconds));
        // 气泡长度
        TioAudioBubbleUtils.setAudioBubbleWidth(container, audio.seconds);
        // 初始化播放器
        TioAudioPlayer.getInstance().init(onPlayerListener, getMessage().getId());
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
                String resUrl = HttpCache.getResUrl(audio.url);
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
