package com.tiocloud.social;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.qmui.ShareBottomSheetDialog;
import com.watayouxiang.social.callback.SocialShareCallback;
import com.watayouxiang.social.entities.QQShareEntity;
import com.watayouxiang.social.entities.ShareEntity;
import com.watayouxiang.social.entities.WXShareEntity;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/29
 *     desc   :
 * </pre>
 */
public class TioShareHelper {
    private final ShareBottomSheetDialog dialog;

    private final SocialShareCallback mShareCallback = new SocialShareCallback() {
        @Override
        public void shareSuccess(int type) {
            TioToast.showShort("分享成功");
        }

        @Override
        public void socialError(String msg) {
            TioToast.showShort(msg);
        }
    };

    /**
     * 分享 h5 构造方法
     *
     * @param activity Activity
     * @param proxy    h5 代理
     */
    public TioShareHelper(Activity activity, H5UrlProxy proxy) {
        dialog = new ShareBottomSheetDialog(activity, new ShareBottomSheetDialog.OnItemClickListener() {
            @Override
            public void onClickQQ(QMUIBottomSheet dialog, View itemView) {
                if (proxy != null) {
                    ShareEntity entity = QQShareEntity.createImageTextInfo(proxy.getTitle(), proxy.getH5Url(), proxy.getImgUrl(), proxy.getSubtitle(), "城市客栈");
                    TioSocial.INSTANCE.socialHelper.shareQQ(activity, entity, mShareCallback);
                }
            }

            @Override
            public void onClickWX(QMUIBottomSheet dialog, View itemView) {
                if (proxy != null) {
                    ShareEntity entity = WXShareEntity.createWebPageInfo(false, proxy.getH5Url(), null, proxy.getTitle(), proxy.getSubtitle());
                    TioSocial.INSTANCE.socialHelper.shareWX(activity, entity, mShareCallback);
                }
            }

            @Override
            public void onClickWXMoments(QMUIBottomSheet dialog, View itemView) {
                if (proxy != null) {
                    ShareEntity entity = WXShareEntity.createWebPageInfo(true, proxy.getH5Url(), null, proxy.getTitle(), proxy.getSubtitle());
                    TioSocial.INSTANCE.socialHelper.shareWX(activity, entity, mShareCallback);
                }
            }
        });
    }

    /**
     * 分享图片构造方法
     *
     * @param activity    Activity
     * @param imgUrlProxy 图片代理
     */
    public TioShareHelper(Activity activity, ImgUrlProxy imgUrlProxy) {
        dialog = new ShareBottomSheetDialog(activity, new ShareBottomSheetDialog.OnItemClickListener() {
            @Override
            public void onClickQQ(QMUIBottomSheet dialog, View itemView) {
                if (imgUrlProxy != null) {
                    ShareEntity entity = QQShareEntity.createImageInfo(imgUrlProxy.getImgUrl(), "城市客栈");
                    TioSocial.INSTANCE.socialHelper.shareQQ(activity, entity, mShareCallback);
                }
            }

            @Override
            public void onClickWX(QMUIBottomSheet dialog, View itemView) {
                if (imgUrlProxy != null) {
                    ShareEntity entity = WXShareEntity.createImageInfo(false, imgUrlProxy.getImgUrl());
                    TioSocial.INSTANCE.socialHelper.shareWX(activity, entity, mShareCallback);
                }
            }

            @Override
            public void onClickWXMoments(QMUIBottomSheet dialog, View itemView) {
                if (imgUrlProxy != null) {
                    ShareEntity entity = WXShareEntity.createImageInfo(true, imgUrlProxy.getImgUrl());
                    TioSocial.INSTANCE.socialHelper.shareWX(activity, entity, mShareCallback);
                }
            }
        });
    }

    /**
     * Activity 回调
     */
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 用处：qq登录和分享回调，以及微博登录回调
        // qq分享如果选择留在qq，通过home键退出，再进入app则不会有回调
        TioSocial.INSTANCE.socialHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void show() {
        dialog.show();
    }

    // ====================================================================================
    // innerClass
    // ====================================================================================

    public interface ImgUrlProxy {
        String getImgUrl();
    }

    public interface H5UrlProxy {
        String getH5Url();

        String getTitle();

        String getSubtitle();

        String getImgUrl();
    }

}
