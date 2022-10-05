package com.tiocloud.commonwallet;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/03
 *     desc   :
 * </pre>
 */
public interface iWallet {

    int getLayoutId_redPaperMsg();

    void RedPaperDialog_show(Context context, iRedPaperVo redPaperVo);

    iRedPaperDialog RedPaperDialog_new(Context context, iRedPaperVo _vo);

    void OpenRedPaperDialog_show(Context context, iDialogType dialogType, iRedPaperVo _vo);

    void WalletActivity_start(FragmentActivity activity);

    void OpenWalletActivity_start(Activity activity);

    void PaperDetailActivity_start(Context context, String serialnumber);

    void RedPaperActivity_startGroup(Activity activity, String chatLinkId);

    void RedPaperActivity_startP2P(Activity activity, String chatLinkId);
}
