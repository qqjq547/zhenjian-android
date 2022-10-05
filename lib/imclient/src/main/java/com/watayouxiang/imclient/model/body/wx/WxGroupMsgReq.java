package com.watayouxiang.imclient.model.body.wx;

/**
 * author : TaoWang
 * date : 2020/3/12
 * desc :
 */
public class WxGroupMsgReq {
    private String chatlinkid;
    /**
     * 开始的消息id，此字段传递是，一般代表第一页（不含）以后的数据
     * <p>
     * endmid和startmid互斥
     */
    private String startmid;
    /**
     * 截至消息id,同步使用，获取最新到endmid的消息
     * <p>
     * endmid和startmid互斥
     */
    private String endmid;

    private WxGroupMsgReq(String chatlinkid, String startmid, String endmid) {
        this.chatlinkid = chatlinkid;
        this.startmid = startmid;
        this.endmid = endmid;
    }

    public static WxGroupMsgReq startMid(String chatlinkid, String startmid) {
        return new WxGroupMsgReq(chatlinkid, startmid, null);
    }

    public static WxGroupMsgReq endMid(String chatlinkid, String endmid) {
        return new WxGroupMsgReq(chatlinkid, null, endmid);
    }

    @Override
    public String toString() {
        return "WxGroupMsgReq{" +
                "chatlinkid='" + chatlinkid + '\'' +
                ", startmid='" + startmid + '\'' +
                ", endmid='" + endmid + '\'' +
                '}';
    }
}
