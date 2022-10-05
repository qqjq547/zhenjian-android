package com.tiocloud.chat.feature.session.group;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.lzy.okgo.model.Response;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.base.BaseConstants;
import com.tiocloud.chat.baseNewVersion.utils2.SPUtilsNew;
import com.tiocloud.chat.feature.group.info.GroupInfoActivity;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.feature.session.common.SessionActivity;
import com.tiocloud.chat.feature.session.common.action.model.base.BaseAction;
import com.tiocloud.chat.feature.session.common.model.SessionExtras;
import com.tiocloud.chat.feature.session.common.model.SessionType;
import com.tiocloud.chat.feature.session.group.customization.GroupActions;
import com.tiocloud.chat.feature.session.group.fragment.GroupSessionFragment;
import com.tiocloud.chat.feature.session.group.mvp.GroupActivityContract;
import com.tiocloud.chat.feature.session.group.mvp.GroupActivityPresenter;
import com.tiocloud.chat.util.ScreenUtil;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.chat.util.TimeUtil;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.SpanUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.confirm.TioConfirmDialog;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.GroupInfoReq;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;
import com.watayouxiang.imclient.TioIMClient;
import com.watayouxiang.imclient.model.body.wx.WxChatItemInfoResp;

import java.util.ArrayList;
import java.util.Locale;

/**
 * author : TaoWang
 * date : 2019-12-30
 * desc : 群聊
 */
public class GroupSessionActivity extends SessionActivity implements GroupActivityContract.View {

    public static void active(@NonNull Context context, @NonNull String groupId) {
        start(context, MainActivity.class, groupId, null);
    }

    public static void enter(@NonNull Context context, @NonNull String chatLinkId) {
        start(context, MainActivity.class, null, chatLinkId);
    }

