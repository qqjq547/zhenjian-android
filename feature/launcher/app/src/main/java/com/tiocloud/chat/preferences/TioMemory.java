package com.tiocloud.chat.preferences;

/**
 * author : TaoWang
 * date : 2020/5/8
 * desc :
 */
public class TioMemory {

    // ====================================================================================
    // IN_CHAT_LINK_ID
    // ====================================================================================

    private static String IN_CHAT_LINK_ID = null;

    public static void setInChatLinkId(String inChatLinkId) {
        IN_CHAT_LINK_ID = inChatLinkId;
    }

    public synchronized static String getInChatLinkId() {
        return IN_CHAT_LINK_ID;
    }
}
