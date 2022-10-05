package com.tiocloud.chat.feature.group.card.fragment.mvp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.widget.dialog.base.CardDialog;
import com.tiocloud.chat.feature.group.card.GroupCardActivity;
import com.tiocloud.chat.feature.group.card.fragment.adapter.ExSelectGroupAdapter;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.CheckSendCardReq;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.TioToast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class FragmentSelectGroupPresenter extends FragmentSelectGroupContract.Presenter {

    private ExSelectGroupAdapter adapter;

    public FragmentSelectGroupPresenter(FragmentSelectGroupContract.View view) {
        super(new FragmentSelectGroupModel(), view);
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new ExSelectGroupAdapter() {
            @Override
            protected void onClickGroupItem(MailListResp.Group group) {
                super.onClickGroupItem(group);
                checkSendCardReq(group);
            }
        };
        recyclerView.setAdapter(adapter);
        // 添加头部
        View view = new View(recyclerView.getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtils.dp2px(12));
        view.setLayoutParams(params);
        adapter.addHeaderView(view);
    }

    private void checkSendCardReq(MailListResp.Group group) {
        CheckSendCardReq req = new CheckSendCardReq(group.groupid);
        req.setCancelTag(getModel());
        req.get(new TioCallback<Void>() {
            @Override
            public void onTioSuccess(Void aVoid) {
                showConfirmDialog(group);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void showConfirmDialog(MailListResp.Group group) {
        CardDialog cardDialog = new CardDialog(getView().getActivity());
        cardDialog.tv_title.setText("群聊邀请");
        cardDialog.hiv_avatar.load_tioAvatar(group.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(group.avatar))
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
//                            HelperGlide.loadHead(getContext(),imagebyte2, cardDialog.hiv_avatar);
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
        cardDialog.tv_usrName.setText(StringUtil.nonNull(group.name));
        cardDialog.tv_positiveBtn.setText("发送名片");
        cardDialog.tv_positiveBtn.setOnClickListener(view -> {
            Activity activity = getView().getActivity();
            if (activity instanceof GroupCardActivity) {
                GroupCardActivity friendActivity = (GroupCardActivity) activity;
                friendActivity.closePage(group.groupid);
            }
        });
        cardDialog.show();
    }

    // ====================================================================================
    // 搜索
    // ====================================================================================

    @Override
    public void search(final String keyWord) {
        if (adapter == null) return;
        getModel().getMailList(keyWord, new BaseModel.DataProxy<List<MailListResp.Group>>() {
            @Override
            public void onSuccess(List<MailListResp.Group> groups) {
                adapter.updateData(groups, keyWord);
            }
        });
    }

}
