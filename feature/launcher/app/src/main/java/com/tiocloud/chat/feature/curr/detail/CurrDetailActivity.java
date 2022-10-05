package com.tiocloud.chat.feature.curr.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tiocloud.account.TioAccount;
import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.dialognew.CommonTextInputDialog;
import com.tiocloud.chat.baseNewVersion.dialognew.SexSelectDialog;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.databinding.TioCurrInfoActivityBinding;
import com.tiocloud.chat.feature.curr.detail.mvp.CurrInfoContract;
import com.tiocloud.chat.feature.curr.detail.mvp.CurrInfoPresenter;
import com.tiocloud.chat.feature.curr.modify.ModifyActivity;
import com.tiocloud.chat.feature.curr.modify.model.ModifyType;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.model.request.UpdateNickReq;
import com.watayouxiang.httpclient.model.request.UpdateSexReq;
import com.watayouxiang.httpclient.model.request.UpdateSignReq;
import com.watayouxiang.httpclient.model.response.UserCurrResp;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * author : TaoWang
 * date : 2020/3/12
 * desc : 个人信息页
 */
public class CurrDetailActivity extends TioActivity implements CurrInfoContract.View {

    private TioCurrInfoActivityBinding binding;
    private CurrInfoPresenter presenter;

    public static void start(Context context) {
        Intent starter = new Intent(context, CurrDetailActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.tio_curr_info_activity);
        setTransparent(this);

        binding.titleBar.setTitle("个人资料");
        presenter = new CurrInfoPresenter(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.getAvatarDialog().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.updateUIData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onUserCurrResp(UserCurrResp userCurr) {
        binding.hivAvatar.load_tioAvatar(userCurr.avatar);
//        Glide.with(getContext())
//                .downloadOnly()
//                .load(getResUrl(userCurr.avatar))
//                .listener(new RequestListener<File>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                        Log.d("===下载失败===","===");
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===下载成功===",resource.getName()+"==="+resource.getAbsolutePath());
//                        byte[] bytes= FileUtils.File2byte(resource);
//                        try {
//                            byte[] imagebyte2=  FileUtils.encryptByte(bytes);
////                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
////                                    msgImageView.setImageBitmap(bitmap);
//                            HelperGlide.loadHead(getContext(),imagebyte2, binding.hivAvatar);
//
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        return false;
//                    }
//                })
//                .preload();
        binding.tvNick.setText(StringUtil.nonNull(userCurr.nick));
        binding.tvGender.setText(StringUtil.nonNull(userCurr.getSex()));
        binding.tvSign.setText(StringUtil.nonNull(userCurr.sign));
        binding.tvRegion.setText(StringUtil.nonNull(userCurr.getRegion()));
        binding.tvInviteCode.setText(StringUtil.nonNull(userCurr.invitecode));

        binding.rlNick.setOnClickListener(view ->
//                ModifyActivity.start_curr(view.getContext(), ModifyType.CURR_NICK, userCurr.nick);
        new CommonTextInputDialog(this)
                .setEditHeight(100)
                .setTopTitle(getString(R.string.nick))
                .setSubTitle(getString(R.string.good_nick))
                .setPositiveText(getString(R.string.save))
                .setMaxLimit(20)
                .setEdittext(userCurr.nick)
                .showClearButton(false)
                .setOnBtnListener(new CommonTextInputDialog.OnBtnListener() {
                    @SuppressLint("StringFormatInvalid")
                    @Override
                    public void onClickPositive(View view, String submitTxt, CommonTextInputDialog dialog) {
                        if (TextUtils.isEmpty(submitTxt)){
                            ToastUtils.showShort(getString(R.string.nick_not_empty));
                            return;
                        }
                        dialog.dismiss();
                        UpdateNickReq updateNickReq = new UpdateNickReq(submitTxt);
                        updateNickReq.setCancelTag(this);
                        updateNickReq.post(new TioCallback<Void>() {
                            @Override
                            public void onTioSuccess(Void aVoid) {
//                                    proxy.onSuccess(null);
                                ToastUtils.showShort(getString(R.string.repair_success));
                                presenter.updateUIData();
                            }

                            @Override
                            public void onTioError(String msg) {
                                ToastUtils.showShort(msg);
                            }
                        });
                    }

                    @Override
                    public void onClickNegative(View view, CommonTextInputDialog dialog) {
                        dialog.dismiss();
                    }
                }).show()
        );
        binding.rlSign.setOnClickListener(view ->
//                ModifyActivity.start_curr(view.getContext(), ModifyType.CURR_SIGN, userCurr.sign)
                new CommonTextInputDialog(this)
                        .setEditHeight(200)
                        .setTopTitle(getString(R.string.personal_sign))
                        .setSubTitle(getString(R.string.good_sign))
                        .setPositiveText(getString(R.string.save))
                        .setMaxLimit(50)
                        .setEdittext(userCurr.sign)
                        .showClearButton(false)
                        .setOnBtnListener(new CommonTextInputDialog.OnBtnListener() {
                            @Override
                            public void onClickPositive(View view, String submitTxt, CommonTextInputDialog dialog) {
                                dialog.dismiss();
                                UpdateSignReq req = new UpdateSignReq(submitTxt);
                                req.setCancelTag(this);
                                req.post(new TioCallback<Void>() {
                                    @Override
                                    public void onTioSuccess(Void aVoid) {
//                                    proxy.onSuccess(null);
                                        ToastUtils.showShort(getString(R.string.save_success));
                                        presenter.updateUIData();
                                    }

                                    @Override
                                    public void onTioError(String msg) {
//                                    proxy.onFailure(msg);
                                        presenter.updateUIData();
                                    }
                                });
                            }

                            @Override
                            public void onClickNegative(View view, CommonTextInputDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show()

        );
        binding.rlGender.setOnClickListener(view ->
//                presenter.showGenderDialog(view.getContext(), userCurr.sex)
        new SexSelectDialog(CurrDetailActivity.this)
                .setOnBtnListener(new SexSelectDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, int sex, SexSelectDialog dialog) {
                        reqUpdateSex(String.valueOf(sex));
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickNegative(View view, SexSelectDialog dialog) {
                        dialog.dismiss();
                    }
                }).show()
        );
        binding.vgAvatar.setOnClickListener(view -> presenter.getAvatarDialog().show());
    }
    private void reqUpdateSex(String sex) {
        UpdateSexReq req = new UpdateSexReq(sex);
        req.setCancelTag(this);
        req.post(new TioCallback<Void>() {
            @Override
            public void onTioSuccess(Void aVoid) {
                ToastUtils.showShort(getString(R.string.repair_success));
                presenter.updateUIData();
            }

            @Override
            public void onTioError(String msg) {

            }
        });
    }
}
