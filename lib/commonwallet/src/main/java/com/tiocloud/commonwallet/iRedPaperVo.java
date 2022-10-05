package com.tiocloud.commonwallet;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/25
 *     desc   :
 * </pre>
 */
public class iRedPaperVo {
    public String avatar;
    public String name;
    public String gift;
    public boolean isSendMsg;
    public String serialNumber;

    public iRedPaperVo(String avatar, String name, String gift, boolean isSendMsg, String serialNumber) {
        this.avatar = avatar;
        this.name = name;
        this.gift = gift;
        this.isSendMsg = isSendMsg;
        this.serialNumber = serialNumber;
    }
}
