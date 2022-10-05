package com.watayouxiang.httpclient.model.response;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/11/17
 *     desc   :
 * </pre>
 */
public class PayGetWalletInfoResp {
    /**
     * walletId : 6288883250000000103
     * nameDesc : 王*
     * idCardRzStatus : SUCCESS
     * personRzStatus : INIT
     * setUpPasswrod : false
     * balance : 0
     * idCardNoDesc : 3303***459
     * operatorRzStatus : SUCCESS
     * mobileDesc : 187***7675
     */

    /**
     * 新生支付
     * I: 	--------------------------------------------------
     * I: 	| {
     * I: 	|     "data": {
     * I: 	|         "mercny": 0,
     * I: 	|         "walletid": "100011563457",
     * I: 	|         "uid": 37878,
     * I: 	|         "merstatus": "00",
     * I: 	|         "auditstatus": "03",
     * I: 	|         "authstatus": "00",
     * I: 	|         "merid": "300008795977",
     * I: 	|         "bankcards": [],
     * I: 	|         "cny": 0
     * I: 	|     },
     * I: 	|     "ok": true
     * I: 	| }
     * I: 	--------------------------------------------------
     */

    /**
     * 钱包 ID
     */
    private String walletId;
    /**
     * 姓名
     */
    private String nameDesc;
    /**
     * 实名认证状态
     */
    private String idCardRzStatus;
    /**
     * 人像认证状态
     */
    private String personRzStatus;
    /**
     * 设置密码状态：是：true 否：false
     */
    private String setUpPasswrod;
    /**
     * 钱包余额，单位为 分
     */
    private String balance;
    /**
     * 身份证号码
     */
    private String idCardNoDesc;
    /**
     * 运营商认证状态
     */
    private String operatorRzStatus;
    /**
     * 注册手机
     */
    private String mobileDesc;

    public String cny;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getNameDesc() {
        return nameDesc;
    }

    public void setNameDesc(String nameDesc) {
        this.nameDesc = nameDesc;
    }

    public String getIdCardRzStatus() {
        return idCardRzStatus;
    }

    public void setIdCardRzStatus(String idCardRzStatus) {
        this.idCardRzStatus = idCardRzStatus;
    }

    public String getPersonRzStatus() {
        return personRzStatus;
    }

    public void setPersonRzStatus(String personRzStatus) {
        this.personRzStatus = personRzStatus;
    }

    public String getSetUpPasswrod() {
        return setUpPasswrod;
    }

    public void setSetUpPasswrod(String setUpPasswrod) {
        this.setUpPasswrod = setUpPasswrod;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getIdCardNoDesc() {
        return idCardNoDesc;
    }

    public void setIdCardNoDesc(String idCardNoDesc) {
        this.idCardNoDesc = idCardNoDesc;
    }

    public String getOperatorRzStatus() {
        return operatorRzStatus;
    }

    public void setOperatorRzStatus(String operatorRzStatus) {
        this.operatorRzStatus = operatorRzStatus;
    }

    public String getMobileDesc() {
        return mobileDesc;
    }

    public void setMobileDesc(String mobileDesc) {
        this.mobileDesc = mobileDesc;
    }
}
