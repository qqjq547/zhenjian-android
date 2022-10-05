package com.tiocloud.newpay.feature.bankcard_remove;

import java.io.Serializable;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/16
 *     desc   :
 * </pre>
 */
public class RemoveBankCardVo implements Serializable {
    // 卡列表返回值id
    public String bankcardid;
    // 协议绑卡agrno
    public String agrno;

    public RemoveBankCardVo(String bankcardid, String agrno) {
        this.bankcardid = bankcardid;
        this.agrno = agrno;
    }
}
