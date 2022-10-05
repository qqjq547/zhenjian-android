package com.watayouxiang.httpclient.model.vo;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/21
 *     desc   :
 * </pre>
 */
public enum GroupRoleEnum {
    OWNER(1),
    MEMBER(2),
    MGR(3);

    int code;

    GroupRoleEnum(int code) {
        this.code = code;
    }

    public static GroupRoleEnum codeOf(int code) {
        GroupRoleEnum[] values = values();
        for (GroupRoleEnum e : values) {
            if (e.code == code) return e;
        }
        return null;
    }
}
