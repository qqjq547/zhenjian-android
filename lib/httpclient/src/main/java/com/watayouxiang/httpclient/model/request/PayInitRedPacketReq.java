package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/17
 *     desc   : 初始化红包（塞入红包）
 * </pre>
 */
public class PayInitRedPacketReq extends BaseReq<Integer> {

    // 1普通红包，2手气红包
    private final String mode;
    // 会话id
    private final String chatlinkid;
    // 红包数量: 一对一红包数量为1。普通群红包和拼手气红包 数量最大 100 个。
    private final String num;
    // 备注，选填
    private final String remark;
    // 单笔金额: 以分为单位普通红包
    private final String singlecny;
    // 总金额
    private final String cny;

    public PayInitRedPacketReq(String mode, String chatlinkid, String num, String remark, String singlecny, String cny) {
        this.mode = mode;
        this.chatlinkid = chatlinkid;
        this.num = num;
        this.remark = remark;
        this.singlecny = singlecny;
        this.cny = cny;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("mode", mode)
                .append("chatlinkid", chatlinkid)
                .append("num", num)
                .append("remark", remark)
                .append("singlecny", singlecny)
                .append("cny", cny)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<Integer>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/initredpacket.tio_x";
    }
}
