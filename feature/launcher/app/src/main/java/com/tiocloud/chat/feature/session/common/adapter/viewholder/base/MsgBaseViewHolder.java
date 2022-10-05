package com.tiocloud.chat.feature.session.common.adapter.viewholder.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.util.TimeUtil;
import com.tiocloud.chat.widget.dialog.tio.SessionMsgDialog;
import com.watayouxiang.androidutils.recyclerview.RecyclerViewHolder;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;

/**
 * author : TaoWang
 * date : 2019-12-30
 * desc : 基本Holder
 */
public abstract class MsgBaseViewHolder extends RecyclerViewHolder<MsgAdapter, BaseViewHolder, TioMsg> {

    private static final int leftContentBgId = R.drawable.bubble_left;
    private static final int rightContentBgId = R.drawable.bubble_right;

    private View view;
    private Context context;
    public TioMsg message;

    private FrameLayout contentContainer;
    private TioImageView avatarLeft;
    private TioImageView avatarRight;
    private TextView nickLeft;
    private TextView nickRight;
    public TextView timeView;
    private TextView tv_receipt_left;
    private TextView tv_receipt_right;
    public LinearLayout ll_contentLong;
    public LinearLayout message_item_name_layout;
    @Nullable
    public Activity getActivity() {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public TioMsg getMessage() {
        return message;
    }

    // ==============================================================================
    // create
    // ==============================================================================

    public MsgBaseViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    public void convert(BaseViewHolder holder, TioMsg data, int position, boolean isScrolling) {
        view = holder.getConvertView();
        context = holder.itemView.getContext();
        message = data;

        inflate();
        refresh(holder);
    }

    private void inflate() {
        ll_contentLong=findViewById(R.id.ll_contentLong);
        message_item_name_layout=findViewById(R.id.message_item_name_layout);
        // 内容
        contentContainer = findViewById(R.id.message_item_content);
        // 头像
        avatarLeft = findViewById(R.id.message_item_portrait_left);
        avatarRight = findViewById(R.id.message_item_portrait_right);
        // 昵称
        nickLeft = findViewById(R.id.message_item_nickname_left);
        nickRight = findViewById(R.id.message_item_nickname_right);
        // 时间
        timeView = findViewById(R.id.message_item_time);
        // 消息接收状况
        tv_receipt_left = findViewById(R.id.tv_receipt_left);
        tv_receipt_right = findViewById(R.id.tv_receipt_right);

        // 避免多次填充布局
        int contentResId;
        if (contentContainer.getChildCount() == 0 && (contentResId = contentResId()) != 0) {
            View.inflate(view.getContext(), contentResId, contentContainer);
        }
        inflateContent();
    }

    private void refresh(BaseViewHolder holder) {
        setHeadImageView();
        setContent();
        setNickView();
        setTimeView();
        setDisplayReceipt();
        bindContent(holder);

    }

    // ====================================================================================
    // init
    // ====================================================================================

    private void setDisplayReceipt() {
        TextView show = message.isSendMsg() ? tv_receipt_left : tv_receipt_right;
        TextView hide = message.isSendMsg() ? tv_receipt_right : tv_receipt_left;

        hide.setVisibility(View.GONE);

        Boolean displayReceipt = message.getReadMsg();
        if (!message.isSendMsg() || displayReceipt == null) {
            // 不是发送的消息 || 无数据
            show.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.VISIBLE);
            if (displayReceipt) {
                // 已读
                show.setText("已读");
                show.setSelected(false);
            } else {
                // 未读
                show.setText("未读");
                show.setSelected(true);
            }
        }
    }

    private void setTimeView() {
        Long time = message.getTime();
        if (time != null) {
            timeView.setVisibility(View.VISIBLE);
            String timeText = TimeUtil.getShowTime(time, false);
            timeView.setText(String.valueOf(timeText));
        } else {
            timeView.setVisibility(View.GONE);
        }
    }

