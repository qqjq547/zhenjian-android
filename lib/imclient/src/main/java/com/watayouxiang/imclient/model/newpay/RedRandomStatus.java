package com.watayouxiang.imclient.model.newpay;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/19
 *     desc   : 红包分配状态
 * </pre>
 */
public class RedRandomStatus {
    /**
     * 已转账
     */
    public static final byte SUCCESS = 1;

    /**
     * 初始化
     */
    public static final byte INIT = 2;

    /**
     * 分配中
     */
    public static final byte RANDOM = 3;

    /**
     * 超时
     */
    public static final byte TIMECOUT = 4;
}