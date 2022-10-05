package com.tiocloud.chat.feature.group.info.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.dialognew.CommonTextInputDialog;
import com.tiocloud.chat.constant.TioExtras;
import com.tiocloud.chat.feature.curr.modify.ModifyActivity;
import com.tiocloud.chat.feature.curr.modify.model.ModifyType;
import com.tiocloud.chat.feature.group.info.GroupInfoActivity;
import com.tiocloud.chat.feature.group.info.fragment.adapter.MemberItem;
import com.tiocloud.chat.feature.group.info.fragment.adapter.MemberListAdapter;
import com.tiocloud.chat.feature.group.info.fragment.mvp.FragmentGroupInfoContract;
import com.tiocloud.chat.feature.group.info.fragment.mvp.FragmentGroupInfoPresenter;
import com.tiocloud.chat.feature.group.member.GroupMemberActivity;
import com.tiocloud.chat.feature.group.mgr.GroupMgrActivity;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.tiocloud.chat.feature.user.detail.mvp.UserPresenter;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.androidutils.listener.OnSimpleCheckedChangeListener;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.page.TioFragment;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.ModifyNameReq;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;
import com.watayouxiang.httpclient.model.response.GroupUserListResp;
import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;
import com.watayouxiang.qrcode.feature.qrcode_group.GroupQRCodeActivity;

