package com.watayouxiang.imclient.model.body.wx.msg;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2/8/21
 *     desc   :
 * </pre>
 */
public class InnerMsgApply {
    /*
    {
        "applymsg":"3",
        "groupid":"795",
        "id":687,
        "operuid":23436,
        "status":2
    }
    */

    /**
     * 申请理由
     */
    public String applymsg;
    /**
     * 组id
     */
    public long groupid;
    /**
     * id
     */
    public long id;
    /**
     * 操作者uid
     */
    public long operuid;
    /**
     * 是否已处理：1已处理，2未处理
     */
    public int status;
}
