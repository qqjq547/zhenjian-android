package com.watayouxiang.db.prefernces;

import com.watayouxiang.httpclient.preferences.HttpPreferences;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/03
 *     desc   : 数据库 配置文件
 * </pre>
 */
public class TioDBPreferences extends PreferencesUtils {

    // ====================================================================================
    // currUserId
    // ====================================================================================

    public static long getCurrUid() {
        return HttpPreferences.getCurrUid();
    }
    public static void clearCurrUid(){
        HttpPreferences.clearCurrUid();
    }
}
