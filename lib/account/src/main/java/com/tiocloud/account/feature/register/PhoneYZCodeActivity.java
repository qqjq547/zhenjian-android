package com.tiocloud.account.feature.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.tiocloud.account.R;
import com.watayouxiang.androidutils.page.BaseActivity;
import com.watayouxiang.androidutils.widget.edittext.PayPwdEditText;
import com.watayouxiang.androidutils.widget.edittext.TioEditText;
import com.watayouxiang.androidutils.widget.edittext.TioPayPwdEditText;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.SmsCheckReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

public class PhoneYZCodeActivity extends BaseActivity {
    private String nameNum,passs,friendInviteCode;
    private String registerType;
    private TioEditText et_yanzhengma;
    private TioPayPwdEditText et_Code;
    private     CodeCountDownTimer codeCountDownTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_yzcode);
        statusbar(false);
        registerType=getIntent().getStringExtra("registerType");
        nameNum=getIntent().getStringExtra("nameNum");
        passs=getIntent().getStringExtra("passs");
        friendInviteCode=getIntent().getStringExtra("friendInviteCode");
        et_yanzhengma=findViewById(R.id.et_yanzhengma);
        findViewById(R.id.rlClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        et_Code=findViewById(R.id.et_Code);
        et_Code.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
//                Log.d("====onFinish===","=="+str);
                getNext(str);

            }
        });

        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=et_Code.getPwdText();
                if(!TextUtils.isEmpty(code)){
                    ToastUtils.showLong("请输入验证码");
                    return;
                }
                if(code.length()<6){
                    ToastUtils.showLong("请正确的验证码");
                    return;
                }
                getNext(code);

            }
        });
        TextView btSendCode=findViewById(R.id.btSendCode);
        codeCountDownTimer = new CodeCountDownTimer(getActivity(), 60000, 1000, btSendCode);
        codeCountDownTimer.start();
        btSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SmsSendReq("2", nameNum, "").setCancelTag(this).post(new TioCallback<String>() {
                    @Override
                    public void onTioSuccess(String s) {
                        Log.e("===发送验证码==","=="+s);
                        codeCountDownTimer = new CodeCountDownTimer(getActivity(), 60000, 1000, btSendCode);
                        codeCountDownTimer.start();
                    }

                    @Override
                    public void onTioError(String msg) {
                        ToastUtils.showLong(msg);
                    }
                });
            }
        });
    }
    private void getNext(String code){
        new SmsCheckReq("2", nameNum, code).setCancelTag(this).post(new TioCallback<String>() {
            @Override
            public void onTioSuccess(String s) {
                Log.d("====onTioSuccess==","=="+s);
                startActivity(new Intent(PhoneYZCodeActivity.this,PerfectMessageActivity.class)
                        .putExtra("code",code)
                        .putExtra("registerType",registerType)
                        .putExtra("nameNum",nameNum)
                        .putExtra("friendInviteCode",friendInviteCode)
                        .putExtra("passs",passs));
            }

            @Override
            public void onTioError(String msg) {
                ToastUtils.showLong(msg);

            }
        });
    }
}