package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.session.feature.join_group_apply_info.JoinGroupApplyInfoActivity;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgApply;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/2/8
 *     desc   : 入群申请
 * </pre>
 */
public class MsgJoinGroupApplyViewHolder extends MsgBaseViewHolder {

    private TextView tv_txt;
    private InnerMsgApply apply;

    public MsgJoinGroupApplyViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.tio_msg_item_join_group_apply;
    }

    @Override
    protected void inflateContent() {
        tv_txt = findViewById(R.id.tv_txt);
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        Object obj = getMessage().getContentObj();
        // 容错处理
        if (!(obj instanceof InnerMsgApply)) {
            tv_txt.setText("");
            return;
        }
        // 初始化
        apply = (InnerMsgApply) obj;
        initUI();
    }

    private void initUI() {
        String name = getMessage().getName();
        String mid = getMessage().getId();

        // 富文本实现
        SpanUtils spanUtils = SpanUtils.with(tv_txt)
                .append(String.format(Locale.getDefault(), "%s想邀请好友加入群聊, ", name))
                .setForegroundColor(Color.parseColor("#A8A8A8"));
        if (apply.status == 2) {
            spanUtils.append("去确认")
                    .setClickSpan(Color.parseColor("#4C94FF"), false, new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Activity activity = getActivity();
                            if (activity != null) {
                                JoinGroupApplyInfoActivity.start(activity, apply.id, mid);
                            }
                        }
                    });
        } else if (apply.status == 1) {
            spanUtils.append("已确认")
                    .setClickSpan(Color.parseColor("#4C94FF"), false, new OnSingleClickListener() {
                        @Override
                        public void onSingleClick(View view) {
                            Activity activity = getActivity();
                            if (activity != null) {
                                JoinGroupApplyInfoActivity.start(activity, apply.id, mid);
                            }
                        }
                    });
        }
        spanUtils.create();
    }

}
