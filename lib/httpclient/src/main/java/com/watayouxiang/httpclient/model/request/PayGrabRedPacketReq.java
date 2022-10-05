package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayGrabRedPacketResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/24
 *     desc   :
 * </pre>
 */
public class PayGrabRedPacketReq extends BaseReq<PayGrabRedPacketResp> {
    /**
     * 红包的订单号（易支付）
     */
    private final String serialnumber;
    /**
     * 会话id
     */
    private final String chatlinkid;
    /**
     * 红包id（新生支付）
     */
    private String rid;

    public static PayGrabRedPacketReq getInstance(String rid, String chatlinkid) {
        return new PayGrabRedPacketReq(null, chatlinkid, rid);
    }

    private PayGrabRedPacketReq(String serialnumber, String chatlinkid, String rid) {
        this.serialnumber = serialnumber;
        this.chatlinkid = chatlinkid;
        this.rid = rid;
    }

    public PayGrabRedPacketReq(String serialnumber, String chatlinkid) {
        this.serialnumber = serialnumber;
        this.chatlinkid = chatlinkid;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("serialnumber", serialnumber)
                .append("chatlinkid", chatlinkid)
                .append("rid", rid)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayGrabRedPacketResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/grabRedpacket.tio_x";
    }
}
