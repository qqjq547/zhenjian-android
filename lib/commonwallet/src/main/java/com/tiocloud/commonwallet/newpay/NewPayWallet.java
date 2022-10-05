package com.tiocloud.commonwallet.newpay;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.tiocloud.commonwallet.R;
import com.tiocloud.commonwallet.iDialogType;
import com.tiocloud.commonwallet.iRedPaperDialog;
import com.tiocloud.commonwallet.iRedPaperVo;
import com.tiocloud.commonwallet.iWallet;
import com.tiocloud.newpay.feature.open.OpenWalletActivity;
import com.tiocloud.newpay.feature.paperdetail.PaperDetailActivity;
import com.tiocloud.newpay.feature.redpaper.RedPaperActivity;
import com.tiocloud.newpay.feature.wallet.WalletActivity;
import com.tiocloud.newpay.dialog.OpenRedPaperDialog;
import com.tiocloud.newpay.dialog.RedPaperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/03
 *     desc   :
 * </pre>
 */
public class NewPayWallet implements iWallet {
    @Override
    public void RedPaperDialog_show(Context context, iRedPaperVo _vo) {
        RedPaperDialog.RedPaperVo vo = new RedPaperDialog.RedPaperVo(_vo.avatar, _vo.name, _vo.gift, _vo.isSendMsg, _vo.serialNumber);
        new RedPaperDialog(context, vo).show();
    }

    @Override
    public void WalletActivity_start(FragmentActivity activity) {
        WalletActivity.start(activity);
    }

    @Override
    public void OpenWalletActivity_start(Activity activity) {
        OpenWalletActivity.start(activity);
    }

    @Override
    public int getLayoutId_redPaperMsg() {
        return R.layout.wallet_redpaper_msg;
    }

    @Override
    public void PaperDetailActivity_start(Context context, String rid) {
        PaperDetailActivity.start(context, rid);
    }

    @Override
    public void OpenRedPaperDialog_show(Context context, iDialogType dialogType, iRedPaperVo _vo) {
        RedPaperDialog.RedPaperVo vo = new RedPaperDialog.RedPaperVo(_vo.avatar, _vo.name, _vo.gift, _vo.isSendMsg, _vo.serialNumber);
        new OpenRedPaperDialog(context, dialogType.value, vo).show();
    }

    @Override
    public void RedPaperActivity_startGroup(Activity activity, String chatLinkId) {
        RedPaperActivity.startGroup(activity, chatLinkId);
    }

    @Override
    public void RedPaperActivity_startP2P(Activity activity, String chatLinkId) {
        RedPaperActivity.startP2P(activity, chatLinkId);
    }

    @Override
    public iRedPaperDialog RedPaperDialog_new(Context context, iRedPaperVo _vo) {
        return new NewPayRedPaperDialog(context, _vo);
    }
}
