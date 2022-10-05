package com.tiocloud.chat.feature.home.chat.adapter;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ColorUtils;
import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.util.MoonUtil;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.chat.util.TimeUtil;
import com.tiocloud.chat.widget.textview.ListUnreadTextView;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.utils.HtmlUtils;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.imclient.model.MsgTemplate;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/07/13
 *     desc   : ui 处理
 * </pre>
 */
class BaseUIAdapter extends BaseDataAdapter {

    // 当前用户昵称
    private final String currNick = CurrUserTableCrud.curr_getNick();
    // 当前用户 id
    private final long currUid = TioDBPreferences.getCurrUid();

    private String getCurrNick() {
        return currNick;
    }

    public BaseUIAdapter(@NonNull RecyclerView recyclerView) {
        super(R.layout.tio_chat_list_item, recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatListResp.List item) {
        View cl_container = helper.getView(R.id.cl_container);
        TioImageView avatar = helper.getView(R.id.v_avatar);
        TextView name = helper.getView(R.id.v_name);
        TextView recentMsg = helper.getView(R.id.v_recent_msg);
        TextView time = helper.getView(R.id.tv_time);
        ListUnreadTextView unreadMsgNum = helper.getView(R.id.tv_unreadMsgNum);
        View iv_topFlag = helper.getView(R.id.iv_topFlag);

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
        // 发送时间
        initTime(time, item.sendtime);
        // 未读消息数
        unreadMsgNum.setUnread(item.notreadcount, true, item.msgfreeflag == 1);
        // 置顶标志显隐
        iv_topFlag.setVisibility(item.topflag == 1 ? View.VISIBLE : View.GONE);
        // item 的背景
        cl_container.setBackgroundColor(ColorUtils.getColor(item.topflag == 1 ? R.color.gray_f2f2f2 : R.color.transparent));
        // 最新消息内容
        initMsg(recentMsg, item);
    }

    /**
     * 发送时间
     *
     * @param tvTime   控件
     * @param sendTime 发送时间
     */
    private void initTime(TextView tvTime, String sendTime) {
        Long _sendTime = TimeUtil.dateString2Long(sendTime);
        if (_sendTime != null) {
            tvTime.setVisibility(View.VISIBLE);
            String showTime = TimeUtil.getShowTime(_sendTime, true);
            tvTime.setText(StringUtil.nonNull(showTime));
        } else {
            tvTime.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化消息显示
     */
    private void initMsg(TextView recentMsg, ChatListResp.List item) {
        // 1、重置富文本
        SpanUtils.with(recentMsg)
                .append("").setForegroundColor(Color.parseColor("#888888"))
                .create();

        // 2、构建新的富文本
        SpanUtils utils = SpanUtils.with(recentMsg);

        // ---消息免打扰
        if (item.msgfreeflag == 1 && item.notreadcount != 0) {
            // 开启消息免打扰 && 有未读消息
            // 显示 "[3条消息]"
            utils.append(String.format(Locale.getDefault(), "[%d条消息] ", item.notreadcount)).setForegroundColor(Color.parseColor("#888888"));
        }

        // ---已读/未读
        if (item.chatmode == 1 && currUid == item.lastmsguid && item.sysflag != 1) {
            // 私聊 && 最后一条消息的发送者是自己 && 非系统消息
            // 显示 "[未读]" / "[已读]"
            if (item.toreadflag == 1) {
                utils.append("[已读] ").setForegroundColor(Color.parseColor("#888888"));
            } else if (item.toreadflag == 2) {
                utils.append("[未读] ").setForegroundColor(Color.parseColor("#3A88FB"));
            }
        }

        // ---艾特
        if (item.chatmode == 2 && item.atreadflag == 2) {
            // 群聊 && 有未读的艾特消息
            // 显示 "[有人@你]"
            utils.append("[有人@你] ").setForegroundColor(Color.parseColor("#3A88FB"));
        }
        int showmsg= SPStaticUtils.getInt("ChatNtf");

        // ---消息内容
        boolean setContent;
        if (item.sysflag == 1) {
            String sysMsgTemp = MsgTemplate.getTipMsg(item.sysmsgkey, item.opernick, item.tonicks, getCurrNick());
            if (sysMsgTemp != null) {
                if(showmsg==2){
                    if(sysMsgTemp.contains("撤回")){
                        sysMsgTemp="";
                    }
                }
                // 系统消息 && 存在"会话模版"
                // 显示 "会话模版"
                utils.append(sysMsgTemp).setForegroundColor(Color.parseColor("#888888"));
                setContent = true;
            } else {
                setContent = false;
            }
        } else {
            if (item.chatmode == 2) {
                String textcontent=item.msgresume;
                // 非系统消息 && 群聊会话
                // 显示 "昵称: 消息内容"
                utils.append(String.format(Locale.getDefault(),
                        "%s: %s",
                        StringUtils.null2Length0(item.fromnick),
                        StringUtils.null2Length0(textcontent)
                )).setForegroundColor(Color.parseColor("#888888"));
                setContent = true;
            } else {
                setContent = false;
            }
        }

//        Log.d("====sysMsgTemp==","=="+item.msgresume);
        String textcontent=item.msgresume;
        if (item.sysflag == 1){
            if(showmsg==2){
                if(textcontent!=null){
                    if(textcontent.contains("撤回")){
                        textcontent="";
                    }
                }

            }
        }
        if (!setContent) {
            // 没有设置消息
            // 显示 "消息内容"
            utils.append(StringUtil.nonNull(HtmlUtils.unescapeHtml(textcontent)));
        }
        // ---构建富文本
        SpannableStringBuilder builder = utils.create();

        // 3、表情识别替换
        MoonUtil.identifyFaceExpression(recentMsg, builder, ImageSpan.ALIGN_BOTTOM, 0.35f);
    }
}

