package com.tiocloud.chat.feature.group.mgr.mvp;

import android.util.Log;
import android.widget.CompoundButton;

import com.tiocloud.chat.feature.forbidden.ForbiddenMvpPresenter;
import com.watayouxiang.androidutils.listener.TioErrorCallback;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.GroupIsTuiQunReq;
import com.watayouxiang.httpclient.model.request.GroupMemberIsShowReq;
import com.watayouxiang.httpclient.model.request.GroupVipClearReq;
import com.watayouxiang.httpclient.model.request.ModifyFriendFlagReq;
import com.watayouxiang.httpclient.model.request.ModifyReviewReq;
import com.watayouxiang.httpclient.model.request.GroupSystemNotificationReq;
import com.watayouxiang.httpclient.model.request.GroupAnnouncementDisplayReq;
import com.watayouxiang.httpclient.model.request.GroupSizeDisplayReq;
import com.watayouxiang.httpclient.model.response.ForbiddenUserListResp;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;

public class Presenter extends Contract.Presenter {
    private final ForbiddenMvpPresenter forbiddenPresenter = new ForbiddenMvpPresenter();

    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void detachView() {
        super.detachView();
        forbiddenPresenter.detachView();
    }

    @Override
    public void init() {
        getView().resetUI();
    }

    @Override
    public void refresh() {
        // 获取群聊信息
        getModel().getGroupInfo("1", getView().getGroupId(), new BaseModel.DataProxy<GroupInfoResp>() {
            @Override
            public void onSuccess(GroupInfoResp groupInfo) {
                final GroupInfoResp.GroupUser groupUser = groupInfo.groupuser;
                final GroupInfoResp.Group group = groupInfo.group;
                if (groupUser == null || group == null) {
                    TioToast.showShort("groupUser or group is null");
                    return;
                }
                getView().onGroupInfoResp(groupInfo);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
        // 禁言列表
        forbiddenPresenter.getModel().reqForbiddenUserList("1", getView().getGroupId(), null, new TioCallback<ForbiddenUserListResp>() {
            @Override
            public void onTioSuccess(ForbiddenUserListResp resp) {
                getView().onForbiddenUserListResp(resp);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void toggleInviteMemberSwitch(final boolean isChecked, final CompoundButton compoundButton) {
        getModel().reqModifyInviteSwitch(isChecked, getView().getGroupId(), new BaseModel.DataProxy<String>() {

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
//                Log.d("====开启成员邀请===","==="+msg);

                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void toggleInviteApplySwitch(boolean isChecked, CompoundButton compoundButton) {
        new ModifyReviewReq(isChecked ? "1" : "2", getView().getGroupId()).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void toggleAddFriendSwitch(boolean isChecked, CompoundButton compoundButton) {
        String groupId = getView().getGroupId();
        new ModifyFriendFlagReq(isChecked ? "1" : "2", groupId).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void groupSystemNotificationReg(boolean isChecked, CompoundButton compoundButton2) {
        String groupId = getView().getGroupId();
        new GroupSystemNotificationReq(isChecked ? "1" : "2", groupId).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton2.setChecked(!isChecked);

            }

        });

    }

    @Override
    public void groupSizeDisplayReq(boolean isChecked, CompoundButton compoundButton) {
        new GroupSizeDisplayReq(isChecked ? "1" : "2", getView().getGroupId()).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void groupMemberIsShowReq(boolean isChecked, CompoundButton compoundButton) {
        new GroupMemberIsShowReq(isChecked ? "1" : "2", getView().getGroupId()).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void groupVipClearReq(boolean isChecked, CompoundButton compoundButton) {
        new GroupVipClearReq(isChecked ? "1" : "2", getView().getGroupId()).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void groupistuiqunReq(boolean isChecked, CompoundButton compoundButton) {
        new GroupIsTuiQunReq(isChecked ? "1" : "2", getView().getGroupId()).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }


    @Override
    public void groupAnnouncementDisplayReg(boolean isChecked, CompoundButton compoundButton) {

        new GroupAnnouncementDisplayReq(isChecked ? "1" : "2", getView().getGroupId()).setCancelTag(this).post(new TioErrorCallback<Object>() {
            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }
}
