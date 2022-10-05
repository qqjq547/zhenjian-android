package com.tiocloud.chat.feature.user.detail.mvp;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.page.BaseActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.request.GroupInfoReq;
import com.watayouxiang.httpclient.model.request.IsFriendReq;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;
import com.watayouxiang.httpclient.preferences.HttpPreferences;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc :
 */
public class UserPresenter extends UserContract.Presenter {
    public UserPresenter(UserContract.View view) {
        super(new UserModel(), view);
    }

    @Override
    public void updateUI(final String uid) {
        getModel().isFriend(Integer.parseInt(uid), new BaseModel.DataProxy<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                getView().onIsFriendResp(integer == 1);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }

    // ====================================================================================
    // 访问限制
    // ====================================================================================

    public static void checkStart(BaseActivity activity, String groupId, String toUid) {
        // 如果是自己，直接访问
        if (StringUtils.equals(HttpPreferences.getCurrUid() + "", toUid)) {
            UserDetailActivity.start(activity, toUid);
            return;
        }

        // 如果 群互相加好友开关 关闭
        // 那么只有自己的好友可以访问
        new GroupInfoReq("1", groupId).setCancelTag(activity).get(new TioSuccessCallback<GroupInfoResp>() {
            @Override
            public void onTioSuccess(GroupInfoResp resp) {
                checkStart(activity, toUid, resp);
            }
        });
    }

    public static void checkStart(BaseActivity activity, String toUid, GroupInfoResp groupInfo) {
        // 如果是自己，直接访问
        if (StringUtils.equals(HttpPreferences.getCurrUid() + "", toUid)) {
            UserDetailActivity.start(activity, toUid);
            return;
        }

        // 如果 群互相加好友开关 关闭
        // 那么只有自己的好友可以访问
        final GroupInfoResp.GroupUser groupUser = groupInfo.groupuser;
        final GroupInfoResp.Group group = groupInfo.group;
        if (groupUser == null || group == null) return;

        boolean allowAddFriend = group.friendflag == 1;
        int groupRole = groupUser.grouprole;// 群成员角色：1：群主；2：成员；3：管理员

        if (!allowAddFriend && groupRole == 2) {
            new IsFriendReq(toUid).setCancelTag(activity).get(new TioSuccessCallback<Integer>() {
                @Override
                public void onTioSuccess(Integer integer) {
                    if (integer == 1) UserDetailActivity.start(activity, toUid);
                }
            });
        } else {
            UserDetailActivity.start(activity, toUid);
        }
    }
}
