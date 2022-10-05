package com.tiocloud.chat.widget.popupwindow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.tiocloud.chat.widget.dialog.tio.DeleteSessionDialog;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.MsgFreeFlagReq;
import com.watayouxiang.httpclient.model.request.OperReq;
import com.watayouxiang.httpclient.model.response.ChatListResp;

/**
 * author : TaoWang
 * date : 2020/3/4
 * desc :
 */
public class HomeOpWindow extends BaseHomeWindow {
    private TextView tv_delete;
    private TextView tv_topOper;
    private TextView tv_complaint;
    private TextView tv_DND;

    public HomeOpWindow(View anchor) {
        super(anchor);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.tio_home_oper_window;
    }

    @Override
    protected void initViews() {
        // 删除会话
        tv_delete = findViewById(R.id.tv_delete);
        // 置顶、取消置顶
        tv_topOper = findViewById(R.id.tv_topOper);
        // 投诉
        tv_complaint = findViewById(R.id.tv_complaint);
        // 消息免打扰
        tv_DND = findViewById(R.id.tv_DND);
    }

    public void show(@Nullable ChatListResp.List item) {
        if (item == null) return;
        // 聊天置顶
        tv_topOper.setText(item.topflag == 1 ? "取消置顶" : "置顶聊天");
        tv_topOper.setOnClickListener(v -> postTopChatOper(item));
        // 删除聊天
        tv_delete.setOnClickListener(view -> showDeleteSessionDialog(item, view));
        // 投诉
        // 会话列表-自己的 隐藏“投诉”
        if (String.valueOf(item.uid).equals(item.bizid)) {
            tv_complaint.setVisibility(View.GONE);
        } else {
            tv_complaint.setVisibility(View.VISIBLE);
            tv_complaint.setOnClickListener(v -> showReportDialog(item.id));
        }
        // 消息免打扰
        tv_DND.setText(item.msgfreeflag == 1 ? "取消免打扰" : "消息免打扰");
        tv_DND.setOnClickListener(v -> reqMsgDND(item));
        this.show();
    }

    // ====================================================================================
    // 消息免打扰
    // ====================================================================================

    private void reqMsgDND(ChatListResp.List item) {
        MsgFreeFlagReq msgFreeFlagReq = null;
        boolean openDND = item.msgfreeflag == 1;
        if (item.chatmode == 1) {
            // p2p chat
            msgFreeFlagReq = MsgFreeFlagReq.getInstance_P2P(item.bizid, openDND ? "2" : "1");
        } else if (item.chatmode == 2) {
            // group chat
            msgFreeFlagReq = MsgFreeFlagReq.getInstance_Group(item.bizid, openDND ? "2" : "1");
        }
        if (msgFreeFlagReq != null) {
            msgFreeFlagReq.setCancelTag(this).post(new TioSuccessCallback<Object>() {
                @Override
                public void onTioSuccess(Object o) {
                }
            });
        }
        dismiss();
    }

    // ====================================================================================
    // 投诉
    // ====================================================================================

    private void showReportDialog(String chatlinkid) {
        dismiss();
        new EasyOperDialog.Builder("确定投诉该会话吗？")
                .setPositiveBtnTxt("确定")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        requestReport(chatlinkid);
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getActivity());
    }

    // 投诉请求
    private void requestReport(String chatlinkid) {
        OperReq complaint = OperReq.complaint(chatlinkid);
        complaint.setCancelTag(this);
        complaint.get(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort("投诉成功，等待后台审核");
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // ====================================================================================
    // 删除会话
    // ====================================================================================

    // 显示删除会话的确认弹窗
    private void showDeleteSessionDialog(ChatListResp.List list, View view) {
        dismiss();
        new DeleteSessionDialog((view1, dialog, isCheck) -> postDeleteSessionReq(list, dialog, isCheck)).show_unCancel(view.getContext());
    }

    // 删除会话请求
    private void postDeleteSessionReq(ChatListResp.List list, DeleteSessionDialog dialog, boolean isCheck) {
        OperReq operReq = OperReq.deleteSession(list.id, isCheck);
        operReq.setCancelTag(this);
        operReq.post(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                dialog.dismiss();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    // ====================================================================================
    // 置顶会话
    // ====================================================================================

    // 置顶会话请求
    private void postTopChatOper(ChatListResp.List list) {
        OperReq operReq;
        if (list.topflag != 1) {
            operReq = OperReq.topChat(list.id, true);
        } else {
            operReq = OperReq.topChat(list.id, false);
        }
        operReq.setCancelTag(this);
        operReq.post(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {

            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dismiss();
            }
        });
    }
}
