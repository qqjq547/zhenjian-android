package com.watayouxiang.androidutils.feature.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.databinding.TioVideoPlayerActivityBinding;
import com.watayouxiang.androidutils.engine.AESEncrypt;
import com.watayouxiang.androidutils.engine.PreferencesUtil;
import com.watayouxiang.androidutils.feature.player.mvp.VideoPlayerContract;
import com.watayouxiang.androidutils.feature.player.mvp.VideoPlayerPresenter;
import com.watayouxiang.androidutils.page.TioActivity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/06/10
 *     desc   :
 * </pre>
 */
public class VideoPlayerActivity extends TioActivity implements VideoPlayerContract.View {

    private static final String EXTRA_VIDEO_URL = "EXTRA_VIDEO_URL";

    private VideoPlayerPresenter presenter;

    public static void start(Context context, String videoUrl) {
        Intent starter = new Intent(context, VideoPlayerActivity.class);
        starter.putExtra(EXTRA_VIDEO_URL, videoUrl);
        if (!(context instanceof Activity)) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    private String getVideoUrl() {
        return getIntent().getStringExtra(EXTRA_VIDEO_URL);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(this);
        BarUtils.transparentStatusBar(this);

        TioVideoPlayerActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.tio_video_player_activity);
        presenter = new VideoPlayerPresenter(this);
        presenter.initPlayer(binding.videoPlayer, getVideoUrl());
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pauseVideo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resumeVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        int im_file_encrypt= PreferencesUtil.getInt("im_file_encrypt",0);
        if(im_file_encrypt==1){
            boolean isRemove= AESEncrypt.deleteFile(getVideoUrl());
        }
//        Log.d("===视频删除: ==" ,isRemove+"==");

    }

    @Override
    public void onBackPressed() {
        if (presenter.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
