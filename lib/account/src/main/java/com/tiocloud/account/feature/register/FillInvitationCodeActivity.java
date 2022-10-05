package com.tiocloud.account.feature.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.tiocloud.account.R;
import com.watayouxiang.androidutils.page.BaseActivity;
import com.watayouxiang.androidutils.widget.edittext.TioEditText;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TaoCallback;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.CheckFriendInviteCodeReq;
import com.watayouxiang.httpclient.model.request.RegisterphoneflagReq;
import com.watayouxiang.httpclient.model.request.SmsSendReq;

public class FillInvitationCodeActivity extends BaseActivity {
    private String nameNum,passs;
    private String registerType;
    private QMUIRoundButton btNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_invitation_code);
        statusbar(false);
        registerType=getIntent().getStringExtra("registerType");
        nameNum=getIntent().getStringExtra("nameNum");
        passs=getIntent().getStringExtra("passs");
        TioEditText et_yaoqingma=findViewById(R.id.et_yaoqingma);

        findViewById(R.id.rlClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btNext=findViewById(R.id.btNext);

        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String yaoqingma=et_yaoqingma.getText().toString().trim();
                // 隐藏键盘
                KeyboardUtil.hideSoftInput(et_yaoqingma);
                if (TextUtils.isEmpty(yaoqingma)) {
                    ToastUtils.showLong("请输入邀请码");
                    return;
                }
                CheckFriendInviteCodeReq req = new CheckFriendInviteCodeReq(yaoqingma);
                req.setCacheMode(CacheMode.REQUEST_FAILED_READ_CACHE);
                btNext.setEnabled(false);

                TioHttpClient.get(this, req, new TioCallback<String>() {
                    @Override
                    public void onTioSuccess(String s) {
                        Log.e("===验证==","=="+s);
                        if(registerType.equals("1")){//手机号验证
                            new SmsSendReq("2", nameNum, "").setCancelTag(this).post(new TioCallback<String>() {
                                @Override
                                public void onTioSuccess(String s) {
                                    Log.e("===发送验证码==","=="+s);
                                    startActivity(new Intent(FillInvitationCodeActivity.this,PhoneYZCodeActivity.class)
                                            .putExtra("registerType",registerType)
                                            .putExtra("nameNum",nameNum)
                                            .putExtra("friendInviteCode",yaoqingma)
                                            .putExtra("passs",passs));
                                }

                                @Override
                                public void onTioError(String msg) {
                                    ToastUtils.showLong(msg);
                                }
                            });

                        }else {
                            startActivity(new Intent(FillInvitationCodeActivity.this,PerfectMessageActivity.class)
                                    .putExtra("registerType",registerType)
                                    .putExtra("nameNum",nameNum)
                                    .putExtra("friendInviteCode",yaoqingma)
                                    .putExtra("passs",passs));
                        }

                    }

                    @Override
                    public void onTioError(String msg) {
                        Log.e("===验证==","=="+msg);
                        ToastUtils.showLong(msg);
                        btNext.setEnabled(true);

                    }
                });

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        btNext.setEnabled(true);

    }

}