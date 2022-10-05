package com.tiocloud.newpay.feature.paperdetail;

import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.tiocloud.newpay.feature.paperdetail.adapter.FooterItem;
import com.tiocloud.newpay.feature.paperdetail.adapter.ListItem;
import com.tiocloud.newpay.feature.paperdetail.adapter.ListModel;
import com.tiocloud.newpay.feature.paperdetail.adapter.ReceiveInfoItem;
import com.tiocloud.newpay.feature.paperdetail.adapter.SendInfoItem;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayRedInfoReq;
import com.watayouxiang.httpclient.model.response.PayRedInfoResp;
import com.watayouxiang.httpclient.model.vo.RedPacketStatus;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/13
 *     desc   :
 * </pre>
 */
public class PaperDetailViewModel extends ViewModel {

    /**
     * 刷新页面
     */
    public void refresh(PaperDetailActivity activity) {
        getRedInfo(activity);
    }

    /**
     * 获取红包信息
     */
    private void getRedInfo(PaperDetailActivity activity) {
        PayRedInfoReq payRedInfoReq = PayRedInfoReq.getInstance(activity.getRid());
        payRedInfoReq.setCancelTag(activity);
        payRedInfoReq.get(new TioCallback<PayRedInfoResp>() {
            @Override
            public void onTioSuccess(PayRedInfoResp payRedInfoResp) {
                PayRedInfoResp.InfoBean info = payRedInfoResp.getInfo();
                activity.onRedInfoResp(info);
                onRedInfoResp(payRedInfoResp, activity);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    /**
     * 红包信息响应
     */
    private void onRedInfoResp(PayRedInfoResp resp, PaperDetailActivity activity) {
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<List<ListModel>>() {
            @Override
            public List<ListModel> doInBackground() throws Throwable {
                return getListModelsInternal(resp);
            }

            @Override
            public void onSuccess(List<ListModel> result) {
                activity.setRefreshData(result);
            }
        });
    }

    private List<ListModel> getListModelsInternal(PayRedInfoResp resp) {
        //****** 获取数据
        PayRedInfoResp.InfoBean info = resp.getInfo();
        List<PayRedInfoResp.GrablistBean> grabList = resp.getGrablist();
        // 当前用户uid
        long currUid = TioDBPreferences.getCurrUid();
        // 是否为红包发送人
        boolean isRedSender = info.getUid() == currUid;
        // 红包被领取个数
        int receivedCount = grabList.size();
        // SUCCESS-已抢完
        // TIMEOUT-24小时超时
        // SEND-抢红包中
        String status = info.getStatus_newPay2payEase();
        // 是否为拼手气红包
        boolean isPinRed = info.getMode() == 2;
        // 群会话
        boolean isGroupSession = info.getChatmode() == 2;
        // 当前抢到红包的人
        PayRedInfoResp.GrablistBean currGetRedUser = null;
        // 已抢红包金额
        long receivedAmount = 0;
        // 最佳手气人
        PayRedInfoResp.GrablistBean bestLuckyUser = null;
        for (int i = 0; i < receivedCount; i++) {
            PayRedInfoResp.GrablistBean bean = grabList.get(i);
            // 当前抢到红包的人
            if (bean.getUid() == currUid) {
                currGetRedUser = bean;
            }
            // 获取最佳手气人
            if (isPinRed && RedPacketStatus.SUCCESS.equals(status)) {
                if (bestLuckyUser == null) {
                    bestLuckyUser = bean;
                } else {
                    int lucky_amount = bestLuckyUser.cny;
                    int curr_amount = bean.cny;

                    if (curr_amount >= lucky_amount) {
                        bestLuckyUser = bean;
                    }
                }
            }
            receivedAmount += bean.cny;
        }
        // 红包总额（8.00）
        String totalAmountStr = MoneyUtils.fen2yuan(String.valueOf(info.cny));
        // 已抢金额（9.90）
        String receivedAmountStr = MoneyUtils.fen2yuan(String.valueOf(receivedAmount));
        // 红包总个数（4）
        int totalCount = info.num;
        // 抢完红包用时
        String completeTime = info.endtime;
        // 红包创建时间
        String createTime = info.starttime;

        //****** 组装 Vo
        List<ListModel> models = new ArrayList<>();

        //****** 抢到的红包信息
        if (currGetRedUser != null) {
            String getRedMoney = MoneyUtils.fen2yuan(String.valueOf(currGetRedUser.cny));
            ReceiveInfoItem receiveInfoItem = new ReceiveInfoItem(getRedMoney);
            models.add(new ListModel(receiveInfoItem));
        }

        //****** 发送的红包信息
        String sendInfo = null;
        if (RedPacketStatus.SUCCESS.equals(status)) {
            // 已抢完
            if (isGroupSession) {
                String fitTimeSpan = "未知时间";
                try {
                    fitTimeSpan = TimeUtils.getFitTimeSpan(completeTime, createTime, 4);
                } catch (Exception ignored) {
                }
                sendInfo = String.format(Locale.getDefault(), "%s个红包共%s元，%s被领完", totalCount, totalAmountStr, fitTimeSpan);
            } else {
                if (isRedSender) {
                    sendInfo = String.format(Locale.getDefault(), "红包金额%s元，该红包已领取", totalAmountStr);
                }
            }
        } else if (RedPacketStatus.TIMEOUT.equals(status)) {
            // 24小时超时
            if (isGroupSession) {
                sendInfo = String.format(Locale.getDefault(), "该红包已过期。已领取%s/%s个，共%s/%s元", receivedCount, totalCount, receivedAmountStr, totalAmountStr);
            } else {
                sendInfo = String.format(Locale.getDefault(), "红包金额%s元，该红包已过期", totalAmountStr);
            }
        } else if (RedPacketStatus.SEND.equals(status)) {
            // 抢红包中
            if (isGroupSession) {
                sendInfo = String.format(Locale.getDefault(), "已领取%s/%s个，共%s/%s元", receivedCount, totalCount, receivedAmountStr, totalAmountStr);
            } else {
                sendInfo = String.format(Locale.getDefault(), "红包金额%s元，等待对方领取", totalAmountStr);
            }
        }
        if (sendInfo != null) {
            SendInfoItem sendInfoItem = new SendInfoItem(sendInfo);
            models.add(new ListModel(sendInfoItem));
        }

        //****** 列表项
        for (int i = 0; i < receivedCount; i++) {
            PayRedInfoResp.GrablistBean bean = grabList.get(i);
            ListItem listItem = new ListItem();
            // 头像
            listItem.setAvatar(HttpCache.getResUrl(bean.getAvatar()));
            // 昵称
            listItem.setName(StringUtils.null2Length0(bean.getNick()));
            // 金额
            listItem.setMoneyInfo(MoneyUtils.fen2yuan(String.valueOf(bean.cny)) + "元");
            // 时间
            listItem.setTimeInfo(StringUtils.null2Length0(bean.grabtime));
            // 最佳手气
            if (bestLuckyUser != null) {
                listItem.setBestLucky(bestLuckyUser.getUid() == bean.getUid());
            } else {
                listItem.setBestLucky(false);
            }
            models.add(new ListModel(listItem));
        }

        //****** 脚注
        FooterItem footerItem = null;
        if (isRedSender) {
            if (RedPacketStatus.TIMEOUT.equals(status)) {
                // 24小时超时
                footerItem = new FooterItem("未领取的红包过期后自动退回");
            } else if (RedPacketStatus.SEND.equals(status)) {
                // 抢红包中
                footerItem = new FooterItem("未领取的红包，将在24小时后自动退回");
            }
        }
        if (footerItem != null) {
            models.add(new ListModel(footerItem));
        }

        //****** 返回 Vo
        return models;
    }
}
