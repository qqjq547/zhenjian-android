package com.tiocloud.chat.feature.group.transfergroup.fragment.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.model.response.GroupUserListResp;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;
import java.util.ListIterator;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/2/25
 * desc :
 */
public class TransferGroupAdapter extends BaseQuickAdapter<GroupUserListResp.GroupMember, BaseViewHolder> {
    private String keyWord;

    public TransferGroupAdapter() {
        super(R.layout.tio_transfer_group_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupUserListResp.GroupMember item) {
        TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_subtitle = helper.getView(R.id.tv_subtitle);

        hiv_avatar.load_tioAvatar(item.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(item.avatar))
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
//                            HelperGlide.loadHead(getContext(),imagebyte2,hiv_avatar);
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
        tv_name.setText(KeywordUtil.matcherSearchTitle(
                Color.parseColor("#FF4C94E8"),
                StringUtil.nonNull(item.nick),
                keyWord));
        tv_subtitle.setVisibility(View.GONE);
    }

    // ====================================================================================
    // handle data
    // ====================================================================================

    public void setNewData(GroupUserListResp friends, String keyWord) {
        this.keyWord = keyWord;
        removeMyselfItem(friends);
        filterItems(keyWord, friends);
        setNewData(friends.list);
    }

    public void addData(GroupUserListResp friends, String keyWord) {
        this.keyWord = keyWord;
        removeMyselfItem(friends);
        filterItems(keyWord, friends);
        addData(friends.list);
    }

    private void filterItems(String keyWord, GroupUserListResp friends) {
        ListIterator<GroupUserListResp.GroupMember> it = friends.list.listIterator();
        while (it.hasNext()) {
            GroupUserListResp.GroupMember user = it.next();
            if (user.nick != null && keyWord != null) {
                if (!user.nick.toLowerCase().contains(keyWord.toLowerCase())) {
                    it.remove();
                }
            }
        }
    }

    private void removeMyselfItem(GroupUserListResp friends) {
        int currUid = 0;
        try {
            currUid = Integer.parseInt(String.valueOf(TioDBPreferences.getCurrUid()));
        } catch (Exception ignored) {
        }

        ListIterator<GroupUserListResp.GroupMember> it = friends.list.listIterator();
        while (it.hasNext()) {
            if (it.next().uid == currUid) {
                it.remove();
                break;
            }
        }
    }
}
