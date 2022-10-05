package com.tiocloud.commonwallet;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/03
 *     desc   :
 * </pre>
 */
public interface iRedPaperDialog {

    void setOnRedPaperListener(OnRedPaperListener l);

    void show();

    void dismiss();

    interface OnRedPaperListener {
        void onReceiveRedPaper();
    }
}
