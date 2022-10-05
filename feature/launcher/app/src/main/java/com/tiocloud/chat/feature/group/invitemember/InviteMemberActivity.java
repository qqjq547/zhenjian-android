package com.tiocloud.chat.feature.group.invitemember;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.KeyboardUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.feature.group.invitemember.fragment.InviteMemberFragment;
import com.watayouxiang.androidutils.listener.SimpleTextWatcher;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.widget.titlebar.WtTitleBar;

/**
 * author : TaoWang
 * date : 2020-02-25
 * desc :
 */
public class InviteMemberActivity extends TioActivity {

    private FrameLayout frameLayout;
    private EditText et_input;
    private WtTitleBar titleBar;
    private InviteMemberFragment fragment;

    public static void start(Context context, String groupId) {
        Intent starter = new Intent(context, InviteMemberActivity.class);
        starter.putExtra(TioExtras.EXTRA_GROUP_ID, groupId);
        context.startActivity(starter);
    }

    public String getGroupId() {
        return getIntent().getStringExtra(TioExtras.EXTRA_GROUP_ID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tio_invite_member);
        findViews();
        initEditText();
        installFragment();
    }

    @Override
    protected void onDestroy() {
        KeyboardUtils.hideSoftInput(this);
        super.onDestroy();
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    private void installFragment() {
        fragment = InviteMemberFragment.create(getGroupId());
        fragment.setContainerId(frameLayout.getId());
        fragment.presenter.installMenuBtn(titleBar.getTvRight());
        addFragment(fragment);
    }

    private void initEditText() {
        et_input.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (s != null && fragment != null) {
                    String keyWord = s.toString();
                    fragment.presenter.search(keyWord);
                }
            }
        });
    }

    private void findViews() {
        frameLayout = findViewById(R.id.frameLayout);
        et_input = findViewById(R.id.et_input);
        titleBar = findViewById(R.id.titleBar);
    }
}
