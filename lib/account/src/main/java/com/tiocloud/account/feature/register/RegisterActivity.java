package com.tiocloud.account.feature.register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tiocloud.account.R;
import com.tiocloud.account.databinding.AccountRegisterActivityBinding;
import com.tiocloud.account.feature.login.LoginActivity;
import com.watayouxiang.androidutils.page.BaseFragment;
import com.watayouxiang.androidutils.page.BindingActivity;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.callback.TaoConvert;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.AccountCheckReq;
import com.watayouxiang.httpclient.model.request.CheckFriendInviteCodeReq;
import com.watayouxiang.httpclient.model.request.RegisterNewflagReq;
import com.watayouxiang.httpclient.model.request.RegisterphoneflagReq;
import com.watayouxiang.httpclient.model.request.SmsCheckReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/12/22
 *     desc   : 手机号注册
 * </pre>
 */
public class RegisterActivity extends BindingActivity<AccountRegisterActivityBinding> {

    private PhoneRegisterFragment phoneFragment;
    private PhoneRegisterBindEmailFragment emailFragment;
    private ShowPage showPage;
    private String registerType="2";
    public static final String INVITE_CODE = "";

    public static void start(Context context) {
        Intent starter = new Intent(context, RegisterActivity.class);
        if (!(context instanceof Activity)) {
            starter.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.account_register_activity;
    }

    @Override
    protected Integer background_color() {
        return Color.WHITE;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setData(this);
        resetUI();
        statusbar(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (showPage == null) return;
        switch (showPage) {
            case REGISTER:
                phoneFragment.onActivityResult(requestCode, resultCode, data);
                break;
            case BIND_EMAIL:
                emailFragment.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void resetUI() {
//        phoneFragment = new PhoneRegisterFragment();
//        emailFragment = new PhoneRegisterBindEmailFragment();
//
//        addFragment(phoneFragment);
//        addFragment(emailFragment);
//        showPhoneRegisterFragment();
        RegisterphoneflagReq req = new RegisterphoneflagReq();//phoneflag
        req.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        TioHttpClient.get(this, req, new TaoCallback<BaseResp<String>>() {
            @Override
            public void onSuccess(Response<BaseResp<String>> response) {
                String data = response.body().getData();
                Log.e("===注册==","=="+data);
                if(data!=null){
                    registerType=data;
                    if(data.equals("2")){
                        binding.etLoginName.setHint("请输入账号");
                    }else {
                        binding.etLoginName.setHint("请输入手机号");
                        binding.etLoginName.setInputType(InputType.TYPE_CLASS_NUMBER);
                        binding.etLoginName.setMaxEms(11);
                    }

                }

            }

            @Override
            public void onError(Response<BaseResp<String>> response) {
                super.onError(response);
                Log.e("===注册==","=="+response.getException().getMessage());

            }
        });
        binding.rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(getActivity());
                finish();
            }
        });
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.start(getActivity());
                finish();
            }
        });
        binding.protocolLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.protocolCheckBox.isChecked()){
                    binding.protocolCheckBox.setChecked(false);
                }else {
                    binding.protocolCheckBox.setChecked(true);
                }
            }
        });
        binding.btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameNum= binding.etLoginName.getText().toString().trim();
                String passs= binding.etPwd.getText().toString().trim();
                String passsTwo= binding.etPwdTwo.getText().toString().trim();
                // 隐藏键盘
                KeyboardUtil.hideSoftInput(binding.etLoginName);

                if (TextUtils.isEmpty(nameNum)) {
                    if(registerType.equals("2")){
                        ToastUtils.showLong("请输入账号");
                    }else {
                        ToastUtils.showLong("请输入手机号");
                    }
                    return;
                }
                if (TextUtils.isEmpty(passs)) {
                    ToastUtils.showLong("请输入登录密码");
                    return;
                }
