package com.watayouxiang.imclient.model.body.wx.msg;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/06/05
 *     desc   :
 * </pre>
 */
public enum InnerMsgType {
    TEXT(1, "文字", String.class),
    BLOG(2, "博客", null),
    FILE(3, "文件", InnerMsgFile.class),
    AUDIO(4, "音频", InnerMsgAudio.class),
    VIDEO(5, "视频", InnerMsgVideo.class),
    IMAGE(6, "图片", InnerMsgImage.class),
    CARD(9, "明片", InnerMsgCard.class),
    CALL_VIDEO(10, "视频电话", InnerMsgCall.class),
    CALL_AUDIO(11, "音频电话", InnerMsgCall.class),
    RED_PAPER(12, "红包", InnerMsgRed.class),
    JOIN_GROUP_APPLY(13, "进群申请", InnerMsgApply.class),
    TEMPLATE(88, "模版消息", InnerMsgTemplate.class);

    private int code;
    private String desc;
    private Object reference;

    InnerMsgType(int code, String desc, Object reference) {
        this.code = code;
        this.desc = desc;
        this.reference = reference;
    }

    public static InnerMsgType valueOf(int code) {
        InnerMsgType[] values = values();
        for (int i = 0, len = values.length; i < len; i++) {
            if (values[i].code == code) {
                return values[i];
            }
        }
        return null;
    }
}
