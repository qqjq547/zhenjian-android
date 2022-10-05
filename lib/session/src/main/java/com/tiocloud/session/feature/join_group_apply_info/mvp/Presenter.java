package com.tiocloud.session.feature.join_group_apply_info.mvp;

import android.text.TextUtils;

import com.watayouxiang.androidutils.listener.TioSuccessCallback;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.model.request.DealGroupApplyReq;
import com.watayouxiang.httpclient.model.request.GroupApplyInfoReq;
import com.watayouxiang.httpclient.model.response.GroupApplyInfoResp;

public class Presenter extends Contract.Presenter {
    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public void init() {
        getView().resetUI();

        reqApplyInfo();
    }

    @Override
    public void agreeApply(GroupApplyInfoResp.ApplyBean apply) {
        int aid = apply.getId();
        String mid = getView().getMid();
        if (aid == 0 || TextUtils.isEmpty(mid)) {
            TioToast.showShort("请求参数为空");
            return;
        }
        new DealGroupApplyReq(mid, aid + "").setCancelTag(this).post(new TioSuccessCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                TioToast.showShort("同意邀请成功");
                getView().onAgreeSuccess(mid, aid);
            }
        });
    }

    private void reqApplyInfo() {
        long aid = getView().getAid();
        if (aid == 0) {
            TioToast.showShort("aid为空");
            return;
        }
        new GroupApplyInfoReq(String.valueOf(aid)).setCancelTag(this).get(new TioSuccessCallback<GroupApplyInfoResp>() {
            @Override
            public void onTioSuccess(GroupApplyInfoResp resp) {
                if (resp != null) getView().onApplyInfoResp(resp);
            }

            @Override
            public void onTioError(String msg) {
                super.onTioError(msg);
                getView().finish();
            }
        });
    }
}
