package com.tiocloud.account.feature.login_sms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.FragmentUtils;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountSmsLoginInputCodeFragmentBinding;
import com.tiocloud.account.mvp.login.LoginContract;
import com.tiocloud.account.mvp.login.LoginPresenter;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;

import java.util.Locale;

/**
 * <pre>
 *     desc   : 2、输入验证码
 * </pre>
 */
public class InputCodeFragment extends BindingFragment<AccountSmsLoginInputCodeFragmentBinding>
        implements LoginContract.View, FragmentUtils.OnBackClickListener, LoginContract.OnTimerListener, LoginContract.OnSendSmsListener {

    private static final String KEY_PHONE = "KEY_PHONE";

    public final ObservableField<String> txt_code = new ObservableField<>("");
    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);

    private LoginPresenter presenter;

    private InputCodeFragment() {
    }

    public static InputCodeFragment getInstance(@NonNull String phone) {
        InputCodeFragment fragment = new InputCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PHONE, phone);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    private String getPhone() {
        return getArguments().getString(KEY_PHONE);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_sms_login_input_code_fragment;
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
        presenter = new LoginPresenter(this);
        resetUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public boolean onBackClick() {
        FragmentUtils.replace(this, InputPhoneFragment.getInstance());
        return true;
    }

    private void resetUI() {
        onSendSmsSuccess();
    }

    // 确定按钮
    public void onClick_ok(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.codeLogin(getPhone(), txt_code.get(), getTioActivity());
    }

    // 获取验证码
    public void onClick_reqPhoneCode(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.sendSms(getTioActivity(), getPhone(), this);
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

    @Override
    public void onSendSmsSuccess() {
        presenter.startCodeTimer(this);
    }
}
