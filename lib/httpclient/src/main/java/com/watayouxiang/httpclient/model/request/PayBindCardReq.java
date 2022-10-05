package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayBindCardResp;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   : 发起绑卡（发短信业务）
 * </pre>
 */
public class PayBindCardReq extends BaseReq<PayBindCardResp> {
    // 信用卡-Y 信用卡ccv2
    private String cvv2;
    // 信用卡-Y 信用卡有效期
    private String availabledate;

    private final String name;
    private final String mobile;
    // 证件号码-Y
    private final String cardno;
    // 银行卡code-Y
    private final String bankcardno;
    private String cardtype;
    private String bankname;// 银行code
    // 储蓄卡
    public PayBindCardReq(String name, String mobile, String cardno, String bankcardno,String cardtype,String bankname) {
        this.name = name;
        this.mobile = mobile;
        this.cardno = cardno;
        this.bankcardno = bankcardno;
        this.cardtype = cardtype;
        this.bankname = bankname;
    }

    // 信用卡
    public PayBindCardReq(String cvv2, String availabledate, String name, String mobile, String cardno, String bankcardno,String cardtype,String bankname) {
        this.cvv2 = cvv2;
        this.availabledate = availabledate;
        this.name = name;
        this.mobile = mobile;
        this.cardno = cardno;
        this.bankcardno = bankcardno;
        this.cardtype = cardtype;
        this.bankname = bankname;
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("name", name)
                .append("mobile", mobile)
                .append("cardno", cardno)
                .append("bankcardno", bankcardno)
                .append("cardtype", cardtype)
                .append("bankcode", "")
                .append("bankname", bankname)

                .append("cvv2", cvv2)
                .append("availabledate", availabledate)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayBindCardResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/bindcard.tio_x";
    }
}
