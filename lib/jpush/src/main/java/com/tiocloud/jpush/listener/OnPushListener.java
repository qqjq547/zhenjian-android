package com.tiocloud.jpush.listener;

import android.content.Context;

import cn.jpush.android.api.NotificationMessage;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/09/28
 *     desc   :
 * </pre>
 */
public interface OnPushListener {

    void onNotificationClick(Context context, String chatLinkId);
}
