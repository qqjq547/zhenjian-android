package com.tiocloud.chat.constant;

import com.tiocloud.chat.BuildConfig;

public class TioConfig {
    public static  final String BASE_URL = "";

    /**
     * 握手密钥
     */
    public static final String HAND_SHAKE_KEY = "WS52U4uV0pztMu7";

    public static  String OSS_objectName = "ip/"+ BuildConfig.ip+".json";

    // 访问的endpoint地址
    public static final String OSS_ENDPOINT = "oss-accelerate.aliyuncs.com";
    public static final String BUCKET_NAME = "tyiposs";
    public static final String OSS_ACCESS_KEY_ID = "LTAI5tHpHfyaVihRsNKiNb3o";
    public static final String OSS_ACCESS_KEY_SECRET = "iQZNj7BsHit2GBB6sFIM0oJdBE9duU";
}
