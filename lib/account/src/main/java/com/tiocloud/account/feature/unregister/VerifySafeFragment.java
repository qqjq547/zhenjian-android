package com.tiocloud.account.feature.unregister;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.SpanUtils;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountUnregisterVerifysafeFragmentBinding;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.tiocloud.account.mvp.sms.SmsContract;
import com.tiocloud.account.mvp.sms.SmsPresenter;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.page.MvpFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.model.request.UserLogoutReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/04/16
 *     desc   : 2、验证安全
 * </pre>
 */
public class VerifySafeFragment extends MvpFragment<SmsPresenter, AccountUnregisterVerifysafeFragmentBinding>
        implements SmsContract.View, SmsContract.OnSendSmsListener, SmsContract.OnTimerListener {

    public final ObservableField<String> txt_phone = new ObservableField<>("");
    public final ObservableField<String> txt_code = new ObservableField<>("");
    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);
    private UserCurrResp mUserCurrResp;
    private final Object TAG = VerifySafeFragment.this;

    public static VerifySafeFragment getInstance() {
        return new VerifySafeFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_unregister_verifysafe_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setData(this);
        presenter.getCurrUserInfo(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                mUserCurrResp = resp;
                txt_phone.set(resp.phone);
                binding.tvTip.setText(String.format("短信验证码将发送至%s", resp.phone));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TioHttpClient.cancel(TAG);
    }

    @Override
    public SmsPresenter newPresenter() {
        return new SmsPresenter(this);
    }

    // 获取验证码
    public void onClick_reqPhoneCode(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.sendSms(getTioActivity(), txt_phone.get(), "11", this);
    }

    // 确定按钮
    public void onClick_ok(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        showTipDialog();
    }

    private void showTipDialog() {
        SpannableStringBuilder stringBuilder = new SpanUtils()
                .append("即将注销当前账号！\n").setForegroundColor(Color.parseColor("#333333")).setFontSize(16, true)
                .append("请在最后确认一次").setForegroundColor(Color.parseColor("#666666")).setFontSize(14, true)
                .create();
        new EasyOperDialog.Builder(stringBuilder)
                .setPositiveBtnTxt("确认注销")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqUnregister();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getTioActivity());
    }

    private void reqUnregister() {
        if (mUserCurrResp == null) {
            TioToast.showShort("获取当前用户信息失败");
            return;
        }
        new UserLogoutReq(mUserCurrResp.id + "", txt_code.get()).setCancelTag(TAG).post(new TioSuccessCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                TioToast.showShort("注销成功");
                LogoutPresenter.kickOut();
            }
        });
    }

    @Override
    public void onSendSmsSuccess() {
        presenter.startCodeTimer(this);
    }

    @Override
    public void OnTimerRunning(int second) {
        isStartTimer.set(true);
        binding.tvReqPhoneCode.setText(String.format(Locale.getDefault(), "已发送(%ds)", second));
    }

    @Override
    public void OnTimerStop() {
        isStartTimer.set(false);
        binding.tvReqPhoneCode.setText("获取验证码");
    }
}
