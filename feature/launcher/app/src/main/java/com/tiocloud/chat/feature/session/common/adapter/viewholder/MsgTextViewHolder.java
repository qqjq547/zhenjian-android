package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import static com.tiocloud.chat.util.AESEncrypt.getFileName;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.LinkMovementClickMethod;
import com.tiocloud.chat.baseNewVersion.NoUnderlineSpan;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.feature.session.common.model.TextContent;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.FileUtils;
import com.tiocloud.chat.util.MoonUtil;
import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * author : TaoWang
 * date : 2019-12-30
 * desc : 文本类型消息
 */
public class MsgTextViewHolder extends MsgBaseViewHolder {
    private TextView bodyTextView;

    public MsgTextViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.message_item_text;
    }

    @Override
    protected void inflateContent() {
        bodyTextView = findViewById(R.id.tv_message);
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        String content = getMessage().getContent();
        content=TextContent.fromJson(content);
        Log.d("hjq","bindContent="+content);
        if (content == null) content = "";
//        Log.d("===消息内容==",content+"====="+ getMessage().getName()+"=="+ getMessage().getAitName());
//        utils.append("[有人@你] ").setForegroundColor(Color.parseColor("#3A88FB"));
//        SpannableStringBuilder builder = utils.create();

//        if(content.contains("@")){
//
//        }
        // 表情识别
        MoonUtil.identifyFaceExpression(bodyTextView, content, ImageSpan.ALIGN_BOTTOM);
        bodyTextView.setOnClickListener(onContentClick());
        bodyTextView.setOnLongClickListener(onContentLongClick());
//        removeHyperLinkUnderline(bodyTextView);
        bodyTextView.setMovementMethod(new LinkMovementClickMethod());

    }

    @Override
    protected View.OnLongClickListener onContentLongClick() {
        return view -> {
            Log.d("===文本长按==","==");
            showAttachView(view, getCopyText());
            return true;
        };
    }
    //去掉下划线
    private void removeHyperLinkUnderline(TextView tv) {
        CharSequence text = tv.getText();
        if(text instanceof Spannable){
            Spannable spannable = (Spannable) tv.getText();
            NoUnderlineSpan noUnderlineSpan = new NoUnderlineSpan();
            spannable.setSpan(noUnderlineSpan,0,text.length(), Spanned.SPAN_MARK_MARK);
        }
    }


    @Override
    protected View.OnClickListener onContentClick() {
        return v -> {
            Log.d("===文本dianji==","==");

        };
    }

    private String getCopyText() {
        if (bodyTextView != null) {
            return bodyTextView.getText().toString();
        }
        return null;
    }

    @Override
    protected boolean isShowContentBg() {
        return true;
    }
}