    private void setNickView() {
        TextView show = message.isSendMsg() ? nickRight : nickLeft;
        TextView hide = message.isSendMsg() ? nickLeft : nickRight;

        hide.setVisibility(View.GONE);

        boolean showName = message.isShowName();
        if (showName) {
            show.setVisibility(View.VISIBLE);
        } else {
            show.setVisibility(View.GONE);
        }

        String name = message.getName();
        show.setText(StringUtils.null2Length0(name));
    }

    private void setContent() {
        if (isShowContentBg()) {
            if (message.isSendMsg()) {
                contentContainer.setBackgroundResource(rightContentBgId);
                contentContainer.setPadding(SizeUtils.dp2px(10), SizeUtils.dp2px(8), SizeUtils.dp2px(15), SizeUtils.dp2px(8));
            } else {
                contentContainer.setBackgroundResource(leftContentBgId);
                contentContainer.setPadding(SizeUtils.dp2px(15), SizeUtils.dp2px(8), SizeUtils.dp2px(10), SizeUtils.dp2px(8));
            }
        }
        contentContainer.setOnClickListener(onContentClick());
        contentContainer.setOnLongClickListener(onContentLongClick());
    }

    private void setHeadImageView() {
        TioImageView show = message.isSendMsg() ? avatarRight : avatarLeft;
        TioImageView hide = message.isSendMsg() ? avatarLeft : avatarRight;

        hide.setVisibility(View.GONE);

        String avatar = message.getAvatar();
        if (avatar != null) {
            show.load_tioAvatar(avatar);
            show.setVisibility(View.VISIBLE);
            show.setOnClickListener(onAvatarClicked());
            show.setOnLongClickListener(onAvatarLongClick());
//            Glide.with(getContext())
//                    .downloadOnly()
//                    .load(getResUrl(avatar))
//                    .listener(new RequestListener<File>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                            Log.d("===下载失败===","===");
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                            byte[] bytes= FileUtils.File2byte(resource);
//                            try {
//                                byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                                HelperGlide.loadHead(getContext(),imagebyte2,show);
//
//
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            return false;
//                        }
//                    })
//                    .preload();
            ClickUtils.applyPressedBgDark(show);
            ClickUtils.applyPressedViewScale(show);
        } else {
            show.setVisibility(View.GONE);
        }
    }

    // ==============================================================================
    // avatar
    // ==============================================================================

    protected View.OnClickListener onAvatarClicked() {
        return getAdapter().onAvatarClick(getMessage());
    }

    protected View.OnLongClickListener onAvatarLongClick() {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return getAdapter().onAvatarLongClick(v, getMessage());
            }
        };
    }

    // ==============================================================================
    // content
    // ==============================================================================

    protected abstract int contentResId();

    protected abstract void inflateContent();

    protected abstract void bindContent(BaseViewHolder holder);

    protected boolean isShowContentBg() {
        return false;
    }

    protected View.OnClickListener onContentClick() {
        return null;
    }

    protected View.OnLongClickListener onContentLongClick() {
        return view -> {
            Log.d("===图片长按==","==="+"===");
            showAttachView(view, null);
            return true;
        };
    }

    public FrameLayout getContentContainer() {
        return contentContainer;
    }

    // ==============================================================================
    // 拓展
    // ==============================================================================

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id) {
        return (T) view.findViewById(id);
    }

    /**
     * 显示消息粘附视图
     *
     * @param attachView 粘附于哪个 view
     * @param copyText   复制的 text
     */
    protected void showAttachView(@NonNull View attachView, @Nullable String copyText) {
        TioMsg msg = getMessage();

        // 弹窗
        Context context = attachView.getContext();
        if (context instanceof Activity) {
            new SessionMsgDialog((Activity) context)
                    // 复制
                    .setCopyData(copyText)
                    // 消息撤回
                    .setWithdrawData(msg)
                    .setComplaintSC(TioApplication.getInstanceKit().chatmode+"")

                    // 消息删除
                    .setDeleteData(msg)
                    // 消息转发
                    .setForwardData(msg)
                    // 举报
                    .setComplaintData(msg)
                    // 显示
                    .show_canceledOnTouchOutside(attachView.getContext());
        }
    }
}
