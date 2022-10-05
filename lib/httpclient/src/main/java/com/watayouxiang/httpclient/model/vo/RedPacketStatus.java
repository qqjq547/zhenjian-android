package com.watayouxiang.httpclient.model.vo;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/30
 *     desc   : 红包状态（易支付）
 * </pre>
 */
public class RedPacketStatus {
    /**
     * 结束成功
     */
    public static final String SUCCESS = "SUCCESS";
    /**
     * 失败
     */
    public static final String FAIL = "FAIL";
    /**
     * 超时
     */
    public static final String TIMEOUT = "TIMEOUT";
    /**
     * 抢红包中
     */
    public static final String SEND = "SEND";
    /**
     * 取消
     */
    public static final String CANCEL = "CANCEL";
}
