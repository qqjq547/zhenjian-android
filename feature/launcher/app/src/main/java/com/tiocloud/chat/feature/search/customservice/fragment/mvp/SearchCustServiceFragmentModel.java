package com.tiocloud.chat.feature.search.customservice.fragment.mvp;

import com.lzy.okgo.model.Response;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.CustServiceSearchReq;
import com.watayouxiang.httpclient.model.request.UserSearchReq;
import com.watayouxiang.httpclient.model.response.CustServiceTeamListResp;
import com.watayouxiang.httpclient.model.response.UserSearchResp;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public class SearchCustServiceFragmentModel extends SearchCustServiceFragmentContract.Model {

    @Override
    public void searchCustService(String keyWord, int pageNumber, final DataProxy<CustServiceTeamListResp> proxy) {
        new CustServiceSearchReq(keyWord,pageNumber).setCancelTag(this).get(new TioCallbackImpl<CustServiceTeamListResp>() {
            @Override
            public void onTioSuccess(CustServiceTeamListResp resp) {
                if (resp != null) {
                    proxy.onSuccess(resp);
                }
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }

}