import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class GroupInfoFragment extends TioFragment implements FragmentGroupInfoContract.View {

    private FragmentGroupInfoPresenter presenter;
    private ViewHolder holder;
    private MemberListAdapter memberListAdapter;
    private GroupInfoResp mGroupInfoResp = null;

    public static GroupInfoFragment create(String groupId) {
        GroupInfoFragment fragment = new GroupInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TioExtras.EXTRA_GROUP_ID, groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public String getGroupId() {
        return getArguments().getString(TioExtras.EXTRA_GROUP_ID);
    }

    @Override
    public String getChatLinkId() {
        String groupId = getGroupId();
        int _groupId = Integer.parseInt(groupId);
        return String.valueOf(-_groupId);
    }

    public static class ViewHolder {
        public TextView tv_groupName;
        public TextView tv_viewAllMember;
        public LinearLayout ll_viewAllMember;
        public TextView tv_groupIntro;
        public TextView tv_groupNotice;
        public RecyclerView rv_memberList;
        public RelativeLayout rl_deleteChatRecord;
        public RelativeLayout rl_complaint;
        public LinearLayout ll_groupName;
        public LinearLayout ll_groupNotice;
        public LinearLayout ll_groupIntro;
        public ImageView iv_arrow_groupName;
        public LinearLayout ll_groupOwner;
        public TextView tv_groupOwner;
        public LinearLayout ll_groupNick;
        public TextView tv_groupNick;
        public RelativeLayout rl_QRCode;
        public RelativeLayout rl_groupMgr;
        public View v_groupMgrBottom;
        public CheckBox switch_topChat;
        public CheckBox switch_DND;

        private ViewHolder(View v) {
            tv_groupName = v.findViewById(R.id.tv_groupName);
            tv_viewAllMember = v.findViewById(R.id.tv_viewAllMember);
            ll_viewAllMember = v.findViewById(R.id.ll_viewAllMember);
            tv_groupIntro = v.findViewById(R.id.tv_groupIntro);
            tv_groupNotice = v.findViewById(R.id.tv_groupNotice);
            rv_memberList = v.findViewById(R.id.rv_memberList);
            rl_deleteChatRecord = v.findViewById(R.id.rl_deleteChatRecord);
            rl_complaint = v.findViewById(R.id.rl_complaint);
            ll_groupName = v.findViewById(R.id.ll_groupName);
            ll_groupNotice = v.findViewById(R.id.ll_groupNotice);
            ll_groupIntro = v.findViewById(R.id.ll_groupIntro);
            iv_arrow_groupName = v.findViewById(R.id.iv_arrow_groupName);
            ll_groupOwner = v.findViewById(R.id.ll_groupOwner);
            tv_groupOwner = v.findViewById(R.id.tv_groupOwner);
            ll_groupNick = v.findViewById(R.id.ll_groupNick);
            tv_groupNick = v.findViewById(R.id.tv_groupNick);
            rl_QRCode = v.findViewById(R.id.rl_QRCode);
            rl_groupMgr = v.findViewById(R.id.rl_groupMgr);
            v_groupMgrBottom = v.findViewById(R.id.v_groupMgrBottom);
            switch_topChat = v.findViewById(R.id.switch_topChat);
            switch_DND = v.findViewById(R.id.switch_DND);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        presenter = new FragmentGroupInfoPresenter(this);
        View view = inflater.inflate(R.layout.tio_group_info_fragment, container, false);

        holder = new ViewHolder(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.init();
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadUIData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public TioActivity getTioActivity() {
        return (TioActivity) getActivity();
    }

    @Override
    public void initPageUI() {
        if (holder == null) return;
        if (presenter == null) return;

        // 成员列表
        memberListAdapter = new MemberListAdapter(null, holder.rv_memberList);
        memberListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MemberItem item = memberListAdapter.getData().get(position);
                if (item.getItemType() == MemberItem.BUTTON) {// 按钮
                    item.button.onItemClick(getTioActivity(), getGroupId());
                } else if (item.getItemType() == MemberItem.USER) {// 用户
                    String toUid = String.valueOf(item.user.uid);
                    UserPresenter.checkStart(getTioActivity(), toUid, mGroupInfoResp);
                }
            }
        });
        // 查看所有成员
        holder.ll_viewAllMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupMemberActivity.start(getTioActivity(), getGroupId());
            }
        });
        // 清除聊天记录
        holder.rl_deleteChatRecord.setVisibility(View.VISIBLE);
        holder.rl_deleteChatRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.showClearChatRecordDialog();
            }
        });
        // 投诉
        holder.rl_complaint.setOnClickListener(v -> presenter.reqComplaint(getChatLinkId()));
        // 群二维码
        if (ModuleConfig.ENABLE_QR_CODE) {
            holder.rl_QRCode.setVisibility(View.VISIBLE);
            holder.rl_QRCode.setOnClickListener(new ClickUtils.OnDebouncingClickListener() {
                @Override
                public void onDebouncingClick(View v) {
                    GroupQRCodeActivity.start(getTioActivity(), getGroupId());
                }
            });
        } else {
            holder.rl_QRCode.setVisibility(View.GONE);
        }
        // 群管理
        holder.rl_groupMgr.setVisibility(View.GONE);
        holder.v_groupMgrBottom.setVisibility(View.GONE);
        holder.rl_groupMgr.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                GroupMgrActivity.start(getTioActivity(), getGroupId());
            }
        });
        // 聊天置顶
        boolean topChat = presenter.isTopChat();
        holder.switch_topChat.setChecked(topChat);
        holder.switch_topChat.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleTopChatSwitch(isChecked, compoundButton);
            }
        });
        // 消息免打扰
        holder.switch_DND.setChecked(false);
        holder.switch_DND.setOnCheckedChangeListener(new OnSimpleCheckedChangeListener() {
            @Override
            public void onUserCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                super.onUserCheckedChanged(compoundButton, isChecked);
                presenter.toggleDNDSwitch(isChecked, compoundButton);
            }
        });
    }

    @Override
    public void setMenuBtn(GroupRoleEnum groupRoleEnum) {
        if (getActivity() instanceof GroupInfoActivity) {
            GroupInfoActivity activity = (GroupInfoActivity) getActivity();
            activity.getTitleBar().getIvRight().setOnClickListener(view -> {
                if (presenter == null) return;
                presenter.showOperDialog(groupRoleEnum);
            });
        }
    }

    @Override
    public void setUIData(GroupInfoResp groupInfo) {
        mGroupInfoResp = groupInfo;

        if (groupInfo == null) return;
        final GroupInfoResp.Group group = groupInfo.group;
        GroupInfoResp.GroupUser groupUser = groupInfo.groupuser;
        if (groupUser == null || group == null) return;

        if (holder == null) return;
        if (presenter == null) return;

        final boolean mgrPermission = (groupUser.grouprole == 1 || groupUser.grouprole == 3);

        if(group.shownumflag==1){
            holder.tv_viewAllMember.setText(String.format(Locale.getDefault(), "查看全部成员(%d)", group.joinnum));

        }else {
            holder.tv_viewAllMember.setText(String.format(Locale.getDefault(), "查看全部成员", ""));

        }
        // 查看全部成员
        // 名称
        holder.tv_groupName.setText(StringUtil.nonNull(group.name));
        holder.iv_arrow_groupName.setVisibility(mgrPermission ? View.VISIBLE : View.INVISIBLE);
        holder.ll_groupName.setOnClickListener(mgrPermission ? new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ModifyActivity.start_group(view.getContext(), ModifyType.GROUP_NAME, group.id, group.name);
                new CommonTextInputDialog(getActivity())
                        .setTopTitle(getString(R.string.repair_group_nick))
                        .setSubTitle(getString(R.string.good_group_nick))
                        .setPositiveText(getString(R.string.confirm))
                        .setEdittext(group.name)
                        .setMaxLimit(30)
                        .setEditHeight(60)
                        .setOnBtnListener(new CommonTextInputDialog.OnBtnListener() {
                            @Override
                            public void onClickPositive(View view, String submitTxt, CommonTextInputDialog dialog) {
                                if (TextUtils.isEmpty(submitTxt)){
                                    ToastUtils.showShort(getString(R.string.group_nick_not_empty));
                                    return;
                                }
                                ModifyNameReq req = new ModifyNameReq(group.id, submitTxt.trim());
                                req.setCancelTag(this);
                                req.post(new TioCallback<String>() {
                                    @Override
                                    public void onTioSuccess(String s) {
                                        presenter.loadUIData();
                                    }

                                    @Override
                                    public void onTioError(String msg) {
                                    }
                                });
                                dialog.dismiss();
                            }

                            @Override
                            public void onClickNegative(View view, CommonTextInputDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        } : null);
        holder.ll_groupName.setClickable(mgrPermission);
        // 介绍
        holder.tv_groupIntro.setText(StringUtil.nonNull(group.intro));
        holder.ll_groupIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClickGroupIntroItem(mgrPermission, group);
            }
        });
        // 公告
        holder.tv_groupNotice.setText(StringUtil.nonNull(group.notice));
        holder.ll_groupNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onClickGroupNoticeItem(mgrPermission, group);

            }
        });
        //群公告开启并且公告内容部位空时 开启
