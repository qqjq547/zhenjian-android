package com.tiocloud.chat.feature.home.friend.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.chat.feature.home.friend.adapter.BaseContactAdapter;
import com.tiocloud.chat.feature.home.friend.adapter.model.IContact;
import com.tiocloud.chat.feature.home.friend.adapter.model.item.ContactItem;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020-01-16
 * desc : 联系人 ViewHolder
 */
public class ContactHolder extends BaseViewHolder<ContactItem> {

    @Override
    public int getLayoutId() {
        return R.layout.tio_contacts_item;
    }

    @Override
    public void convert(BaseContactAdapter adapter, int position, ContactItem item) {
        IContact contact = item.getContact();
        boolean lastPosition = item.isLastPosition();

        TextView name = findViewById(R.id.contacts_name);
        name.setText(StringUtil.nonNull(contact.getName()));

        TioImageView avatar = findViewById(R.id.contact_avatar);
        avatar.load_tioAvatar(contact.getAvatar());
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(contact.getAvatar()))
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
//                            HelperGlide.loadHead(getContext(),imagebyte2, avatar);
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
        View v_divider = findViewById(R.id.v_divider);
        v_divider.setVisibility(lastPosition ? View.INVISIBLE : View.VISIBLE);

    }
}