    public static void start(@NonNull Context context,
                             @Nullable Class<? extends Activity> backToClass,
                             @Nullable String groupId,
                             @Nullable String chatLinkId) {
        checkPermission(() -> {
            boolean handshake = TioIMClient.getInstance().isHandshake();
//            Log.d("=====进入群聊页：groupId = %s, chatLinkId = %s, handshake = %b",groupId+"==="+ chatLinkId+"===="+ handshake);

            if (!handshake) {
                TioToast.showShort("当前网络异常");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(SessionExtras.EXTRA_BACK_TO_CLASS, backToClass);
            intent.putExtra(SessionExtras.EXTRA_GROUP_ID, groupId);
            intent.putExtra(SessionExtras.EXTRA_CHAT_LINK_ID, chatLinkId);
            intent.putExtra(SessionExtras.EXTRA_ACTIONS, GroupActions.get());
            intent.setClass(context, GroupSessionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        });
    }

    // ====================================================================================
    // param
    // ====================================================================================

    @Nullable
    public String getGroupId() {
        return getIntent().getStringExtra(SessionExtras.EXTRA_GROUP_ID);
    }

    @Override
    public void setGroupId(String groupId) {
        getIntent().putExtra(SessionExtras.EXTRA_GROUP_ID, groupId);
    }

    @Nullable
    public String getChatLinkId() {
        return getIntent().getStringExtra(SessionExtras.EXTRA_CHAT_LINK_ID);
    }

    @Nullable
    @Override
    protected Class<? extends Activity> getBackToClass() {
        return (Class<? extends Activity>) getIntent().getSerializableExtra(SessionExtras.EXTRA_BACK_TO_CLASS);
    }

    @Nullable
    @Override
    public ArrayList<BaseAction> getActions() {
        return (ArrayList<BaseAction>) getIntent().getSerializableExtra(SessionExtras.EXTRA_ACTIONS);
    }

    @NonNull
    @Override
    public SessionType getSessionType() {
        return SessionType.GROUP;
    }

    // ====================================================================================
    // init
    // ====================================================================================

    private GroupActivityContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new GroupActivityPresenter(this);
        presenter.init();
    }
    private float fontSizeScale;
    @Override
    public Resources getResources() {
        Resources res =super.getResources();
        Configuration config = res.getConfiguration();
        if(fontSizeScale>0.5){
            config.fontScale= fontSizeScale;//1 设置正常字体大小的倍数
        }
        res.updateConfiguration(config,res.getDisplayMetrics());
        return res;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        presenter.init();
    }

    @Override
    public void initUI() {
        setTitle(R.string.session_group);
        fontSizeScale = (float) SPUtilsNew.get(this, BaseConstants.SP_FontScale, 0.0f);

        String groupId = getGroupId();
        String chatLinkId = getChatLinkId();

        if (chatLinkId != null) {
            presenter.enterGroup(chatLinkId);
        } else if (groupId != null) {
            presenter.activeGroup(groupId);
        } else {
            TioToast.showShort("初始化失败");
            finish();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.getChatInfo();
    }

    @Override
    public void onChatInfoResp(WxChatItemInfoResp resp) {
        WxChatItemInfoResp.DataBean data = resp.data;
        // 标题
        getTitleBar().setTitle(data.name);
//        Log.d("==标题==","==="+data.shownumflag);


        // menu
        String groupId = data.bizid;
        boolean isEnableChat = data.linkflag == 1;
        if (isEnableChat) {
            getTitleBar().getMoreBtn().setVisibility(View.VISIBLE);
            getTitleBar().getMoreBtn().setOnClickListener(view -> GroupInfoActivity.start(getActivity(), groupId));
        } else {
            getTitleBar().getMoreBtn().setVisibility(View.INVISIBLE);
        }
        topGonggaoISShow(groupId, data.joinnum );
    }
    private void topGonggaoISShow(String groupId,int Renshu){
//        Log.d("====群聊id==","=="+groupId);
        GroupInfoReq groupInfoReq = new GroupInfoReq("1",groupId);
        TioHttpClient.get(this, groupInfoReq, new TaoCallback<BaseResp<GroupInfoResp>>() {
            @Override
            public void onSuccess(Response<BaseResp<GroupInfoResp>> response) {
                GroupInfoResp groupInfo = response.body().getData();
                if (groupInfo != null) {
                    final GroupInfoResp.Group group = groupInfo.group;
                    if (groupInfo == null || group == null) return;
                    mar_textView.setText(group.notice+"");
                    //群公告 / 人数显示
                    if(group.goinshownoticeflag==1){
                        if(TextUtils.isEmpty(group.notice)){
                            ll_gonggaoView.setVisibility(View.GONE);

                        }else {
                            ll_gonggaoView.setVisibility(View.VISIBLE);
                        }
                    }else {
                        ll_gonggaoView.setVisibility(View.GONE);
                    }
                    shownumflag=group.shownumflag;

                    if(shownumflag==1){
                        getTitleBar().setSubtitle("(" + Renshu+ ")");
                    }else {
                        getTitleBar().setSubtitle("");

                    }
                }
            }
        });

    }

    @Override
    public void initFragment(String chatLinkId, String groupId) {
        GroupSessionFragment fragment = GroupSessionFragment.create(chatLinkId, groupId);
        this.replaceFragment(fragment);
        GroupInfoReq groupInfoReq = new GroupInfoReq("1",groupId);
        TioHttpClient.get(this, groupInfoReq, new TaoCallback<BaseResp<GroupInfoResp>>() {
            @Override
            public void onSuccess(Response<BaseResp<GroupInfoResp>> response) {
                GroupInfoResp groupInfo = response.body().getData();
                if (groupInfo != null) {
                    final GroupInfoResp.Group group = groupInfo.group;

                    //群公告
                    if(group!=null&&group.goinshownoticeflag==1){
                        if(!TextUtils.isEmpty(group.notice)){
                            String noticetime = group.noticetime;
                            String time = TimeUtil.dateLong2String(TimeUtil.dateString2Long(noticetime), "yyyy-MM-dd");
                            String formatTime = TextUtils.isEmpty(time) ? "" : String.format(Locale.getDefault(), "（%s）", time);
                            SpannableStringBuilder format = SpanUtils
                                    // 标题
                                    .getBuilder("群公告")
                                    .setTexSize(ScreenUtil.sp2px(18))
                                    // 时间
                                    .append(formatTime)
                                    .setForegroundColor(Color.parseColor("#FF909090"))
                                    .setTexSize(ScreenUtil.sp2px(16))
                                    // 内容
                                    .append(String.format(Locale.getDefault(), "\n\n%s", StringUtil.nonNull(group.notice)))
                                    .setTexSize(ScreenUtil.sp2px(16))
                                    .create();

                            new TioConfirmDialog(format, Gravity.START, new TioConfirmDialog.OnConfirmListener() {
                                @Override
                                public void onConfirm(View view, TioConfirmDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show_unCancel(getActivity());
                        }

                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
