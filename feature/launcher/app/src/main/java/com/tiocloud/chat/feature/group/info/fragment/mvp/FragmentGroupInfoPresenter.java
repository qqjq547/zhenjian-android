package com.tiocloud.chat.feature.group.info.fragment.mvp;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.curr.modify.ModifyActivity;
import com.tiocloud.chat.feature.curr.modify.model.ModifyType;
import com.tiocloud.chat.feature.group.transfergroup.TransferGroupActivity;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.util.ScreenUtil;
import com.tiocloud.chat.util.StringUtil;
import com.tiocloud.chat.util.TimeUtil;
import com.tiocloud.chat.widget.dialog.base.GroupOperDialog;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.SpanUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.confirm.TioConfirmDialog;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.db.dao.ChatListTableCrud;
import com.watayouxiang.db.table.ChatListTable;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.MsgFreeFlagReq;
import com.watayouxiang.httpclient.model.request.OperReq;
import com.watayouxiang.httpclient.model.response.DelGroupResp;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;
import com.watayouxiang.httpclient.model.response.GroupUserListResp;
import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020-02-26
 * desc :
 */
public class FragmentGroupInfoPresenter extends FragmentGroupInfoContract.Presenter {

    public FragmentGroupInfoPresenter(FragmentGroupInfoContract.View view) {
        super(new FragmentGroupInfoModel(), view);
    }

    @Override
    public void init() {
        getView().initPageUI();
    }

