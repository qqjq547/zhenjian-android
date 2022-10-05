package com.watayouxiang.httpclient.callback;

import android.util.Log;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.watayouxiang.httpclient.model.BaseResp;

import java.lang.reflect.Type;

public abstract class TioCallback<Data> extends AbsCallback<BaseResp<Data>> {
    private Type type;

    public TioCallback() {
    }

    /**
     * 避免泛型擦除，使用示例：
     * Type type = new TypeToken<TioResp<ImServerResp>>() {}.getType();
     */
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public BaseResp<Data> convertResponse(okhttp3.Response response) throws Throwable {
        TioConvert<Data> convert = new TioConvert<>(type);
        BaseResp<Data> resp = convert.convertResponse(response);
//        Log.d("===解析===","=="+resp.getData());
        ResponseHandler.handleResponse(resp);
        return resp;
    }

    // ====================================================================================
    // 原生
    // ====================================================================================

    @Override
    public void onStart(Request<BaseResp<Data>, ? extends Request> request) {
        super.onStart(request);
    }

    @Override
    public void onCacheSuccess(Response<BaseResp<Data>> response) {
        super.onCacheSuccess(response);
        onResponse(response);
    }

    @Override
    public void onSuccess(Response<BaseResp<Data>> response) {
        onResponse(response);
    }

    @Override
    public void onError(Response<BaseResp<Data>> response) {
        super.onError(response);
        onResponse(response);
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }

    // ====================================================================================
    // t-io
    // ====================================================================================

    private Response<BaseResp<Data>> response;

    private void onResponse(Response<BaseResp<Data>> response) {
        this.response = response;
//        Log.d("==http===isOk==",response.message()+"==response=="+response.body());

        if (response.isSuccessful()) {
            // http success
            BaseResp<Data> body = response.body();
//            Log.d("==isOk=00=","=="+response.body());

            if (body.isOk()) {
//                Log.d("==isOk=11=","=="+body.getData());
                // tio success
                onTioSuccess(body.getData());
            } else {
//                Log.d("==isOk=22=","=="+body.getMsg());

                // tio error
                onTioError(body.getMsg());
            }
        } else {
            Log.d("==http===isOk==","==异常==");

            // http error
            onTioError(ExceptionHandler.handleException(response));
        }
    }

    public boolean isFromCache() {
        if (response != null) {
            return response.isFromCache();
        }
        return false;
    }

    public String getTioMsg() {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return response.body().getMsg();
        }
        return null;
    }

    public boolean isTioError() {
        if (response != null && response.isSuccessful() && response.body() != null) {
            return !response.body().isOk();
        }
        return false;
    }

    public abstract void onTioSuccess(Data data);

    public abstract void onTioError(String msg);
}
