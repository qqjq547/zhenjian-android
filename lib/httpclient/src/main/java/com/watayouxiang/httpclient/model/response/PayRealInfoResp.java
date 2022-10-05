package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   :
 * </pre>
 */
public class PayRealInfoResp {
    /**
     * 2021-03-15 14:20:08.783 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-15 14:20:08.783 I/TioHttpClient: 	| {
     * 2021-03-15 14:20:08.783 I/TioHttpClient: 	|     "data": {
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	|         "mobile": "18768177675",
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	|         "name": "王涛",
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	|         "id": 88,
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	|         "cardno": "330329********2459"
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	|     },
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	|     "ok": true
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	| }
     * 2021-03-15 14:20:08.784 I/TioHttpClient: 	--------------------------------------------------
     */

    public String mobile;
    public String name;
    // 身份证id
    public String cardno;
    public String id;

}
