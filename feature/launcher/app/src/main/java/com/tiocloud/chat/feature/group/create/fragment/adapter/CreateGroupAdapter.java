package com.tiocloud.chat.feature.group.create.fragment.adapter;

import android.graphics.Color;
import android.text.TextUtils;
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
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.feature.group.create.fragment.adapter.model.MultiModel;
import com.tiocloud.chat.feature.group.create.fragment.adapter.model.MultiType;
import com.tiocloud.chat.feature.group.create.fragment.adapter.model.SectionModel;
import com.tiocloud.chat.util.KeywordUtil;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.httpclient.model.response.MailListResp;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/2/25
 * desc :
 */
public class CreateGroupAdapter extends BaseMultiItemQuickAdapter<MultiModel, BaseViewHolder> implements BaseQuickAdapter.OnItemClickListener {
    private LinkedList<String> checkedIds = new LinkedList<>();
    private LinkedList<String> checkedNames = new LinkedList<>();
    private String keyWord;

    public CreateGroupAdapter() {
        super(null);
        addItemType(MultiType.SECTION.value, R.layout.tio_create_group_item_section);
        addItemType(MultiType.CONTACT.value, R.layout.tio_create_group_item);

        checkedIds.clear();
        checkedNames.clear();
        setOnItemClickListener(this);
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    protected void convert(BaseViewHolder helper, MultiModel item) {
        switch (item.type) {
            case CONTACT:
                convertContact(helper, item.contact, item.isHideDivider);
                break;
            case SECTION:
                convertSection(helper, item.section);
                break;
        }
    }

    private void convertSection(BaseViewHolder helper, SectionModel section) {
        helper.setText(R.id.tv_title, StringUtil.nonNull(section.title));
    }

    private void convertContact(BaseViewHolder helper, MailListResp.Friend item, boolean isHideDivider) {
        TioImageView hiv_avatar = helper.getView(R.id.hiv_avatar);
        TextView tv_name = helper.getView(R.id.tv_name);
        TextView tv_subtitle = helper.getView(R.id.tv_subtitle);
        CheckBox checkBox = helper.getView(R.id.checkBox);
        View v_divider = helper.getView(R.id.v_divider);

        // 头像
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
        // title
        tv_name.setText(KeywordUtil.matcherSearchTitle(
                Color.parseColor("#FF4C94E8"),
                !TextUtils.isEmpty(item.remarkname) ? item.remarkname : StringUtil.nonNull(item.nick),
                keyWord));
        tv_name.setTag(String.format(Locale.getDefault(), "%s", "lkmjnjjj"));
        tv_name.setVisibility(TextUtils.isEmpty(tv_name.getText()) ? View.GONE : View.VISIBLE);
        // 昵称
        tv_subtitle.setText(KeywordUtil.matcherSearchTitle(
                Color.parseColor("#FF4C94E8"),
                TextUtils.isEmpty(item.remarkname) ? "" : ("昵称: " + item.nick),
                keyWord));
        tv_subtitle.setVisibility(TextUtils.isEmpty(tv_subtitle.getText()) ? View.GONE : View.VISIBLE);
        // 单选
        checkBox.setClickable(false);
        checkBox.setChecked(checkedIds.contains(String.valueOf(item.uid)));
        // 下划线显隐
        v_divider.setVisibility(isHideDivider ? View.GONE : View.VISIBLE);
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
        if (multiModel.type == MultiType.CONTACT) {
            MailListResp.Friend friend = multiModel.contact;

            String uid = String.valueOf(friend.uid);
            if (!checkedIds.remove(uid)) {
                checkedIds.add(uid);
            }
            if (!checkedNames.remove(friend.nick)) {
                checkedNames.add(friend.nick);
            }
            notifyItemChanged(position);
            onCheckedItemChange(checkedIds);
        }
    }

    protected void onCheckedItemChange(LinkedList<String> checkedIds) {

    }

    public LinkedList<String> getCheckedIds() {
        return checkedIds;
    }

    public LinkedList<String> getCheckedNames() {
        return checkedNames;
    }


}
