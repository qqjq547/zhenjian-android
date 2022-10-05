package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   :
 * </pre>
 */
public class PayBindCardResp {
    /**
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	| {
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|     "data": {
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "cardno": "621799*********5341",
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "id": 9,
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "merid": "300008795977",
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "merorderid": "2021031516768137",
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "phone": "18768177675",
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "reqid": "R007_37878_20210315161259",
     * 2021-03-15 16:13:01.115 I/TioHttpClient: 	|         "uid": 37878,
     * 2021-03-15 16:13:01.116 I/TioHttpClient: 	|         "username": "王涛",
     * 2021-03-15 16:13:01.116 I/TioHttpClient: 	|         "walletid": "100011563457"
     * 2021-03-15 16:13:01.116 I/TioHttpClient: 	|     },
     * 2021-03-15 16:13:01.116 I/TioHttpClient: 	|     "ok": true
     * 2021-03-15 16:13:01.116 I/TioHttpClient: 	| }
     * 2021-03-15 16:13:01.116 I/TioHttpClient: 	--------------------------------------------------
     */

    public String cardno;
    public int id;
    public String merid;
    public String merorderid;
    public String phone;
    public String reqid;
    public int uid;
    public String username;
    public String walletid;

}
