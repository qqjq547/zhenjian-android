package com.tiocloud.chat.feature.main.model;

import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.feature.main.fragment.MainWebFragment;
import com.tiocloud.chat.widget.reference.reminder.ReminderId;
import com.tiocloud.chat.feature.main.base.MainTabFragment;
import com.tiocloud.chat.feature.main.fragment.MainChatFragment;
import com.tiocloud.chat.feature.main.fragment.MainUserFragment;

public class MainTab {
//    CHAT(0, ReminderId.CHAT, MainChatFragment.class, R.string.talk, R.drawable.tio_main_chats_selector, R.layout.tio_main_chat_fragment),
//    FRIEND(1, ReminderId.FRIEND, MainFriendFragment.class, R.string.good_friend, R.drawable.tio_main_friend_selector, R.layout.tio_main_friend_fragment),
//    FIVE(2, ReminderId.FIVE, MainFiveFragment.class, R.string.shouye, R.drawable.tio_main_found_selector, R.layout.tio_main_five_fragment),
//    IDEO( MainWebFragment.class, R.string.video_area,null, R.drawable.tio_main_home_selector, R.layout.tio_main_web_fragment),
//    BEAUTY(MainWebFragment.class, R.string.beauty_area,null, R.drawable.tio_main_beauty_selector, R.layout.tio_main_web_fragment),
//    GOLD( MainWebFragment.class, R.string.gold_area,null, R.drawable.tio_main_gold_selector, R.layout.tio_main_web_fragment),
    public static final MainTab CHAT=new MainTab(MainChatFragment.class, TioApplication.sApplication.getString(R.string.talk), null,R.drawable.tio_main_chats_selector, R.layout.tio_main_chat_fragment);
    public static final MainTab USER=new MainTab(MainUserFragment.class,TioApplication.sApplication.getString(R.string.mine), null,R.drawable.tio_main_user_selector, R.layout.tio_main_user_fragment);
//    public final int reminderId;
    public Class<? extends MainTabFragment> clazz;
    public String title;
    public String[] selectUrlIcon;
    public int iconId;
    public int layoutId;
    private int tabIndex;

   public MainTab(Class<? extends MainTabFragment> clazz, String title,String[] selectUrlIcon, int iconId, int layoutId) {
        this.clazz = clazz;
        this.title = title;
        this.selectUrlIcon=selectUrlIcon;
        this.iconId = iconId;
        this.layoutId = layoutId;
    }

    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }

    public int getTabIndex() {
        return tabIndex;
    }
}
