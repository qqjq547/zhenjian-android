package com.tiocloud.chat.widget.dialog.tio;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.share.msg.ShareMsgActivity;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.TioDialog;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.EmojiCollectReq;
import com.watayouxiang.httpclient.model.request.MsgOperReq;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgCard;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/25
 *     desc   :
 * </pre>
 */
public class SessionMsgDialog extends TioDialog {

    private final Activity activity;
    private String chatmode;//1：私聊；2：群聊
    private String msgId;

    public SessionMsgDialog(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected int getDialogContentId() {
        return R.layout.tio_session_msg_dialog;
    }

    @Override
    protected void initDialogContentView() {
        super.initDialogContentView();
        initCopyView();
        initWithdrawView();
        initDeleteView();
        initForwardView();
        initComplaintView();
    }

    // ====================================================================================
    // 举报
    // ====================================================================================

    private String complaint_mids;
    private String complaint_chatLinkId;
    private boolean complaint_enableComplaint;

    public SessionMsgDialog setComplaintData(TioMsg msg) {
        this.complaint_mids = msg.getId();
        this.complaint_chatLinkId = msg.getChatLinkId();
        this.complaint_enableComplaint = enableComplaint(msg.getMsgType());
        return this;
    }

    private boolean enableComplaint(TioMsgType msgType) {
        if (msgType == null) return false;
        switch (msgType) {
            case call:
            case redPaper:
                return false;
            default:
                return true;
        }
    }

    private void initComplaintView() {
        TextView tv_complaint = findViewById(R.id.tv_complaint);
        if (complaint_mids != null && complaint_chatLinkId != null && complaint_enableComplaint) {
            tv_complaint.setVisibility(View.VISIBLE);
            tv_complaint.setOnClickListener(v -> reqComplaint());
        } else {
            tv_complaint.setVisibility(View.GONE);
        }
    }

    private void reqComplaint() {
        if (complaint_mids != null && complaint_chatLinkId != null) {
            MsgOperReq complaint = MsgOperReq.complaint(complaint_chatLinkId, complaint_mids);
            complaint.setCancelTag(this);
            complaint.get(new TioCallback<String>() {
                @Override
                public void onTioSuccess(String s) {
                    TioToast.showShort("举报成功，等待后台审核");
                    dismiss();
                }

                @Override
                public void onTioError(String msg) {
                    TioToast.showShort(msg);
                }
            });
        }
    }

    // ====================================================================================
    // 消息转发
    // ====================================================================================

    private String forward_chatlinkid;
    private String forward_mids;

    public SessionMsgDialog setForwardData(TioMsg msg) {
        boolean enableForward = enableForward(msg);
        if (enableForward) {
            this.forward_chatlinkid = msg.getChatLinkId();
            this.forward_mids = msg.getId();
        }
        return this;
    }

    /**
     * @return 是否支持消息转发
     */
    private boolean enableForward(TioMsg msg) {
        TioMsgType msgType = msg.getMsgType();
        if (msgType != null) {
            switch (msgType) {
                case call:
                case tip:
                case audio:
                case unknown:
                case redPaper:
                    return false;
                case card:
                    // 群名片不支持转发
                    InnerMsgCard card = (InnerMsgCard) msg.getContentObj();
                    return card != null && card.cardtype == 1;
                default:
                    return true;
            }
        }
        return false;
    }

    private void initForwardView() {
        TextView tv_forward = findViewById(R.id.tv_forward);
        if (forward_chatlinkid != null && forward_mids != null) {
            tv_forward.setVisibility(View.VISIBLE);
            tv_forward.setOnClickListener(view -> {
                ShareMsgActivity.start(activity, forward_chatlinkid, forward_mids);
                dismiss();
            });
        } else {
            tv_forward.setVisibility(View.GONE);
        }
    }

    // ====================================================================================
    // 删除消息
    // ====================================================================================

    private String delete_chatlinkid;
    private String delete_mids;

    public SessionMsgDialog setDeleteData(TioMsg msg) {
        this.delete_chatlinkid = msg.getChatLinkId();
        this.delete_mids = msg.getId();
        return this;
    }

    private void initDeleteView() {
        TextView tv_delete = findViewById(R.id.tv_delete);
        if (delete_chatlinkid != null && delete_mids != null) {
            tv_delete.setVisibility(View.VISIBLE);
            tv_delete.setOnClickListener(view -> postDeleteReq());
        } else {
            tv_delete.setVisibility(View.GONE);
        }
    }

    private void postDeleteReq() {
        if (delete_chatlinkid != null && delete_mids != null) {
            MsgOperReq msgOperReq = new MsgOperReq(delete_chatlinkid, delete_mids, "1");
            msgOperReq.setCancelTag(this);
            msgOperReq.post(new TioCallback<String>() {
                @Override
                public void onTioSuccess(String s) {
                    dismiss();
                }

                @Override
                public void onTioError(String msg) {
                    TioToast.showShort(msg);
                }
            });
        }
    }

    // ====================================================================================
    // 撤回消息
    // ====================================================================================

    private String withdraw_chatlinkid;
    private String withdraw_mids;
    private boolean enableWithdraw_myMsg = false;
    private boolean enableWithdraw_otherMsg = false;

    private boolean enableMyMsgWithdraw(TioMsg msg) {
        TioMsgType msgType = msg.getMsgType();
        boolean isSend = msg.isSendMsg();

        return (msgType != TioMsgType.redPaper) && (isSend);
    }

    private boolean enableOtherMsgWithdraw(TioMsg msg) {
        TioMsgType msgType = msg.getMsgType();
        String chatLinkId = msg.getChatLinkId();

        boolean isGroupMgr = false;
        ChatListTable chatListTable = ChatListTableCrud.query_chatLinkId(chatLinkId);
        if (chatListTable != null) {
            int bizrole = chatListTable.getBizrole();
            isGroupMgr = bizrole == 1 || bizrole == 3;
        } else {
            TioLogger.e(StringUtils.format("查询不到chatLinkId为%s的会话", chatLinkId));
        }

        return (msgType != TioMsgType.redPaper) && (isGroupMgr);
    }

    public SessionMsgDialog setWithdrawData(TioMsg msg) {
        enableWithdraw_myMsg = enableMyMsgWithdraw(msg);
        enableWithdraw_otherMsg = enableOtherMsgWithdraw(msg);

        if (enableWithdraw_myMsg || enableWithdraw_otherMsg) {
            this.withdraw_chatlinkid = msg.getChatLinkId();
            this.withdraw_mids = msg.getId();
        }

        return this;
    }

    private void initWithdrawView() {
        TextView tv_withdraw = findViewById(R.id.tv_withdraw);

        if (withdraw_chatlinkid != null && withdraw_mids != null) {
            tv_withdraw.setVisibility(View.VISIBLE);
            tv_withdraw.setOnClickListener(this::doWithdrawAction);
        } else {
            tv_withdraw.setVisibility(View.GONE);
        }
    }

    private void doWithdrawAction(View view) {
        if (enableWithdraw_myMsg) {
            // 撤回自己的消息
            // 直接撤回
            postWithdrawReq(this::dismiss);
        } else if (enableWithdraw_otherMsg) {
            dismiss();

            // 撤回别人的消息（群主/管理员）
            // 需要二次确认
            new EasyOperDialog.Builder("确定撤回该群聊消息？")
                    .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                        @Override
                        public void onClickPositive(View view, EasyOperDialog dialog) {
                            postWithdrawReq(dialog::dismiss);
                        }

                        @Override
                        public void onClickNegative(View view, EasyOperDialog dialog) {
                            dialog.dismiss();
                        }
                    })
                    .build()
                    .show_unCancel(view.getContext());
        }
    }

    private void postWithdrawReq(OnWithdrawOkCallback callback) {
        if (withdraw_chatlinkid != null && withdraw_mids != null) {
            MsgOperReq msgOperReq = new MsgOperReq(withdraw_chatlinkid, withdraw_mids, "9");
            msgOperReq.setCancelTag(this);
            msgOperReq.post(new TioCallback<String>() {
                @Override
                public void onTioSuccess(String s) {
                    if (callback != null) callback.onSuccess();
                }

                @Override
                public void onTioError(String msg) {
                    TioToast.showShort(msg);
                }
            });
        }
    }

    private interface OnWithdrawOkCallback {
        void onSuccess();
    }

    // ====================================================================================
    // 复制消息
    // ====================================================================================

    private String copy_text;

    public SessionMsgDialog setCopyData(String text) {
        this.copy_text = text;
        return this;
    }

    private void initCopyView() {
        TextView tv_copy = findViewById(R.id.tv_copy);
        TextView tv_collect=findViewById(R.id.tv_collect);

        if (copy_text != null) {
            tv_collect.setVisibility(View.GONE);
            tv_copy.setVisibility(View.VISIBLE);
            tv_copy.setOnClickListener(view -> {
                StringUtil.copyText(copy_text);
                dismiss();
            });
        } else {
            tv_copy.setVisibility(View.GONE);
            tv_collect.setVisibility(View.VISIBLE);
        }
        tv_collect.setOnClickListener(view ->{
//            Log.d("====收藏==",chatmode+"===");
            EmojiCollectReq complaint = EmojiCollectReq.complaint(chatmode, complaint_mids);
            complaint.setCancelTag(this);
            complaint.post(new TioCallback<String>() {
                @Override
                public void onTioSuccess(String s) {
                    TioToast.showShort("收藏成功");
                    Log.d("====收藏==",chatmode+"==="+s);

                }

                @Override
                public void onTioError(String msg) {
                    TioToast.showShort(msg);
                }
            });

        });
    }
    public SessionMsgDialog setComplaintSC(String chatmode2) {
        chatmode=chatmode2;

        return this;
    }
}
