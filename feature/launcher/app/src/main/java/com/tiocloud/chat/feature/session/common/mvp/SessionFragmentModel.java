package com.tiocloud.chat.feature.session.common.mvp;

import com.lzy.okgo.cache.CacheMode;
import com.watayouxiang.httpclient.model.request.UserCurrReq;

public class SessionFragmentModel extends SessionFragmentContract.Model {
    public SessionFragmentModel() {
        super(false);
    }

    @Override
    public UserCurrReq getUserCurrReq() {
        UserCurrReq userCurrReq = new UserCurrReq();
        userCurrReq.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        userCurrReq.setCancelTag(this);
        return userCurrReq;
    }

}
