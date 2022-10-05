package com.tiocloud.chat.feature.group.info.fragment.adapter.model;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.group.removemember.RemoveMemberActivity;
import com.watayouxiang.androidutils.page.TioActivity;

/**
 * author : TaoWang
 * date : 2020/3/16
 * desc : 删除成员
 */
public class RemoveButton extends ButtonModel {
    public RemoveButton() {
        super("删除", R.drawable.btn_delete);
    }

    @Override
    public void onItemClick(TioActivity activity, String groupId) {
        RemoveMemberActivity.start(activity, groupId);
    }
}
