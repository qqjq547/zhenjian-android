package com.tiocloud.chat.feature.group.invitemember.fragment.mvp;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.group.invitemember.fragment.adapter.InviteMemberAdapter;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.listener.OnSingleClickListener;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.form.EasyFormDialog;
import com.watayouxiang.httpclient.model.request.GroupInfoReq;
import com.watayouxiang.httpclient.model.request.JoinGroupApplyReq;
import com.watayouxiang.httpclient.model.response.ApplyGroupFdListResp;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;

import java.util.LinkedList;
import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class InviteMemberPresenter extends InviteMemberContract.Presenter {

    public InviteMemberPresenter(InviteMemberContract.View view) {
        super(new InviteMemberModel(), view);
    }

    @Override
    public void init() {
        getView().initRecyclerView();
        initMenuBtn();
        search(null);
    }

    @Override
    public void search(final String keyWord) {
        final InviteMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;
        getModel().getApplyGroupFdList(getView().getGroupId(), keyWord, new BaseModel.DataProxy<ApplyGroupFdListResp>() {
            @Override
            public void onSuccess(ApplyGroupFdListResp friends) {
                adapter.setNewData(friends, keyWord);
            }
        });
    }

    // ====================================================================================
    // 安装菜单
    // ====================================================================================

    private TextView tv_menuBtn;

    @Override
    public void installMenuBtn(TextView tv_menuBtn) {
        this.tv_menuBtn = tv_menuBtn;
    }

    private void initMenuBtn() {
        final InviteMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;

        tv_menuBtn.setEnabled(false);
        tv_menuBtn.setText("邀请");

        adapter.setOnCheckedChangeListener(linkedList -> {
            if (linkedList.size() > 0) {
                tv_menuBtn.setText(String.format(Locale.getDefault(), "邀请(%d)", linkedList.size()));
                tv_menuBtn.setEnabled(true);
            } else {
                tv_menuBtn.setText("邀请");
                tv_menuBtn.setEnabled(false);
            }
        });
        tv_menuBtn.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                inviteMember();
            }
        });
    }

    // ====================================================================================
    // 邀请入群
    // ====================================================================================

    private void inviteMember() {
        final InviteMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;
        LinkedList<String> checkedIds = adapter.getCheckedIds();
        String uidList = StringUtil.list2String(checkedIds);
        if (uidList == null) return;

        // 获取群信息
        reqGroupInfo(getView().getGroupId(), uidList);
    }

    private void reqGroupInfo(String groupId, String uidList) {
        new GroupInfoReq("1", groupId).setCancelTag(this).get(new TioSuccessCallback<GroupInfoResp>() {
            @Override
            public void onTioSuccess(GroupInfoResp resp) {
                GroupInfoResp.Group group = resp.group;
                GroupInfoResp.GroupUser groupuser = resp.groupuser;
                if (group == null || groupuser == null) {
                    TioToast.showShort("获取群信息失败");
                    return;
                }

                int grouprole = groupuser.grouprole;
                int joinmode = group.joinmode;

                if (grouprole == 1 || grouprole == 3) {// 群主、管理员
                    // 直接邀请成员
                    postInviteMember(uidList);
                } else if (grouprole == 2) {// 成员
                    if (joinmode == 2) {// 关闭审核
                        // 直接邀请成员
                        postInviteMember(uidList);
                    } else if (joinmode == 1) {// 开启审核
                        // 提交进群审核表单
                        showJoinGroupApplyDialog(uidList);
                    } else {
                        TioToast.showShort("未知 joinmode：" + joinmode);
                    }
                } else {
                    TioToast.showShort("未知 grouprole：" + grouprole);
                }

            }
        });
    }

    // 显示进群审核弹窗
    private void showJoinGroupApplyDialog(String uidList) {
        new EasyFormDialog.Builder("群主已开启邀请审核，邀请好友进群可说明邀请理由")
                .setMinLine(1)
                .setMaxLength(30)
                .setHint("邀请理由")
                .setPositiveBtnTxt(StringUtils.getString(R.string.send))
                .setOnBtnListener(new EasyFormDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyFormDialog dialog, String inputTxt) {
                        if (TextUtils.isEmpty(inputTxt)) {
                            TioToast.showShort("请输入邀请理由");
                            return;
                        }
                        reqJoinGroupApply(uidList, inputTxt);
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyFormDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_cancelable(getView().getActivity());
    }

    // 提交进群审核表单
    private void reqJoinGroupApply(String uidList, String applyMsg) {
        new JoinGroupApplyReq(uidList, getView().getGroupId(), applyMsg).post(new TioSuccessCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                TioToast.showShort("申请入群成功");
                getView().getActivity().finish();
            }
        });
    }

    // 直接邀请成员
    private void postInviteMember(String uidList) {
        getModel().postJoinGroup(getView().getGroupId(), uidList, new BaseModel.DataProxy<String>() {
            @Override
            public void onSuccess(String createGroupResp) {
                getView().getActivity().finish();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }
}
