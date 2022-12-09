package com.tiocloud.chat.feature.user.detail.feature.friend;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.TioFriendInfoFragmentBinding;
import com.tiocloud.chat.feature.session.p2p.P2PSessionActivity;
import com.tiocloud.chat.feature.user.detail.feature.friend.mvp.FriendDetailContract;
import com.tiocloud.chat.feature.user.detail.feature.friend.mvp.FriendDetailPresenter;
import com.tiocloud.chat.util.TioImageBrowser;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.UserInfoResp;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc :
 * 好友 - 信息详情页
 */
public class FriendDetailFragment extends TioFragment implements FriendDetailContract.View {

    private FriendDetailPresenter presenter;
    private TioFriendInfoFragmentBinding binding;

    public static TioFragment create(@NonNull String uid) {
        FriendDetailFragment fragment = new FriendDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TioExtras.EXTRA_USER_ID, uid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    public String getUid() {
        return getArguments().getString(TioExtras.EXTRA_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.tio_friend_info_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new FriendDetailPresenter(this);
        presenter.init();

    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.reqUserInfo();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void initViews() {
        // 头像
        binding.hivAvatar.load_tioAvatar(null);
        // 名称
        binding.tvRemarkName.setText("");
        // 昵称
        binding.tvName.setText("");
        // 地址
        binding.tvAddress.setText("");
        // 签名
        binding.tvSign.setText("");
        // 删除好友
        binding.tvDeleteFriend.setOnClickListener(view -> presenter.doDelFriend(view));
        // 聊天
        binding.tvP2pTalk.setOnClickListener(view1 ->

                P2PSessionActivity.active(getTioActivity(), getUid()));
        binding.ivEditRemarkName.setOnClickListener(view ->
                presenter.doModifyRemarkName(view.getContext(), getUid())
        );

    }

    @Override
    public void onUserInfoResp(UserInfoResp resp) {
        // 头像
        binding.hivAvatar.load_tioAvatar(resp.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(resp.avatar))
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
//                            HelperGlide.loadHead(getContext(),imagebyte2,binding.hivAvatar);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        TioImageBrowser.getInstance().clickViewShowPic(binding.hivAvatar, resp.avatar);

        // 签名
        binding.tvSign.setText(TextUtils.isEmpty(resp.sign) ? getString(R.string.tahaimeiyougexingsin):resp.sign);

        // 昵称
        binding.tvName.setText(StringUtils.null2Length0(resp.nick));
        // 地址
//        if(!TextUtils.isEmpty( resp.city)){
//            binding.tvAddress.setText(String.format(Locale.getDefault(), "%s %s", resp.country, resp.city));
//            binding.llAddress.setVisibility(View.VISIBLE);
//        }else {
//            binding.llAddress.setVisibility(View.GONE);
//
//        }
        // 备注名、昵称
        if (!TextUtils.isEmpty(resp.remarkname)) {
            binding.tvRemarkName.setText(resp.remarkname);
            binding.tvName.setVisibility(View.VISIBLE);
            binding.tvName.setText(resp.nick);
        } else {
            binding.tvRemarkName.setText(resp.nick);

        }
    }
}
