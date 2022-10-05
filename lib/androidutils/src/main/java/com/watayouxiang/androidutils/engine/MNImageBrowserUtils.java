package com.watayouxiang.androidutils.engine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ImageUtils;
import com.maning.imagebrowserlibrary.MNImageBrowser;
import com.maning.imagebrowserlibrary.listeners.OnLongClickListener;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.dialognew.PicSaveOperDialog;
import com.watayouxiang.androidutils.widget.TioToast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/08/17
 *     desc   :
 * </pre>
 */
public abstract class MNImageBrowserUtils {

    /**
     * 获取资源地址
     */
    public abstract String getResUrl(String imgUrl);

    public void showPic(@NonNull View view, int currentPosition, List<ImageAesFingerprinBean> imgUrlsfingerprint,@NonNull String... imgUrls) {
        /* 处理数据 */
        String imgUrl = null;
        ArrayList<String> imgUrlList = null;
        if (imgUrls.length == 0) {
            return;
        } else if (imgUrls.length == 1) {
            imgUrl = getResUrl(imgUrls[0]);
            currentPosition = 0;
        } else {
            imgUrlList = new ArrayList<>();
            for (String url : imgUrls) {
                imgUrlList.add(getResUrl(url));
            }
            if (currentPosition < 0 || currentPosition > imgUrlList.size() - 1) {
                currentPosition = 0;
            }
        }
        PicSaveOperDialog   operDialog;

        /* 显示图片预览 */
        MNImageBrowser imageBrowser = MNImageBrowser.with(view.getContext())
                // 图片加载器
                .setImageEngine(new MNImageBrowserEngine(imgUrlsfingerprint))
                // 手势下拉缩小效果是否开启
                .setOpenPullDownGestureEffect(true)
                // 自定义ProgressView，不设置默认默认没有
                .setCustomProgressViewLayoutID(R.layout.pic_viewer_progress)

                // 设置当前位置
                .setCurrentPosition(currentPosition);
        imageBrowser.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public void onLongClick(FragmentActivity activity, ImageView view, int position, String url) {
//                Log.d("===长按保存==","===="+ imageBrowser.getCurrentPosition());

                PicSaveOperDialog   operDialog = new PicSaveOperDialog(view.getContext()) {
                    @Override
                    protected void onClick(PicSaveOperDialog dialog, View v) {
                        super.onClick(dialog, v);
//                        Log.d("===长按保存==","==22==");
                        if (v.getId() == R.id.tv_saveBtn) {// 退群
                            Bitmap createFromViewBitmap = QMUIDrawableHelper.createBitmapFromView(view);
                            File png = ImageUtils.save2Album(createFromViewBitmap, Bitmap.CompressFormat.PNG);
                            if (png != null) {
                                TioToast.showShort("已成功保存至相册");
                            } else {
                                TioToast.showShort("下载失败");
                            }
                        }
                        dialog.cancel();
                    }
                };
                operDialog.show();
            }
        });
        if (imgUrl != null) {

            // 必须（setImageList和setImageUrl二选一，会覆盖）-设置单张图片
            imageBrowser.setImageUrl(getResUrl(imgUrl));
        } else if (imgUrlList != null) {
            // 必须（setImageList和setImageUrl二选一，会覆盖）-图片集合
            imageBrowser.setImageList(imgUrlList);
        }

        imageBrowser.show(view);
    }

    public void showPic(@NonNull View view, @NonNull String imgUrl) {
        showPic(view, 0, null,imgUrl);
    }

    public void clickViewShowPic(@NonNull View view, int currentPosition, @NonNull String... imgUrls) {
        if (!view.isClickable()) {
            view.setClickable(true);
        }
        view.setOnClickListener(v -> showPic(v, currentPosition, null,imgUrls));
    }

    public void clickViewShowPic(@NonNull View view, @NonNull String imgUrl) {
        clickViewShowPic(view, 0, null,imgUrl);
    }

}
