package com.tiocloud.chat.feature.user.group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tiocloud.chat.R;
import com.watayouxiang.androidutils.page.TioActivity;
import androidx.annotation.Nullable;

public class GroupListActivity extends TioActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tio_group_list_activity);
        GroupFragment fragment = new GroupFragment();
        fragment.setContainerId(R.id.fl_group);
        TioActivity tioActivity = getActivity();
        if (tioActivity != null) {
            tioActivity.replaceFragment(fragment);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, GroupListActivity.class);
        context.startActivity(starter);
    }
}
