package com.tiocloud.chat.feature.user.group.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ResourceUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.chat.widget.header.NumCountHeader;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-01-23
 * desc :
 */
public class GroupAdapter extends BaseQuickAdapter<MailListResp.Group, BaseViewHolder> {

    @Override
    public void setNewData(@Nullable List<MailListResp.Group> data) {
        super.setNewData(data);
    }

    public GroupAdapter(RecyclerView recyclerView) {
        super(R.layout.tio_group_list_item);
        Context context = recyclerView.getContext();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(this);
        // 添加列头
        NumCountHeader header = new NumCountHeader(recyclerView.getContext());
        addHeaderView(header);
    }

    @Override
    protected void convert(BaseViewHolder helper, MailListResp.Group item) {
        TioImageView avatar = helper.getView(R.id.hiv_avatar);
        TextView name = helper.getView(R.id.tv_name);
        TextView tv_memberNum = helper.getView(R.id.tv_memberNum);

        // 头像
        avatar.load_tioAvatar(item.avatar);
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
//                            HelperGlide.loadHead(getContext(),imagebyte2,avatar);
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
        // 昵称
        name.setText(StringUtil.nonNull(item.name));
        if(item.shownumflag==1){
            // 群人数
            tv_memberNum.setText(String.format(Locale.getDefault(), "%d人", item.joinnum));
        }else {
            // 群人数
            tv_memberNum.setText(String.format(Locale.getDefault(), "", ""));
        }
          // 标志
        Drawable drawableLeft = null;
        GroupRoleEnum groupRoleEnum = item.getGroupRoleEnum();
        if (groupRoleEnum == GroupRoleEnum.OWNER) {
            drawableLeft = ResourceUtils.getDrawable(R.drawable.tio_ic_group_owner);
        } else if (groupRoleEnum == GroupRoleEnum.MGR) {
            drawableLeft = ResourceUtils.getDrawable(R.drawable.tio_ic_group_mgr);
        }
        if (drawableLeft != null) {
            drawableLeft.setBounds(0, 0, drawableLeft.getMinimumWidth(), drawableLeft.getMinimumHeight());

        }
        tv_memberNum.setCompoundDrawables(drawableLeft, null, null, null);
    }

    // 设置群组个数
    public void setGroupSize(int size) {
        LinearLayout headerLayout = getHeaderLayout();
        int childCount = headerLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = headerLayout.getChildAt(i);
            if (child instanceof NumCountHeader) {
                NumCountHeader header = (NumCountHeader) child;
                header.setCenterText(String.format(Locale.getDefault(), "%d个群聊", size));
            }
        }
    }
}
