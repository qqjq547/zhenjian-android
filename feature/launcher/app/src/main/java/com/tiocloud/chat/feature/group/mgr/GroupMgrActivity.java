package com.tiocloud.chat.feature.group.mgr;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.databinding.TioGroupMgrActivityBinding;
import com.tiocloud.chat.feature.forbidden.ForbiddenMvpPresenter;
import com.tiocloud.chat.feature.group.mgr.mvp.Contract;
import com.tiocloud.chat.feature.group.mgr.mvp.Presenter;
import com.tiocloud.chat.feature.group.silent.SilentMgrActivity;
import com.watayouxiang.androidutils.listener.OnSimpleCheckedChangeListener;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.httpclient.model.response.ForbiddenUserListResp;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/05
 *     desc   : 群管理
 * </pre>
 */
public class GroupMgrActivity extends BindingActivity<TioGroupMgrActivityBinding> implements Contract.View {

    private Presenter presenter;
    private final ForbiddenMvpPresenter forbiddenPresenter = new ForbiddenMvpPresenter();

    public static void start(Context context, String groupId) {
        Intent starter = new Intent(context, GroupMgrActivity.class);
        starter.putExtra(TioExtras.EXTRA_GROUP_ID, groupId);
        context.startActivity(starter);
    }

    public String getGroupId() {
        return getIntent().getStringExtra(TioExtras.EXTRA_GROUP_ID);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.tio_group_mgr_activity;
    }

    @Override
    protected Boolean statusBar_lightMode() {
        return true;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.WHITE;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBar;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new Presenter(this);
        presenter.init();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        forbiddenPresenter.detachView();
    }

    @Override
    public void resetUI() {
        // 开启成员邀请
        binding.switchToggleInviteMember.setChecked(false);
        binding.switchToggleInviteMember.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleInviteMemberSwitch(isChecked, compoundButton);
            }
        });
        // 邀请审核
        binding.switchToggleInviteApply.setChecked(false);
        binding.switchToggleInviteApply.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleInviteApplySwitch(isChecked, compoundButton);
            }
        });
        binding.switchXitongtongzhi.setChecked(false);
        binding.switchQunrenshu.setChecked(false);
        binding.switchJinqungonggao.setChecked(false);

        //是否允许成员退群
        binding.switchTuiqun.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.groupistuiqunReq(isChecked, compoundButton);


            }
        });
        //群内系统通知
        binding.switchXitongtongzhi.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.groupSystemNotificationReg(isChecked, compoundButton);


            }
        });
        //显示群人数
        binding.switchQunrenshu.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.groupSizeDisplayReq(isChecked, compoundButton);

            }
        });
        //群成员显示
        binding.switchQunchengyuan.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.groupMemberIsShowReq(isChecked, compoundButton);

            }
        });
        //清理15天不上线会员
        binding.switchVipClear.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.groupVipClearReq(isChecked, compoundButton);

            }
        });
        //进群显示公告
        binding.switchJinqungonggao.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.groupAnnouncementDisplayReg(isChecked, compoundButton);


            }
        });
        // 禁言列表
        binding.tvBanListSubtitle.setText("");
        // 群体禁言
        binding.switchBanAll.setChecked(false);
        binding.switchBanAll.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                forbiddenPresenter.toggleSwitch_ForbiddenGroupMember(getGroupId(), compoundButton, isChecked);
            }
        });
        // 禁言名单
        binding.rlBanList.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                SilentMgrActivity.start(getActivity(), getGroupId());
            }
        });
        // 群内互加好友
        binding.switchToggleAddFriend.setChecked(false);
        binding.switchToggleAddFriend.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleAddFriendSwitch(isChecked, compoundButton);
            }
        });
    }

    @Override
    public void onGroupInfoResp(GroupInfoResp groupInfo) {
        final GroupInfoResp.Group group = groupInfo.group;
        GroupInfoResp.GroupUser groupUser = groupInfo.groupuser;
        if (groupUser == null || group == null) return;

        // 邀请审核
        binding.switchToggleInviteApply.setChecked(group.joinmode == 1);
        // 成员邀请开关
        binding.switchToggleInviteMember.setChecked(group.isAllowInviteMember());
        // 全体禁言开关
        binding.switchBanAll.setChecked(group.isForbiddenMemberTalk());
        // 群内互加好友
        boolean openAddFriend = group.friendflag == 1;
        binding.switchToggleAddFriend.setChecked(openAddFriend);
        binding.switchXitongtongzhi.setChecked(group.sysnoticeflag==1);
        binding.switchQunrenshu.setChecked(group.shownumflag==1);
        binding.switchJinqungonggao.setChecked(group.goinshownoticeflag==1);
        binding.switchQunchengyuan.setChecked(group.groupmembershowflag==1);
        binding.switchVipClear.setChecked(group.clearmemberflag==1);
        binding.switchTuiqun.setChecked(group.leaveflag==1);



    }

    @Override
    public void onForbiddenUserListResp(ForbiddenUserListResp resp) {
        int totalRow = resp.getTotalRow();
        // 禁言人数
        binding.tvBanListSubtitle.setText(StringUtils.format("%s人", totalRow));
    }
}
