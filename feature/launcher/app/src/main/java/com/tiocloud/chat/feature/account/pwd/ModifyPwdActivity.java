package com.tiocloud.chat.feature.account.pwd;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.tiocloud.account.mvp.logout.LogoutContract;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.watayouxiang.androidutils.widget.edittext.TioEditText;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.titlebar.WtTitleBar;

/**
 * author : TaoWang
 * date : 2020/3/13
 * desc :
 */
public class ModifyPwdActivity extends TioActivity implements LogoutContract.View {

    private TioEditText et_oldPwd;
    private TioEditText et_newPwd;
    private TioEditText et_newPwdConfirm;
    private TextView tv_save;

    private final ModifyPwdModel modifyPwdModel = new ModifyPwdModel();
    private final LogoutPresenter logoutPresenter = new LogoutPresenter(this);
    private WtTitleBar titleBar;

    public static void start(Context context) {
        Intent starter = new Intent(context, ModifyPwdActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(0xFFF8F8F8));
        setContentView(R.layout.tio_modify_pwd_activity);
        findViews(getWindow().getDecorView());
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        modifyPwdModel.detachModel();
        logoutPresenter.detachView();
    }

    private void initViews() {
        titleBar.setTitle(getString(R.string.modify_pwd));
        tv_save.setOnClickListener(view -> {

            String oldPwd = et_oldPwd.getSubmitText();
            String newPwd = et_newPwd.getSubmitText();
            String newPwdConfirm = et_newPwdConfirm.getSubmitText();

            if (newPwd == null) {
                TioToast.showShort("密码不能为空");
                return;
            }
            if (!newPwd.equals(newPwdConfirm)) {
                TioToast.showShort("确认密码和新密码不一致");
                return;
            }

            updatePwd(oldPwd, newPwd);
        });
    }

    private void updatePwd(String oldPwd, String newPwd) {
        modifyPwdModel.updatePwd(oldPwd, newPwd, new BaseModel.DataProxy<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // 密码修改成功
                TioToast.showShort("密码修改成功，请重新登录");
                // ModifyPwdActivity.this.finish();
                logoutPresenter.logout(getActivity());
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }

    private void findViews(View decorView) {
        titleBar = decorView.findViewById(R.id.titleBar);
        et_oldPwd = decorView.findViewById(R.id.et_oldPwd);
        et_newPwd = decorView.findViewById(R.id.et_newPwd);
        et_newPwdConfirm = decorView.findViewById(R.id.et_newPwdConfirm);
        tv_save = decorView.findViewById(R.id.tv_save);
    }
}
