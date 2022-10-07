package com.tiocloud.chat.feature.main.model;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.main.fragment.MainWebFragment;
import com.tiocloud.chat.widget.reference.reminder.ReminderId;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.feature.main.fragment.MainChatFragment;
import com.tiocloud.chat.feature.main.fragment.MainUserFragment;

public enum MainTab {
//    CHAT(0, ReminderId.CHAT, MainChatFragment.class, R.string.talk, R.drawable.tio_main_chats_selector, R.layout.tio_main_chat_fragment),
//    FRIEND(1, ReminderId.FRIEND, MainFriendFragment.class, R.string.good_friend, R.drawable.tio_main_friend_selector, R.layout.tio_main_friend_fragment),
//    FIVE(2, ReminderId.FIVE, MainFiveFragment.class, R.string.shouye, R.drawable.tio_main_found_selector, R.layout.tio_main_five_fragment),
    VIDEO(0, ReminderId.VIDEO, MainWebFragment.class, R.string.video_area, R.drawable.tio_main_home_selector, R.layout.tio_main_web_fragment),
    BEAUTY(1, ReminderId.BEAUTY, MainWebFragment.class, R.string.beauty_area, R.drawable.tio_main_beauty_selector, R.layout.tio_main_web_fragment),
    GOLD(2, ReminderId.GOLD, MainWebFragment.class, R.string.gold_area, R.drawable.tio_main_gold_selector, R.layout.tio_main_web_fragment),
    CHAT(3, ReminderId.CHAT, MainChatFragment.class, R.string.talk, R.drawable.tio_main_chats_selector, R.layout.tio_main_chat_fragment),
    USER(4, ReminderId.USER, MainUserFragment.class, R.string.mine, R.drawable.tio_main_user_selector, R.layout.tio_main_user_fragment);
    public final int tabIndex;
    public final int reminderId;
    public final Class<? extends MainTabFragment> clazz;
    public final int titleId;
    public final int iconId;
    public final int layoutId;

    MainTab(int index, int reminderId, Class<? extends MainTabFragment> clazz, int titleId, int iconId, int layoutId) {
        this.tabIndex = index;
        this.reminderId = reminderId;
        this.clazz = clazz;
        this.titleId = titleId;
        this.iconId = iconId;
        this.layoutId = layoutId;
    }

    public static MainTab fromReminderId(int reminderId) {
        for (MainTab value : MainTab.values()) {
            if (value.reminderId == reminderId) {
                return value;
            }
        }
        return null;
    }

    public static MainTab fromTabIndex(int tabIndex) {
        for (MainTab value : MainTab.values()) {
            if (value.tabIndex == tabIndex) {
                return value;
            }
        }
        return null;
    }
}
