package com.tiocloud.newpay.feature.recharge_result;

import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.feature.recharge_result.fragment.FailedFragment;
import com.tiocloud.newpay.feature.recharge_result.fragment.ProcessFragment;
import com.tiocloud.newpay.feature.recharge_result.fragment.SuccessFragment;
import com.tiocloud.newpay.tools.MoneyUtils;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayRechargeQueryReq;
import com.watayouxiang.httpclient.model.response.PayRechargeQueryResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   :
 * </pre>
 */
public class RechargeResultViewModel extends ViewModel {

    private RechargeResult rechargeResult;

    public void getRechargeQuery(String serialNumber, RechargeResultActivity activity) {
        PayRechargeQueryReq rechargeQueryReq = new PayRechargeQueryReq(serialNumber);
        rechargeQueryReq.setCancelTag(this);
        rechargeQueryReq.get(new TioCallback<PayRechargeQueryResp>() {
            @Override
            public void onTioSuccess(PayRechargeQueryResp resp) {
                processRechargeQuery(resp, activity);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void processRechargeQuery(PayRechargeQueryResp resp, RechargeResultActivity activity) {
        String status = resp.getStatus();
        String amount = MoneyUtils.fen2yuan(resp.getAmount());
        String orderErrorMsg = StringUtils.null2Length0(resp.getOrdererrormsg());

        RechargeResult rechargeResult;
        TioFragment fragment;

        if ("SUCCESS".equals(status)) {
            rechargeResult = RechargeResult.getSuccess(amount);
            fragment = new SuccessFragment();
        } else if ("PROCESS".equals(status)) {
            rechargeResult = RechargeResult.getProcess(amount);
            fragment = new ProcessFragment();
        } else if ("FAIL".equals(status)) {
            rechargeResult = RechargeResult.getFailed(amount, orderErrorMsg);
            fragment = new FailedFragment();
        } else {
            rechargeResult = RechargeResult.getFailed("", "未知充值结果");
            fragment = new FailedFragment();
        }

        this.rechargeResult = rechargeResult;
        activity.replaceFragment(fragment);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        TioHttpClient.cancel(this);
    }

    public RechargeResult getRechargeResult() {
        return rechargeResult;
    }

    // ====================================================================================
    // 新生支付
    // ====================================================================================

    public void getRechargeQuery(String rid, String reqId, RechargeResultActivity activity) {
        PayRechargeQueryReq rechargeQueryReq = new PayRechargeQueryReq(rid, reqId);
        rechargeQueryReq.setCancelTag(this);
        rechargeQueryReq.get(new TioCallback<PayRechargeQueryResp>() {
            @Override
            public void onTioSuccess(PayRechargeQueryResp resp) {
                processRechargeQuery2(resp, activity);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void processRechargeQuery2(PayRechargeQueryResp resp, RechargeResultActivity activity) {
        String status = resp.getStatus();
        String amount = MoneyUtils.fen2yuan(resp.getAmount());
        String orderErrorMsg = StringUtils.null2Length0(resp.getOrdererrormsg());

        RechargeResult rechargeResult;
        TioFragment fragment;

        if ("1".equals(status)) {
            rechargeResult = RechargeResult.getSuccess(amount);
            fragment = new SuccessFragment();
        } else if ("2".equals(status)) {
            rechargeResult = RechargeResult.getProcess(amount);
            fragment = new ProcessFragment();
        } else if ("3".equals(status)) {
            rechargeResult = RechargeResult.getFailed(amount, orderErrorMsg);
            fragment = new FailedFragment();
        } else {
            rechargeResult = RechargeResult.getFailed("", "未知充值结果");
            fragment = new FailedFragment();
        }

        this.rechargeResult = rechargeResult;
        activity.replaceFragment(fragment);
    }
}
