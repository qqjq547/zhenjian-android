package com.tiocloud.account;

import com.blankj.utilcode.util.Utils;
import com.tiocloud.account.feature.bind_phone.BindPhoneActivity;
import com.tiocloud.account.feature.t_bind_phone.TBindPhoneActivity;
import com.tiocloud.common.ModuleConfig;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/21
 *     desc   :
 * </pre>
 */
public class TioAccount {

    private static AccountBridge BRIDGE = null;

    public static void init(AccountBridge bridge) {
        BRIDGE = bridge;
    }

    public static AccountBridge getBridge() {
        return BRIDGE;
    }

    /**
     * 检测是否绑定手机
     */
    public static void checkIsBindPhone(UserCurrResp resp) {
        boolean bindPhone = resp.isBindPhone();
        boolean thirdbindflag = resp.isThirdbindflag();

        if (!bindPhone) {
            if (thirdbindflag) {
                if (!ModuleConfig.ENABLE_THIRD_PARTY_LOGIN) return;
                TBindPhoneActivity.start(Utils.getApp());
            } else {
                if (!ModuleConfig.ENABLE_SMS_LOGIN) return;
                BindPhoneActivity.start(Utils.getApp());
            }
        }
    }

}
