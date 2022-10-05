package com.tiocloud.session;

import android.content.Context;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/03
 *     desc   :
 * </pre>
 */
public class TioSession {
    private static Bridge bridge;

    public static void init(@NonNull Bridge bridge) {
        TioSession.bridge = bridge;
    }

    public static Bridge getBridge() {
        return bridge;
    }

    public interface Bridge {
        void startUserDetailActivity(Context context, String uid);
    }
}
