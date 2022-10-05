package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/27
 *     desc   :
 * </pre>
 */
public class PayGrabRedPacketStatResp {

    /**
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	--------------------------------------------------
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	| {
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|     "data": {
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|         "nick": "wata",
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|         "num": "3",
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|         "avatar": "\/user\/base\/avatar\/20200115\/7\/1432571217333833301630976.png",
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|         "cny": 6
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|     },
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	|     "ok": true
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	| }
     * 2021-03-19 15:25:01.290 I/TioHttpClient: 	--------------------------------------------------
     */

    /**
     * amount : 19
     * num : 18
     */

    private int amount;
    public int cny;
    private String num;
    private String avatar;
    private String nick;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
