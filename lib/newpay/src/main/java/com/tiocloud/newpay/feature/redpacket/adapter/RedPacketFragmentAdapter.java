package com.tiocloud.newpay.feature.redpacket.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.tiocloud.newpay.feature.redpacket.feature.receive.ReceiveFragment;
import com.tiocloud.newpay.feature.redpacket.feature.send.SendFragment;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/03
 *     desc   :
 * </pre>
 */
public class RedPacketFragmentAdapter extends FragmentStatePagerAdapter {
    private final String[] titles = {"我收到的", "我发出的"};
    private final Fragment[] fragments = {
            ReceiveFragment.newInstance(),
            SendFragment.newInstance()
    };

    public RedPacketFragmentAdapter(@NonNull FragmentManager fm) {
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

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