//        if(group.goinshownoticeflag==1){
//            if(!TextUtils.isEmpty(group.notice)){
//                presenter.onClickGroupNoticeItem(mgrPermission, group);
//            }
//        }

        // 群昵称
        holder.tv_groupNick.setText(StringUtil.nonNull(groupUser.groupnick));
        holder.ll_groupNick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyActivity.start_group(v.getContext(), ModifyType.GROUP_NICK, group.id, groupUser.groupnick);
            }
        });
        // 群管理
        holder.rl_groupMgr.setVisibility(mgrPermission ? View.VISIBLE : View.GONE);
        holder.v_groupMgrBottom.setVisibility(mgrPermission ? View.VISIBLE : View.GONE);
        // 消息免打扰
        boolean openDND = groupUser.msgfreeflag == 1;
        holder.switch_DND.setChecked(openDND);
    }

    @Override
    public MemberListAdapter getMemberListAdapter() {
        return memberListAdapter;
    }

    @Override
    public void setGroupOwnerInfo(GroupUserListResp.GroupMember user) {
        if (user == null) return;
        if (holder == null) return;

        String nick = user.nick;
        final int uid = user.uid;

        // 群主
        holder.tv_groupOwner.setText(StringUtil.nonNull(nick));
        holder.ll_groupOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetailActivity.start(getTioActivity(), String.valueOf(uid));
            }
        });
    }
}
