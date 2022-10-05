package com.tiocloud.chat.feature.user.detail.feature.nonfriend;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.R;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.TioAddFriendFragmentBinding;
import com.tiocloud.chat.feature.user.detail.feature.nonfriend.mvp.NonFriendContract;
import com.tiocloud.chat.feature.user.detail.feature.nonfriend.mvp.NonFriendPresenter;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.UserInfoResp;
import com.watayouxiang.androidutils.page.TioFragment;
import com.tiocloud.chat.util.TioImageBrowser;
import com.tiocloud.chat.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc : 非好友 - 信息详情页
 */
public class NonFriendDetailFragment extends TioFragment implements NonFriendContract.View {

    private TioAddFriendFragmentBinding binding;

    public static TioFragment create(String uid) {
        NonFriendDetailFragment fragment = new NonFriendDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TioExtras.EXTRA_USER_ID, uid);
        fragment.setArguments(bundle);
        return fragment;
    }

    private NonFriendPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TioAddFriendFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new NonFriendPresenter(this);
        presenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public String getUid() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            return arguments.getString(TioExtras.EXTRA_USER_ID, null);
        }
        return null;
    }

    @Override
    public void resetViews() {
        binding.tvName.setText("");
        binding.tvAddress.setText("");
    }

    @Override
    public void initViews() {
        binding.tvAddFriend.setOnClickListener(v -> presenter.doAddFriend(v));
    }

    @Override
    public void onUserInfoResp(UserInfoResp resp) {
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
//                            Glide.with(getContext())
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(binding.hivAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        binding.tvName.setText(StringUtil.nonNull(resp.nick));
//        binding.tvAddress.setText(String.format(Locale.getDefault(), "%s %s", resp.country, resp.city));
        binding.tvSign.setText(TextUtils.isEmpty(resp.sign) ? getString(R.string.tahaimeiyougexingsin):resp.sign);

        // 签名
        if (!TextUtils.isEmpty(resp.sign)) {
            binding.ivSign.setVisibility(View.VISIBLE);
            binding.tvSign.setVisibility(View.VISIBLE);
            binding.tvSign.setText(resp.sign);
        }

        // 昵称
        binding.tvName.setText(StringUtil.nonNull(resp.nick));

        // 头像查看
        TioImageBrowser.getInstance().clickViewShowPic(binding.hivAvatar, resp.avatar);
    }
}
