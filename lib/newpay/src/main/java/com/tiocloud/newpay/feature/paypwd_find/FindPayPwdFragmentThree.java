package com.tiocloud.newpay.feature.paypwd_find;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletFindPayPwdFragmentThreeBinding;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.confirm.EasyConfirmDialog;
import com.watayouxiang.httpclient.model.request.ResetPayPwdReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   :
 * </pre>
 */
public class FindPayPwdFragmentThree extends BindingFragment<WalletFindPayPwdFragmentThreeBinding> {

    private static final String KEY_PWD_TEXT = "KEY_PWD_TEXT";

    public static FindPayPwdFragmentThree getInstance(String pwdText) {
        FindPayPwdFragmentThree fragmentTwo = new FindPayPwdFragmentThree();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PWD_TEXT, pwdText);
        fragmentTwo.setArguments(bundle);
        return fragmentTwo;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_find_pay_pwd_fragment_three;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.WHITE;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String pwdTxt = getArguments().getString(KEY_PWD_TEXT, null);

        binding.payPwdEditText.setOnTextFinishListener(str -> {
            if (!StringUtils.equals(pwdTxt, str)) {
                TioToast.showShort("两次密码输入不一致，请重新输入");
                binding.payPwdEditText.clearText();
                return;
            }

            reqResetPayPwd(str);
        });
    }

    private void reqResetPayPwd(String newPwd) {
        FindPayPwdFragmentThree tag = this;
        FindPayPwdActivity activity = (FindPayPwdActivity) getActivity();
        String smsCode = activity.getSmsCode();

        new UserCurrReq().setCancelTag(tag).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {

                new ResetPayPwdReq(smsCode, newPwd, resp.phone).setCancelTag(tag).post(new TioSuccessCallback<Object>() {
                    @Override
                    public void onTioSuccess(Object o) {
                        showOkDialog();
                    }
                });

            }
        });
    }

    private void showOkDialog() {
        new EasyConfirmDialog.Builder(getTioActivity())
                .setMessage("支付密码修改成功！")
                .setOnConfirmListener((view, dialog) -> finish())
                .build()
                .show();
    }
}
