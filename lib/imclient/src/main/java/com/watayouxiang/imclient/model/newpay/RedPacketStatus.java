package com.watayouxiang.imclient.model.newpay;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/19
 *     desc   : 新生支付 - 红包状态
 * </pre>
 */
public class RedPacketStatus {
    /**
     * 处理中-发送中-SEND
     */
    public static final byte PROCESS = 1;

    /**
     * 初始化
     */
    public static final byte INIT = 2;

    /**
     * 支付中
     */
    public static final byte PAYING = 3;

    /**
     * 支付确认中
     */
    public static final byte PAYING_CONFIRM = 4;

    /**
     * 正常结束
     */
    public static final byte SUCCESS  = 5;

    /**
     * 超时结束
     */
    public static final byte TIMEOUT = 6;

    /**
     * 取消结束
     */
    public static final byte CANCEL = 7;

    /**
     * 失败
     */
    public static final byte FAIL = 8;
}