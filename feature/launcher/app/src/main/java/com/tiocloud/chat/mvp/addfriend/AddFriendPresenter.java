package com.tiocloud.chat.mvp.addfriend;

import android.app.Activity;
import android.view.View;

import com.tiocloud.chat.widget.dialog.tio.ApplyFriendDialog;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.httpclient.model.response.AddFriendResp;
import com.watayouxiang.httpclient.model.response.FriendApplyResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc :
 */
public class AddFriendPresenter extends AddFriendContract.Presenter {

    public AddFriendPresenter() {
        super(new AddFriendModel());
    }

    /**
     * 1、获取当前用户信息
     * 2、验证好友状态
     * **｜ 需要申请，才能加好友
     * **｜**｜显示申请好友弹窗
     * **｜ 不需要验证，直接添加好友
     * **｜**｜直接添加好友
     */
    @Override
    public void checkStart(final int uid, final AddFriendProxy _proxy, Activity activity) {
        getModel().init(new BaseModel.DataProxy<UserCurrResp>() {
            @Override
            public void onSuccess(UserCurrResp resp) {
                checkAddFriend(uid, _proxy, activity);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                _proxy.onFailure(msg);
                _proxy.onFinish();
            }
        });
    }

    /**
     * 1、获取当前用户信息
     * 2、显示申请好友弹窗
     */
    @Override
    public void uncheckStart(int uid, AddFriendProxy _proxy, Activity activity) {
        getModel().init(new BaseModel.DataProxy<UserCurrResp>() {
            @Override
            public void onSuccess(UserCurrResp resp) {
                showApplyFriendDialog(uid, _proxy, activity);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                _proxy.onFailure(msg);
                _proxy.onFinish();
            }
        });
    }

    // ====================================================================================
    // 检测
    // ====================================================================================

    private void checkAddFriend(final int uid, final AddFriendProxy _proxy, Activity activity) {
        getModel().checkAddFriend(uid, new BaseModel.DataProxy<Integer>() {
            @Override
            public void onSuccess(Integer data) {
                if (data == 1) {// 需要申请，才能加好友
                    showApplyFriendDialog(uid, _proxy, activity);
                } else {// 不需要验证，直接添加好友
                    addFriend(uid, _proxy);
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                _proxy.onFailure(msg);
                _proxy.onFinish();
            }
        });
    }

    // ====================================================================================
    // 添加非验证的好友
    // ====================================================================================

    private void addFriend(int uid, final AddFriendProxy _proxy) {
        getModel().postAddFriend(uid, new BaseModel.DataProxy<AddFriendResp>() {
            @Override
            public void onSuccess(AddFriendResp addFriendResp) {
                _proxy.onAddFriendResp(addFriendResp);
                _proxy.onFinish();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                _proxy.onFailure(msg);
                _proxy.onFinish();
            }
        });
    }

    // ====================================================================================
    // 好友申请
    // ====================================================================================

    private void showApplyFriendDialog(final int uid, final AddFriendProxy _proxy, Activity activity) {
        new ApplyFriendDialog("我是" + getModel().getCurrUserNick(), new ApplyFriendDialog.OnBtnListener() {
            @Override
            public void onClickPositive(View view, String submitTxt, ApplyFriendDialog dialog) {
                friendApply(uid, submitTxt, dialog, _proxy);
            }

            @Override
            public void onClickNegative(View view, ApplyFriendDialog dialog) {
                dialog.dismiss();
                _proxy.onClickDialogNegativeBtn();
                _proxy.onFinish();
            }
        }).show_unCancel(activity);
    }

    private void friendApply(int uid, String submitTxt, final ApplyFriendDialog dialog, final AddFriendProxy _proxy) {
        getModel().postFriendApply(uid, submitTxt, new BaseModel.DataProxy<FriendApplyResp>() {
            @Override
            public void onSuccess(FriendApplyResp friendApplyResp) {
                dialog.dismiss();
                _proxy.onFriendApplyResp(friendApplyResp);
                _proxy.onFinish();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                _proxy.onFailure(msg);
                _proxy.onFinish();
            }
        });
    }
}
