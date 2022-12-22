package com.tiocloud.chat.widget.popupwindow;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.group.create.CreateGroupActivity;
import com.tiocloud.chat.feature.search.customservice.SearchCustServiceActivity;
import com.tiocloud.chat.feature.search.user.SearchUserActivity;
import com.tiocloud.chat.feature.session.p2p.P2PSessionActivity;
import com.tiocloud.chat.util.ScreenUtil;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.CheckCardKefuXiaoZuReq;
import com.watayouxiang.httpclient.model.request.CustServiceSearchReq;
import com.watayouxiang.httpclient.model.response.CustServiceTeamListResp;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgCard;
import com.watayouxiang.qrcode.feature.qrcode_decoder.QRCodeDecoderActivity;

/**
 * author : TaoWang
 * date : 2020-02-19
 * desc :
 */
public class HomePopupWindow extends BasePopupWindow {

    public HomePopupWindow(View anchor) {
        super(anchor);
    }

    @Override
    protected int getViewLayout() {
        return R.layout.tio_home_popup2;
    }

    @Override
    protected void initViews() {
        findViewById(R.id.ll_create_group).setOnClickListener(view -> {
            CreateGroupActivity.start(getActivity());
            dismiss();
        });
        findViewById(R.id.ll_add_friend).setOnClickListener(view -> {
            SearchUserActivity.start(getActivity());
            dismiss();
        });
        findViewById(R.id.ll_addZhuanshu).setVisibility(View.GONE);
        findViewById(R.id.ll_addZhuanshu).setOnClickListener(view -> {
//            openKefuCard(getActivity(),"test");
//            searchCustServiceTeam("test");
            SearchCustServiceActivity.start(getActivity());
            dismiss();
        });
        if (ModuleConfig.ENABLE_QR_CODE) {
            findViewById(R.id.ll_qrcode_decoder).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_qrcode_decoder).setOnClickListener(view -> {
                QRCodeDecoderActivity.start(getActivity());
                dismiss();
            });
        }else {
            findViewById(R.id.ll_qrcode_decoder).setVisibility(View.GONE);
        }
    }
    public void searchCustServiceTeam(String searchkey){
        new CustServiceSearchReq(searchkey).setCancelTag(this).get(new TioCallbackImpl<CustServiceTeamListResp>() {
            @Override
            public void onTioSuccess(CustServiceTeamListResp custServiceTeamListResp) {
                if (!custServiceTeamListResp.getList().isEmpty()){
                    P2PSessionActivity.active(getActivity(),custServiceTeamListResp.getList().get(0).getRotatecurrid()+"");
                }else {
                    TioToast.showShort("没有找到相关客服");
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);

            }
        });
    }
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
    public void show() {
        // show animator
        ObjectAnimator animator = ObjectAnimator.ofFloat(getAnchor(), "rotation", 0, 45);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
        HomePopupWindow.super.showAsDropDown(getAnchor(), 0, ScreenUtil.dp2px(8));
        // bg alpha
        bgAlpha(0.8f);
    }

    @Override
    public void dismiss() {
        // dismiss animator
        ObjectAnimator animator = ObjectAnimator.ofFloat(getAnchor(), "rotation", 45, 0);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        });
        animator.start();
        // dismiss window
        HomePopupWindow.super.dismiss();
        // bg alpha
        bgAlpha(1.0f);
    }
}
