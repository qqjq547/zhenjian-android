package com.watayouxiang.qrcode;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/09
 *     desc   :
 * </pre>
 */
public class TioQRCode {

    private static QRCodeBridge bridge;

    static {
        // TAG = "BGAQRCode"
        BGAQRCodeUtil.setDebug(BuildConfig.DEBUG);
    }

    public static void init(QRCodeBridge bridge) {
        TioQRCode.bridge = bridge;
    }

    public static QRCodeBridge getBridge() {
        return bridge;
    }
}
