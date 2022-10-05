package com.watayouxiang.httpclient.model.request;

import com.google.gson.reflect.TypeToken;
import com.watayouxiang.httpclient.model.BaseReq;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.TioMap;
import com.watayouxiang.httpclient.model.response.PayWithholdResp;
import com.watayouxiang.httpclient.utils.MD5Utils;

import java.lang.reflect.Type;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/18
 *     desc   :
 * </pre>
 */
public class PayWithholdReq extends BaseReq<PayWithholdResp> {
    /**
     * 充值金额，字符串，分
     */
    private final String amount;
    /**
     * 备注，选填
     */
    private String remark;
    /**
     * 提现的银行卡签约号
     */
    private String agrno;
    /**
     * 支付密码加密同登录
     */
    private String paypwd;
    /**
     *  1 是支付宝 2 不是
     */
    private String alipayFlag;
    private String cardid;



    // 易支付
    public PayWithholdReq(String amount) {
        this.amount = amount;
    }

    // 新生支付
    public PayWithholdReq(String amount, String cardid, String paypwd, String loginname,String alipayFlag) {
        this.amount = amount;
        this.cardid=cardid;
        this.alipayFlag = alipayFlag;
        this.paypwd = MD5Utils.getMd5("${" + loginname + "}" + paypwd);
    }

    @Override
    public TioMap<String, String> params() {
        return TioMap.getParamMap()
                .append("amount", amount)
                .append("remark", remark)
                .append("cardid", cardid)
                .append("paypwd", paypwd)
                .append("alipayFlag", alipayFlag)
                ;
    }

    @Override
    public Type bodyType() {
        return new TypeToken<BaseResp<PayWithholdResp>>() {
        }.getType();
    }

    @Override
    public String path() {
        return "/mytio/pay/withhold.tio_x";
    }
}
