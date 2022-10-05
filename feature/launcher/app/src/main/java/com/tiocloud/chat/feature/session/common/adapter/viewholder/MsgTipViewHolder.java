package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.SPStaticUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioNotFriendMsg;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.mvp.addfriend.AddFriendContract;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.AddFriendResp;
import com.watayouxiang.httpclient.model.response.FriendApplyResp;
import com.watayouxiang.imclient.model.body.wx.WxFriendErrorNtf;

/**
 * author : TaoWang
 * date : 2019-12-30
 * desc : Tip类型消息
 */
public class MsgTipViewHolder extends MsgBaseViewHolder {
    private TextView notificationTextView;

    public MsgTipViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.message_item_notification;
    }

    @Override
    protected void inflateContent() {
        notificationTextView = findViewById(R.id.message_item_notification_label);
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        TioMsg message = getMessage();

        if (message instanceof TioNotFriendMsg) {
            TioNotFriendMsg tioP2PErrorMsg = (TioNotFriendMsg) message;
            handleTioP2PErrorMsg(tioP2PErrorMsg);
        } else {
            int showmsg= SPStaticUtils.getInt("ChatNtf");
//
            if(showmsg==2){
                if(message.getContent().contains("撤回")){
                    ViewGroup.LayoutParams layoutParams2 = message_item_name_layout.getLayoutParams();
                    layoutParams2.height = (int)  ViewGroup.LayoutParams.WRAP_CONTENT;
                    message_item_name_layout.setVisibility(View.GONE);
                    ll_contentLong.setVisibility(View.GONE);
                    ViewGroup.LayoutParams layoutParams = ll_contentLong.getLayoutParams();
                    layoutParams.height = (int) 0;
                }else {
                    ViewGroup.LayoutParams layoutParams2 = message_item_name_layout.getLayoutParams();
                    layoutParams2.height = (int)  ViewGroup.LayoutParams.WRAP_CONTENT;
                    message_item_name_layout.setVisibility(View.VISIBLE);
                    ll_contentLong.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams layoutParams = ll_contentLong.getLayoutParams();
                    layoutParams.height = (int)  ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }else {
                ViewGroup.LayoutParams layoutParams2 = message_item_name_layout.getLayoutParams();
                layoutParams2.height = (int)  ViewGroup.LayoutParams.WRAP_CONTENT;
                message_item_name_layout.setVisibility(View.VISIBLE);
                ll_contentLong.setVisibility(View.VISIBLE);

                ViewGroup.LayoutParams layoutParams = ll_contentLong.getLayoutParams();
                layoutParams.height = (int)  ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            handleTioMsg(message);
//            Log.d("=====message===","=showmsg=="+showmsg+"===="+message.getContent());

//            Log.d("=====message===","=showmsg=="+message.getShowmsg()+"==Sendbysys=="+message.getSendbysys()+message.getContent());
        }
    }

    private void handleTioP2PErrorMsg(TioNotFriendMsg msg) {
        WxFriendErrorNtf ntf = msg.getWxFriendErrorNtf();

        if (ntf.getCode() == WxFriendErrorNtf.Code.NO_LINK) {
            SpanUtils.with(notificationTextView)
                    .append("你还不是他(她)好友 ")
                    .setForegroundColor(Color.parseColor("#FF909090"))
                    .append("发送好友验证")
                    .setClickSpan(Color.parseColor("#4C94E8"), true, v -> showAddFriendDialog(msg.getToUid()))
                    .create();
        } else {
            SpanUtils.with(notificationTextView)
                    .append(ntf.msg)
                    .setForegroundColor(Color.parseColor("#FF909090"))
                    .create();
        }
    }

    private void showAddFriendDialog(String _toUid) {
        int toUid = -1;
        try {
            toUid = Integer.parseInt(_toUid);
        } catch (Exception ignored) {
        }
        if (toUid == -1) {
            TioToast.showShort("好友id转换失败");
            return;
        }
        getAdapter().getAddFriendPresenter().uncheckStart(toUid, new AddFriendContract.Presenter.AddFriendProxy() {
            @Override
            public void onAddFriendResp(AddFriendResp data) {
                TioToast.showShort("好友添加成功");
            }

            @Override
            public void onFriendApplyResp(FriendApplyResp data) {
                super.onFriendApplyResp(data);
                TioToast.showShort("好友申请成功");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        }, getActivity());
    }

    private void handleTioMsg(TioMsg msg) {
        String content = msg.getContent();
        if (TextUtils.isEmpty(content)) {
            content = getContext().getResources().getString(R.string.unknown_notification);
        }

        SpanUtils.with(notificationTextView)
                .append(content)
                .setForegroundColor(Color.parseColor("#FF909090"))
                .create();
    }

    @Override
    protected View.OnLongClickListener onContentLongClick() {
        return null;
    }
}
