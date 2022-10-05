package com.watayouxiang.qrcode.feature.qrcode_my.mvp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.qrcode.R;

import java.io.File;
import java.util.Locale;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class Presenter extends Contract.Presenter {

    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }

    @Override
    public File saveQRCode2Album(View root) {
        Bitmap createFromViewBitmap = QMUIDrawableHelper.createBitmapFromView(root);
        return ImageUtils.save2Album(createFromViewBitmap, Bitmap.CompressFormat.PNG);
    }

    @Override
    public void playBackgroundBlinkAnimation(View root) {
        QMUIViewHelper.playBackgroundBlinkAnimation(root, Color.WHITE);
    }

    @Override
    public void init() {
        getView().resetUI();
        getModel().reqUserCurr(new TioCallback<UserCurrResp>() {
            @Override
            public void onTioSuccess(UserCurrResp resp) {
                getView().onUserCurrResp(resp);
                getMyQRCode(resp.id);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void getMyQRCode(int uid) {
        String baseUrl = "https://a.app.qq.com/o/simple.jsp?pkgname=com.tiocloud.chat";
        String myQRCodeUrl = String.format(Locale.getDefault(), "%s&uid=%d", baseUrl, uid);

        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Bitmap>() {
            @Override
            public Bitmap doInBackground() throws Throwable {
//                Bitmap logoBitmap = BitmapFactory.decodeResource(Utils.getApp().getResources(), R.drawable.qrcode_logo);
                return QRCodeEncoder.syncEncodeQRCode(
                        myQRCodeUrl,
                        BGAQRCodeUtil.dp2px(Utils.getApp(), 227),
                        Color.BLACK,
                        Color.TRANSPARENT,
                        null);
            }

            @Override
            public void onSuccess(Bitmap result) {
                if (result == null) {
                    TioToast.showShort("二维码生成失败");
                    return;
                }
                getView().onMyQRCodeGet(result);
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                TioToast.showShort("二维码生成失败");
            }
        });
    }

    // ====================================================================================
    // 权限检测
    // ====================================================================================

    public static void checkPermission(OnCheckPermissionCallback callback) {
        PermissionUtils.permission(PermissionConstants.STORAGE)
                .rationale((activity, shouldRequest) -> shouldRequest.again(true))
                .callback((isAllGranted, granted, deniedForever, denied) -> {
                    if (isAllGranted) {
                        if (callback != null) {
                            callback.onPermissionAllGranted();
                        }
                    } else {
                        if (!deniedForever.isEmpty()) {
                            PermissionUtils.launchAppDetailsSettings();
                        }
                        TioToast.showShort("权限不足，无法使用");
                    }
                })
                .request();
    }

    public interface OnCheckPermissionCallback {
        void onPermissionAllGranted();
    }
}
