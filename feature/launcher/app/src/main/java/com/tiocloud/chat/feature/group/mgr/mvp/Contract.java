package com.tiocloud.chat.feature.group.mgr.mvp;

import android.widget.CompoundButton;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.response.ForbiddenUserListResp;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;

public interface Contract {
    interface View extends BaseView {
        void resetUI();

        String getGroupId();

        void onGroupInfoResp(GroupInfoResp groupInfo);

        void onForbiddenUserListResp(ForbiddenUserListResp resp);
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }

        public abstract void reqModifyInviteSwitch(final boolean isChecked, String groupId, final DataProxy<String> proxy);

        public abstract void getGroupInfo(String userFlag, String groupId, final DataProxy<GroupInfoResp> proxy);

    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view, boolean registerEvent) {
            super(model, view, registerEvent);
        }

        public abstract void init();

        public abstract void refresh();

        public abstract void toggleInviteMemberSwitch(boolean isChecked, CompoundButton compoundButton);

        public abstract void toggleInviteApplySwitch(boolean isChecked, CompoundButton compoundButton);

        public abstract void toggleAddFriendSwitch(boolean isChecked, CompoundButton compoundButton);

        public abstract void groupSystemNotificationReg(boolean isChecked, CompoundButton compoundButton);
        public abstract void groupSizeDisplayReq(boolean isChecked, CompoundButton compoundButton);

        public abstract void groupMemberIsShowReq(boolean isChecked, CompoundButton compoundButton);
        public abstract void groupVipClearReq(boolean isChecked, CompoundButton compoundButton);
        public abstract void groupistuiqunReq(boolean isChecked, CompoundButton compoundButton);

        public abstract void groupAnnouncementDisplayReg(boolean isChecked, CompoundButton compoundButton);

    }
}
