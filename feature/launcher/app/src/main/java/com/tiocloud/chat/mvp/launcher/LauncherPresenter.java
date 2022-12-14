package com.tiocloud.chat.mvp.launcher;

import android.Manifest;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;
import com.tiocloud.account.TioAccount;
import com.tiocloud.account.data.AccountSP;
import com.tiocloud.account.mvp.logout.LogoutPresenter;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.OssDataJsonBean;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.chat.util.AppUpdateTool;
import com.tiocloud.chat.widget.dialog.tio.ProtectGuideDialog;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.androidutils.widget.dialog.progress.SingletonProgressDialog;
import com.watayouxiang.db.dao.CurrUserTableCrud;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.callback.TioCallbackImpl;
import com.watayouxiang.httpclient.model.request.LoginReq;
import com.watayouxiang.httpclient.model.request.UserCurrReq;
import com.watayouxiang.httpclient.model.response.ConfigResp;
import com.watayouxiang.httpclient.model.response.LoginResp;
import com.watayouxiang.httpclient.model.response.UserCurrResp;
import com.watayouxiang.httpclient.preferences.CookieUtils;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.httpclient.utils.PreferencesUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Random;

import static org.webrtc.ContextUtils.getApplicationContext;

import androidx.annotation.NonNull;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc : ?????????????????????
 */
public class LauncherPresenter extends LauncherContract.Presenter {

    private AppUpdateTool appUpdateTool;

