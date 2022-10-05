package com.tiocloud.chat.feature.browser;

import com.watayouxiang.httpclient.preferences.HttpCache;

import java.io.Serializable;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/02/23
 *     desc   :
 * </pre>
 */
public class ShareBean implements Serializable {
    /**
     * 主标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 超链接 url
     */
    private String url;

    /**
     * 图片 url
     */
    private String img;

    public ShareBean(String title, String subtitle, String url, String img) {
        this.title = title;
        this.subtitle = subtitle;
        this.url = url;
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getUrl() {
        return HttpCache.getResUrl(url);
    }

    public String getImg() {
        return HttpCache.getResUrl(img);
    }

    @Override
    public String toString() {
        return "ShareBean{" +
                "title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", url='" + url + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}
