package com.watayouxiang.androidutils.listener;

import com.watayouxiang.httpclient.callback.TioCallback;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/19
 *     desc   :
 * </pre>
 */
public abstract class TioErrorCallback<Data> extends TioCallback<Data> {
    @Override
    public void onTioSuccess(Data data) {

    }
}
