package com.tiocloud.chat.feature.session.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lzy.okgo.model.Response;
import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.view.MarqueTextView;
import com.tiocloud.chat.feature.session.common.action.model.base.BaseAction;
import com.tiocloud.chat.feature.session.common.action.util.ActionUtil;
import com.tiocloud.chat.feature.session.common.model.SessionType;
import com.tiocloud.chat.feature.session.common.mvp.SessionActivityContract;
import com.tiocloud.chat.feature.session.common.mvp.SessionActivityPresenter;
import com.tiocloud.chat.widget.titlebar.SessionTitleBar;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.GroupInfoReq;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;

import java.util.ArrayList;

/**
 * author : TaoWang
 * date : 2019-12-27
 * desc : 会话页
 * 1、定制化配置
 * 2、关闭后回退到哪个页面
 */
public abstract class SessionActivity extends TioActivity implements SessionActivityContract.View {

    public static SessionActivity activitys;
    public static void checkPermission(Runnable runnable) {
        SessionActivityPresenter.checkPermission(runnable);
    }

    @Nullable
    private SessionFragment fragment;
    private SessionTitleBar titleBar;
    private SessionActivityPresenter presenter;
    public int shownumflag=0;//显示人数开关
    public LinearLayout ll_gonggaoView;
    public MarqueTextView mar_textView;
    public static SessionActivity getInstanceActivity() {
        return activitys;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new SessionActivityPresenter(this);
        presenter.initBackgroundDrawable();

        setContentView(R.layout.team_message_activity);
        activitys=this;
        titleBar = findViewById(R.id.titleBar);
        ll_gonggaoView=findViewById(R.id.ll_gonggaoView);
        mar_textView=findViewById(R.id.mar_textView);
    }
    @Override
    protected void onStart() {
        super.onStart();
        presenter.registerScreenShotListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unregisterScreenShotListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActionUtil.onActivityResult(requestCode, resultCode, data, getActions());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ActionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, getActions());
    }

    @Override
    public void onBackPressed() {
        // 优先处理messageFragment返回事件
        if (fragment != null && fragment.onBackPressed()) {
            return;
        }

        super.onBackPressed();

        // 返回到哪个Activity
        Class<? extends Activity> backToClass = getBackToClass();
        if (backToClass != null) {
            Intent intent = new Intent();
            intent.setClass(this, backToClass);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
    }

    public <T extends SessionFragment> void replaceFragment(T fragment) {
        this.fragment = fragment;
        fragment.setContainerId(R.id.fragment_container);
        super.replaceFragment(fragment);
    }

    // ====================================================================================
    // getter
    // ====================================================================================

    @Nullable
    protected abstract Class<? extends Activity> getBackToClass();

    @Nullable
    public abstract ArrayList<BaseAction> getActions();

    @NonNull
    public abstract SessionType getSessionType();

    public SessionTitleBar getTitleBar() {
        return titleBar;
    }

    @Nullable
    public String getGroupId() {
        return null;
    }

    @Nullable
    public String getUid() {
        return null;
    }
}
