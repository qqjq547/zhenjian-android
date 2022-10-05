package com.tiocloud.account.feature.unregister;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.SpanUtils;
import com.tiocloud.account.BuildConfig;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountUnregisterReaditemFragmentBinding;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.page.BindingFragment;
import com.watayouxiang.androidutils.tools.WtTimer;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.request.UserLogoutCheckReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/04/16
 *     desc   : 1、阅读条款
 * </pre>
 */
public class ReadItemFragment extends BindingFragment<AccountUnregisterReaditemFragmentBinding> {

    private static final int INIT_TIME = 15;
    private WtTimer wtTimer;
    private final ReadItemFragment TAG = ReadItemFragment.this;

    public static ReadItemFragment getInstance() {
        return new ReadItemFragment();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_unregister_readitem_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (wtTimer != null) {
            wtTimer.stop();
        }
        TioHttpClient.cancel(TAG);
    }

    private void initView() {
        // 确认按钮
        binding.btnOk.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                checkUnregister();
            }
        });
        if (BuildConfig.DEBUG) {
            binding.btnOk.setOnLongClickListener(v -> {
                nextPage();
                return true;
            });
        }
        // 倒计时
        startCountdown();
        // 显示内容
        binding.tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        binding.tvContent.setText(new SpanUtils()
                .append("注销须知：\n").setForegroundColor(Color.parseColor("#666666")).setFontSize(16, true)
                .append("\n")
                .append("1、注销后，你将无法登陆；\n").setForegroundColor(Color.parseColor("#333333")).setFontSize(16, true)
                .append("2、所有信息将被永久删除\n").setForegroundColor(Color.parseColor("#333333")).setFontSize(16, true)
                .append("  例：（聊天记录、好友、群聊等），你的好友将无法在与你取得联系；\n").setForegroundColor(Color.parseColor("#666666")).setFontSize(14, true)
                .append("3、如钱包还有余额，需要全部提现成功后才能注销；\n").setForegroundColor(Color.parseColor("#333333")).setFontSize(16, true)
                .append("4、已发红包、转账需要全部被领完或过期退回后才能注销；\n").setForegroundColor(Color.parseColor("#333333")).setFontSize(16, true)
                .append("5、账号状态异常情况下如封号等，将不能注销；\n").setForegroundColor(Color.parseColor("#333333")).setFontSize(16, true)
                .create());
    }

    private void nextPage() {
        FragmentUtils.replace(ReadItemFragment.this, VerifySafeFragment.getInstance());
    }

    private void checkUnregister() {
        new UserCurrReq().setCancelTag(TAG).get(new TioSuccessCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                new UserLogoutCheckReq(resp.id + "").setCancelTag(TAG).post(new TioSuccessCallback<Object>() {
                    @Override
                    public void onTioSuccess(Object o) {
                        nextPage();
                    }
                });
            }
        });
    }

    private void startCountdown() {
        if (wtTimer == null) {
            wtTimer = new WtTimer();
        }
        wtTimer.start((count, timer) -> {
            if (count < INIT_TIME) {
                onCountdown(false, INIT_TIME - count);
            } else {
                timer.stop();
                onCountdown(true, INIT_TIME - count);
            }
        }, true, 0, 1000);
    }

    public void onCountdown(boolean isStop, int second) {
        if (isStop) {
            binding.btnOk.setEnabled(true);
            binding.btnOk.setText("申请注销");
        } else {
            binding.btnOk.setEnabled(false);
            binding.btnOk.setText(String.format(Locale.getDefault(), "申请注销(%ds)", second));
        }
    }
}
