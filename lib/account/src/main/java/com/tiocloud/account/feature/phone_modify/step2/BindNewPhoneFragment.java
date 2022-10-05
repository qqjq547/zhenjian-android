package com.tiocloud.account.feature.phone_modify.step2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;

import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountBindNewPhoneFragmentBinding;
import com.tiocloud.account.feature.phone_modify.ModifyPhoneActivity;
import com.watayouxiang.androidutils.page.BindingFragment;

import java.util.Locale;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/19
 *     desc   : 2、绑定新手机
 * </pre>
 */
public class BindNewPhoneFragment extends BindingFragment<AccountBindNewPhoneFragmentBinding> implements MvpContract.View {

    public final ObservableField<String> txt_phone = new ObservableField<>("");
    public final ObservableField<String> txt_code = new ObservableField<>("");
    public final ObservableField<String> txt_pwd = new ObservableField<>("");
    public final ObservableField<Boolean> isStartTimer = new ObservableField<>(false);

    private MvpPresenter presenter;

    @Override
    protected int getContentViewId() {
        return R.layout.account_bind_new_phone_fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.setData(this);
        presenter = new MvpPresenter(this);
        presenter.init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void resetUI() {
        setStatus(2);
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

    @Override
    public void onBindNewPhoneSuccess() {
        ModifyPhoneActivity activity = (ModifyPhoneActivity) getActivity();
        activity.nextFragment();
    }

    /**
     * @param status 1 验证原手机，2 绑定新手机，3 修改成功
     */
    private void setStatus(int status) {
        if (status == 1) {
            binding.ivPoint1.setImageResource(R.drawable.account_point_current);
            binding.ivPoint2.setImageResource(R.drawable.account_point_unfinished);
            binding.ivPoint3.setImageResource(R.drawable.account_point_unfinished);
            binding.vLine1.setBackgroundColor(Color.parseColor("#F1F1F1"));
            binding.vLine2.setBackgroundColor(Color.parseColor("#F1F1F1"));
            binding.tvText1.setTextColor(Color.parseColor("#333333"));
            binding.tvText2.setTextColor(Color.parseColor("#888888"));
            binding.tvText3.setTextColor(Color.parseColor("#888888"));
        } else if (status == 2) {
            binding.ivPoint1.setImageResource(R.drawable.account_point_finish);
            binding.ivPoint2.setImageResource(R.drawable.account_point_current);
            binding.ivPoint3.setImageResource(R.drawable.account_point_unfinished);
            binding.vLine1.setBackgroundColor(Color.parseColor("#AFCFFF"));
            binding.vLine2.setBackgroundColor(Color.parseColor("#F1F1F1"));
            binding.tvText1.setTextColor(Color.parseColor("#888888"));
            binding.tvText2.setTextColor(Color.parseColor("#333333"));
            binding.tvText3.setTextColor(Color.parseColor("#888888"));
        } else if (status == 3) {
            binding.ivPoint1.setImageResource(R.drawable.account_point_finish);
            binding.ivPoint2.setImageResource(R.drawable.account_point_finish);
            binding.ivPoint3.setImageResource(R.drawable.account_point_current);
            binding.vLine1.setBackgroundColor(Color.parseColor("#AFCFFF"));
            binding.vLine2.setBackgroundColor(Color.parseColor("#AFCFFF"));
            binding.tvText1.setTextColor(Color.parseColor("#888888"));
            binding.tvText2.setTextColor(Color.parseColor("#888888"));
            binding.tvText3.setTextColor(Color.parseColor("#333333"));
        }
    }

    public void onClick_reqPhoneCode(View view) {
        presenter.reqSendSms(getTioActivity(), txt_phone.get());
    }

    public void onClick_ok(View view) {
        presenter.bindNewPhone(txt_phone.get(), txt_code.get(), txt_pwd.get());
    }
}
