package com.tiocloud.chat.feature.share.group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.R;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.ActivityShareGroupBinding;
import com.tiocloud.chat.feature.share.group.feature.recent.RecentFragment;
import com.tiocloud.chat.feature.share.group.feature.result.ResultFragment;
import com.tiocloud.chat.feature.share.group.model.ShareCardEntity;
import com.tiocloud.chat.feature.share.group.model.ShareCardFrom;
import com.tiocloud.chat.feature.share.group.model.ShareCardTo;
import com.tiocloud.chat.feature.share.group.mvp.ShareGroupContract;
import com.tiocloud.chat.feature.share.group.mvp.ShareGroupPresenter;
import com.tiocloud.chat.widget.dialog.base.CardDialog;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.CheckSendCardReq;
import com.watayouxiang.androidutils.listener.SimpleTextWatcher;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.widget.TioToast;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/07/22
 *     desc   : 把 "群组" 分享给 "好友/群聊"
 * </pre>
 */
public class ShareGroupActivity extends TioActivity implements ShareGroupContract.View {

    private ShareGroupPresenter presenter;
    private ActivityShareGroupBinding binding;
    private RecentFragment recentFragment;
    private ResultFragment resultFragment;

    public static void start(Activity context, String groupId) {
        CheckSendCardReq req = new CheckSendCardReq(groupId);
        req.setCancelTag(context);
        req.get(new TioCallback<Void>() {
            @Override
            public void onTioSuccess(Void aVoid) {
                startInternal(context, groupId);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private static void startInternal(Activity context, String groupId) {
        Intent starter = new Intent(context, ShareGroupActivity.class);
        starter.putExtra(TioExtras.EXTRA_GROUP_ID, groupId);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ShareGroupPresenter(this);
        presenter.initUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void bindContentView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_share_group);
    }

    @Override
    public void initEditText() {
        binding.etInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                presenter.onEtTextChanged(s);
            }
        });
    }

    @Override
    public void initFragmentContainers() {
        recentFragment = new RecentFragment();
        recentFragment.setContainerId(binding.frameLayout.getId());
        addFragment(recentFragment);

        resultFragment = new ResultFragment();
        resultFragment.setContainerId(binding.frameLayout.getId());
        addFragment(resultFragment);
    }

    @Override
    public void showRecentPage() {
        showFragment(recentFragment);
        hideFragment(resultFragment);
    }

    @Override
    public void showSearchResultPage(String s) {
        resultFragment.setSearchKey(s);
        showFragment(resultFragment);
        hideFragment(recentFragment);
    }

    @Override
    public void shareCard(ShareCardTo entity) {
        if (presenter != null) {
            presenter.shareCard(entity);
        }
    }

    @Override
    public String getFromGroupId() {
        return getIntent().getStringExtra(TioExtras.EXTRA_GROUP_ID);
    }

    @Override
    public void showCardDialog(ShareCardEntity entity) {
        ShareCardFrom from = entity.from;
        ShareCardTo to = entity.to;

        CardDialog cardDialog = new CardDialog(getActivity());
        cardDialog.tv_title.setText("发送给");
        cardDialog.hiv_avatar.load_tioAvatar(to.toAvatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(to.toAvatar))
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
//                                    .into(cardDialog.hiv_avatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        cardDialog.tv_usrName.setText(StringUtils.null2Length0(to.toName));
        cardDialog.tv_positiveBtn.setText("发送名片");
        cardDialog.tv_positiveBtn.setOnClickListener(view -> {
            presenter.reqShareCard("2", to.toUid, to.toGroupId, from.fromGroupId);
        });
        cardDialog.show();
    }
}
