package com.tiocloud.chat.feature.group.info.fragment.adapter;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.feature.group.info.fragment.adapter.model.AddButton;
import com.tiocloud.chat.feature.group.info.fragment.adapter.model.RemoveButton;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.GroupUserListResp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/2/27
 * desc :
 */
public class MemberListAdapter extends BaseMultiItemQuickAdapter<MemberItem, BaseViewHolder> {

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public MemberListAdapter(List<MemberItem> data, RecyclerView recyclerView) {
        super(data);
        addItemType(MemberItem.USER, R.layout.tio_member_list_item);
        addItemType(MemberItem.BUTTON, R.layout.tio_member_list_item_btn);

        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), 5, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(this);
    }

    /**
     * @param mgrPermission          有群管理权限吗
     * @param groupUserList          群成员列表
     * @param inviteMemberPermission 有邀请好友权限吗
     */
    public void setNewData(boolean mgrPermission, GroupUserListResp groupUserList, boolean inviteMemberPermission) {
        List<MemberItem> items = new ArrayList<>();

        // 数据处理
        List<GroupUserListResp.GroupMember> list = groupUserList.list;
        int maxItemSize = 15;
        if (mgrPermission || inviteMemberPermission) --maxItemSize;
        if (mgrPermission) --maxItemSize;
        maxItemSize = Math.min(maxItemSize, list.size());
        List<GroupUserListResp.GroupMember> members = list.subList(0, maxItemSize);

        // 数据整合
        for (GroupUserListResp.GroupMember user : members) {
            items.add(new MemberItem(user));
        }
        if (mgrPermission || inviteMemberPermission) {
            items.add(new MemberItem(new AddButton()));
        }
        if (mgrPermission) {
            items.add(new MemberItem(new RemoveButton()));
        }

        // 显示
        setNewData(items);
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberItem item) {
        switch (helper.getItemViewType()) {
            case MemberItem.USER:
                TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
                TextView tv_name = helper.getView(R.id.tv_name);
                hiv_avatar.load_tioAvatar(item.user.avatar);
//                Glide.with(getContext())
//                        .downloadOnly()
//                        .load(getResUrl(item.user.avatar))
//                        .listener(new RequestListener<File>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                                Log.d("===下载失败===","===");
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                                byte[] bytes= FileUtils.File2byte(resource);
//                                try {
//                                    byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                                    HelperGlide.loadHead(getContext(),imagebyte2, hiv_avatar);
//
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                return false;
//                            }
//                        })
//                        .preload();
                tv_name.setText(StringUtil.nonNull(item.user.nick));
                break;
            case MemberItem.BUTTON:
                ImageView iv_icon = helper.getView(R.id.iv_icon);
                TextView tv_btn = helper.getView(R.id.tv_btn);
                iv_icon.setImageResource(item.button.iconRes);
                tv_btn.setText(item.button.name);
                break;
        }
    }
}
