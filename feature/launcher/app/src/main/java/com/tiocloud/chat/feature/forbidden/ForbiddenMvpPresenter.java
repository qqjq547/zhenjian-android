package com.tiocloud.chat.feature.forbidden;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.GroupManagerReq;
import com.watayouxiang.httpclient.model.request.KickGroupReq;
import com.watayouxiang.httpclient.model.request.RevocationAllMessageReq;
import com.watayouxiang.httpclient.model.response.ForbiddenFlagResp;
import com.watayouxiang.httpclient.model.response.ForbiddenResp;
import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

public class ForbiddenMvpPresenter extends ForbiddenMvpContract.Presenter {
    public ForbiddenMvpPresenter() {
        super(new ForbiddenMvpModel(), false);
    }

    // ====================================================================================
    // 群会话页 - 长按头像
    // ====================================================================================

    @Override
    public void longClickAvatar(String uid, String groupId, View v, ForbiddenMvpContract.OnAitProxy aitProxy) {
        // 查询禁言权限
        getModel().reqForbiddenFlag(uid, groupId, new TioCallback<ForbiddenFlagResp>() {
            @Override
            public void onTioSuccess(ForbiddenFlagResp resp) {
                showLeftListPopup2(resp, v, aitProxy, uid, groupId);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void showLeftListPopup2(ForbiddenFlagResp resp, View v, ForbiddenMvpContract.OnAitProxy aitProxy, String uid, String groupId) {
        //-- 禁言权限
        boolean haveForbiddenPermission = resp.haveForbiddenPermission();
        boolean forbidden = resp.isForbidden();
        String forbiddenMode = resp.getForbiddenMode();
        // 是否有角色管理权限
        boolean haveMgrPermission = resp.getRolegrant() == 1;
        // 是否为群管理员
        boolean isGroupMgr = resp.getGroupRoleEnum() == GroupRoleEnum.MGR;
        // 踢人权限
        boolean kickPermission = resp.getKickgrant() == 1;
        // 非群成员
        boolean notGroupUser = resp.getUserstatus() == 2;

        if (notGroupUser) {
            // 如果非群成员，无操作
            TioToast.showShort("非群成员");
            return;
        } else if (!haveForbiddenPermission && !haveMgrPermission) {
            // 如果没有 "禁言权限" 和 "管理权限"，只有艾特操作
            aitProxy.insertAitMemberInner();
            return;
        }

        @Nullable Boolean _forbidden = null;
        @Nullable Boolean _groupMgr = null;
        boolean _removeMemberPermission = false;
        if (haveForbiddenPermission && !isGroupMgr) {
            _forbidden = forbidden;
        }
        if (haveMgrPermission) {
            _groupMgr = isGroupMgr;
        }
        if (kickPermission && !isGroupMgr) {
            _removeMemberPermission = true;
        }

        // 显示 popup
        showLeftListPopup(v, _forbidden, _groupMgr, _removeMemberPermission, new LeftListPopup.OnSimplePopupListener() {
            @Override
            public void onClickAitItem(LeftListPopup popup) {
                aitProxy.insertAitMemberInner();
                popup.dismiss();
            }

            @Override
            public void onClickRemoveMemberItem(LeftListPopup popup) {
                showRemoveMemberDialog(v, uid, groupId);
                popup.dismiss();
            }
            //撤回全部消息
            @Override
            public void onClickRemoveMemberItem_CheHui(LeftListPopup popup) {
                new RevocationAllMessageReq(uid, groupId).setCancelTag(this).post(new TioCallback<String>() {
                    @Override
                    public void onTioSuccess(String s) {
                        TioToast.showShort("撤回成功");
//                        Log.d("====","=="+s);
                    }

                    @Override
                    public void onTioError(String msg) {
                        TioToast.showShort(msg);
                    }
                });
            }

            @Override
            public void onClickSilentItem(LeftListPopup popup, boolean forbidden) {
                if (forbidden) {
                    // 取消禁言
                    forbidden_cancelUser(forbiddenMode, uid, groupId, "取消禁言成功");
                } else {
                    // 显示禁言时长选择弹窗
                    showSilentTimeSingleChoiceDialog(v.getContext(), (secondTime, dialog) -> {
                        if (secondTime == -1) {
                            TioToast.showShort("未知时长");
                        } else if (secondTime == Long.MAX_VALUE) {
                            // 长久禁言
                            forbidden_forever(uid, groupId, "禁言成功");
                        } else {
                            // 时常禁言
                            forbidden_duration(secondTime + "", uid, groupId, "禁言成功");
                        }
                        dialog.dismiss();
                    });
                }
                popup.dismiss();
            }

            @Override
            public void onClickGroupMgrItem(LeftListPopup popup, boolean groupMgr) {
                if (groupMgr) {
                    // 取消管理员
                    reqGroupManager(uid, groupId, "2", "取消管理员成功");
                } else {
                    // 设为管理员
                    reqGroupManager(uid, groupId, "3", "设为管理员成功");
                }
                popup.dismiss();
            }
        });
    }

    private void showRemoveMemberDialog(View v, String uid, String groupId) {
        new EasyOperDialog.Builder("确定将该用户移出群聊？")
                .setPositiveBtnTxt("确定")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqRemoveMember(dialog, uid, groupId);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(v.getContext());
    }

    private void reqRemoveMember(EasyOperDialog dialog, String uid, String groupId) {
        new KickGroupReq(uid, groupId).setCancelTag(this).post(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort("移出群聊成功");
                dialog.dismiss();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void showLeftListPopup(View v, Boolean forbidden, Boolean groupMgr, boolean removeMemberPermission, LeftListPopup.OnPopupListener listener) {
        new LeftListPopup(v.getContext(), forbidden, groupMgr, removeMemberPermission)
                .setOnPopupListener(listener)
                .show(v);
    }

    @Override
    public void showSilentTimeSingleChoiceDialog(Context context, SilentTimeSingleChoiceDialog.OnDialogCallback callback) {
        new SilentTimeSingleChoiceDialog(context)
                .setOnDialogCallback(callback)
                .show();
    }

    // ====================================================================================
    // 禁言操作
    // ====================================================================================

    @Override
    public void forbidden(String mode, String duration, String uid, String groupid, String oper, TioCallback<ForbiddenResp> callback) {
        getModel().reqForbidden(mode, duration, uid, groupid, oper, callback);
    }

    @Override
    public void forbidden(String mode, String duration, String uid, String groupid, String oper) {
        forbidden(mode, duration, uid, groupid, oper, new TioCallback<ForbiddenResp>() {
            @Override
            public void onTioSuccess(ForbiddenResp forbiddenResp) {
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void forbidden(String mode, String duration, String uid, String groupid, String oper, String successTip) {
        forbidden(mode, duration, uid, groupid, oper, new TioCallback<ForbiddenResp>() {
            @Override
            public void onTioSuccess(ForbiddenResp forbiddenResp) {
                TioToast.showShort(successTip);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void forbidden_cancelUser(String mode, String uid, String groupId) {
        forbidden(mode, null, uid, groupId, "2");
    }

    @Override
    public void forbidden_cancelUser(String mode, String uid, String groupId, String successTip) {
        forbidden(mode, null, uid, groupId, "2", successTip);
    }

    @Override
    public void forbidden_cancelUser(String mode, String uid, String groupId, TioCallback<ForbiddenResp> callback) {
        forbidden(mode, null, uid, groupId, "2", callback);
    }

    @Override
    public void forbidden_forever(String uid, String groupId) {
        forbidden("3", null, uid, groupId, "1");
    }

    @Override
    public void forbidden_forever(String uid, String groupId, String successTip) {
        forbidden("3", null, uid, groupId, "1", successTip);
    }

    @Override
    public void forbidden_duration(String duration, String uid, String groupId) {
        forbidden("1", duration, uid, groupId, "1");
    }

    @Override
    public void forbidden_duration(String duration, String uid, String groupId, String successTip) {
        forbidden("1", duration, uid, groupId, "1", successTip);
    }

    // ====================================================================================
    // 群禁言开关
    // ====================================================================================

    @Override
    public void toggleSwitch_ForbiddenGroupMember(String groupId, CompoundButton compoundButton, boolean isChecked) {
        getModel().reqForbidden("4", null, null, groupId, isChecked ? "1" : "2", new TioCallback<ForbiddenResp>() {
            @Override
            public void onTioSuccess(ForbiddenResp forbiddenResp) {
            }

            @Override
            public void onTioError(String msg) {
                compoundButton.setChecked(!isChecked);
            }
        });
    }

    // ====================================================================================
    // 群成员列表页 - 长按 "列表项"
    // ====================================================================================

    @Override
    public void longClickGroupMemberListItem(View v, String uid, String groupId, ForbiddenMvpContract.GroupMemberPageProxy proxy) {
        // 查询禁言权限
        getModel().reqForbiddenFlag(uid, groupId, new TioCallback<ForbiddenFlagResp>() {
            @Override
            public void onTioSuccess(ForbiddenFlagResp resp) {
                // 显示弹窗
                showMgrPopup2(v, resp, uid, groupId, proxy);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void showMgrPopup2(View v, ForbiddenFlagResp resp, String uid, String groupId, ForbiddenMvpContract.GroupMemberPageProxy proxy) {
        //-- 禁言权限
        boolean haveForbiddenPermission = resp.haveForbiddenPermission();
        boolean forbidden = resp.isForbidden();
        String forbiddenMode = resp.getForbiddenMode();
        // 是否有角色管理权限
        boolean haveMgrPermission = resp.getRolegrant() == 1;
        // 是否为群管理员
        boolean isGroupMgr = resp.getGroupRoleEnum() == GroupRoleEnum.MGR;

        // 如果没有权限，不处理
        if (!haveForbiddenPermission && !haveMgrPermission) return;

        @Nullable Boolean _forbidden = null;
        @Nullable Boolean _groupMgr = null;
        if (haveForbiddenPermission && !isGroupMgr) {
            _forbidden = forbidden;
        }
        if (haveMgrPermission) {
            _groupMgr = isGroupMgr;
        }

        showMgrPopup(v, _forbidden, _groupMgr, new MgrPopup.OnSimplePopupListener() {
            @Override
            public void onClickSilentItem(MgrPopup popup, boolean forbidden) {
                if (forbidden) {
                    // 取消禁言
                    forbidden_cancelUser(forbiddenMode, uid, groupId, "取消禁言成功");
                } else {
                    // 显示禁言时长选择弹窗
                    showSilentTimeSingleChoiceDialog(v.getContext(), (secondTime, dialog) -> {
                        if (secondTime == -1) {
                            TioToast.showShort("未知时长");
                        } else if (secondTime == Long.MAX_VALUE) {
                            // 长久禁言
                            forbidden_forever(uid, groupId, "禁言成功");
                        } else {
                            // 时常禁言
                            forbidden_duration(secondTime + "", uid, groupId, "禁言成功");
                        }
                        dialog.dismiss();
                    });
                }
                popup.dismiss();
            }

            @Override
            public void onClickGroupMgrItem(MgrPopup popup, boolean groupMgr) {
                if (groupMgr) {
                    // 取消管理员
                    reqGroupManager(uid, groupId, "2", "取消管理员成功", proxy);
                } else {
                    // 设为管理员
                    reqGroupManager(uid, groupId, "3", "设为管理员成功", proxy);
                }
                popup.dismiss();
            }
        });
    }

    private void reqGroupManager(String uid, String groupId, String groupRole, String successTip, @Nullable ForbiddenMvpContract.GroupMemberPageProxy proxy) {
        new GroupManagerReq(uid, groupId, groupRole).setCancelTag(getModel()).post(new TioSuccessCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                TioToast.showShort(successTip);
                if (proxy != null) proxy.reloadGroupMemberList();
            }
        });
    }

    private void reqGroupManager(String uid, String groupId, String groupRole, String successTip) {
        this.reqGroupManager(uid, groupId, groupRole, successTip, null);
    }

    @Override
    public void showMgrPopup(View v, @Nullable Boolean forbidden, @Nullable Boolean groupMgr, MgrPopup.OnPopupListener listener) {
        new MgrPopup(v.getContext(), forbidden, groupMgr)
                .setOnPopupListener(listener)
                .show(v);
    }
}
