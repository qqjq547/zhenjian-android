package com.tiocloud.account.mvp.t_login;

import android.app.Activity;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.ConfigResp;
import com.watayouxiang.httpclient.model.response.TLogin2Resp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.social.entities.ThirdInfoEntity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 12/28/20
 *     desc   :
 * </pre>
 */
public interface TLoginContract {
    interface View extends BaseView {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(View view) {
            super(new TLoginModel(), view);
        }

        public abstract void startTLogin(ThirdInfoEntity info, Activity activity);
    }

    abstract class Model extends BaseModel {
        public abstract void reqTLogin1(String type, String openid, String unionid, String token, TioCallback<String> callback);

        public abstract void reqTLogin1QQ(String openid, String unionid, String token, TioCallback<String> callback);

        public abstract void reqTLogin1WX(String openid, String unionid, String token, TioCallback<String> callback);

        public abstract void reqTLogin2(String type, String unionid, String uuid, String openid, String nick, String avatar, String sex,
                                        String qq_is_yellow_vip, String qq_yellow_vip_level,
                                        String wx_country, String wx_province, String wx_city,
                                        TioCallback<TLogin2Resp> callback);

        public abstract void reqTLogin2QQ(String unionid, String uuid, String openid, String nick, String avatar, String sex,
                                          String qq_is_yellow_vip, String qq_yellow_vip_level,
                                          TioCallback<TLogin2Resp> callback);

        public abstract void reqTLogin2WX(String unionid, String uuid, String openid, String nick, String avatar, String sex,
                                          String wx_country, String wx_province, String wx_city,
                                          TioCallback<TLogin2Resp> callback);

        public abstract void reqUserCurr(TioCallback<UserCurrResp> callback);

        public abstract void reqConfig(TioCallback<ConfigResp> callback);

        public abstract void reqLoginQQ(ThirdInfoEntity info, String token, DataProxy<UserCurrResp> proxy);

        public abstract void reqLoginWX(ThirdInfoEntity info, String token, DataProxy<UserCurrResp> proxy);
    }
}
