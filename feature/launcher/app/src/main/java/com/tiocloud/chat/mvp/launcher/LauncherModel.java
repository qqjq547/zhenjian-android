package com.tiocloud.chat.mvp.launcher;

import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.lzy.okgo.cache.CacheMode;
import com.tiocloud.chat.feature.share.friend.mvp.ShareFriendPresenter;
import com.tiocloud.chat.util.PreferencesUtil;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.ConfigReq;
import com.watayouxiang.httpclient.model.response.ConfigResp;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc :
 */
public class LauncherModel extends LauncherContract.Model {
    @Override
    public void requestConfig(final DataProxy<ConfigResp> proxy) {
        ConfigReq configReq = new ConfigReq();
        configReq.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        configReq.setCancelTag(this);
        configReq.get(new TioCallbackImpl<ConfigResp>() {
            @Override
            public void onTioSuccess(ConfigResp configResp) {
                Log.d("====config success==" ,configResp.toString());
                PreferencesUtil.saveInt("im_burst_transfer_file",configResp.im_burst_transfer_file);
                PreferencesUtil.saveInt("im_file_encrypt",configResp.im_file_encrypt);
                PreferencesUtil.saveString("app_find_page_base_list", GsonUtils.toJson(configResp.app_find_page_base_list));
                PreferencesUtil.saveInt("appinvitecodeflag",configResp.appinvitecodeflag);
                // 回调
                proxy.onSuccess(configResp);
            }

            @Override
            public void onTioError(String msg) {
                TioLogger.d("req config error: " + msg);
                if (msg != null) {
                    proxy.onFailure(msg);
                }
            }
        });
    }

    @Override
    public boolean isLogin() {
        return CurrUserTableCrud.curr_isLogin();
    }
}