//                if (!passs.equals(passsTwo)) {
//                    ToastUtils.showLong("两个密码不一致");
//                    return;
//                }
//                if(!binding.protocolCheckBox.isChecked()){
//                    ToastUtils.showLong("请先勾选用户协议和隐私政策");
//                    return;
//                }
                binding.btRegister.setEnabled(false);
                new AccountCheckReq(nameNum).setCancelTag(this).post(new TioCallback<Object>() {
                    @Override
                    public void onTioSuccess(Object s) {
                        Log.d("===账号是否可用==","=="+s);
                        RegisterNewflagReq req2 = new RegisterNewflagReq();
                        req2.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
                        TioHttpClient.get(req2, new TioCallback<Object>() {

                            @Override
                            public void onTioSuccess(Object o) {
                                Log.d("===是否邀请==","=="+o.toString());
                                String json = new Gson().toJson(o);
                                try {
                                    JSONObject jsonObject=new JSONObject(json);
                                    int phoneflag=jsonObject.getInt("phoneflag");
                                    int appinvitecodeflag=jsonObject.getInt("appinvitecodeflag");
                                    Log.d("===是否邀请==",appinvitecodeflag+"=="+phoneflag);

                                    if(phoneflag==1){//注册是否需要手机号  1是  2否
                                        if(appinvitecodeflag==1){//是否需要邀请码
                                            autoInviteCode(nameNum,passs);
                                        }else {
                                            new SmsSendReq("2", nameNum, "").setCancelTag(this).post(new TioCallback<String>() {
                                                @Override
                                                public void onTioSuccess(String s) {
                                                    Log.e("===发送验证码==","=="+s);
                                                    startActivity(new Intent(RegisterActivity.this,PhoneYZCodeActivity.class)
                                                            .putExtra("registerType",registerType)
                                                            .putExtra("nameNum",nameNum)
                                                            .putExtra("friendInviteCode","")
                                                            .putExtra("passs",passs));
                                                }
                                                @Override
                                                public void onTioError(String msg) {
                                                    ToastUtils.showLong(msg);
                                                }
                                            });
                                        }
                                    }else {
                                        if(appinvitecodeflag==1) {//是否需要邀请码
                                            autoInviteCode(nameNum,passs);
                                        }else {
                                            startActivity(new Intent(RegisterActivity.this,PerfectMessageActivity.class)
                                                    .putExtra("registerType",registerType)
                                                    .putExtra("nameNum",nameNum)
                                                    .putExtra("friendInviteCode","")
                                                    .putExtra("passs",passs));
                                        }

                                    }



                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onTioError(String msg) {
                                Log.e("===是否邀请==","=onTioError="+msg);
                                ToastUtils.showLong(msg);

                            }
                        });

                    }

                    @Override
                    public void onTioError(String msg) {
                        Log.e("===账号是否可用==","=="+msg);
                        ToastUtils.showLong(msg);
                        binding.btRegister.setEnabled(true);

                    }
                });


//                new SmsCheckReq(biztype, mobile, code).setCancelTag(this).post(callback);

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        binding.btRegister.setEnabled(true);

    }
    @Override
    public <T extends BaseFragment> T addFragment(T fragment) {
        fragment.setContainerId(binding.flContainer.getId());
        return super.addFragment(fragment);
    }

    public void showPhoneRegisterBindEmailFragment() {
        hideFragment(phoneFragment);
        showFragment(emailFragment);
        showPage = ShowPage.BIND_EMAIL;
    }

    public void showPhoneRegisterFragment() {
        hideFragment(emailFragment);
        showFragment(phoneFragment);
        showPage = ShowPage.REGISTER;
    }

    private enum ShowPage {
        REGISTER, BIND_EMAIL
    }
    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            LoginActivity.start(getActivity());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void autoInviteCode(String nameNum,String passs) {
        if (TextUtils.isEmpty(INVITE_CODE)){
            startActivity(new Intent(RegisterActivity.this,FillInvitationCodeActivity.class)
                    .putExtra("registerType",registerType)
                    .putExtra("nameNum",nameNum)
                    .putExtra("passs",passs));
            return;
        }
        CheckFriendInviteCodeReq req = new CheckFriendInviteCodeReq(INVITE_CODE);
        req.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
        TioHttpClient.get(this, req, new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                Log.e("===验证==", "==" + s);
                if (registerType.equals("1")) {//手机号验证
                    new SmsSendReq("2", nameNum, "").setCancelTag(this).post(new TioCallback<String>() {
                        @Override
                        public void onTioSuccess(String s) {
                            Log.e("===发送验证码==", "==" + s);
                            startActivity(new Intent(RegisterActivity.this, PhoneYZCodeActivity.class)
                                    .putExtra("registerType", registerType)
                                    .putExtra("nameNum", nameNum)
                                    .putExtra("friendInviteCode", INVITE_CODE)
                                    .putExtra("passs", passs));
                        }

                        @Override
                        public void onTioError(String msg) {
                            ToastUtils.showLong(msg);
                        }
                    });

                } else {
                    startActivity(new Intent(RegisterActivity.this, PerfectMessageActivity.class)
                            .putExtra("registerType", registerType)
                            .putExtra("nameNum", nameNum)
                            .putExtra("friendInviteCode", INVITE_CODE)
                            .putExtra("passs", passs));
                }

            }

            @Override
            public void onTioError(String msg) {
                Log.e("===验证==", "==" + msg);
                ToastUtils.showLong(msg);
            }
        });
    }

}
