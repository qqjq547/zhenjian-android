package com.tiocloud.chat.feature.user.selectfriend.fragment.mvp;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.feature.user.selectfriend.SelectFriendActivity;
import com.tiocloud.chat.feature.user.selectfriend.fragment.adapter.ExSelectFriendAdapter;
import com.tiocloud.chat.widget.ContactsCatalogView;
import com.tiocloud.chat.widget.dialog.base.CardDialog;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.tiocloud.chat.util.StringUtil;

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
public class FragmentSelectFriendPresenter extends FragmentSelectFriendContract.Presenter {

    private ExSelectFriendAdapter adapter;

    public FragmentSelectFriendPresenter(FragmentSelectFriendContract.View view) {
        super(new FragmentSelectFriendModel(), view);
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    public void initRecyclerView(RecyclerView recyclerView, ContactsCatalogView contactsCatalogView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new ExSelectFriendAdapter() {
            @Override
            protected void onClickContactItem(MailListResp.Friend friend) {
                super.onClickContactItem(friend);
                showConfirmDialog(friend);
            }
        };
        adapter.installCatalogView(contactsCatalogView, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void showConfirmDialog(MailListResp.Friend friend) {
        CardDialog cardDialog = new CardDialog(getView().getActivity());
        cardDialog.hiv_avatar.load_tioAvatar(friend.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(friend.avatar))
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
        cardDialog.tv_usrName.setText(!TextUtils.isEmpty(friend.remarkname) ? friend.remarkname : StringUtil.nonNull(friend.nick));
        cardDialog.tv_positiveBtn.setText("发送名片");
        cardDialog.tv_positiveBtn.setOnClickListener(view -> {
            Activity activity = getView().getActivity();
            if (activity instanceof SelectFriendActivity) {
                SelectFriendActivity friendActivity = (SelectFriendActivity) activity;
                friendActivity.closePage(friend.uid);
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
        getModel().getMailList(keyWord, new BaseModel.DataProxy<List<MailListResp.Friend>>() {
            @Override
            public void onSuccess(List<MailListResp.Friend> friends) {
                adapter.updateData(friends, keyWord);
            }
        });
    }

}
