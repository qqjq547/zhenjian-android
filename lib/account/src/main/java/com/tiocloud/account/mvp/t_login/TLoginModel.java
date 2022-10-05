package com.tiocloud.account.mvp.t_login;

import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.ConfigReq;
import com.watayouxiang.httpclient.model.request.TLogin1Req;
import com.watayouxiang.httpclient.model.request.TLogin2Req;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.ConfigResp;
import com.watayouxiang.httpclient.model.response.TLogin2Resp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.social.entities.QQInfoEntity;
import com.watayouxiang.social.entities.ThirdInfoEntity;
import com.watayouxiang.social.entities.WXInfoEntity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 12/28/20
 *     desc   :
 * </pre>
 */
public class TLoginModel extends TLoginContract.Model {

    @Override
    public void reqTLogin1(String type, String openid, String unionid, String token, TioCallback<String> callback) {
        new TLogin1Req(type, openid, unionid, token).setCancelTag(this).post(callback);
    }

    @Override
    public void reqTLogin1QQ(String openid, String unionid, String token, TioCallback<String> callback) {
        TLogin1Req.getQQInstance(openid, unionid, token).setCancelTag(this).post(callback);
    }

    @Override
    public void reqTLogin1WX(String openid, String unionid, String token, TioCallback<String> callback) {
        TLogin1Req.getWXInstance(openid, unionid, token).setCancelTag(this).post(callback);
    }

    @Override
    public void reqTLogin2(String type, String unionid, String uuid, String openid, String nick, String avatar, String sex, String qq_is_yellow_vip, String qq_yellow_vip_level, String wx_country, String wx_province, String wx_city, TioCallback<TLogin2Resp> callback) {
        new TLogin2Req(type, unionid, uuid, openid, nick, avatar, sex, qq_is_yellow_vip, qq_yellow_vip_level, wx_country, wx_province, wx_city).setCancelTag(this).post(callback);
    }

    @Override
    public void reqTLogin2QQ(String unionid, String uuid, String openid, String nick, String avatar, String sex, String qq_is_yellow_vip, String qq_yellow_vip_level, TioCallback<TLogin2Resp> callback) {
        TLogin2Req.getQQInstance(unionid, uuid, openid, nick, avatar, sex, qq_is_yellow_vip, qq_yellow_vip_level).setCancelTag(this).post(callback);
    }

    @Override
    public void reqTLogin2WX(String unionid, String uuid, String openid, String nick, String avatar, String sex, String wx_country, String wx_province, String wx_city, TioCallback<TLogin2Resp> callback) {
        TLogin2Req.getWXInstance(unionid, uuid, openid, nick, avatar, sex, wx_country, wx_province, wx_city).setCancelTag(this).post(callback);
    }

    @Override
    public void reqUserCurr(TioCallback<UserCurrResp> callback) {
        new UserCurrReq().setCancelTag(this).get(callback);
    }

    @Override
    public void reqConfig(TioCallback<ConfigResp> callback) {
        new ConfigReq().setCancelTag(this).get(callback);
    }

    // ====================================================================================
    // 二次封装
    // ====================================================================================

    @Override
    public void reqLoginQQ(ThirdInfoEntity info, String token, DataProxy<UserCurrResp> proxy) {
        if (info == null) {
            proxy.onFailure("ThirdInfoEntity is null");
            return;
        }

        String openId = info.getOpenId();
        String unionId = info.getUnionId();

        // 第1步
        reqTLogin1QQ(openId, unionId, token, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String uuid) {

                // 参数
                String nickname = info.getNickname();
                String avatar = info.getAvatar();
                QQInfoEntity qqInfo = info.getQqInfo();
                String gender = null;
                String is_yellow_vip = null;
                String yellow_vip_level = null;
                if (qqInfo != null) {
                    gender = qqInfo.getGender();
                    if ("男".equals(gender)) {
                        gender = "1";
                    } else if ("女".equals(gender)) {
                        gender = "2";
                    }
                    is_yellow_vip = qqInfo.getIs_yellow_vip();
                    yellow_vip_level = qqInfo.getYellow_vip_level();
                }

                // 第2步
                // String unionid, String uuid, String openid, String nick, String avatar, String sex,
                // String qq_is_yellow_vip, String qq_yellow_vip_level,
                reqTLogin2QQ(unionId, uuid, openId, nickname, avatar, gender, is_yellow_vip, yellow_vip_level, new TioCallback<TLogin2Resp>() {
                    @Override
                    public void onTioSuccess(TLogin2Resp tLogin2Resp) {
                        // 登录成功
                        onTLoginSuccess(proxy);
                    }

                    @Override
                    public void onTioError(String msg) {
                        proxy.onFailure(msg);
                    }
                });
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }

    @Override
    public void reqLoginWX(ThirdInfoEntity info, String token, DataProxy<UserCurrResp> proxy) {
        if (info == null) {
            proxy.onFailure("ThirdInfoEntity is null");
            return;
        }

        String openId = info.getOpenId();
        String unionId = info.getUnionId();

        // 第1步
        reqTLogin1WX(openId, unionId, token, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String uuid) {

                // 参数
                String nickname = info.getNickname();
                String avatar = info.getAvatar();
                String gender = null;
                String country = null;
                String province = null;
                String city = null;
                WXInfoEntity wxInfo = info.getWxInfo();
                if (wxInfo != null) {
                    gender = wxInfo.getSex() + "";
                    country = wxInfo.getCountry();
                    province = wxInfo.getProvince();
                    city = wxInfo.getCity();
                }

                // 第2步
                // String unionid, String uuid, String openid, String nick, String avatar, String sex,
                // String wx_country, String wx_province, String wx_city,
                reqTLogin2WX(unionId, uuid, openId, nickname, avatar, gender, country, province, city, new TioCallback<TLogin2Resp>() {
                    @Override
                    public void onTioSuccess(TLogin2Resp tLogin2Resp) {
                        // 登录成功
                        onTLoginSuccess(proxy);
                    }

                    @Override
                    public void onTioError(String msg) {
                        proxy.onFailure(msg);
                    }
                });
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }

    private void onTLoginSuccess(DataProxy<UserCurrResp> proxy) {
        reqUserCurr(new TioCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                proxy.onSuccess(resp);
            }

            @Override
            public void onTioError(String msg) {
                proxy.onFailure(msg);
            }
        });
    }
}
