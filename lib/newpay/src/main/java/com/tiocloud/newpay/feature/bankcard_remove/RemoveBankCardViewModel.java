package com.tiocloud.newpay.feature.bankcard_remove;

import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyModel;
import com.tiocloud.newpay.feature.paypwd_verify.VerifyPayPwdFragment;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.confirm.EasyConfirmDialog;
import com.watayouxiang.androidutils.widget.dialog.confirm.TioConfirmDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.PayUnbindCardReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.PayUnbindCardResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/16
 *     desc   :
 * </pre>
 */
public class RemoveBankCardViewModel extends ViewModel {

    public VerifyPayPwdFragment addFragment(RemoveBankCardActivity activity, int containerId) {
        VerifyPayPwdFragment verifyPayPwdFragment = VerifyPayPwdFragment.getInstance(VerifyModel.getInstance_removeBankCard());
        verifyPayPwdFragment.setOnInputPayPwdFinish(new VerifyPayPwdFragment.OnInputPayPwdListener() {
            @Override
            public void onInputPayPwdFinish(String payPwd) {
                reqRemoveBankCard(payPwd, activity);
            }
        });
        FragmentUtils.add(activity.getSupportFragmentManager(), verifyPayPwdFragment, containerId);
        return verifyPayPwdFragment;
    }

    private void reqRemoveBankCard(String payPwd, RemoveBankCardActivity activity) {
        new UserCurrReq().setCancelTag(activity).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {

                RemoveBankCardVo vo = activity.getRemoveBankCardVo();
                new PayUnbindCardReq(vo.bankcardid, vo.agrno, payPwd, resp.loginname).setCancelTag(activity).post(new TioCallback<PayUnbindCardResp>() {
                    @Override
                    public void onTioSuccess(PayUnbindCardResp o) {
                        showUnbindSuccessDialog(activity);
                    }

                    @Override
                    public void onTioError(String msg) {
                        TioToast.showShort(msg);
                        activity.getVerifyPayPwdFragment().clearPayPwd();
                    }
                });

            }
        });
    }

    private void showUnbindSuccessDialog(RemoveBankCardActivity activity) {
        new EasyConfirmDialog.Builder(activity)
                .setMessage("解绑成功")
                .setOnConfirmListener(new TioConfirmDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(View view, TioConfirmDialog dialog) {
                        dialog.dismiss();
                        activity.finish();
                    }
                })
                .build()
                .show();
    }

}
