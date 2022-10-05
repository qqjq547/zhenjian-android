package com.tiocloud.newpay.feature.alipbind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tiocloud.newpay.R;
import com.tiocloud.newpay.databinding.ActivityBindAliPayBinding;
import com.watayouxiang.androidutils.page.BindingLightActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.AliPayBindNumReq;
import com.watayouxiang.httpclient.model.request.AliPayGetDataReq;
import com.watayouxiang.httpclient.model.request.UnAliPayBindNumReq;
import com.watayouxiang.httpclient.model.response.GetAliPayDataResp;

public class BindAliPayActivity  extends BindingLightActivity<ActivityBindAliPayBinding> {
    private String paypass;
    public static void start(Context context,String paypass) {
        Intent starter = new Intent(context, BindAliPayActivity.class);
        starter.putExtra("paypass",paypass);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paypass=getIntent().getStringExtra("paypass");
        initUiData();
        binding.tvOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usetName=binding.etNameUser.getText().toString().trim();
                String amoutNum=binding.etAmountText.getText().toString().trim();
                if (TextUtils.isEmpty(usetName)) {
                    TioToast.showShort("请输入真实姓名");
                    return;
                }
                if (TextUtils.isEmpty(amoutNum)) {
                    TioToast.showShort("请输入支付宝账号");
                    return;
                }
                if(binding.tvOkBtn.getText().equals("解除绑定")){
//                    TioToast.showShort("解除绑定");
                    startActivityForResult(new Intent(BindAliPayActivity.this,UnAliPayBingActivity.class), 200);//2是我们自己定义常量，对应下面使用到的resultCode
                }else {
                    bindingAliData(usetName,amoutNum);
//                    TioToast.showShort("绑定");
                }
            }
        });

    }
    private void initUiData(){
        new AliPayGetDataReq().setCancelTag(this).get(new TioCallback<GetAliPayDataResp>() {
            @Override
            public void onTioSuccess(GetAliPayDataResp dataResp) {
                Log.d("===获取支付宝==","=onTioSuccess="+dataResp.alipayname);
                if(!TextUtils.isEmpty(dataResp.alipayname)){
                    binding.etNameUser.setText(dataResp.alipayname);
                    binding.etNameUser.setFocusable(false);
                    binding.etAmountText.setText(dataResp.alipay);
                    binding.etAmountText.setFocusable(false);
                    binding.tvOkBtn.setText("解除绑定");
                    binding.etNameUser.setFocusableInTouchMode(false);
                    binding.etAmountText.setFocusableInTouchMode(false);

                }else {
                    binding.etNameUser.setText("");
                    binding.etAmountText.setText("");
                    binding.tvOkBtn.setText("绑定");
                    binding.etNameUser.setFocusable(true);
                    binding.etAmountText.setFocusable(true);
                    binding.etNameUser.setFocusableInTouchMode(true);
                    binding.etAmountText.setFocusableInTouchMode(true);
                    binding.etNameUser.requestFocus();
                }


            }

            @Override
            public void onTioError(String msg) {
                Log.d("===获取支付宝onTioError==","=="+msg);

            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (200 == requestCode) {
            //do something
            umbindingAliData();
        }
    }
    private void bindingAliData(String usetName,String amoutNum){
        new AliPayBindNumReq(usetName, amoutNum).setCancelTag(this).post(new TioCallback<Object>() {
            @Override
            public void onTioSuccess(Object o) {
                Log.d("===绑定支付宝==","=="+o.toString());
                TioToast.showShort("绑定成功");
                finish();
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(""+msg);

            }
        });
    }
    private void umbindingAliData(){
        Log.d("===解除绑定==",paypass+"==");
        binding.etNameUser.setText("");
        binding.etAmountText.setText("");
        binding.tvOkBtn.setText("绑定");
        binding.etNameUser.setFocusable(true);
        binding.etAmountText.setFocusable(true);
        binding.etNameUser.setFocusableInTouchMode(true);
        binding.etAmountText.setFocusableInTouchMode(true);
        binding.etNameUser.requestFocus();
//        finish();
        TioToast.showShort("解绑成功");

    }
    @Override
    protected int getContentViewId() {
        return R.layout.activity_bind_ali_pay;
    }

    @Override
    protected View statusBar_holder() {
        return binding.statusBarNew;
    }

    @Override
    protected Integer statusBar_color() {
        return Color.WHITE;
    }

}