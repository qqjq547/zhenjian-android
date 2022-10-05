package com.watayouxiang.db.event;

import androidx.annotation.NonNull;

import com.watayouxiang.db.TioDBHelper;
import com.watayouxiang.db.utils.LoggerUtils;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/18
 *     desc   :
 * </pre>
 */
public class DBEventBus {

    // ====================================================================================
    // 发送事件
    // ====================================================================================

    public static void post_UIThread(@NonNull ChatListTableEvent event) {
        LoggerUtils.d("会话列表同步事件：" + event);
        TioDBHelper.getUIHandler().post(() -> post(event));
    }

    public static void post(Object event) {
        TioDBHelper.getEventEngine().post(event);
    }
}
