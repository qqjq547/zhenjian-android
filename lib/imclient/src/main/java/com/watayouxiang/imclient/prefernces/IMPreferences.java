package com.watayouxiang.imclient.prefernces;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/25
 *     desc   : 配置文件
 * </pre>
 */
public class IMPreferences extends PreferencesUtils {

    // ====================================================================================
    // hand_shake_key
    // ====================================================================================

    private static final String KEY_HAND_SHAKE_KEY = "hand_shake_key";

    public static void saveHandShakeKey(String handShakeKey) {
        saveString(KEY_HAND_SHAKE_KEY, handShakeKey);
    }

    public static String getHandShakeKey() {
        return getString(KEY_HAND_SHAKE_KEY, null);
    }

    // ====================================================================================
    // hand_shake_key
    // ====================================================================================

    private static final String KEY_IP = "key_ip";

    public static void saveIp(String handShakeKey) {
        saveString(KEY_IP, handShakeKey);
    }

    public static String getIp() {
        return getString(KEY_IP, null);
    }

    // ====================================================================================
    // json_format_log
    // ====================================================================================

    private static final String KEY_JSON_FORMAT_LOG = "key_json_format_log";

    public static void saveJsonFormatLog(boolean jsonFormatLog) {
        saveBoolean(KEY_JSON_FORMAT_LOG, jsonFormatLog);
    }

    public static boolean getJsonFormatLog() {
        return getBoolean(KEY_JSON_FORMAT_LOG, true);
    }

}
