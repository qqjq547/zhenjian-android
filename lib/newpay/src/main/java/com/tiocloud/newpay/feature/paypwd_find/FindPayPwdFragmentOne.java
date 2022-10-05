package com.tiocloud.newpay.feature.paypwd_find;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletFindPayPwdFragmentOneBinding;
import com.watayouxiang.androidutils.page.MvpFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/08
 *     desc   :
 * </pre>
 */
public class FindPayPwdFragmentOne extends MvpFragment<FragmentOnePresenter, WalletFindPayPwdFragmentOneBinding> implements FragmentOneContract.View {

    public final ObservableField<String> txt_phone = new ObservableField<>("");
    public final ObservableField<String> txt_code = new ObservableField<>("");
    public final ObservableField<String> txt_tip_phone = new ObservableField<>("");// 158****8765
    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);

    public static FindPayPwdFragmentOne getInstance() {
        return new FindPayPwdFragmentOne();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_find_pay_pwd_fragment_one;
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
        binding.setData(this);
        presenter.init();
    }

    @Override
    public FragmentOnePresenter newPresenter() {
        return new FragmentOnePresenter(this);
    }

    // 获取验证码
    public void onClick_reqPhoneCode(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.reqSendSms(txt_phone.get());
    }

    // 下一步
    public void onClick_next(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;

        String smsCode = binding.etCode.getText().toString().trim();
        FindPayPwdActivity activity = (FindPayPwdActivity) getActivity();
        activity.setSmsCode(smsCode);

        FragmentUtils.replace(this, FindPayPwdFragmentTwo.getInstance());
    }

    @Override
    public void onUserCurrResp(UserCurrResp resp) {
        String phone = resp.phone;

        txt_phone.set(phone);

        // 158****8765
        String showPhone = null;
        if (phone.length() == 11) {
            String start = phone.substring(0, 3);
            String end = phone.substring(phone.length() - 4);
            showPhone = StringUtils.format("%s****%s", start, end);
        }
        if (showPhone != null) {
            txt_tip_phone.set(showPhone);
        }
    }

    @Override
    public void onCodeTimerRunning(int second) {
        isStartTimer.set(true);
        binding.tvReqPhoneCode.setText(String.format(Locale.getDefault(), "已发送(%ds)", second));
    }

    @Override
    public void onCodeTimerStop() {
        isStartTimer.set(false);
        binding.tvReqPhoneCode.setText("获取验证码");
    }
}
