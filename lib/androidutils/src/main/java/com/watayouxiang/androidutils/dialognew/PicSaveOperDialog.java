package com.watayouxiang.androidutils.dialognew;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import com.watayouxiang.androidutils.R;
import com.watayouxiang.httpclient.model.vo.GroupRoleEnum;

public class PicSaveOperDialog  extends BaseDialogNew implements DialogInterface.OnCancelListener, View.OnClickListener {


    private View tv_saveBtn;
    private View tv_cancel;

    public PicSaveOperDialog(final Context context) {
        super(context);
        setAnimation(R.style.tio_bottom_dialog_anim);
        setFullScreenWidth();
        setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setOnCancelListener(this);
        setContentView(LayoutInflater.from(context).inflate(R.layout.tio_bottom_dialog_pic_save, null));
        initViews();
    }

    private void initViews() {
        tv_saveBtn = findViewById(R.id.tv_saveBtn);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_saveBtn.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
    }

    private void initUI() {

    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
//
//    public void show(String s) {
//        initUI();
//        super.show();
//    }

    @Deprecated
    @Override
    public void show() {
         super.show();
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            cancel();
        }
        onClick(this, v);
    }

    protected void onClick(PicSaveOperDialog picSaveOperDialog, View v) {

    }
}