package com.tiocloud.commonwallet;

import com.tiocloud.commonwallet.newpay.NewPayWallet;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/03
 *     desc   :
 * </pre>
 */
public class TioWallet {
    private static final iWallet WALLET = new NewPayWallet();

    public static iWallet getWallet() {
        return WALLET;
    }
}