    @Override
    public void loadUIData() {
        // 获取群聊信息
        getModel().getGroupInfo("1", getView().getGroupId(), new BaseModel.DataProxy<GroupInfoResp>() {
            @Override
            public void onSuccess(GroupInfoResp groupInfo) {
                final GroupInfoResp.GroupUser groupUser = groupInfo.groupuser;
                final GroupInfoResp.Group group = groupInfo.group;
                if (groupUser == null || group == null) return;

                GroupRoleEnum groupRoleEnum = groupUser.getGroupRoleEnum();
                final boolean mgrPermission = (groupRoleEnum == GroupRoleEnum.OWNER || groupRoleEnum == GroupRoleEnum.MGR);
                final boolean showInviteMember = group.applyflag == 1;

                // 群信息
                getView().setUIData(groupInfo);
                // 设置更多按钮
                getView().setMenuBtn(groupRoleEnum);
                // 获取群成员列表
                reqMemberListData(mgrPermission, showInviteMember);
            }

            private void reqMemberListData(final boolean mgrPermission, final boolean showInviteMember) {
                getModel().getGroupUserList(String.valueOf(1), getView().getGroupId(), new BaseModel.DataProxy<GroupUserListResp>() {
                    @Override
                    public void onSuccess(GroupUserListResp groupUserList) {
                        getView().getMemberListAdapter().setNewData(mgrPermission, groupUserList, showInviteMember);
                        if (groupUserList.list.size() > 0) {
                            getView().setGroupOwnerInfo(groupUserList.list.get(0));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void showClearChatRecordDialog() {
        final TioActivity context = getView().getTioActivity();
        new EasyOperDialog.Builder("确定要删除本群的聊天记录吗?")
                .setPositiveBtnTxt("确定")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, final EasyOperDialog dialog) {
                        reqClearChatRecord(dialog, context);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(context);
    }

    private void reqClearChatRecord(final EasyOperDialog dialog, final TioActivity context) {
        getModel().reqClearChatRecord(getView().getChatLinkId(), new BaseModel.DataProxy<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                TioToast.showShort(s);
                dialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }

    private GroupOperDialog operDialog;

    @Override
    public void showOperDialog(GroupRoleEnum groupRoleEnum) {
        if (operDialog == null) {
            operDialog = new GroupOperDialog(getView().getTioActivity()) {
                @Override
                protected void onClick(GroupOperDialog dialog, View v) {
                    super.onClick(dialog, v);
                    if (v.getId() == R.id.tv_exitGroup) {// 退群
                        showExitGroupDialog();
                    } else if (v.getId() == R.id.tv_dissolveGroup) {// 解散群
                        showDissolveGroupDialog();
                    } else if (v.getId() == R.id.tv_transferGroup) {// 转让群
                        TransferGroupActivity.start(getView().getTioActivity(), getView().getGroupId());
                    }
                    dialog.cancel();
                }
            };
        }
        operDialog.show(groupRoleEnum);
    }

    @Override
    public void onClickGroupIntroItem(boolean groupOwner, @NonNull GroupInfoResp.Group group) {
        if (groupOwner) {
            ModifyActivity.start_group(getView().getTioActivity(), ModifyType.GROUP_INTRO, group.id, group.intro);
        } else {
            SpannableStringBuilder format = SpanUtils.getBuilder("群简介")
                    .setTexSize(ScreenUtil.sp2px(18))
                    .append(String.format(Locale.getDefault(), "\n\n%s", StringUtil.nonNull(group.intro)))
                    .setTexSize(ScreenUtil.sp2px(16))
                    .create();
            new TioConfirmDialog(format, Gravity.START, new TioConfirmDialog.OnConfirmListener() {
                @Override
                public void onConfirm(View view, TioConfirmDialog dialog) {
                    dialog.dismiss();
                }
            }).show_unCancel(getView().getTioActivity());
        }
    }

    @Override
    public void onClickGroupNoticeItem(boolean groupOwner, @NonNull GroupInfoResp.Group group) {
        if (groupOwner) {
            ModifyActivity.start_group(getView().getTioActivity(), ModifyType.GROUP_NOTICE, group.id, group.notice);
        } else {
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
            }).show_unCancel(getView().getTioActivity());
        }
    }

    @Override
    public void reqComplaint(String chatLinkId) {
        showReportDialog(chatLinkId);
    }

    @Override
    public boolean isTopChat() {
        String chatLinkId = getView().getChatLinkId();
        ChatListTable chatListTable = ChatListTableCrud.query_chatLinkId(chatLinkId);
        if (chatListTable != null) {
            return chatListTable.getTopflag() == 1;
        } else {
            TioLogger.e(StringUtils.format("查询不到chatLinkId为%s的会话", chatLinkId));
        }
        return false;
    }

    @Override
    public void toggleTopChatSwitch(final boolean isChecked, final CompoundButton compoundButton) {
        String chatLinkId = getView().getChatLinkId();
        ChatListTable chatListTable = ChatListTableCrud.query_chatLinkId(chatLinkId);
        if (chatListTable == null) {
            TioLogger.e(StringUtils.format("查询不到chatLinkId为%s的会话", chatLinkId));
            return;
        }
        // 置顶操作
        OperReq.topChat(chatLinkId, isChecked).setCancelTag(this).post(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {

            }

            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    @Override
    public void toggleDNDSwitch(boolean isChecked, CompoundButton compoundButton) {
        String groupId = getView().getGroupId();
        String freeflag = isChecked ? "1" : "2";
        MsgFreeFlagReq.getInstance_Group(groupId, freeflag).setCancelTag(this).post(new TioCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {

            }

            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    private void showReportDialog(String chatlinkid) {
        new EasyOperDialog.Builder("确定投诉该群吗？")
                .setPositiveBtnTxt("确定")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        requestReport(chatlinkid);
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getTioActivity());
    }

    private void requestReport(String chatLinkId) {
        OperReq operReq = OperReq.complaint(chatLinkId);
        operReq.setCancelTag(this);
        operReq.get(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort("投诉成功，等待后台审核");
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void showDissolveGroupDialog() {
        new EasyOperDialog.Builder("解散后，所有与此群有关的记录都将被删除！确认解散吗？")
                .setPositiveBtnTxt("解散")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqDelGroup(dialog);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getTioActivity());
    }

    private void reqDelGroup(final EasyOperDialog dialog) {
        getModel().requestDelGroup(getView().getGroupId(), new BaseModel.DataProxy<DelGroupResp>() {
            @Override
            public void onSuccess(DelGroupResp resp) {
                MainActivity.start(getView().getTioActivity());
                dialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }

    private void showExitGroupDialog() {
        new EasyOperDialog.Builder("确定退出当前群聊？\n退出后将不再接收此群消息")
                .setPositiveBtnTxt("退出")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqExitGroup(dialog);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getTioActivity());
    }

    private void reqExitGroup(final EasyOperDialog dialog) {
        getModel().requestLeaveGroup(getView().getGroupId(), new BaseModel.DataProxy<String>() {
            @Override
            public void onSuccess(String resp) {
                MainActivity.start(getView().getTioActivity());
                dialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }
}
