package com.tiocloud.chat.feature.user.detail.feature.nonfriend.mvp;

import android.app.Activity;
import android.view.View;

import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.tiocloud.chat.mvp.addfriend.AddFriendContract;
import com.tiocloud.chat.mvp.addfriend.AddFriendPresenter;
import com.watayouxiang.httpclient.model.response.AddFriendResp;
import com.watayouxiang.httpclient.model.response.FriendApplyResp;
import com.watayouxiang.httpclient.model.response.UserInfoResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc :
 */
public class NonFriendPresenter extends NonFriendContract.Presenter {

    private final AddFriendPresenter addFriendPresenter;

    public NonFriendPresenter(NonFriendContract.View view) {
        super(new NonFriendModel(), view);
        addFriendPresenter = new AddFriendPresenter();
    }

    @Override
    public void detachView() {
        super.detachView();
        addFriendPresenter.detachView();
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    public void init() {
        getView().resetViews();
        getView().initViews();
        getModel().getUserInfo(getView().getUid(), new BaseModel.DataProxy<UserInfoResp>() {
            @Override
            public void onSuccess(UserInfoResp resp) {
                getView().onUserInfoResp(resp);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }

    @Override
    public void doAddFriend(View view) {
        String id = getModel().getUid();
        if (id == null) {
            return;
        }

        view.setEnabled(false);
        addFriendPresenter.checkStart(Integer.parseInt(id), new AddFriendContract.Presenter.AddFriendProxy() {
            @Override
            public void onAddFriendResp(AddFriendResp data) {
                TioToast.showShort("好友添加成功");
                resetFragment();
            }

            @Override
            public void onFriendApplyResp(FriendApplyResp data) {
                super.onFriendApplyResp(data);
                TioToast.showShort("好友申请成功");
                resetFragment();
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

            private void resetFragment() {
                Activity activity = getView().getActivity();
                if (!(activity instanceof UserDetailActivity)) {
                    throw new ClassCastException("activity not UserInfoActivity");
                }
                UserDetailActivity userActivity = (UserDetailActivity) activity;
                userActivity.updateUI();
            }
        }, getView().getActivity());
    }
}
