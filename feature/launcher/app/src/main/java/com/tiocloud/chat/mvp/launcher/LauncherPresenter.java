package com.tiocloud.chat.mvp.launcher;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCustomSignerCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.OSSUtils;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PermissionUtils;
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
import java.util.Random;

import static org.webrtc.ContextUtils.getApplicationContext;

/**
 * author : TaoWang
 * date : 2020-02-12
 * desc : 应用启动表现层
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
        getOSSGetIP();
    }
    private void getOSSGetIP(){

        String objectName = TioConfig.OSS_objectName;
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
//                String signedString = OSSUtils.sign(Config.OSS_ACCESS_KEY_ID, Config.OSS_ACCESS_KEY_SECRET, content);
        OSSCustomSignerCredentialProvider credentialProvider = new OSSCustomSignerCredentialProvider() {
            @Override
            public String signContent(String content) {
                // 此处本应该是客户端将contentString发送到自己的业务服务器,然后由业务服务器返回签名后的content。关于在业务服务器实现签名算法
                // 详情请查看http://help.aliyun.com/document_detail/oss/api-reference/access-control/signature-header.html。客户端
                // 的签名算法实现请参考OSSUtils.sign(accessKey,screctKey,content)
                String signedString = OSSUtils.sign(TioConfig.OSS_ACCESS_KEY_ID, TioConfig.OSS_ACCESS_KEY_SECRET, content);
                return signedString;
            }
        };
        OSS oss = new OSSClient(TioApplication.getInstanceKit(), TioConfig.OSS_ENDPOINT, credentialProvider, conf);
        GetObjectRequest get = new GetObjectRequest(TioConfig.BUCKET_NAME, objectName);
        //设置下载进度回调
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
            }
        });
        try {
            // 同步执行下载请求，返回结果
            GetObjectResult getResult =oss.getObject(get);
            // 获取文件输入流
            InputStream inputStream = getResult.getObjectContent();
//            byte[] buffer = new byte[2048];
//            int len;
            InputStreamReader inputStreamReader=  new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String  contentJson = "";
            while (true){
                String content=reader.readLine();
                if (content == null) break;
                contentJson=contentJson+content;
//                        System.out.println("========" + content);
            }
            Log.d("===ss222===","=="+contentJson);
            OssDataJsonBean jsonBean=new Gson().fromJson(contentJson,OssDataJsonBean.class);
            if(jsonBean!=null){

                if(jsonBean.getAPI_URLS()!=null&&jsonBean.getAPI_URLS().size()>0){
                    int min=1;
                    int max=jsonBean.getAPI_URLS().size();
                    Random random = new Random();
                    int randomNum=  random.nextInt(max)%(max-min+1) + min;
                    HttpPreferences.saveBaseUrl(jsonBean.getAPI_URLS().get(randomNum-1));

                    HttpCache.TIO_BASE_URL=jsonBean.getAPI_URLS().get(randomNum-1);
                    String save=HttpCache.TIO_BASE_URL.substring(0, HttpCache.TIO_BASE_URL.length()-5);
                    String getStringUrl=PreferencesUtil.getString(PreferencesUtil.SAVEBASEURL,"");
                    if(!TextUtils.isEmpty(getStringUrl)){
                        Log.d("====是否包含==","==是==="+save);
                        reqConfig();
                    }else {
//                        LogoutPresenter.clearLoginInfo();
//                        Log.d("====是否包含==","==否==="+"===上次保存Url==="+getStringUrl+"===本次访问Url=="+save);
                        String account = AccountSP.getLoginName();
                        String  paw= AccountSP.getKeyLoginPwd();
                        PreferencesUtil.saveString(PreferencesUtil.SAVEBASEURL,save);

                        if (!TextUtils.isEmpty(account) &&!TextUtils.isEmpty(paw) &&TioDBPreferences.getCurrUid()>0) {
                            LoginReq.getPwdInstance(paw, account).setCancelTag(this).post(new TioCallbackImpl<LoginResp>() {
                                @Override
                                public void onTioSuccess(LoginResp loginResp) {
                                    Log.d("====自动登录==","=====");
                                    // 获取用户信息
                                    new UserCurrReq().setCancelTag(this).get(new TioCallbackImpl<UserCurrResp>() {
                                        @Override
                                        public void onTioSuccess(UserCurrResp userCurrResp) {
                                            Log.d("hjq","getPwdInstance");
//                                            Log.d("====自动登录==","===获取信息==");
                                            // 存储用户信息
                                            CurrUserTableCrud.insert(userCurrResp);
                                            // 打开主页
                                            TioAccount.getBridge().startMainActivity(Utils.getApp());
                                            // 关闭其他页面
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
                        }else {
                            reqConfig();
                        }

                    }

                    System.out.println("====URL====" + HttpCache.TIO_BASE_URL);

                }
                if(jsonBean.getRES_URLS()!=null&&jsonBean.getRES_URLS().size()>0){
                    int min=1;
                    int max=jsonBean.getRES_URLS().size();
                    Random random = new Random();
                    int randomNum=  random.nextInt(max)%(max-min+1) + min;
                    HttpPreferences.saveResUrl(jsonBean.getRES_URLS().get(randomNum-1));
                    HttpCache.TIO_RES_URL=jsonBean.getRES_URLS().get(randomNum-1);
                    System.out.println("====RES====" + HttpCache.TIO_RES_URL);

                }
//                if(jsonBean.getAPI_URLS()!=null&&jsonBean.getAPI_URLS().size()>0){
//                    HttpPreferences.saveBaseUrl(jsonBean.getAPI_URLS().get(0));
////                    TioConfig.BASE_URL=jsonBean.getAPI_URLS().get(0);
//                    HttpCache.TIO_BASE_URL=jsonBean.getAPI_URLS().get(0);
//                    reqConfig();
//
//                }
//                if(jsonBean.getRES_URLS()!=null&&jsonBean.getRES_URLS().size()>0){
//                    HttpPreferences.saveResUrl(jsonBean.getRES_URLS().get(0));
//                    HttpCache.TIO_RES_URL=jsonBean.getRES_URLS().get(0);
//
//                }
            }
//                HttpPreferences.saveResUrl("");

        } catch (ClientException e) {
            // 本地异常如网络异常等
            e.printStackTrace();
        } catch (ServiceException e) {
            // 服务异常
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkAppUpdate() {
        // 检测app更新
        if (appUpdateTool == null) {
            appUpdateTool = new AppUpdateTool(getView().getActivity());
            appUpdateTool.setOnNextStepListener(this::showGuideDialog);
        }
        appUpdateTool.checkUpdateOnLauncher();
    }

    private void showGuideDialog() {
        // 显示隐私政策确认弹窗
        new ProtectGuideDialog(getView().getActivity(), this::reqPermission).checkConfirm();
    }

    private void reqPermission() {
        // 请求权限
        PermissionUtils.permission(PermissionConstants.PHONE)
                .rationale((activity, shouldRequest) -> shouldRequest.again(true))
                .callback((isAllGranted, granted, deniedForever, denied) -> reqConfig())
                .request();
    }

    private void reqConfig() {
        // 获取配置信息
        getModel().requestConfig(new BaseModel.DataProxy<ConfigResp>() {
            @Override
            public void onSuccess(ConfigResp resp) {

                openNextPage(resp);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                exitApp("获取配置信息失败：" + msg);
            }
        });
    }

    private void openNextPage(ConfigResp resp) {
        Log.d("hjq","openNextPage");
//        if (!getModel().isLogin()) {
//            getView().openLoginPage();
//        } else {
//            getView().openMainPage();
//        }
//        getView().finish();
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
        //避免退出软件
//        getView().openLoginPage();
//        AppUtils.exitApp();
    }
}
