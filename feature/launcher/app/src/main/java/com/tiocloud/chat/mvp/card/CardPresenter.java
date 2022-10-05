package com.tiocloud.chat.mvp.card;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.tiocloud.chat.feature.session.group.GroupSessionActivity;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.CheckCardJoinGroupReq;
import com.watayouxiang.httpclient.model.request.CheckCardKefuXiaoZuReq;
import com.watayouxiang.httpclient.model.request.JoinGroupReq;

public class CardPresenter extends CardContract.Presenter {
    private CardContract.OpenGroupCardListener openGroupCardListener;

    public CardPresenter() {
        super(new CardModel());
    }

    @Override
    public void openKefuCard(Context context, String teamid) {
        new EasyOperDialog.Builder("是否添加客服小组")
                .setPositiveBtnTxt("是")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        dialog.dismiss();

                        new CheckCardKefuXiaoZuReq(teamid).setCancelTag(this).get(new TioCallbackImpl<Object>() {
                            @Override
                            public void onTioSuccess(Object o) {
//                                Log.d("=====客服小组===","==="+o.toString());

                                TioToast.showShort("添加客服小组成功");

                            }

                            @Override
                            public void onTioError(String msg) {
                                TioToast.showShort(msg);

                            }
                        });
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();

                    }
                })
                .build()
                .show_unCancel(context);
    }

    @Override
    public void openP2PCard(Context context, String uid) {
        UserDetailActivity.start(context, uid);
    }

    @Override
    public void openGroupCard(Context context, String groupId, String shareFromUid, CardContract.OpenGroupCardListener listener) {
        this.openGroupCardListener = listener;
        this.openGroupCard(context, groupId, shareFromUid);
    }

    @Override
    public void openGroupCard(Context context, String groupId, String shareFromUid) {
        new CheckCardJoinGroupReq(groupId, shareFromUid).setCancelTag(this).get(new TioCallbackImpl<Integer>() {
            @Override
            public void onTioSuccess(Integer integer) {
                if (integer == 1) {
                    // 已经进群
                    openGroupSessionPage(context, groupId);
                    if (openGroupCardListener != null) {
                        openGroupCardListener.onOpenGroupCardSuccess();
                    }
                } else if (integer == 2) {
                    // 未进群
                    showJoinGroupConfirmDialog(context, groupId, shareFromUid);
                } else {
                    // 未知状态
                    TioToast.showShort("unknown resp: " + integer);
                    if (openGroupCardListener != null) {
                        openGroupCardListener.onOpenGroupCardError();
                    }
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
                if (openGroupCardListener != null) {
                    openGroupCardListener.onOpenGroupCardError();
                }
            }
        });
    }

    private void showJoinGroupConfirmDialog(Context context, String groupId, String shareFromUid) {
        new EasyOperDialog.Builder("是否接受邀请加入群聊？")
                .setPositiveBtnTxt("加入群聊")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqJoinGroup(groupId, shareFromUid, dialog);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                        if (openGroupCardListener != null) {
                            openGroupCardListener.onOpenGroupCardError();
                        }
                    }
                })
                .build()
                .show_unCancel(context);
    }

    private void reqJoinGroup(String groupId, String shareFromUid, EasyOperDialog dialog) {
        long currUid = TioDBPreferences.getCurrUid();
        if (currUid == -1) {
            TioToast.showShort("currUid is empty");
            return;
        }
        // public JoinGroupReq(String uids, String groupid, String applyuid) {
        new JoinGroupReq(String.valueOf(currUid), groupId, shareFromUid).setCancelTag(this).post(new TioCallbackImpl<String>() {
            @Override
            public void onTioSuccess(String s) {
                TioToast.showShort(s);
                dialog.dismiss();
                openGroupSessionPage(dialog.getContext(), groupId);
            }

            @Override
            public void onTioError(String msg) {
                super.onTioError(msg);
                TioToast.showShort(msg);
            }
        });
    }

    private void openGroupSessionPage(Context context, String groupId) {
        GroupSessionActivity.active(context, groupId);
        if (openGroupCardListener != null) {
            openGroupCardListener.onOpenGroupCardSuccess();
        }
    }
}
