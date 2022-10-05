package com.tiocloud.chat.feature.session.common.model;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tiocloud.chat.feature.session.common.action.model.base.BaseAction;
import com.tiocloud.chat.feature.session.common.proxy.SessionProxy;
import com.watayouxiang.androidutils.page.TioActivity;

import java.util.ArrayList;
import java.util.List;

public class SessionContainer {

    @NonNull
    public final TioActivity activity;
    public final SessionProxy proxy;
    public final View rootView;
    @NonNull
    public final List<BaseAction> actions;
    @NonNull
    public final String chatLinkId;

    public SessionContainer(@NonNull TioActivity activity, SessionProxy proxy, View rootView,
                            @Nullable List<BaseAction> actions, @NonNull String chatLinkId) {
        this.activity = activity;
        this.proxy = proxy;
        this.rootView = rootView;
        if (actions == null) {
            actions = new ArrayList<>();
        }
        this.actions = actions;
        this.chatLinkId = chatLinkId;
    }
}
