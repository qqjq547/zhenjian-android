package com.watayouxiang.httpclient.model.response;

import java.util.ArrayList;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/15
 *     desc   :
 * </pre>
 */
public class PayBankCardListResp extends ArrayList<PayBankCardListResp.Data> {

    public static class Data {
        /**
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	--------------------------------------------------
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	| {
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|     "data": [
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|         {
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "backcolor": "#2BC5AF",
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "agrno": "202103150001263162",
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "id": 12,
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "cardtype": 1,
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "bankname": "中国邮政储蓄银行股份有限公司",
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "banklogo": "\/bankicon\/POST.png",
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "cardno": "621799*********5341",
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "bankcode": "PSBC",
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|             "bankwatermark": "\/bankicon\/youzheng.png"
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|         }
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|     ],
         * 2021-03-15 17:48:08.640 I/TioHttpClient: 	|     "ok": true
         * 2021-03-15 17:48:08.641 I/TioHttpClient: 	| }
         * 2021-03-15 17:48:08.641 I/TioHttpClient: 	--------------------------------------------------
         */

        public String backcolor;
        public String agrno;
        public int id;
        public int cardtype;
        public String bankname;
        public String banklogo;
        public String cardno;
        public String bankcode;
        public String bankwatermark;
        public String phone;
    }

}
