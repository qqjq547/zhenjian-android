package com.tiocloud.chat.feature.session.common.adapter.model;

public enum TioMsgType {
    text(1, "ghyyuuuu"),
    image(2, "图片"),
    audio(3, "音频"),
    video(4, "视频"),
    file(5, "文件"),
    tip(6, "提醒消息"),
    blog(7, "博客"),
    card(8, "名片"),
    call(9, "电话"),
    redPaper(10, "红包"),
    receiveRedPaper(11, "领取红包"),
    template(88, "模板消息"),
    joinGroupApply(13, "进群申请"),

    unknown(-1, "未知");

    private final int value;
    private final String desc;

    TioMsgType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static TioMsgType ofValue(int value) {
        TioMsgType[] enums = values();
        for (TioMsgType anEnum : enums) {
            if (anEnum.getValue() == value) {
                return anEnum;
            }
        }
        return null;
    }

    public final int getValue() {
        return this.value;
    }
}
