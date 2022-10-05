package com.tiocloud.account.feature.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.account.R;
import com.tiocloud.account.TioAccount;
import com.tiocloud.account.databinding.AccountAccountActivityBinding;
import com.tiocloud.account.feature.phone_modify.ModifyPhoneActivity;
import com.tiocloud.account.feature.unregister.UnregisterActivity;
import com.tiocloud.account.mvp.account.AccountContract;
import com.tiocloud.account.mvp.account.AccountPresenter;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/15
 *     desc   : 账号
 * </pre>
 */
public class AccountActivity extends BindingActivity<AccountAccountActivityBinding> implements AccountContract.View {

    private AccountPresenter presenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AccountActivity.class);
        context.startActivity(starter);
    }

    public final ObservableField<String> curr_account = new ObservableField<>("账号：");
    public final ObservableField<String> email = new ObservableField<>("");
    public final ObservableField<String> phone = new ObservableField<>("");

    @Override
    protected int getContentViewId() {
        return R.layout.account_account_activity;
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
    protected Boolean statusBar_lightMode() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(this);
        presenter = new AccountPresenter(this);
        presenter.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    // 修改手机号
    public void onClick_modifyPhone(View v) {
        ModifyPhoneActivity.start(getActivity());
    }

    // 修改密码
    public void onClick_modifyPwd(View v) {
        TioAccount.getBridge().startModifyPwdActivity(getActivity());
    }

    // 账号注销
    public void onClick_unregisterAccount(View v) {
        if (!ClickUtils.isViewSingleClick(v)) return;
        UnregisterActivity.start(getActivity());
    }

    @Override
    public void onUserCurrResp(UserCurrResp resp) {
        // 账号
        curr_account.set(String.format(Locale.getDefault(), "账号：%s", resp.loginname));
        // 邮箱
        email.set(StringUtils.null2Length0(resp.email));
        // 手机
        phone.set(StringUtils.null2Length0(resp.phone));
    }
}
