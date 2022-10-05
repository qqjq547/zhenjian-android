package com.tiocloud.newpay.feature.bill.fragment;

import java.io.Serializable;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/26
 *     desc   :
 * </pre>
 */
public class BillVo implements Serializable {
    public static final BillVo ALL = new BillVo("全部", null);
    public static final BillVo RECHARGE = new BillVo("充值", "1");
    public static final BillVo WITHDRAW = new BillVo("提现", "2");
    public static final BillVo RED_PAPER = new BillVo("红包", "3");

    public String name;
    public String mode;

    private BillVo(String name, String mode) {
        this.name = name;
        this.mode = mode;
    }
}
