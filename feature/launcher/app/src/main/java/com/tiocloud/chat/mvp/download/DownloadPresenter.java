package com.tiocloud.chat.mvp.download;

import android.app.Activity;
import android.text.format.Formatter;

import com.blankj.utilcode.util.Utils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.httpclient.callback.TioFileCallback;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.io.File;
import java.text.NumberFormat;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/10/14
 *     desc   :
 * </pre>
 */
public class DownloadPresenter extends DownloadContract.Presenter implements AutoCloseable {
    public DownloadPresenter() {
        super(new DownloadModel(), false);
    }

    @Override
    public void downloadWithTip(String url, Activity activity) {
        OkGo.<File>get(HttpCache.getResUrl(url))
                .execute(new TioFileCallback() {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        if (activity != null) {
                            SingletonProgressDialog.show_unCancel(activity, "下载中...");
                        }
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        TioToast.showShort("下载完成");
                    }

                    @Override
                    public void onError(Response<File> response) {
                        TioToast.showShort("下载失败");
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        TioLogger.d("下载进度：" + progress);

                        String downloadLength = Formatter.formatFileSize(Utils.getApp(), progress.currentSize);
                        String totalLength = Formatter.formatFileSize(Utils.getApp(), progress.totalSize);
                        TioLogger.d("DownloadSize：" + downloadLength + "/" + totalLength);

                        String speed = Formatter.formatFileSize(Utils.getApp(), progress.speed);
                        TioLogger.d("NetSpeed：" + String.format("%s/s", speed));

                        NumberFormat numberFormat = NumberFormat.getPercentInstance();
                        numberFormat.setMinimumFractionDigits(2);
                        TioLogger.d("Progress：" + numberFormat.format(progress.fraction));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        SingletonProgressDialog.dismiss();
                    }
                });
    }

    @Override
    public void close() throws Exception {
        detachView();
    }
}
