package com.tiocloud.account.feature.retrieve_pwd;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;

import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountRetrievePwdInputPhoneFragmentBinding;
import com.tiocloud.account.mvp.retrieve_pwd.RetrievePwdContract;
import com.tiocloud.account.mvp.retrieve_pwd.RetrievePwdPresenter;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.utils.ClickUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/24
 *     desc   : 1、输入手机
 * </pre>
 */
public class InputPhoneFragment extends BindingFragment<AccountRetrievePwdInputPhoneFragmentBinding>
        implements RetrievePwdContract.View, RetrievePwdContract.OnSmsBeforeCheckListener {

    public final ObservableField<String> txt_phone = new ObservableField<>("");

    private RetrievePwdPresenter presenter;

    @Override
    protected int getContentViewId() {
        return R.layout.account_retrieve_pwd_input_phone_fragment;
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
        presenter = new RetrievePwdPresenter(this);
        resetUI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    private void resetUI() {

    }

    // 确定按钮
    public void onClick_ok(View view) {
        if (!ClickUtils.isViewSingleClick(view)) return;
        presenter.reqSmsBeforeCheck(txt_phone.get(), this);
    }

    private RetrievePwdActivity getRetrievePwdActivity() {
        FragmentActivity activity = getActivity();
        if (activity instanceof RetrievePwdActivity) {
            return (RetrievePwdActivity) activity;
        }
        return null;
    }

    @Override
    public void onSmsBeforeCheckSuccess() {
        getRetrievePwdActivity().showSmsCodeFragment(txt_phone.get());
    }
}
