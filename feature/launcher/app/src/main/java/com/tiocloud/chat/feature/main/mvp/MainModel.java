package com.tiocloud.chat.feature.main.mvp;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.FoundListReq;
import com.watayouxiang.httpclient.model.request.FoundListResp;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public class MainModel extends MainContract.Model {

    @Override
    public void requestFoundList(DataProxy<FoundListResp> proxy) {
        FoundListReq foundListReq = new FoundListReq("");
        foundListReq.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        foundListReq.setCancelTag(this);
        TioHttpClient.get(foundListReq, new TioCallback<FoundListResp>() {
            @Override
            public void onTioSuccess(FoundListResp foundListResp) {
                proxy.onSuccess(foundListResp);
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }
}
