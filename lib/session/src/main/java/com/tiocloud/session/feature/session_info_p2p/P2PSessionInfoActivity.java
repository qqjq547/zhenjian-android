package com.tiocloud.session.feature.session_info_p2p;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.session.R;
import com.tiocloud.session.TioSession;
import com.tiocloud.session.databinding.SessionSessionInfoP2pActivityBinding;
import com.tiocloud.session.feature.session_info_p2p.mvp.Contract;
import com.tiocloud.session.feature.session_info_p2p.mvp.Presenter;
import com.watayouxiang.androidutils.constant.Extra;
import com.watayouxiang.androidutils.listener.OnSimpleCheckedChangeListener;
import com.watayouxiang.androidutils.page.MvpLightActivity;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static java.security.AccessController.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/03
 *     desc   :
 * </pre>
 */
public class P2PSessionInfoActivity extends MvpLightActivity<Presenter, SessionSessionInfoP2pActivityBinding> implements Contract.View {

    public static void start(Context context, String chatLinkId) {
        Intent starter = new Intent(context, P2PSessionInfoActivity.class);
        starter.putExtra(Extra.CHAT_LINK_ID, chatLinkId);
        context.startActivity(starter);
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.session_session_info_p2p_activity;
    }

    @Override
    public Presenter newPresenter() {
        return new Presenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.init();
    }

    @Override
    public String getChatLinkId() {
        return getIntent().getStringExtra(Extra.CHAT_LINK_ID);
    }

    @Override
    public void onChatInfoResp(WxChatItemInfoResp.DataBean resp) {
        // 头像
        binding.avatarTvName.setText(StringUtils.null2Length0(resp.name));
        binding.avatarTivAvatar.load_tioAvatar(resp.avatar);
//        Glide.with(this)
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
//                            Glide.with(P2PSessionInfoActivity.this)
//                                    .load(imagebyte2)
//                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                                    .into(binding.avatarTivAvatar);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        binding.avatarRlContainer.setOnClickListener(view -> TioSession.getBridge().startUserDetailActivity(getActivity(), resp.bizid));
        // 消息免打扰
        binding.dndSwitch.setChecked(resp.isOpenDND());
        binding.dndSwitch.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleDNDSwitch(resp.bizid, isChecked, compoundButton);
            }
        });
        // 聊天置顶
        binding.topChatSwitch.setChecked(resp.isTopChat());
        binding.topChatSwitch.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleTopChatSwitch(isChecked, compoundButton);
            }
        });
        // 清空聊天记录
        binding.clearRlContainer.setOnClickListener(view -> presenter.showClearChatRecordDialog());
        // 举报用户
        binding.reportRlContainer.setOnClickListener(view -> presenter.showReportDialog());
    }

    @Override
    public void resetUI() {
        // 头像
        binding.avatarTvName.setText(StringUtils.null2Length0(null));
        binding.avatarTivAvatar.load_tioAvatar(null);
        binding.avatarRlContainer.setOnClickListener(null);
        // 消息免打扰
        binding.dndSwitch.setChecked(false);
        binding.dndSwitch.setOnCheckedChangeListener(null);
        // 聊天置顶
        binding.topChatSwitch.setChecked(false);
        binding.topChatSwitch.setOnCheckedChangeListener(null);
        // 清空聊天记录
        binding.clearRlContainer.setOnClickListener(null);
        // 举报用户
        binding.reportRlContainer.setOnClickListener(null);
    }
}
