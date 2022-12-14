package com.tiocloud.account.feature.register;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.Request;
import com.tiocloud.account.FilletTransformation;
import com.tiocloud.account.NewGlideEngine;
import com.tiocloud.account.R;
import com.tiocloud.account.TioAccount;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.account.dialognew.AvatarDialog;
import com.tiocloud.account.dialognew.CommonTextInputDialog;
import com.tiocloud.account.dialognew.SexSelectDialog;
import com.watayouxiang.androidutils.page.BaseActivity;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.androidutils.utils.UrlUtil;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.httpclient.TioHttpClient;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.BaseResp;
import com.watayouxiang.httpclient.model.request.AccountNewRegisterReq;
import com.watayouxiang.httpclient.model.request.AccountRegisterReq;
import com.watayouxiang.httpclient.model.request.LoginReq;
import com.watayouxiang.httpclient.model.request.UpdateNickReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.AccountRegisterResp;
import com.watayouxiang.httpclient.model.response.LoginResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.yalantis.ucrop.UCrop;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PerfectMessageActivity extends BaseActivity {
    private String loginName,passs;
    private TextView tv_nameText,tv_sexText;
    private AvatarDialog avatarDialog;
    private final static int REQ_CODE_IMAGE_GIF = 1334;// ???????????????gif
    private String imagePath;
    private TioImageView hiv_avatar;
    private List<String> mCurrentHQKSelectedPath = new ArrayList<>();//????????????
    private int sexType=3;
    private String friendInviteCode;
    private String registerType;
    private Uri mDestination;
    private String code;
    private int imageNum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfect_message);
        statusbar(false);
        loginName=getIntent().getStringExtra("nameNum");
        passs=getIntent().getStringExtra("passs");
        friendInviteCode=getIntent().getStringExtra("friendInviteCode");
        registerType=getIntent().getStringExtra("registerType");
        code=getIntent().getStringExtra("code");
        tv_nameText=findViewById(R.id.tv_nameText);
        tv_sexText=findViewById(R.id.tv_sexText);
        hiv_avatar=findViewById(R.id.hiv_avatar);
        findViewById(R.id.rlClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nametext=tv_nameText.getText().toString();

                if(TextUtils.isEmpty(imagePath)){
                    ToastUtils.showLong("???????????????");
                    return;
                }
                if(TextUtils.isEmpty(nametext)){
                    ToastUtils.showLong("???????????????");
                    return;
                }
                if(sexType==0){
                    ToastUtils.showLong("???????????????");
                    return;
                }
                String phone="";
                String codenew ="";
                if(registerType.equals("1")){
                    phone=loginName;
                    codenew=code;
                }
                new AccountNewRegisterReq(loginName,phone, nametext, passs, codenew,friendInviteCode,sexType+"",imagePath).setCancelTag(this).upload(new TioCallback<Void>() {
                    @Override
                    public void onStart(Request<BaseResp<Void>, ? extends Request> request) {
                        super.onStart(request);
                        SingletonProgressDialog.show_unCancel(PerfectMessageActivity.this, "?????????...");
                    }

                    @Override
                    public void onTioSuccess(Void aVoid) {
                        Log.d("=====??????===",loginName+"==");
                        SingletonProgressDialog.dismiss();

                        // ???????????????
                        AccountSP.putLoginName(loginName);
                        AccountSP.putKeyLoginPwd(passs);
                        // ??????????????????
                        SingletonProgressDialog.show_unCancel(PerfectMessageActivity.this, "????????????????????????...");
                        LoginReq.getPwdInstance(passs,loginName).setCancelTag(this).post(new TioCallbackImpl<LoginResp>() {
                            @Override
                            public void onTioSuccess(LoginResp loginResp) {
                                // ??????????????????
                                new UserCurrReq().setCancelTag(this).get(new TioCallbackImpl<UserCurrResp>() {
                                    @Override
                                    public void onTioSuccess(UserCurrResp userCurrResp) {

                                        // ???????????????
                                        AccountSP.putLoginName(loginName);
                                        // ??????????????????
                                        CurrUserTableCrud.insert(userCurrResp);
                                        // ????????????
                                        TioAccount.getBridge().startMainActivity(Utils.getApp());
                                        // ??????????????????
                                        ActivityUtils.finishAllActivities();
                                    }
                                    @Override
                                    public void onTioError(String msg) {
                                        ToastUtils.showLong(msg);
                                    }
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                        SingletonProgressDialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onTioError(String msg) {
                                ToastUtils.showLong(msg);
                                SingletonProgressDialog.dismiss();
                            }
                        });

                    }

                    @Override
                    public void onTioError(String msg) {
                        TioToast.showShort(msg);

                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        SingletonProgressDialog.dismiss();
                    }
                });

            }
        });
        findViewById(R.id.vg_avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PermissionUtils.permission(PermissionConstants.STORAGE,PermissionConstants.CAMERA)
                        .rationale((activity, shouldRequest) -> shouldRequest.again(true))
                        .callback((isAllGranted, granted, deniedForever, denied) -> {
                            if (isAllGranted) {
                                mDestination=null;
                                imageNum++;
                                mDestination = Uri.fromFile(new File(getCacheDir(), "cropImage"+imageNum+".jpeg"));

                                if (avatarDialog == null) {
                                    avatarDialog = new AvatarDialog(getActivity());
                                    avatarDialog.setActivity(getActivity());
                                }
                                avatarDialog.show();
//                                Matisse.from(PerfectMessageActivity.this)
//                                        .choose(MimeType.ofImage(), false)
//                                        .countable(true)
//                                        .capture(true)//??????????????????
//                                        .theme(R.style.Matisse_Dracula)
//                                        .captureStrategy(new CaptureStrategy(true, "com.meita.zhenjian.chat.fileprovider", "test"))
//                                        .maxSelectable(1)
//                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
//                                        .thumbnailScale(0.85f)
//                                        .imageEngine(new NewGlideEngine())
//                                        .showSingleMediaType(true)
//                                        .originalEnable(true)
//                                        .maxOriginalSize(1)
//                                        .autoHideToolbarOnSingleTap(true)
//                                        .forResult(REQ_CODE_IMAGE_GIF);
                            } else {
                                if (!deniedForever.isEmpty()) {
                                    PermissionUtils.launchAppDetailsSettings();
                                }
                                TioToast.showShort("???????????????????????????");
                            }
                        })
                        .request();
            }
        });
        findViewById(R.id.rlName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommonTextInputDialog(PerfectMessageActivity.this)
                        .setEditHeight(100)
                        .setTopTitle(getString(R.string.nick))
                        .setSubTitle(getString(R.string.good_nick))
                        .setPositiveText(getString(R.string.save))
                        .setMaxLimit(20)
                        .setEdittext(tv_nameText.getText().toString())
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
//                                Log.d("===??????==","=="+submitTxt);
                                tv_nameText.setText(submitTxt);
                            }

                            @Override
                            public void onClickNegative(View view, CommonTextInputDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        findViewById(R.id.rlSex).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SexSelectDialog(PerfectMessageActivity.this)
                        .setOnBtnListener(new SexSelectDialog.OnBtnListener() {
                            @Override
                            public void onClickPositive(View view, int sex, SexSelectDialog dialog) {
                                dialog.dismiss();
//                                Log.d("===??????==","=="+sex);
                                sexType=sex;
                                switch (sex){
                                    case 1:
                                        tv_sexText.setText("???");
                                        break;
                                    case 2:
                                        tv_sexText.setText("???");
                                        break;
                                    case 3:
                                        tv_sexText.setText("??????");
                                        break;
                                }

                            }

                            @Override
                            public void onClickNegative(View view, SexSelectDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }
    private String imagename;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (requestCode == REQ_CODE_IMAGE_GIF) {
//            mCurrentHQKSelectedPath = Matisse.obtainPathResult(data);
            ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
            // ????????????
            if (resultPhotos == null || resultPhotos.size() == 0) {
                return;
            }

            // ????????????
            Photo photo = resultPhotos.get(0);
            if (UrlUtil.isImageSuffix(photo.path) || UrlUtil.isGifSuffix(photo.path)) {
                UCrop.Options options = new UCrop.Options();
                // ?????????????????????
                options.setToolbarColor(this.getResources().getColor(R.color.white));
                options.setStatusBarColor(this.getResources().getColor(R.color.white));
                options.setToolbarWidgetColor(this.getResources().getColor(R.color.black));
                // ??????????????????
                options.setHideBottomControls(true);
                // ????????????
                options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                // ????????????????????????
                options.setCompressionQuality(100);
                // ????????????
                UCrop.of(photo.uri, mDestination)
                        // ?????????
                        .withAspectRatio(1, 1)
                        // ????????????
                        .withMaxResultSize(1024, 1024)
                        // ????????????
                        .withOptions(options)
                        .start(this, UCrop.REQUEST_CROP);
////                uploadAvatar(photo.path);
                imagename = photo.name;

            }

//            for (String path : mCurrentHQKSelectedPath) {
//                imagePath=path;
//                Log.d("=====??????=11===","=="+imagePath);
//                Glide.with(this).load(path)
//                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
//                        .into(hiv_avatar);
////
//            }
        } else if (requestCode == UCrop.REQUEST_CROP) {
//            try {
                String path = mDestination.getPath();
                imagePath = path;
                Log.d("=====??????=11===", "==" + imagePath);
                Glide.with(this).load(path).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(hiv_avatar);

//                try {
//                    byte[] imagebyte=  FileUtils.toByteArray(path);
//                    byte[] imagebyte2=  FileUtils.encryptByte(imagebyte);
//                    FileUtils.byteArrayToFile(imagebyte2, FileUtils.bytePath+imagename);
//                    imagePath=FileUtils.bytePath+imagename;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                } catch (Exception e) {
//                    Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
            }


    }
}