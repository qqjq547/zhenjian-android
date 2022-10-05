package com.watayouxiang.httpclient.callback;

import android.net.ParseException;
import android.util.Log;

import com.blankj.utilcode.util.SDCardUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.JsonParseException;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.model.Response;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.SysErrlogReq;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.File;
import java.io.FilenameFilter;
import java.net.ConnectException;
import java.net.UnknownHostException;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/06/23
 *     desc   :
 * </pre>
 */
class ExceptionHandler {
    /**
     * 获取异常信息
     */
    public static <Data> String handleException(Response<BaseResp<Data>> response) {
        // 没有错误
        Throwable e = response.getException();
        if (e == null) return "没有错误";

        String msg = "网络请求失败";
        if (e instanceof HttpException) {
            int code = response.code();
            if (code != -1) {
                msg = code + "错误";
            } else {
                msg = "网络错误";
            }
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {
            msg = "网络连接失败，请稍后再试！";
        } else if (e instanceof ConnectTimeoutException
                || e instanceof java.net.SocketTimeoutException) {
            msg = "连接超时";
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            msg = "证书验证异常";
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            msg = "获取数据失败";
            ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Void>() {
                @Override
                public Void doInBackground() throws Throwable {
                    uploadLogInternal();
                    return null;
                }

                @Override
                public void onSuccess(Void result) {

                }
            });
        }
        return msg;
    }
    private static void uploadLogInternal() {
        String crashDir = getCrashDir();
        File crashFile = new File(crashDir);
        File[] logs = crashFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        if (logs != null) {
            for (File log : logs) {
                String absolutePath = log.getAbsolutePath();
               Log.d("准备上传崩溃日志：" ,"=="+ absolutePath);
                boolean ok = reqUpload(absolutePath);
                Log.d("崩溃日志上传成功：" ,"=="+ absolutePath);

                if (ok) {
                    File file = new File(absolutePath);
                    String newNameName = absolutePath.replace(".txt", ".log");
                    File newFile = new File(newNameName);
                    boolean success = file.renameTo(newFile);
                    if (success) {
                        Log.d("崩溃修改成功：" ,"=="+ absolutePath);

                    }
                }
            }
        }
    }
    private static boolean reqUpload(String absolutePath) {
        SysErrlogReq sysErrlogReq = new SysErrlogReq(absolutePath);
        Response<BaseResp<String>> response = sysErrlogReq.upload();
        if (response.isSuccessful()) {
            BaseResp<String> body = response.body();
            return body.isOk();
        }
        return false;
    }
    private static final String FILE_SEP = System.getProperty("file.separator");

    private static String getCrashDir() {
        String dirPath;
        if (SDCardUtils.isSDCardEnableByEnvironment()
                && Utils.getApp().getExternalFilesDir(null) != null)
            dirPath = Utils.getApp().getExternalFilesDir(null) + FILE_SEP + "crash" + FILE_SEP;
        else {
            dirPath = Utils.getApp().getFilesDir() + FILE_SEP + "crash" + FILE_SEP;
        }
        return dirPath;
    }

}
