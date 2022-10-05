package com.tiocloud.chat.preferences;

import com.tiocloud.chat.util.PreferencesUtil;

/**
 * author : TaoWang
 * date : 2020/2/28
 * desc :
 */
public class TioPreferences {

    // ====================================================================================
    // AgreeProtectGuide
    // ====================================================================================

    private static final String KEY_AGREE_PROTECT_GUIDE = "agree_protect_guide";

    public static void saveAgreeProtectGuide(boolean agreeProtectGuide) {
        PreferencesUtil.saveBoolean(KEY_AGREE_PROTECT_GUIDE, agreeProtectGuide);
    }

    public static boolean getAgreeProtectGuide() {
        return PreferencesUtil.getBoolean(KEY_AGREE_PROTECT_GUIDE, false);
    }

    // ====================================================================================
    // AgreeProtectGuide
    // ====================================================================================

    private static final String KEY_MSG_ENCRYPTION = "key_msg_encryption";

    public static void saveMsgEncryption(boolean agreeProtectGuide) {
        PreferencesUtil.saveBoolean(KEY_MSG_ENCRYPTION, agreeProtectGuide);
    }

    public static boolean getMsgEncryption() {
        return PreferencesUtil.getBoolean(KEY_AGREE_PROTECT_GUIDE, true);
    }

}
