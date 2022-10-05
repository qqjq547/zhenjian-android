package com.tiocloud.account.feature.phone_modify;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tiocloud.account.feature.phone_modify.step1.ConfirmOldPhoneFragment;
import com.tiocloud.account.feature.phone_modify.step2.BindNewPhoneFragment;
import com.tiocloud.account.feature.phone_modify.step3.ModifyPhoneOkFragment;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/19
 *     desc   :
 * </pre>
 */
class PagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] fragments = {
            new ConfirmOldPhoneFragment(),
            new BindNewPhoneFragment(),
            new ModifyPhoneOkFragment(),
    };

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
