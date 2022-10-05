package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/18
 *     desc   :
 * </pre>
 */
public class PayQuickRedPacketResp {
    /**
     * 新生支付
     *
     * 2021-03-18 16:08:47.461 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-18 16:08:47.461 I/TioHttpClient: 	| {
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|     "data": {
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "agrno": "202103160001270162",
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "bizcreattime": "2021-03-18 16:08:46",
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "id": 48,
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "merid": "300008795977",
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "merorderid": "2021031816157224",
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "ordererrormsg": "",
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "paynotifyurl": "https:\/\/tx.t-io.org\/mytio\/paycallback\/redpacket.tio_x?uid=37878",
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "paytimeout": 5,
     * 2021-03-18 16:08:47.462 I/TioHttpClient: 	|         "paytimeouttime": 1616055227258,
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	|         "paytype": 2,
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	|         "reqid": "T007_37878_20210318160846",
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	|         "status": 3,
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	|         "uid": 37878
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	|     },
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	|     "ok": true
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	| }
     * 2021-03-18 16:08:47.463 I/TioHttpClient: 	--------------------------------------------------
     */

    // 快捷支付确认订单
    public String merorderid;
    // 红包id
    public String id;
}
