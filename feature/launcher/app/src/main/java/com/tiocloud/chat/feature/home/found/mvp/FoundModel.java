package com.tiocloud.chat.feature.home.found.mvp;

import androidx.annotation.Nullable;

import com.lzy.okgo.cache.CacheMode;
import com.tiocloud.chat.feature.main.fragment.MainFoundFragment;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.FoundListReq;
import com.watayouxiang.httpclient.model.request.FoundListResp;
import com.watayouxiang.androidutils.widget.TioToast;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public class FoundModel extends FoundContract.Model {
    @Override
    public void requestFoundList(final DataProxy<FoundListResp> proxy) {
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

    @Override
    public void setGroupNum(@Nullable MainFoundFragment fragment, int groupNum) {
        if (fragment != null) {
            fragment.setAppendTitle(groupNum);
        } else {
            TioToast.showShort("获取不到父容器");
        }
    }
}