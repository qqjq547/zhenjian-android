package com.tiocloud.account.feature.register;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;

import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountPhoneRegisterFragmentBinding;
import com.tiocloud.account.feature.login.LoginActivity;
import com.tiocloud.account.feature.login_sms.SmsLoginActivity;
import com.tiocloud.account.mvp.register.RegisterContract;
import com.tiocloud.account.mvp.register.RegisterPresenter;
import com.tiocloud.account.widget.ThirdPartyLoginView;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.ApplyListReq;
import com.watayouxiang.httpclient.model.request.RegisterphoneflagReq;
import com.watayouxiang.httpclient.model.response.ApplyListResp;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/24
 *     desc   :
 * </pre>
 */
public class PhoneRegisterFragment extends BindingFragment<AccountPhoneRegisterFragmentBinding> implements RegisterContract.View {

    public final ObservableField<String> txt_login_name = new ObservableField<>("");
    public final ObservableField<String> txt_phone = new ObservableField<>("");
    public final ObservableField<String> txt_invite_code = new ObservableField<>("");
    public final ObservableField<String> txt_code = new ObservableField<>("");
    public final ObservableField<String> txt_nick = new ObservableField<>("");
    public final ObservableField<String> txt_pwd = new ObservableField<>("");
    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);

    private RegisterPresenter presenter;

    @Override
    protected int getContentViewId() {
        return R.layout.account_phone_register_fragment;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.parseColor("#DBEAFF");
    }

    @Override
    protected Boolean statusBar_lightMode() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setData(this);
        presenter = new RegisterPresenter(this);
        resetUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ThirdPartyLoginView.onActivityResult(requestCode, resultCode, data);
    }

    private void resetUI() {
        // 默认不绑定邮箱
        binding.tvBindEmail.setSelected(false);
        binding.etLoginName.setKeyListener(new DigitsKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL;
            }

            @NonNull
            @Override
            protected char[] getAcceptedChars() {
                return "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
            }
        });
        // 验证码登录
        if (!ModuleConfig.ENABLE_SMS_LOGIN) {
            binding.tvCodeLogin.setVisibility(View.INVISIBLE);
        }



    }

    // 密码登录
    public void onClick_topRight(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        LoginActivity.start(getActivity());
        finish();
    }

    // 验证码登录
    public void onClick_codeLogin(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        SmsLoginActivity.start(getActivity());
        finish();
    }
    CodeCountDownTimer codeCountDownTimer;

    // 注册
    public void onClick_ok(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        codeCountDownTimer = new CodeCountDownTimer(getActivity(), 60000, 1000, binding.btRegister);
        codeCountDownTimer.start();
//        presenter.phoneRegister(txt_phone.get(), txt_code.get(), txt_nick.get(), txt_pwd.get(), binding.protocolView.isCheckbox.get());
        presenter.phoneRegister(txt_login_name.get(),"", "", txt_invite_code.get(), txt_nick.get(), txt_pwd.get(), true, binding.btRegister);
    }

    // 绑定邮箱
    public void onClick_bindEmail(View view) {
        getRegisterActivity().showPhoneRegisterBindEmailFragment();
    }

    // 获取验证码
    public void onClick_reqPhoneCode(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.reqSendSms(getActivity(), txt_phone.get());
    }

    @Override
    public void onPhoneRegisterSuccess() {
        TioToast.showShort("注册成功，请登录");
        finish();
        LoginActivity.start(getActivity());
    }

    @Override
    public void onSendSmsSuccess() {
        presenter.startCodeTimer(60);
    }

    @Override
    public void onCodeTimerRunning(int second) {
        isStartTimer.set(true);
//        binding.tvReqPhoneCode.setText(String.format(Locale.getDefault(), "已发送(%ds)", second));
    }

    @Override
    public void onCodeTimerStop() {
        isStartTimer.set(false);
//        binding.tvReqPhoneCode.setText("获取验证码");
    }

    @Override
    public void onRegisterBindEmailSuccess() {

    }

    private RegisterActivity getRegisterActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof RegisterActivity) {
            return (RegisterActivity) activity;
        }
        return null;
    }
}
