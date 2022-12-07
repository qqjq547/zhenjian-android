package com.tiocloud.chat.feature.session.common.adapter.viewholder.viewmodel;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.tiocloud.chat.feature.session.common.adapter.viewholder.MsgRedPaperViewHolder2;
import com.tiocloud.commonwallet.TioWallet;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.request.PayGrabRedPacketReq;
import com.watayouxiang.httpclient.model.request.PayRedStatusReq;
import com.watayouxiang.httpclient.model.response.PayGrabRedPacketResp;
import com.watayouxiang.httpclient.model.response.PayRedStatusResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/24
 *     desc   :
 * </pre>
 */
public class RedPaperViewModel2 {

    // 获取红包状态
    public void getRedStatus(String rid, MsgRedPaperViewHolder2 holder, boolean showDialog) {
        Activity activity = holder.getActivity();
        if (activity == null) {
            TioToast.showShort("获取不到 activity");
            return;
        }

        PayRedStatusReq payRedStatusReq = PayRedStatusReq.getInstance(rid);
        payRedStatusReq.setCancelTag(activity);
        payRedStatusReq.get(new TioSuccessCallback<PayRedStatusResp>() {
            @Override
            public void onTioSuccess(PayRedStatusResp payRedStatusResp) {
                // 用户是否开户：1：是；2：否
                int openflag = payRedStatusResp.getOpenflag();
                // 自己抢的状态：1成功，2未抢
                String grabstatus = payRedStatusResp.getGrabstatus_newPay2payEase();
                // 红包状态：SUCCESS-已抢完; TIMEOUT-24小时超时; SEND-抢红包中
                String redstatus = payRedStatusResp.getRedstatus_newPay2payEase();

                if (openflag == 2) {
                    // 未开户，跳转开户页
//                    TioWallet.getWallet().OpenWalletActivity_start(activity);
                    TioWallet.getWallet().WalletActivity_start((FragmentActivity) activity);
                } else if (openflag == 1) {
                    // 开户
                    if ("SUCCESS".equals(grabstatus)) {
                        // 自己已抢
                        holder.onRedStatusResp(1, showDialog);
                    } else if ("INIT".equals(grabstatus)) {
                        // 自己未抢
                        if ("SUCCESS".equals(redstatus)) {
                            // 红包已抢完
                            holder.onRedStatusResp(2, showDialog);
                        } else if ("TIMEOUT".equals(redstatus)) {
                            // 24小时超时
                            holder.onRedStatusResp(3, showDialog);
                        } else if ("SEND".equals(redstatus)) {
                            // 抢红包中
                            holder.onRedStatusResp(4, showDialog);
                        }
                    }
                }
            }
        });
    }

    // 抢红包
    public void getGrabRedPacket(MsgRedPaperViewHolder2 holder, String rid) {
        String chatLinkId = holder.getAdapter().getChatLinkId();
        if (chatLinkId == null) {
            TioToast.showShort("获取不到 chatLinkId");
            return;
        }
        Activity activity = holder.getActivity();
        if (activity == null) {
            TioToast.showShort("获取不到 activity");
            return;
        }

        PayGrabRedPacketReq payGrabRedPacketReq = PayGrabRedPacketReq.getInstance(rid, chatLinkId);
        payGrabRedPacketReq.setCancelTag(activity);
        payGrabRedPacketReq.get(new TioSuccessCallback<PayGrabRedPacketResp>() {
            @Override
            public void onTioSuccess(PayGrabRedPacketResp payGrabRedPacketResp) {
                holder.onGrabRedPacketResp(payGrabRedPacketResp);
            }
        });
    }
}
