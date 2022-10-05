package com.watayouxiang.httpclient.model.vo;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/22
 *     desc   : 红包状态（新生支付）
 * </pre>
 */
public interface RedPacketStatus2 {
    /**
     * 处理中-发送中-SEND
     */
    byte PROCESS = 1;

    /**
     * 初始化
     */
    byte INIT = 2;

    /**
     * 支付中
     */
    byte PAYING = 3;

    /**
     * 支付确认中
     */
    byte PAYING_CONFIRM = 4;

    /**
     * 正常结束
     */
    byte SUCCESS = 5;

    /**
     * 超时结束
     */
    byte TIMEOUT = 6;

    /**
     * 取消结束
     */
    byte CANCEL = 7;

    /**
     * 失败
     */
    byte FAIL = 8;
}
