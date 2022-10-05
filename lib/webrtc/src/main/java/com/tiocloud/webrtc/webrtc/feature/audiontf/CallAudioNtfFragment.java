package com.tiocloud.webrtc.webrtc.feature.audiontf;

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
import com.tiocloud.webrtc.databinding.TioCallAudioNtfFragmentBinding;
import com.tiocloud.webrtc.utils.TimeUtil;
import com.tiocloud.webrtc.webrtc.CallActivity;
import com.tiocloud.webrtc.webrtc.data.RTCViewHolderImpl;
import com.tiocloud.webrtc.webrtc.feature.audiontf.mvp.AudioNtfContract;
import com.tiocloud.webrtc.webrtc.feature.audiontf.mvp.AudioNtfPresenter;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.UserInfoResp;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.webrtclib.TioWebRTC;
import com.watayouxiang.webrtclib.model.AudioDevice;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;

/**
 * author : TaoWang
 * date : 2020/5/22
 * desc :
 */
public class CallAudioNtfFragment extends TioFragment implements AudioNtfContract.View {
    private TioCallAudioNtfFragmentBinding binding;
    private AudioNtfPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TioCallAudioNtfFragmentBinding.inflate(inflater, container, false);
        presenter = new AudioNtfPresenter(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BarUtils.addMarginTopEqualStatusBarHeight(binding.ivReplyAvatar);
        BarUtils.addMarginTopEqualStatusBarHeight(binding.ivCallingAvatar);
        BarUtils.addMarginTopEqualStatusBarHeight(binding.localVideoView);

        presenter.reqUserInfo();
        RTCViewHolderImpl rtcViewHolder = new RTCViewHolderImpl(
                binding.localVideoView,
                binding.remoteVideoView);
        presenter.initRTCViews(rtcViewHolder);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
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
        binding.rlReply.setVisibility(View.VISIBLE);
        binding.tvReplyAgree.setVisibility(View.VISIBLE);
        binding.tvReplyAgree.setOnClickListener(view -> TioWebRTC.getInstance().callReply((byte) 1, null));
        binding.tvReplyDisagree.setOnClickListener(view -> TioWebRTC.getInstance().callReply((byte) 2, null));
    }

    @Override
    public void showCallingView() {
        binding.rlReply.setVisibility(View.GONE);
        binding.rlCalling.setVisibility(View.VISIBLE);
        binding.tvCallingHangup.setOnClickListener(view -> TioWebRTC.getInstance().hangUp());
        binding.tvCallingToggleAudio.setOnClickListener(view -> {
            AudioDevice audioDevice = TioWebRTC.getInstance().getAudioDevice();
            if (audioDevice == AudioDevice.SPEAKER) {
                // 听筒模式
                TioWebRTC.getInstance().setAudioDevice(AudioDevice.RECEIVER);
                binding.tvCallingToggleAudio.setSelected(false);
            } else if (audioDevice == AudioDevice.RECEIVER) {
                // 扬声器模式
                TioWebRTC.getInstance().setAudioDevice(AudioDevice.SPEAKER);
                binding.tvCallingToggleAudio.setSelected(true);
            }
        });
        binding.tvCallingToggleMic.setOnClickListener(view -> {
            if (TioWebRTC.getInstance().isLocalAudioEnable()) {
                // 禁止麦克风
                TioWebRTC.getInstance().setLocalAudioEnable(false);
                binding.tvCallingToggleMic.setSelected(true);
            } else {
                // 开启麦克风
                TioWebRTC.getInstance().setLocalAudioEnable(true);
                binding.tvCallingToggleMic.setSelected(false);
            }
        });
    }

    @Override
    public CallActivity getCallActivity() {
        return (CallActivity) getActivity();
    }

    @Override
    public void onUserInfoResp(UserInfoResp userInfoResp) {
        String name = userInfoResp.remarkname;
        if (TextUtils.isEmpty(name)) {
            name = String.valueOf(userInfoResp.nick);
        }
        binding.ivReplyAvatar.load_tioAvatar(userInfoResp.avatar);
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
//                                    .into(binding.ivReplyAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        binding.tvReplyNick.setText(name);
        binding.ivCallingAvatar.load_tioAvatar(userInfoResp.avatar);
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
//
//                            Glide.with(getContext())
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(binding.ivCallingAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        binding.tvCallingNick.setText(name);
    }

    @Override
    public void onCountDownTimer(long l) {
        binding.tvCallingTimer.setText(TimeUtil.formatMS(l));
    }

    @Override
    public void changeWaitingView() {
        binding.tvReplyAgree.setVisibility(View.GONE);
        binding.tvTip.setText("接通中...");
    }
}