    public LauncherPresenter(LauncherContract.View view) {
        super(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (appUpdateTool != null) {
            appUpdateTool.release();
        }
    }

    @Override
    public void init() {
//        checkAppUpdate();
//        reqConfig();
        PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(@NonNull List<String> granted) {
                        getOSSGetIP();
                    }

                    @Override
                    public void onDenied(@NonNull List<String> deniedForever, @NonNull List<String> denied) {
                       ToastUtils.showShort("????????????????????????");
                    }
                })
                .request();

    }
    private void getOSSGetIP(){

        String objectName = TioConfig.OSS_objectName;
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // ?????????????????????15???
        conf.setSocketTimeout(15 * 1000); // socket???????????????15???
        conf.setMaxConcurrentRequest(5); // ??????????????????????????????5???
        conf.setMaxErrorRetry(2); // ????????????????????????????????????2???
//                String signedString = OSSUtils.sign(Config.OSS_ACCESS_KEY_ID, Config.OSS_ACCESS_KEY_SECRET, content);
        OSSCustomSignerCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                // ??????????????????????????????contentString?????????????????????????????????,??????????????????????????????????????????content?????????????????????????????????????????????
                // ???????????????http://help.aliyun.com/document_detail/oss/api-reference/access-control/signature-header.html????????????
                // ??????????????????????????????OSSUtils.sign(accessKey,screctKey,content)
                String signedString = OSSUtils.sign(TioConfig.OSS_ACCESS_KEY_ID, TioConfig.OSS_ACCESS_KEY_SECRET, content);
                return signedString;
            }
        };
        OSS oss = new OSSClient(TioApplication.getInstanceKit(), TioConfig.OSS_ENDPOINT, credentialProvider, conf);
        GetObjectRequest get = new GetObjectRequest(TioConfig.BUCKET_NAME, objectName);
        oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                try {
                    InputStream inputStream = result.getObjectContent();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    String contentJson = "";
                    while (true) {
                        String content = reader.readLine();
                        if (content == null) break;
                        contentJson = contentJson + content;
//                        System.out.println("========" + content);
                    }
                    Log.d("hjq",  contentJson);
                    OssDataJsonBean jsonBean = new Gson().fromJson(contentJson, OssDataJsonBean.class);
                    if (jsonBean != null) {

                        if (jsonBean.getAPI_URLS() != null && jsonBean.getAPI_URLS().size() > 0) {
                            int min = 1;
                            int max = jsonBean.getAPI_URLS().size();
                            Random random = new Random();
                            int randomNum = random.nextInt(max) % (max - min + 1) + min;
                            HttpPreferences.saveBaseUrl(jsonBean.getAPI_URLS().get(randomNum - 1));

                            HttpCache.TIO_BASE_URL = jsonBean.getAPI_URLS().get(randomNum - 1);
                            String save = HttpCache.TIO_BASE_URL.substring(0, HttpCache.TIO_BASE_URL.length() - 5);
                            HttpCache.TIO_RES_URL = save;
                            HttpCache.TIO_BASE_URL = save;
                            String getStringUrl = PreferencesUtil.getString(PreferencesUtil.SAVEBASEURL, "");
                            Log.d("hjq", "getAPI_URLS=" + jsonBean.getAPI_URLS());
                            Log.d("hjq", "TIO_BASE_URL=" + HttpCache.TIO_BASE_URL);
                            Log.d("hjq", "getStringUrl=" + getStringUrl);
                            Log.d("hjq", "save=" + save);
                            if (!TextUtils.isEmpty(getStringUrl) && HttpCache.TIO_BASE_URL.contains(getStringUrl)) {
                                reqConfig();
                            } else {
//                                LogoutPresenter.clearLoginInfo();
                                String account = AccountSP.getLoginName();
                                String paw = AccountSP.getKeyLoginPwd();
                                PreferencesUtil.saveString(PreferencesUtil.SAVEBASEURL, save);

                                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(paw) && TioDBPreferences.getCurrUid() > 0) {
                                    LoginReq.getPwdInstance(paw, account).setCancelTag(this).post(new TioCallbackImpl<LoginResp>() {
                                        @Override
                                        public void onTioSuccess(LoginResp loginResp) {
                                            // ??????????????????
                                            new UserCurrReq().setCancelTag(this).get(new TioCallbackImpl<UserCurrResp>() {
                                                @Override
                                                public void onTioSuccess(UserCurrResp userCurrResp) {
//                                            Log.d("====????????????==","===????????????==");
                                                    // ??????????????????
                                                    CurrUserTableCrud.insert(userCurrResp);
                                                    // ????????????
                                                    TioAccount.getBridge().startMainActivity(Utils.getApp());
                                                    // ??????????????????
                                                    ActivityUtils.finishAllActivities();
                                                }

                                                @Override
                                                public void onTioError(String msg) {

                                                }
                                            });
                                        }

                                        @Override
                                        public void onTioError(String msg) {
                                            reqConfig();
                                        }
                                    });
                                } else {
                                    reqConfig();
                                }

                            }

                            System.out.println("====URL====" + HttpCache.TIO_BASE_URL);

                        }
                        if (jsonBean.getRES_URLS() != null && jsonBean.getRES_URLS().size() > 0) {
                            int min = 1;
                            int max = jsonBean.getRES_URLS().size();
                            Random random = new Random();
                            int randomNum = random.nextInt(max) % (max - min + 1) + min;
                            HttpPreferences.saveResUrl(jsonBean.getRES_URLS().get(randomNum - 1));
                            HttpCache.TIO_RES_URL = jsonBean.getRES_URLS().get(randomNum - 1);
                            System.out.println("====RES====" + HttpCache.TIO_RES_URL);

                        }
                    }
                } catch (Exception e) {
                    // ??????????????????????????????
                    e.printStackTrace();
                    SingletonProgressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(GetObjectRequest request, ClientException clientException, ServiceException serviceException) {
                ToastUtils.showShort("??????????????????");
                SingletonProgressDialog.dismiss();
              if (clientException!=null){
                  clientException.printStackTrace();
              }
                if (serviceException!=null){
                    serviceException.printStackTrace();
                }
            }
        });
    }

    private void checkAppUpdate() {
        // ??????app??????
        if (appUpdateTool == null) {
            appUpdateTool = new AppUpdateTool(getView().getActivity());
            appUpdateTool.setOnNextStepListener(this::showGuideDialog);
        }
        appUpdateTool.checkUpdateOnLauncher();
    }

    private void showGuideDialog() {
        // ??????????????????????????????
        new ProtectGuideDialog(getView().getActivity(), this::reqPermission).checkConfirm();
    }

    private void reqPermission() {
        // ????????????
        PermissionUtils.permission(PermissionConstants.PHONE)
                .rationale((activity, shouldRequest) -> shouldRequest.again(true))
                .callback((isAllGranted, granted, deniedForever, denied) -> reqConfig())
                .request();
    }

    private void reqConfig() {
        // ??????????????????
        getModel().requestConfig(new BaseModel.DataProxy<ConfigResp>() {
            @Override
            public void onSuccess(ConfigResp resp) {
                openNextPage(resp);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                SingletonProgressDialog.dismiss();
                exitApp("???????????????????????????" + msg);
            }
        });
    }

    private void openNextPage(ConfigResp resp) {
//        if (!getModel().isLogin()) {
//            getView().openLoginPage();
//        } else {
//            getView().openMainPage();
//        }
//        getView().finish();
        PreferencesUtil.saveInt(PreferencesUtil.LOGIN_TYPE,resp.user_login_type);
        try {
            if (!getModel().isLogin()) {
                getView().openLoginPage(resp.user_login_type==11);
            } else {
                getView().openMainPage();
            }
            getView().finish();
        }catch (Exception e){
            getView().openLoginPage(true);
        }
    }

    private void exitApp(String reason) {
        TioToast.showShort(reason);
        //??????????????????
        AppUtils.exitApp();
    }
}
