package com.tiocloud.chat.feature.group.card.fragment.adapter;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.feature.group.card.fragment.adapter.model.MultiModel;
import com.tiocloud.chat.feature.group.card.fragment.adapter.model.MultiType;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/2/25
 * desc :
 */
public class SelectGroupAdapter extends BaseMultiItemQuickAdapter<MultiModel, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    private String keyWord;

    public SelectGroupAdapter() {
        super(null);
        addItemType(MultiType.GROUP.value, R.layout.tio_select_group_item);
        setOnItemClickListener(this);
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    protected void convert(BaseViewHolder helper, MultiModel item) {
        if (item.type == MultiType.GROUP) {
            convertGroup(helper, item.group);
        }
    }

    private void convertGroup(BaseViewHolder helper, MailListResp.Group item) {
        TioImageView avatar = helper.getView(R.id.hiv_avatar);
        TextView name = helper.getView(R.id.tv_name);
        TextView tv_memberNum = helper.getView(R.id.tv_memberNum);
        TextView tv_desc = helper.getView(R.id.tv_desc);

        // 群头像
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
        // 群名
        name.setText(KeywordUtil.matcherSearchTitle(
                Utils.getApp().getResources().getColor(R.color.blue_4c94ff),
                StringUtil.nonNull(item.name),
                keyWord));
        // 群人数
        tv_memberNum.setText(String.format(Locale.getDefault(), "%d人", item.joinnum));
        tv_desc.setText(String.format(Locale.getDefault(), "%s", "nbnhjhgg"));
        tv_desc.setVisibility(View.INVISIBLE);
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

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    // ====================================================================================
    // 点击事件
    // ====================================================================================

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MultiModel multiModel = getData().get(position);
        if (multiModel.type == MultiType.GROUP) {
            MailListResp.Group group = multiModel.group;
            onClickGroupItem(group);
        }
    }

    protected void onClickGroupItem(MailListResp.Group group) {

    }

}
