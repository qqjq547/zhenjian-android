package com.tiocloud.chat.feature.settings.mvp;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.widget.SwitchCompat;

import com.tiocloud.account.mvp.logout.LogoutContract;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.tiocloud.chat.baseNewVersion.FontSizeActivity;
import com.tiocloud.chat.baseNewVersion.utils2.IntentUtilsNew;
import com.tiocloud.chat.feature.account.pwd.ModifyPwdActivity;
import com.tiocloud.chat.feature.curr.modify.ModifyActivity;
import com.tiocloud.chat.feature.settings.SettingsActivity;
import com.tiocloud.chat.preferences.TioPreferences;
import com.watayouxiang.androidutils.listener.OnSimpleCheckedChangeListener;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.utils.ClickUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UpdateRemindReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.imclient.utils.DeviceUtils;

import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020-02-19
 * desc :
 */
public class SettingsPresenter extends SettingsContract.Presenter {

    private final LogoutPresenter logoutPresenter;

    public SettingsPresenter(SettingsContract.View view) {
        super(new SettingsModel(), view);
        logoutPresenter = new LogoutPresenter(new LogoutContract.View() {
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        logoutPresenter.detachView();
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    public void init() {
        final SettingsActivity.ViewHolder viewHolder = getView().getViewHolder();
        if (viewHolder == null) return;
        // 退出登录
        viewHolder.tv_logoutBtn.setOnClickListener(v -> logoutPresenter.showLogoutDialog(getView().getActivity()));
        // 清除历史消息
        viewHolder.rl_clearHistoryMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TioToast.showShort("开发中...");
            }
        });
        //账号与安全
        viewHolder.rl_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ClickUtils.isViewSingleClick(v)) return;
//                RetrievePwdActivity.start(getView().getActivity());
                ModifyPwdActivity.start(getView().getActivity());
//                AccountActivity.start(getView().getActivity());

            }
        });
        // 关于城市客栈
        viewHolder.tv_version.setText(String.format(Locale.getDefault(), "v %s ", DeviceUtils.getAppVersion(getView().getActivity())));
//        viewHolder.rl_version.setOnClickListener(v -> AboutAppActivity.start(getView().getActivity()));
//        viewHolder.rl_version.setOnLongClickListener(view -> {
//            String outApkTime = getModel().getOutApkTime(view.getContext());
//            if (!TextUtils.isEmpty(outApkTime)) {
//                TioToast.showShort("打包时间：" + outApkTime);
//                return true;
//            }
//            return false;
//        });
        // 反馈
        viewHolder.rl_feedback.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                ModifyActivity.start_advise(getView().getActivity());
            }
        });
        //字体大小
        viewHolder.rl_fontSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtilsNew.toActivity(getView().getActivity(),new Intent(getView().getActivity(), FontSizeActivity.class));

            }
        });
        // 初始化 ui
        viewHolder.switch_verifyAddFriend.setEnabled(false);
        viewHolder.switch_searchMeAuth.setEnabled(false);
        viewHolder.switch_msgRemind.setEnabled(false);
        viewHolder.switch_encryption.setEnabled(false);
        // 加载数据
        loadRemoteData(viewHolder);
    }

    private void loadRemoteData(final SettingsActivity.ViewHolder viewHolder) {
        getModel().requestCurrUserInfo(new BaseModel.DataProxy<UserCurrResp>() {
            @Override
            public void onSuccess(UserCurrResp resp) {
                // 加我好友时需要验证
                boolean verifyAddFriend = resp.fdvalidtype == 1;
                viewHolder.switch_verifyAddFriend.setEnabled(true);
                viewHolder.switch_verifyAddFriend.setChecked(verifyAddFriend);
                viewHolder.switch_verifyAddFriend.setOnCheckedChangeListener(mSwitchCheckedChangeListener);
                // 允许别人搜索到我
                boolean searchMeAuth = resp.searchflag == 1;
                viewHolder.switch_searchMeAuth.setEnabled(true);
                viewHolder.switch_searchMeAuth.setChecked(searchMeAuth);
                viewHolder.switch_searchMeAuth.setOnCheckedChangeListener(mSwitchCheckedChangeListener);
                // 消息提醒开关
                boolean isMsgRemind = resp.msgremindflag == 1;
                viewHolder.switch_msgRemind.setEnabled(true);
                viewHolder.switch_msgRemind.setChecked(isMsgRemind);
                viewHolder.switch_msgRemind.setOnCheckedChangeListener(mSwitchCheckedChangeListener);
                // 消息加密
                viewHolder.switch_encryption.setEnabled(true);
                viewHolder.switch_encryption.setChecked(TioPreferences.getMsgEncryption());
                viewHolder.switch_encryption.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
                    @Override
                    public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        super.onUserCheckedChanged(compoundButton, isChecked);
                        TioPreferences.saveMsgEncryption(isChecked);
                        if (isChecked) {
                            TioToast.showShort("你已开启消息加密");
                        } else {
                            TioToast.showShort("你已取消消息加密");
                        }
                    }
                });
            }
        });
    }

    private final CompoundButton.OnCheckedChangeListener mSwitchCheckedChangeListener = new OnSimpleCheckedChangeListener() {
        @Override
        public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            super.onUserCheckedChanged(compoundButton, isChecked);

            final SettingsActivity.ViewHolder viewHolder = getView().getViewHolder();
            if (viewHolder == null) return;

            if (compoundButton == viewHolder.switch_verifyAddFriend) {
                updateValidReq(isChecked, viewHolder.switch_verifyAddFriend);
            } else if (compoundButton == viewHolder.switch_searchMeAuth) {
                updateSearchFlagReq(isChecked, viewHolder.switch_searchMeAuth);
            } else if (compoundButton == viewHolder.switch_msgRemind) {
                updateMsgRemindFlagReq(isChecked, viewHolder.switch_msgRemind);
            }
        }
    };

    // ====================================================================================
    // 请求操作
    // ====================================================================================

    private void updateMsgRemindFlagReq(boolean isChecked, CheckBox switch_msgRemind) {
        // 消息提醒开关
        UpdateRemindReq updateRemindReq = new UpdateRemindReq(isChecked ? "1" : "2");
        updateRemindReq.setCancelTag(this);
        updateRemindReq.post(new TioCallback<Void>() {
            @Override
            public void onTioSuccess(Void aVoid) {
                try {
                    int remindflag = Integer.parseInt(updateRemindReq.getRemindflag());
                    // 更新当前用户的消息开关状态
                    CurrUserTableCrud.curr_update_msgremindflag(remindflag);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onTioError(String msg) {
                switch_msgRemind.setChecked(!isChecked);
            }
        });
    }

    private void updateSearchFlagReq(final boolean isChecked, final CheckBox switch_searchMeAuth) {
        // 允许别人搜索到我
        getModel().updateSearchFlagReq(isChecked, new BaseModel.DataProxy<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                switch_searchMeAuth.setChecked(!isChecked);
            }
        });
    }

    private void updateValidReq(final boolean isChecked, final CheckBox switch_verifyAddFriend) {
        // 加我好友时需要验证
        getModel().updateValidReq(isChecked, new BaseModel.DataProxy<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                switch_verifyAddFriend.setChecked(!isChecked);
            }
        });
    }
}
