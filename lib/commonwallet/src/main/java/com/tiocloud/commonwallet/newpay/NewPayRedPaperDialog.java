package com.tiocloud.commonwallet.newpay;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tiocloud.commonwallet.iRedPaperDialog;
import com.tiocloud.commonwallet.iRedPaperVo;
import com.tiocloud.newpay.dialog.RedPaperDialog;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/03
 *     desc   :
 * </pre>
 */
public class NewPayRedPaperDialog implements iRedPaperDialog {

    private final RedPaperDialog redPaperDialog;

    public NewPayRedPaperDialog(@NonNull Context context, iRedPaperVo _vo) {
        RedPaperDialog.RedPaperVo vo = new RedPaperDialog.RedPaperVo(_vo.avatar, _vo.name, _vo.gift, _vo.isSendMsg, _vo.serialNumber);
        redPaperDialog = new RedPaperDialog(context, vo);
    }

    @Override
    public void setOnRedPaperListener(OnRedPaperListener l) {
        redPaperDialog.setOnRedPaperListener(l == null ? null : l::onReceiveRedPaper);
    }

    @Override
    public void show() {
        redPaperDialog.show();
    }

    @Override
    public void dismiss() {
        redPaperDialog.dismiss();
    }

}
