package com.tiocloud.account.feature.phone_modify.step3;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountModifyPhoneOkFragmentBinding;
import com.tiocloud.account.feature.phone_modify.ModifyPhoneActivity;
import com.watayouxiang.androidutils.page.BindingFragment;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/19
 *     desc   : 3、修改手机号成功
 * </pre>
 */
public class ModifyPhoneOkFragment extends BindingFragment<AccountModifyPhoneOkFragmentBinding> implements MvpContract.View {

    private MvpPresenter presenter;

    @Override
    protected int getContentViewId() {
        return R.layout.account_modify_phone_ok_fragment;
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
        setStatus(3);
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

    public void onClick_ok(View view) {
        ModifyPhoneActivity activity = (ModifyPhoneActivity) getActivity();
        activity.onBackPressed();
    }
}
