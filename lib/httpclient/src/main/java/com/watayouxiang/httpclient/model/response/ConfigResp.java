package com.watayouxiang.httpclient.model.response;

import java.io.Serializable;

/**
 * author : TaoWang
 * date : 2020-01-22
 * desc :
 */
public class ConfigResp implements Serializable {
    /**
     * api_suf : .tio_x
     * session_cookie_name : tio_session
     * sitename : t-io官网
     * js_version : 6
     * api_ctx : /mytio
     * res_server : https://res.tiocloud.com
     * im_heartbeat_timeout : 120000
     */

    /**
     * 资源服务器地址
     */
    public String res_server;
    /**
     * cookie名
     */
    public String session_cookie_name;
    /**
     * 网站名字
     */
    public String sitename;
    public String js_version;
    public String api_suf;
    public String api_ctx;
    /**
     * 长连接的超时时间，单位是毫秒
     */
    public long im_heartbeat_timeout;
    public int im_burst_transfer_file;//1, // 是否开启聊天文件快速传输 1 开启 2 关闭
    public int im_file_encrypt;// // 是否开启聊天文件加密 1 开启 2 关闭



    @Override
    public String toString() {
        return "ConfigResp{" +
                "res_server='" + res_server + '\'' +
                ", session_cookie_name='" + session_cookie_name + '\'' +
                ", sitename='" + sitename + '\'' +
                ", js_version='" + js_version + '\'' +
                ", api_suf='" + api_suf + '\'' +
                ", api_ctx='" + api_ctx + '\'' +
                ", im_burst_transfer_file='" + im_burst_transfer_file + '\'' +
                ", im_file_encrypt='" + im_file_encrypt + '\'' +

                ", im_heartbeat_timeout=" + im_heartbeat_timeout +
                '}';
    }
}
