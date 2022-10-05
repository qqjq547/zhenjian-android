package com.watayouxiang.imclient.model.body.wx.msg;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/15
 *     desc   : 模版消息
 * </pre>
 */
public class InnerMsgTemplate {
    /**
     * 主标题
     */
    public String title;

    /**
     * 副标题
     */
    public String subtitle;

    /**
     * 超链接 url
     */
    public String url;

    /**
     * 图片 url
     */
    public String img;

    // ====================================================================================
    // 以下不常用
    // ====================================================================================

    /**
     * 图片简介
     */
    public String imgalt;

    /**
     * 内容
     */
    public String content;

    /**
     * 缩略内容
     */
    public String resume;

    /**
     * 时间
     */
    public String time;

    /**
     * 作者
     */
    public String author;
}
