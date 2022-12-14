package com.tiocloud.chat.feature.group.removemember.fragment.adapter;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import com.watayouxiang.httpclient.model.response.GroupUserListResp;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/2/25
 * desc :
 */
public class RemoveMemberAdapter extends BaseQuickAdapter<GroupUserListResp.GroupMember, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    private final LinkedList<String> checkedIds = new LinkedList<>();

    public RemoveMemberAdapter() {
        super(R.layout.tio_remove_member_item);
        checkedIds.clear();
        setOnItemClickListener(this);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupUserListResp.GroupMember item) {
        TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_subtitle = helper.getView(R.id.tv_subtitle);
        CheckBox checkBox = helper.getView(R.id.checkBox);
        TextView tv_groupOwnerTag = helper.getView(R.id.tv_groupOwnerTag);

        hiv_avatar.load_tioAvatar(item.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(item.avatar))
//                .listener(new RequestListener<File>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                        Log.d("===????????????===","===");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===????????????===",resource.getName()+"==="+resource.getAbsolutePath());
//                        byte[] bytes= FileUtils.File2byte(resource);
//                        try {
//                            byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                            HelperGlide.loadHead(getContext(),imagebyte2, hiv_avatar);
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
        tv_name.setText(StringUtil.nonNull(item.nick));
        tv_subtitle.setVisibility(View.GONE);

        if (item.grouprole == 1) {
            tv_groupOwnerTag.setVisibility(View.VISIBLE);
            tv_groupOwnerTag.setText("??????");
        } else if (item.grouprole == 3) {
            tv_groupOwnerTag.setVisibility(View.VISIBLE);
            tv_groupOwnerTag.setText("?????????");
        } else {
            tv_groupOwnerTag.setVisibility(View.GONE);
        }

        checkBox.setClickable(false);
        checkBox.setChecked(checkedIds.contains(String.valueOf(item.uid)));
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GroupUserListResp.GroupMember friend = getData().get(position);
        String uid = String.valueOf(friend.uid);
        if (!checkedIds.remove(uid)) {
            checkedIds.add(uid);
        }
        notifyItemChanged(position);
        if (onCheckedChangeListener != null)
            onCheckedChangeListener.onCheckedItemChange(checkedIds);
    }

    public LinkedList<String> getCheckedIds() {
        return checkedIds;
    }

    // ====================================================================================
    // OnCheckedChangeListener
    // ====================================================================================

    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        onCheckedChangeListener = listener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedItemChange(LinkedList<String> linkedList);
    }
}
