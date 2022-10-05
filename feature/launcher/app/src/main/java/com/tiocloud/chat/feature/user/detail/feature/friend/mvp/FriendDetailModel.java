package com.tiocloud.chat.feature.user.detail.feature.friend.mvp;

import com.lzy.okgo.model.Response;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.model.request.UserInfoReq;
import com.watayouxiang.httpclient.model.response.UserInfoResp;
import com.watayouxiang.httpclient.model.BaseResp;

/**
 * author : TaoWang
 * date : 2020-02-21
 * desc :
 */
public class FriendDetailModel extends FriendDetailContract.Model {
    @Override
    public void getUserInfo(String uid, final DataProxy<UserInfoResp> proxy) {
        UserInfoReq req = new UserInfoReq(uid);
        TioHttpClient.get(this, req, new TaoCallback<BaseResp<UserInfoResp>>() {
            @Override
            public void onSuccess(Response<BaseResp<UserInfoResp>> response) {
                UserInfoResp data = response.body().getData();
                if (data != null) {
                    proxy.onSuccess(data);
                } else {
                    proxy.onFailure(response.body().getMsg());
                }
            }
        });
    }
}
