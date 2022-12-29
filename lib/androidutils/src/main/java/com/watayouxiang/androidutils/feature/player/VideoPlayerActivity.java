package com.watayouxiang.androidutils.feature.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.databinding.TioVideoPlayerActivityBinding;
import com.watayouxiang.androidutils.engine.AESEncrypt;
import com.watayouxiang.androidutils.engine.PreferencesUtil;
import com.watayouxiang.androidutils.feature.player.mvp.VideoPlayerContract;
import com.watayouxiang.androidutils.feature.player.mvp.VideoPlayerPresenter;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgVideo;

import java.io.File;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;

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
    private static final String EXTRA_FINGERPRINT = "EXTRA_FINGERPRINT";

    private VideoPlayerPresenter presenter;
    private TioVideoPlayerActivityBinding binding;

    public static void start(Context context, String videoUrl,String fingerprint) {
        Intent starter = new Intent(context, VideoPlayerActivity.class);
        starter.putExtra(EXTRA_VIDEO_URL, videoUrl);
        starter.putExtra(EXTRA_FINGERPRINT, fingerprint);
        if (!(context instanceof Activity)) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    private String getVideoUrl() {
        return getIntent().getStringExtra(EXTRA_VIDEO_URL);
    }
    private String getFingerprint() {
        return getIntent().getStringExtra(EXTRA_FINGERPRINT);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.setFullScreen(this);
        BarUtils.transparentStatusBar(this);

        binding = DataBindingUtil.setContentView(this, R.layout.tio_video_player_activity);
        presenter = new VideoPlayerPresenter(this);
        if(!TextUtils.isEmpty(getFingerprint())){
            boolean isfile=fileIsExists( FileUtils.bytePath+getFileName(getVideoUrl()));
            Log.d("===是否存在===","==视频="+isfile);
            if(isfile){
                AESEncrypt.decryptFile(new File(FileUtils.bytePath+getFileName(getVideoUrl())), FileUtils.bytePath, "New"+getFileName(getVideoUrl()),getFingerprint());
                startplay();
            }else {
                binding.linLoading.setVisibility(View.VISIBLE);
                OkGo.<File>get(HttpCache.TIO_RES_URL+getVideoUrl())//
                        .tag(this)//
                        .execute(new FileCallback() {

                            @Override
                            public void onStart(Request<File, ? extends Request> request) {
                            }

                            @Override
                            public void onSuccess(Response<File> response) {
                                try {
                                    saveToAlbum(VideoPlayerActivity.this,response.body().getAbsolutePath());
                                    AESEncrypt.decryptFile(response.body(), FileUtils.bytePath, "New"+getFileName(getVideoUrl()),getFingerprint());
                                    binding.linLoading.setVisibility(View.GONE);
                                    startplay();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response<File> response) {
                               ToastUtils.showShort("下载出错");
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                int current=(int)(progress.currentSize*100/progress.totalSize);
                                binding.circleProgress.setProgress(current);
                            }
                        });

            }
        }else {
            startplay();
        }
    }
//    int count=0;
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//            count+=10;
//            Log.d("hjq","count="+count);
//            binding.circleProgress.setProgress(count);
//            if (count<=100){
//                sendEmptyMessageDelayed(1,500);
//            }
//        }
//    };
//    public void testLoading(){
//        handler.sendEmptyMessage(1);
//    }
    private void startplay(){
        presenter.initPlayer(binding.videoPlayer, FileUtils.bytePath+"New"+getFileName(getVideoUrl()));
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
    public boolean fileIsExists(String strFile) {
        try {
            File f=new File(strFile);
            if(f.exists()) {
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    private void saveToAlbum(Context context, String srcPath) {
        String dcimPath = FileUtils.bytePath;
        File file = new File(dcimPath, getFileName(getVideoUrl()));
        boolean isCopySuccess = com.blankj.utilcode.util.FileUtils.copy(srcPath, file.getAbsolutePath());
        if (isCopySuccess) {
//            Log.d("===保存成功==","=="+getFileName(mVideoUrl));
        } else {
//            Log.d("===保存失败==","=="+getFileName(mVideoUrl));

        }
    }
    public static String getFileName(String urlname) {
        int start = urlname.lastIndexOf("/");
        int end = urlname.length();
        if (start != -1 && end != -1) {
            return urlname.substring(start+1,end);
        } else {
            return null;
        }
    }
}
