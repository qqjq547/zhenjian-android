package com.watayouxiang.imclient.model.body.wx.msg;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/08/10
 *     desc   :
 * </pre>
 */
public class WxMessageUtils {

    /**
     * 获取显示的 "消息内容"
     *
     * @param contentType 消息类型
     * @param content     消息内容
     * @return 显示的 "消息内容"
     */
    public static String getShowContent(int contentType, String content) {
        String temp;
        switch (contentType) {
            case 3:
                temp = "分享一个文件";
                break;
            case 4:
                temp = "[语音消息]";
                break;
            case 5:
                temp = "分享一个视频";
                break;
            case 6:
                temp = "分享一个图片";
                break;
            case 9:
                temp = "分享一个名片";
                break;
            case 10:
                temp = "[视频通话]";
                break;
            case 11:
                temp = "[语音通话]";
                break;
            case 12:
                temp = "发了一个红包";
                break;
            case 13:
                temp = "提交了一条入群申请";
                break;
            case 88:
                temp = "分享一个链接";
                break;
            default:
                temp = content;
                break;
        }
        return temp;
    }

}
