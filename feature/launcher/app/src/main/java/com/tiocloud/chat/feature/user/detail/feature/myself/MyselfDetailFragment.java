package com.tiocloud.chat.feature.user.detail.feature.myself;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.TioMyselfInfoFragmentBinding;
import com.tiocloud.chat.feature.session.p2p.P2PSessionActivity;
import com.tiocloud.chat.feature.user.detail.feature.myself.mvp.MyselfDetailContract;
import com.tiocloud.chat.feature.user.detail.feature.myself.mvp.MyselfDetailPresenter;
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
 * desc :
 * 自己 - 信息详情页
 */
public class MyselfDetailFragment extends TioFragment implements MyselfDetailContract.View {

    private MyselfDetailPresenter presenter;
    private TioMyselfInfoFragmentBinding binding;

    public static TioFragment create(@NonNull String uid) {
        MyselfDetailFragment fragment = new MyselfDetailFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.tio_myself_info_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new MyselfDetailPresenter(this);
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
        binding.tvAddress.setText("");
        binding.tvName.setText("");
        binding.tvSign.setText("");
        binding.tvP2pTalk.setOnClickListener(view1 -> P2PSessionActivity.active(getActivity(), getUid()));
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
//                            HelperGlide.loadHead(getContext(),imagebyte2, binding.hivAvatar);
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
//        binding.tvAddress.setText(String.format(Locale.getDefault(), "%s %s", resp.country, resp.city));

        // 签名
        if (!TextUtils.isEmpty(resp.sign)) {
            binding.ivSign.setVisibility(View.VISIBLE);
            binding.tvSign.setVisibility(View.VISIBLE);
            binding.tvSign.setText(resp.sign);
        } else {
            binding.ivSign.setVisibility(View.INVISIBLE);
            binding.tvSign.setVisibility(View.INVISIBLE);
        }
        if (!TextUtils.isEmpty(resp.remarkname)){
            binding.tvNumber.setText("备注名："+resp.remarkname);
            binding.llBeizhuming.setVisibility(View.VISIBLE);
        }else {
            binding.llBeizhuming.setVisibility(View.GONE);

        }
        // 昵称
        binding.tvName.setText(StringUtil.nonNull(resp.nick));

        // 头像查看
        TioImageBrowser.getInstance().clickViewShowPic(binding.hivAvatar, resp.avatar);
    }
}
