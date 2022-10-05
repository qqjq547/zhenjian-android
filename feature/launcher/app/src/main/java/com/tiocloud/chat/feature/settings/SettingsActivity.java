package com.tiocloud.chat.feature.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.settings.mvp.SettingsContract;
import com.tiocloud.chat.feature.settings.mvp.SettingsPresenter;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.widget.titlebar.WtTitleBar;

/**
 * author : TaoWang
 * date : 2020-02-19
 * desc :
 */
public class SettingsActivity extends TioActivity implements SettingsContract.View {

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    private final SettingsPresenter presenter = new SettingsPresenter(this);
    private ViewHolder viewHolder;

    public static class ViewHolder {
        public TextView tv_logoutBtn;
        public TextView tv_version;
        public CheckBox switch_verifyAddFriend;
        public CheckBox switch_searchMeAuth;
        public CheckBox switch_msgRemind;
        public SwitchCompat switch_encryption;
        private WtTitleBar titleBar;
        public RelativeLayout rl_clearHistoryMsg;
        public RelativeLayout rl_version;
        public RelativeLayout rl_feedback;
        public RelativeLayout rl_fontSize;
        public RelativeLayout rl_account;

        public ViewHolder(View decorView) {
            rl_account=decorView.findViewById(R.id.rl_account);

            rl_fontSize=decorView.findViewById(R.id.rl_fontSize);
            titleBar = decorView.findViewById(R.id.titleBar);
            tv_logoutBtn = decorView.findViewById(R.id.tv_logoutBtn);
            tv_version = decorView.findViewById(R.id.tv_version);
            switch_verifyAddFriend = decorView.findViewById(R.id.switch_verifyAddFriend);
            switch_searchMeAuth = decorView.findViewById(R.id.switch_searchMeAuth);
            switch_msgRemind = decorView.findViewById(R.id.switch_msgRemind);
            rl_clearHistoryMsg = decorView.findViewById(R.id.rl_clearHistoryMsg);
            rl_version = decorView.findViewById(R.id.rl_version);
            switch_encryption = decorView.findViewById(R.id.switch_encryption);
            rl_feedback = decorView.findViewById(R.id.rl_feedback);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tio_settings_activity);
        setTransparent(this);

        viewHolder = new ViewHolder(getWindow().getDecorView());
        presenter.init();
        viewHolder.titleBar.setTitle(getString(R.string.settings));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public ViewHolder getViewHolder() {
        return viewHolder;
    }
}
