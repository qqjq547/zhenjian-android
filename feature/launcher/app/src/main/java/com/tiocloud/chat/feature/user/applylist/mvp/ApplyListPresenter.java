package com.tiocloud.chat.feature.user.applylist.mvp;

import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.tiocloud.chat.feature.user.detail.model.NonFriendApply;
import com.tiocloud.chat.mvp.dealapply.DealApplyContract;
import com.tiocloud.chat.mvp.dealapply.DealApplyPresenter;
import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.httpclient.model.request.IgnoreApplyReq;
import com.watayouxiang.httpclient.model.response.ApplyListResp;
import com.watayouxiang.httpclient.model.response.DealApplyResp;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc :
 */
public class ApplyListPresenter extends ApplyListContract.Presenter {

    private final DealApplyPresenter dealApplyPresenter;

    public ApplyListPresenter(ApplyListContract.View view) {
        super(new ApplyListModel(), view, true);
        dealApplyPresenter = new DealApplyPresenter(() -> getView().getActivity());
    }

    // 用户系统通知
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserSysNtf(WxUserSysNtf ntf) {
        if (ntf.code == 30) {// 申请好友请求
            requestApplyList();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        dealApplyPresenter.detachView();
    }

    @Override
    public void init() {
        getView().initTitleBar();
        getView().initRecyclerView();
    }

    /**
     * 申请列表数据
     */
    @Override
    public void requestApplyList() {
        getModel().getApplyList(new BaseModel.DataProxy<ApplyListResp>() {
            @Override
            public void onSuccess(ApplyListResp data) {
                getView().onApplyListResp(data);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }

    /**
     * 同意添加好友
     *
     * @param applyId
     * @param remarkName
     * @param position
     * @param view
     */
    @Override
    public void doAgreeAddFriend(String applyId, String remarkName, final int position, final View view) {
        view.setEnabled(false);
        dealApplyPresenter.start(applyId, remarkName, new DealApplyContract.Presenter.DealApplyProxy() {
            @Override
            public void onSuccess(DealApplyResp data) {
                getView().onAgreeAddFriend(data, position);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                view.setEnabled(true);
            }
        });
    }

    @Override
    public void openUserDetailActivity(ApplyListResp.Data item) {
        NonFriendApply nonFriendApply = new NonFriendApply(item.greet, String.valueOf(item.id), item.nick);
        UserDetailActivity.start(getView().getActivity(), String.valueOf(item.uid), nonFriendApply);
    }

    @Override
    public void ignoreFriendReq(ApplyListResp.Data item, int position) {
        new EasyOperDialog.Builder("确定忽略好友请求吗?")
                .setPositiveBtnTxt(StringUtils.getString(R.string.ignore))
                .setNegativeBtnTxt(StringUtils.getString(R.string.cancel))
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        reqIgnoreApply(item, position);
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getActivity());
    }

    private void reqIgnoreApply(ApplyListResp.Data item, int position) {
        int uid = item.id;
        new IgnoreApplyReq(String.valueOf(uid)).setCancelTag(this).post(new TioSuccessCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                getView().onIgnoreApplySuccess(position);
            }
        });
    }
}
