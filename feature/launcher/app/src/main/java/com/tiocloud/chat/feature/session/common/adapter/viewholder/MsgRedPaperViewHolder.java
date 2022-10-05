package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.viewmodel.RedPaperViewModel;
import com.tiocloud.commonwallet.TioWallet;
import com.tiocloud.commonwallet.iDialogType;
import com.tiocloud.commonwallet.iRedPaperDialog;
import com.tiocloud.commonwallet.iRedPaperVo;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.response.PayGrabRedPacketResp;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgRed;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/10
 *     desc   : 红包 - 易支付
 * </pre>
 */
public class MsgRedPaperViewHolder extends MsgBaseViewHolder {

    private ConstraintLayout cl_container;
    private TextView tv_title;
    private TextView tv_subtitle;

    @Nullable
    private InnerMsgRed msgRed;
    private final RedPaperViewModel viewModel = new RedPaperViewModel();
    private iRedPaperDialog redPaperDialog;

    public MsgRedPaperViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return TioWallet.getWallet().getLayoutId_redPaperMsg();
    }

    @Override
    protected void inflateContent() {
        cl_container = findViewById(R.id.cl_container);
        tv_title = findViewById(R.id.tv_title);
        tv_subtitle = findViewById(R.id.tv_subtitle);
    }

    @Override
    protected void bindContent(BaseViewHolder holder) {
        msgRed = (InnerMsgRed) getMessage().getContentObj();
        if (msgRed == null) return;

        initUI();
    }

    private void initUI() {
        tv_title.setText(StringUtils.null2Length0(msgRed.text));
        String status = msgRed.status;
        if ("SUCCESS".equals(status)) {
            // 已抢完
            setRedStatus(2);
        } else if ("TIMEOUT".equals(status)) {
            // 24小时超时
            setRedStatus(3);
        } else if ("SEND".equals(status)) {
            // 抢红包中
            setRedStatus(4);
        } else {
            // 其他状态则认为是 "可抢红包"
            setRedStatus(4);
        }
    }

    private void setRedStatus(int status) {
        if (status == 1) {
            // 已领取
            cl_container.setSelected(true);
            tv_subtitle.setVisibility(View.VISIBLE);
            tv_subtitle.setText("已领取");
        } else if (status == 2) {
            // 已抢完
            cl_container.setSelected(true);
            tv_subtitle.setVisibility(View.VISIBLE);
            tv_subtitle.setText("已领取完");
        } else if (status == 3) {
            // 24小时超时
            cl_container.setSelected(true);
            tv_subtitle.setVisibility(View.VISIBLE);
            tv_subtitle.setText("已过期");
        } else if (status == 4) {
            // 抢红包中
            cl_container.setSelected(false);
            tv_subtitle.setVisibility(View.GONE);
        }
    }

    @Override
    protected View.OnClickListener onContentClick() {
        return v -> {
            if (!ClickUtils.isViewSingleClick(v)) return;
            if (msgRed == null) return;

            viewModel.getRedStatus(msgRed.serialnumber, this, true);
        };
    }

    /**
     * 获取红包状态响应
     *
     * @param status     1 已领取，2 已抢完，3 24小时超时，4 抢红包中
     * @param showDialog 是否显示弹窗
     */
    public void onRedStatusResp(int status, boolean showDialog) {
        if (status == 1) {
            // 已领取
            setRedStatus(1);
        } else if (status == 2) {
            // 已抢完
            setRedStatus(2);
        } else if (status == 3) {
            // 24小时超时
            setRedStatus(3);
        } else if (status == 4) {
            // 抢红包中
            setRedStatus(4);
        }

        if (!showDialog) return;
        if (msgRed == null) return;

        String avatar = getMessage().getAvatar();
        avatar = HttpCache.getResUrl(avatar);
        String name = getMessage().getName();
        boolean isSendMsg = getMessage().isSendMsg();
        boolean p2PMsg = getMessage().isP2PMsg();

        iRedPaperVo redPaperVo = new iRedPaperVo(avatar, name, msgRed.text, isSendMsg, msgRed.serialnumber);

        if (status == 1) {
            // 已领取
            // 查看红包详情
            TioWallet.getWallet().PaperDetailActivity_start(getContext(), redPaperVo.serialNumber);
        } else if (status == 2) {
            // 已抢完
            // 显示已经抢完的弹窗
            TioWallet.getWallet().OpenRedPaperDialog_show(getContext(), iDialogType.NONE, redPaperVo);
        } else if (status == 3) {
            // 24小时超时
            if (isSendMsg) {
                // 发送人
                // 查看红包详情
                TioWallet.getWallet().PaperDetailActivity_start(getContext(), redPaperVo.serialNumber);
            } else {
                // 接收人
                // 显示超时的弹窗
                TioWallet.getWallet().OpenRedPaperDialog_show(getContext(), iDialogType.OVERDUE, redPaperVo);
            }
        } else if (status == 4) {
            // 抢红包中
            if (isSendMsg && p2PMsg) {
                // 发送人 && 私聊
                // 查看红包详情
                TioWallet.getWallet().PaperDetailActivity_start(getContext(), redPaperVo.serialNumber);
            } else {
                // 显示待抢弹窗
                redPaperDialog = TioWallet.getWallet().RedPaperDialog_new(getContext(), redPaperVo);
                redPaperDialog.setOnRedPaperListener(() -> viewModel.getGrabRedPacket(MsgRedPaperViewHolder.this, redPaperVo.serialNumber));
                redPaperDialog.show();
            }
        }
    }

    /**
     * 抢红包响应
     */
    public void onGrabRedPacketResp(PayGrabRedPacketResp resp) {
        // 关闭弹窗
        if (redPaperDialog != null) {
            redPaperDialog.dismiss();
        }
        if (msgRed == null) return;

        // 跳转红包详情页
        TioWallet.getWallet().PaperDetailActivity_start(getContext(), msgRed.serialnumber);
        // 查询红包状态
        viewModel.getRedStatus(msgRed.serialnumber, this, false);
    }
}
