package com.watayouxiang.qrcode.feature.qrcode_group.mvp;

import android.app.Activity;
import android.content.Intent;
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
import com.tiocloud.social.TioShareHelper;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.response.GroupInfoResp;
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
    public void init(String groupId) {
        getView().resetUI();
        getModel().reqGroupInfo("1", groupId, new TioCallback<GroupInfoResp>() {
            @Override
            public void onTioSuccess(GroupInfoResp resp) {
                GroupInfoResp.GroupUser groupuser = resp.groupuser;
                GroupInfoResp.Group group = resp.group;
                if (groupuser != null && group != null) {
                    getMyQRCode(group.id, String.valueOf(groupuser.uid));
                }
                getView().onGroupInfoResp(resp);
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    private void getMyQRCode(String groupId, String applyuid) {
        String baseUrl = "https://a.app.qq.com/o/simple.jsp?pkgname=com.tiocloud.chat";
        String myQRCodeUrl = String.format(Locale.getDefault(), "%s&g=%s&applyuid=%s", baseUrl, groupId, applyuid);

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
                getView().onGroupQRCodeGet(result);
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                TioToast.showShort("二维码生成失败");
            }
        });
    }

    // ====================================================================================
    // 三方分享
    // ====================================================================================

    private TioShareHelper shareHelper;
    private String imgUrl;

    @Override
    public void showShareDialog(Activity activity, View root) {
        if (shareHelper == null) {
            shareHelper = new TioShareHelper(activity, (TioShareHelper.ImgUrlProxy) () -> {
                if (imgUrl == null) {
                    File qrCodeFile = saveQRCode2Album(root);
                    if (qrCodeFile != null) {
                        imgUrl = qrCodeFile.getAbsolutePath();
                    }
                }
                return imgUrl;
            });
        }
        shareHelper.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (shareHelper != null) {
            shareHelper.onActivityResult(requestCode, resultCode, data);
        }
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
