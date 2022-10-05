package com.tiocloud.commonwallet;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/03
 *     desc   :
 * </pre>
 */
public enum iDialogType {
    NONE(1),
    OVERDUE(2);

    public int value;

    iDialogType(int value) {
        this.value = value;
    }
}
