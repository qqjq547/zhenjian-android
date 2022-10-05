package com.tiocloud.webrtc.webrtc.feature.videoreq;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.BarUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.webrtc.databinding.TioCallVideoReqFragmentBinding;
import com.tiocloud.webrtc.utils.TimeUtil;
import com.tiocloud.webrtc.webrtc.data.RTCViewHolderImpl;
import com.tiocloud.webrtc.webrtc.feature.videoreq.mvp.VideoReqContract;
import com.tiocloud.webrtc.webrtc.feature.videoreq.mvp.VideoReqPresenter;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.UserInfoResp;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.webrtclib.TioWebRTC;

import org.webrtc.CameraVideoCapturer;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;

/**
 * author : TaoWang
 * date : 2020/5/26
 * desc :
 */
public class CallVideoReqFragment extends TioFragment implements VideoReqContract.View {
    private TioCallVideoReqFragmentBinding binding;
    private VideoReqPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TioCallVideoReqFragmentBinding.inflate(inflater, container, false);
        presenter = new VideoReqPresenter(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BarUtils.addMarginTopEqualStatusBarHeight(binding.ivCallAvatar);
        BarUtils.addMarginTopEqualStatusBarHeight(binding.localVideoView);

        presenter.reqUserInfo();
        RTCViewHolderImpl rtcViewHolder = new RTCViewHolderImpl(
                binding.localVideoView,
                binding.remoteVideoView);
        presenter.initRTCViews(rtcViewHolder);
        presenter.call();
    }

    @Override
    public void closePage() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void showWaitingView() {
        binding.rlCalling.setVisibility(View.GONE);
        binding.rlCall.setVisibility(View.VISIBLE);
        binding.tvCallCancel.setOnClickListener(view -> TioWebRTC.getInstance().callCancel());
    }

    @Override
    public void showCallingView() {
        binding.rlCall.setVisibility(View.GONE);
        binding.rlCalling.setVisibility(View.VISIBLE);
        binding.getRoot().setOnClickListener(view -> binding.rlCalling.setVisibility(binding.rlCalling.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE));
        binding.tvCallingHangup.setOnClickListener(view -> TioWebRTC.getInstance().hangUp());
        binding.tvCallingSwitchCamera.setOnClickListener(view -> TioWebRTC.getInstance().switchCamera(new CameraVideoCapturer.CameraSwitchHandler() {
            @Override
            public void onCameraSwitchDone(boolean isFontCamera) {
                binding.tvCallingSwitchCamera.setSelected(!isFontCamera);
            }

            @Override
            public void onCameraSwitchError(String s) {
                TioToast.showShort(s);
            }
        }));
        binding.localVideoView.setOnClickListener(v -> {
            if (TioWebRTC.getInstance().isSwitchVideoSink()) {
                TioWebRTC.getInstance().switchVideoSink(false);
            } else {
                TioWebRTC.getInstance().switchVideoSink(true);
            }
        });
    }

    @Override
    public void onUserInfoResp(UserInfoResp userInfoResp) {
        binding.ivCallAvatar.load_tioAvatar(userInfoResp.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(userInfoResp.avatar))
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
//                                    .into(binding.ivCallAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        String name = userInfoResp.remarkname;
        if (TextUtils.isEmpty(name)) {
            name = String.valueOf(userInfoResp.nick);
        }
        binding.tvCallNick.setText(name);
    }

    @Override
    public void onCountDownTimer(long l) {
        binding.tvCallingTimer.setText(TimeUtil.formatMS(l));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
