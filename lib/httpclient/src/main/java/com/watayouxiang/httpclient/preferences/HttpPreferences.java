package com.watayouxiang.httpclient.preferences;

import android.app.backup.SharedPreferencesBackupHelper;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.response.ConfigResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.httpclient.utils.PreferencesUtil;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/25
 *     desc   : 配置文件
 * </pre>
 */
public class HttpPreferences extends PreferencesUtils {

    private static final String KEY_BASE_URL = "base_url";
    private static final String KEY_RES_URL = "res_url";
    private static final String KEY_SESSION_COOKIE_NAME = "session_cookie_name";
    private static final String KEY_CURR_USER_ID = "curr_user_id";
    private static final String KEY_IM_HEARTBEAT_TIMEOUT = "im_heartbeat_timeout";

    // ====================================================================================
    // 数据处理
    // ====================================================================================

    public static <Data> void handleResponse(BaseResp<Data> resp) {
        boolean ok = resp.isOk();
        if (ok) {
            Data data = resp.getData();
            if (data instanceof UserCurrResp) {
                UserCurrResp userCurrResp = (UserCurrResp) data;
                // 存储当前uid
                HttpPreferences.saveCurrUid(userCurrResp.id);
            } else if (data instanceof ConfigResp) {
                ConfigResp configResp = (ConfigResp) data;
                // 存储资源服务器地址
//                HttpPreferences.saveResUrl(configResp.res_server);

                // 存储cookieName
                HttpPreferences.saveSessionCookieName(configResp.session_cookie_name);
                // 存储心跳超时时长
                HttpPreferences.saveImHeartbeatTimeout(configResp.im_heartbeat_timeout);
                PreferencesUtil.saveString("app_find_page_base_list", GsonUtils.toJson(configResp.app_find_page_base_list));
            }
        }
    }

    // ====================================================================================
    // baseUrl
    // ====================================================================================

    public static void saveBaseUrl(String baseUrl) {
        saveString(KEY_BASE_URL, baseUrl);
    }

    public static String getBaseUrl() {
        return getString(KEY_BASE_URL, null);
    }

    // ====================================================================================
    // resUrl
    // ====================================================================================

    public static void saveResUrl(String resServer) {
        saveString(KEY_RES_URL, resServer);
    }

    public static String getResUrl() {
        return getString(KEY_RES_URL, null);
    }

    // ====================================================================================
    // sessionCookieName
    // ====================================================================================

    private static void saveSessionCookieName(String sessionCookieName) {
        saveString(KEY_SESSION_COOKIE_NAME, sessionCookieName);
    }

    public static String getSessionCookieName() {
        return getString(KEY_SESSION_COOKIE_NAME, null);
    }

    // ====================================================================================
    // currUserId
    // ====================================================================================

    public static void saveCurrUid(long uid) {
        saveLong(KEY_CURR_USER_ID, uid);
    }

    public static long getCurrUid() {
        return getLong(KEY_CURR_USER_ID, -1);
    }
    public static void clearCurrUid() {
         clear(KEY_CURR_USER_ID);
    }

    // ====================================================================================
    // im_heartbeat_timeout
    // ====================================================================================

    private static void saveImHeartbeatTimeout(long im_heartbeat_timeout) {
        saveLong(KEY_IM_HEARTBEAT_TIMEOUT, im_heartbeat_timeout);
    }

    public static long getImHeartbeatTimeout() {
        return getLong(KEY_IM_HEARTBEAT_TIMEOUT, -1);
    }
}
