package com.tiocloud.newpay.feature.paypwd_verify;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.WalletVerifyPaypwdFragmentBinding;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.widget.TioToast;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/09
 *     desc   : 验证支付密码
 * </pre>
 */
public class VerifyPayPwdFragment extends BindingFragment<WalletVerifyPaypwdFragmentBinding> {

    private static final String KEY_VERIFY_MODEL = "KEY_VERIFY_MODEL";
    private VerifyPayPwdViewModel viewModel;
    private OnVerifyPayPwdListener onVerifyPayPwdListener;
    private OnInputPayPwdListener onInputPayPwdFinish;

    public static VerifyPayPwdFragment getInstance(VerifyModel model) {
        VerifyPayPwdFragment fragment = new VerifyPayPwdFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_VERIFY_MODEL, model);
        fragment.setArguments(bundle);
        return fragment;
    }

    private VerifyModel getVerifyModel() {
        return (VerifyModel) getArguments().getSerializable(KEY_VERIFY_MODEL);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.wallet_verify_paypwd_fragment;
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
        viewModel = newViewModel(VerifyPayPwdViewModel.class);
        resetUI();
    }

    private void resetUI() {
        VerifyModel model = getVerifyModel();

        if (model.isStatusBar_show()) {
            binding.statusBar.setVisibility(View.VISIBLE);
            if (model.getStatusBar_color() != null) {
                setStatusBarColor(model.getStatusBar_color());
            }
        } else {
            binding.statusBar.setVisibility(View.GONE);
        }

        if (model.isTitleBar_show()) {
            binding.titleBar.setVisibility(View.VISIBLE);
            binding.titleBar.setTitle(StringUtils.null2Length0(model.getTitleBar_title()));
        } else {
            binding.titleBar.setVisibility(View.GONE);
        }

        if (model.getFragment_backgroundColor() != null) {
            binding.clRoot.setBackgroundColor(model.getFragment_backgroundColor());
        }

        binding.payPwdEditText.setOnTextFinishListener(str -> {
            if (onInputPayPwdFinish != null) {
                onInputPayPwdFinish.onInputPayPwdFinish(str);
            }

            if (onVerifyPayPwdListener != null) {
                viewModel.reqCheckPayPwd(str, VerifyPayPwdFragment.this);
            }
        });
    }

    public void notifyVerifyPayPwdSuccess(String pwd) {
        KeyboardUtils.hideSoftInput(getTioActivity());

        if (onVerifyPayPwdListener != null) {
            onVerifyPayPwdListener.onVerifyPayPwdSuccess(pwd);
        }
    }

    public void notifyVerifyPayPwdError(String msg) {
        KeyboardUtils.hideSoftInput(getTioActivity());

        clearPayPwd();
        TioToast.showShort(msg);
    }

    // ====================================================================================
    // listener
    // ====================================================================================

    public void clearPayPwd() {
        if (binding != null) {
            binding.payPwdEditText.clearText();
        }
    }

    public void setOnVerifyPayPwdListener(OnVerifyPayPwdListener onVerifyPayPwdListener) {
        this.onVerifyPayPwdListener = onVerifyPayPwdListener;
    }

    public void setOnInputPayPwdFinish(OnInputPayPwdListener onInputPayPwdFinish) {
        this.onInputPayPwdFinish = onInputPayPwdFinish;
    }

    public interface OnVerifyPayPwdListener {
        void onVerifyPayPwdSuccess(String payPwd);
    }

    public interface OnInputPayPwdListener {
        void onInputPayPwdFinish(String payPwd);
    }
}
