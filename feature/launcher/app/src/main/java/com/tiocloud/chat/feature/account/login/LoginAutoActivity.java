package com.tiocloud.chat.feature.account.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.google.gson.Gson;
import com.lzy.okgo.cache.CacheMode;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.account.feature.register.FillInvitationCodeActivity;
import com.tiocloud.account.feature.register.PerfectMessageActivity;
import com.tiocloud.account.feature.register.PhoneYZCodeActivity;
import com.tiocloud.account.feature.register.RegisterActivity;
import com.tiocloud.account.mvp.login.LoginContract;
import com.tiocloud.account.mvp.login.LoginPresenter;
import com.tiocloud.chat.R;
import com.tiocloud.chat.databinding.ActivityLoginAutoBinding;
import com.tiocloud.chat.feature.account.pwd.RetrievePwdActivity;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.AccountCheckReq;
import com.watayouxiang.httpclient.model.request.RegisterNewflagReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

import org.json.JSONObject;

/**
 * author : TaoWang
 * date : 2019-12-31
 * desc : 登录页面
 */
public class LoginAutoActivity extends TioActivity implements LoginContract.View {

    private LoginPresenter presenter;
    private ActivityLoginAutoBinding binding;
    private String channelCode;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginAutoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_auto);
        hideStatusBar();
        setStatusBarLightMode(false);
        presenter = new LoginPresenter(this);
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                // 打印数据便于调试
                Log.d("OpenInstall", "getInstall : installData = " + appData.toString());
                //  获取渠道编号参数
                channelCode = appData.getChannel();
                // 获取自定义参数
                String bindData = appData.getData();
            }
        });
        initViews();
        RegisterNewflagReq req2 = new RegisterNewflagReq();
        req2.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        TioHttpClient.get(req2, new TioCallback<Object>() {

            @Override
            public void onTioSuccess(Object o) {
                Log.d("===是否邀请==","=="+o.toString());
                String json = new Gson().toJson(o);
                try {
                    JSONObject jsonObject=new JSONObject(json);
                    int appinvitecodeflag=jsonObject.getInt("appinvitecodeflag");
                    if (appinvitecodeflag==1&& TextUtils.isEmpty(channelCode)){
                        binding.tvTips.setVisibility(View.GONE);
                        binding.linInvite.setVisibility(View.VISIBLE);
                    }else {
                        binding.tvTips.setVisibility(View.VISIBLE);
                        binding.linInvite.setVisibility(View.GONE);
                        presenter.reqAutoLogin(DeviceUtils.getUniqueDeviceId(),channelCode, getActivity());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onTioError(String msg) {
                ToastUtils.showLong(msg);

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        binding.unbind();
    }

    private void initViews() {
        binding.etYaoqingma.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               binding.btNext.setEnabled(!TextUtils.isEmpty(binding.etYaoqingma.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

}
