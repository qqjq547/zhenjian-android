package com.tiocloud.chat.feature.main.fragment;

import static com.blankj.utilcode.util.BarUtils.setStatusBarCustom;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.friend.FriendFragment;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.feature.search.curr.SearchActivity;
import com.tiocloud.chat.widget.titlebar.HomeTitleBar;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.widget.titlebar.WtTitleBar;

/**
 * author : TaoWang
 * date : 2020-01-10
 * desc : 联系人
 * <p>
 */
public class FriendActivity extends TioActivity {

    private HomeTitleBar homeTitleBar;
    private FriendFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        setStatusBarCustom(findViewById(R.id.fl_statusBar));
        WtTitleBar titleBar=findViewById(R.id.titleBar);
        titleBar.setTitle(getString(R.string.good_friend));
        fragment = new FriendFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content,fragment).commit();
        findViewById(R.id.ll_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(v.getContext());
            }
        });
        fragment.onRefresh();
    }

    public void setAppendTitle(int friendNum) {
        if (homeTitleBar != null) {
            homeTitleBar.setAppendTitle(String.valueOf(friendNum));
        }
    }

}
