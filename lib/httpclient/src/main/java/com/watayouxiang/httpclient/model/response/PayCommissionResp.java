package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/19
 *     desc   :
 * </pre>
 */
public class PayCommissionResp {
    /**
     * 2021-03-19 17:23:30.649 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-19 17:23:30.649 I/TioHttpClient: 	| {
     * 2021-03-19 17:23:30.649 I/TioHttpClient: 	|     "data": {
     * 2021-03-19 17:23:30.649 I/TioHttpClient: 	|         "min": 30,
     * 2021-03-19 17:23:30.649 I/TioHttpClient: 	|         "rate": 10,
     * 2021-03-19 17:23:30.650 I/TioHttpClient: 	|         "max": 200000,
     * 2021-03-19 17:23:30.650 I/TioHttpClient: 	|         "withholdconst": 100
     * 2021-03-19 17:23:30.650 I/TioHttpClient: 	|     },
     * 2021-03-19 17:23:30.650 I/TioHttpClient: 	|     "ok": true
     * 2021-03-19 17:23:30.650 I/TioHttpClient: 	| }
     * 2021-03-19 17:23:30.650 I/TioHttpClient: 	--------------------------------------------------
     */

    /**
     * 最小提现值 (单位：分)
     */
    public long min;
    /**
     * 利率 (单位：千分)
     */
    public long rate;
    /**
     * 最多提现值 (单位：分)
     */
    public long max;
    /**
     * 固定提现手续费 (单位：分)
     */
    public long withholdconst;
    /**
     * 计算好的手续费（分）
     * <p>
     * 注：只有请求传了 amount 参数时才有值
     */
    public long commission;
}
