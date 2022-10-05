package com.tiocloud.chat.feature.group.mgr.mvp;

import com.lzy.okgo.model.Response;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.GroupInfoReq;
import com.watayouxiang.httpclient.model.request.ModifyApplyReq;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;

public class Model extends Contract.Model {
    public Model() {
        super(false);
    }

    @Override
    public void reqModifyInviteSwitch(final boolean isChecked, String groupId, final DataProxy<String> proxy) {
        ModifyApplyReq req = new ModifyApplyReq(isChecked, groupId);
        TioHttpClient.post(this, req, new TaoCallback<BaseResp<String>>() {
            @Override
            public void onSuccess(Response<BaseResp<String>> response) {
                if (response.body().isOk()) {
                    proxy.onSuccess(response.body().getData());
                } else {
                    proxy.onFailure(response.body().getMsg());
                }
            }

            @Override
            public void onError(Response<BaseResp<String>> response) {
                super.onError(response);
                proxy.onFailure(response.getException().getMessage());
            }
        });
    }

    @Override
    public void getGroupInfo(String userFlag, String groupId, final DataProxy<GroupInfoResp> proxy) {
        new GroupInfoReq(userFlag, groupId).setCancelTag(this).get(new TioCallback<GroupInfoResp>() {
            @Override
            public void onTioSuccess(GroupInfoResp groupInfoResp) {
                proxy.onSuccess(groupInfoResp);
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }

//    @Override
//    public void Groupxitongtongzhi(String sysnoticeflag, String groupId, DataProxy<String> proxy) {
//        QunXitongTongZhiReg req = new QunXitongTongZhiReg(sysnoticeflag, groupId);
////        TioHttpClient.post(this, req, new TaoCallback<BaseResp<String>>() {
////            @Override
////            public void onSuccess(Response<BaseResp<String>> response) {
////                if (response.body().isOk()) {
////                    proxy.onSuccess(response.body().getData());
////                } else {
////                    proxy.onFailure(response.body().getMsg());
////                }
////            }
////
////            @Override
////            public void onError(Response<BaseResp<String>> response) {
////                super.onError(response);
////                proxy.onFailure(response.getException().getMessage());
////            }
////        });
//    }
//
//    @Override
//    public void Groupxianshirenshu(String shownumflag, String groupId, DataProxy<String> proxy) {
//
//    }
//
//    @Override
//    public void Groupxixianshigonggao(String goinshownoticeflag, String groupId, DataProxy<String> proxy) {
//
//    }
}
