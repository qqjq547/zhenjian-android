package com.tiocloud.chat.feature.main.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.feature.main.model.MainTab;

import java.util.List;

abstract class BaseTabPagerAdapter extends FragmentPagerAdapter {

    private final MainTabFragment[] fragments;
    private final Context context;

    BaseTabPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.fragments = new MainTabFragment[MainTab.values().length];
        for (MainTab tab : MainTab.values()) {
            try {
                // 获取已存在的 fragment
                MainTabFragment fragment = null;
                List<Fragment> fs = fm.getFragments();
                for (Fragment f : fs) {
                    if (f.getClass() == tab.clazz) {
                        fragment = (MainTabFragment) f;
                        break;
                    }
                }
                // 不存在则创建
                if (fragment == null) {
                    fragment = tab.clazz.newInstance();
                }
                // 绑定数据
                fragment.attachTabData(tab);
                // 存储到
                fragments[tab.tabIndex] = fragment;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ====================================================================================
    // getter
    // ====================================================================================

    public int getImageResource(int position) {
        MainTab tab = MainTab.fromTabIndex(position);
        return tab == null ? 0 : tab.iconId;
    }

    public int getCacheCount() {
        return MainTab.values().length;
    }

    public Context getContext() {
        return context;
    }

    // ====================================================================================
    // FragmentPagerAdapter
    // ====================================================================================

    @NonNull
    @Override
    public MainTabFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return MainTab.values().length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        MainTab tab = MainTab.fromTabIndex(position);
        return tab == null ? "" : getContext().getText(tab.titleId);
    }

}