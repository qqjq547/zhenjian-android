package com.tiocloud.session.feature.session_info_p2p.mvp;

import android.view.View;
import android.widget.CompoundButton;

import com.watayouxiang.androidutils.listener.TioErrorCallback;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.MsgFreeFlagReq;
import com.watayouxiang.httpclient.model.request.OperReq;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;

public class Presenter extends Contract.Presenter {
    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void init() {
        getView().resetUI();

        String chatLinkId = getView().getChatLinkId();
        getModel().getChatInfo(chatLinkId, new BaseModel.DataProxy<WxChatItemInfoResp>() {
            @Override
            public void onSuccess(WxChatItemInfoResp wxChatItemInfoResp) {
                super.onSuccess(wxChatItemInfoResp);
                if (wxChatItemInfoResp.data != null) {
                    getView().onChatInfoResp(wxChatItemInfoResp.data);
                }
            }
        });
    }

    @Override
    public void toggleTopChatSwitch(boolean isChecked, CompoundButton compoundButton) {
        OperReq.topChat(getView().getChatLinkId(), isChecked).setCancelTag(this).post(new TioErrorCallback<String>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void toggleDNDSwitch(String uid, boolean isChecked, CompoundButton compoundButton) {
        MsgFreeFlagReq.getInstance_P2P(uid, isChecked ? "1" : "2").setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void showReportDialog() {
        new EasyOperDialog.Builder("确定举报该用户吗？")
                .setPositiveBtnTxt("确定")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqReport(getView().getChatLinkId());
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getActivity());
    }

    @Override
    public void showClearChatRecordDialog() {
        new EasyOperDialog.Builder("确定要清除你与该好友的聊天记录吗?")
                .setPositiveBtnTxt("确定")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, final EasyOperDialog dialog) {
                        reqClearChatRecord(getView().getChatLinkId());
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getActivity());
    }

    private void reqClearChatRecord(String chatLinkId) {
        OperReq.deleteChatRecord(chatLinkId).setCancelTag(this).post(new TioSuccessCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort(s);
            }
        });
    }

    private void reqReport(String chatLinkId) {
        OperReq complaint = OperReq.complaint(chatLinkId);
        complaint.setCancelTag(this);
        complaint.get(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort("举报用户成功，等待后台审核");
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }
}
