package com.tiocloud.chat.feature.home.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tencent.bugly.crashreport.CrashReport;
import com.tiocloud.account.TioAccount;
import com.tiocloud.account.feature.account.AccountActivity;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.databinding.TioUserFragmentBinding;
import com.tiocloud.chat.feature.curr.detail.CurrDetailActivity;
import com.tiocloud.chat.feature.home.user.mvp.UserContract;
import com.tiocloud.chat.feature.home.user.mvp.UserPresenter;
import com.tiocloud.chat.feature.settings.SettingsActivity;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.common.ModuleConfig;
import com.tiocloud.commonwallet.TioWallet;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayOpenReq;
import com.watayouxiang.httpclient.model.response.PayOpenResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.qrcode.feature.qrcode_decoder.QRCodeDecoderActivity;
import com.watayouxiang.qrcode.feature.qrcode_my.MyQRCodeActivity;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-02-03
 * desc :
 */
public class UserFragment extends TioFragment implements UserContract.View {

    private UserPresenter presenter;
    private TioUserFragmentBinding binding;

    @Override
    public void initUI() {
//        setStatusBarCustom(binding);
        binding.tvName.setText("");
        binding.tvEmail.setText("");
        binding.rlSettings.setOnClickListener(v ->
//                CrashReport.testJavaCrash()
        SettingsActivity.start(getActivity())
        );
        binding.rlModifyInfo.setOnClickListener(v -> CurrDetailActivity.start(getActivity()));
        binding.hivAvatar.setOnClickListener(v -> CurrDetailActivity.start(getActivity()));
//        if (ModuleConfig.ENABLE_WALLET) {
//            binding.rlWallet.setVisibility(View.VISIBLE);
//            binding.rlWallet.setOnClickListener(new OnSingleClickListener() {
//                @Override
//                public void onSingleClick(View view) {
//                    TioWallet.getWallet().WalletActivity_start(getActivity());
//                }
//            });
////            if (ModuleConfig.DEBUG) {
////                binding.rlWallet.setOnLongClickListener(v -> {
////                    TioWallet.getWallet().OpenWalletActivity_start(getActivity());
//
////                    return true;
////                });
////            }
//        } else {
//            binding.rlWallet.setVisibility(View.GONE);
//        }
        if (ModuleConfig.ENABLE_QR_CODE) {
            binding.ivQrcode.setVisibility(View.VISIBLE);
            binding.ivQrcode.setOnClickListener(v -> {
                if (ClickUtils.isViewSingleClick(v))
                    MyQRCodeActivity.start(getActivity());
            });
        } else {
            binding.ivQrcode.setVisibility(View.GONE);
        }
        binding.qrDecode.setOnClickListener(v -> QRCodeDecoderActivity.start(getActivity()));
        binding.llNick.setOnClickListener(v -> AccountActivity.start(getActivity()));
    }

    @Override
    public void updateUI(UserCurrResp resp) {
        binding.tvName.setText(StringUtil.nonNull(resp.nick));
        binding.tvEmail.setText(StringUtil.nonNull(resp.phone));
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
        binding.tvZhanghaohao.setText("账号："+resp.loginname);
        // 检测是否绑定手机
//        TioAccount.checkIsBindPhone(resp);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.tio_user_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new UserPresenter(this);
        presenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    public void onRefresh() {
        if (presenter != null) {
            presenter.updateUIData();
        }
    }
}
