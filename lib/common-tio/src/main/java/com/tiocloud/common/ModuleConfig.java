package com.tiocloud.common;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/26
 *     desc   :
 * </pre>
 */
public class ModuleConfig {
    /**
     * 水印
     */
    public static final boolean ENABLE_WATERMARK = true;
    /**
     * 手动更新
     */
    public static final boolean ENABLE_MANUAL_UPDATE = true;
    /**
     * 二维码
     */
    public static final boolean ENABLE_QR_CODE = false;
    /**
     * 钱包
     */
    public static final boolean ENABLE_WALLET = true;
    /**
     * 三方登录
     */
    public static final boolean ENABLE_THIRD_PARTY_LOGIN = true;
    /**
     * 短信登录
     */
    public static final boolean ENABLE_SMS_LOGIN = true;
    /**
     * 是否为 "测试模式"
     */
    public static final boolean DEBUG = BuildConfig.DEBUG;
    /**
     * 是否打开钱包里面充值按钮
     */
    public static final boolean ENABLE_RECHARGE  = false;
    /**
     * 是否打开钱包里面提款按钮
     */
    public static final boolean ENABLE_WITHDRAW  = false;
    /**
     * 自动登录是否开启模拟器验证
     */
    public static final boolean ENABLE_REAL_DEVICE = true;
}
