package com.tiocloud.chat.feature.home.friend.adapter.model.item;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.friend.adapter.model.IGroup;
import com.tiocloud.chat.feature.home.friend.adapter.model.IItem;

/**
 * author : TaoWang
 * date : 2020-01-22
 * desc :
 */
public class FuncItem extends IItem {

    public int imgResId = R.drawable.ic_new_friend;
    public String name = "好友请求";
    public int applyCount;
    public boolean isLast = false;  //是否是最后一个


    @Override
    public int getType() {
        return Type.FUNC;
    }

    @Override
    public String groupId() {
        return IGroup.ID_HEADER;
    }
}
