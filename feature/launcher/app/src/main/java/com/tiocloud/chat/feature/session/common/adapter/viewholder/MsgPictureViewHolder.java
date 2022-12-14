package com.tiocloud.chat.feature.session.common.adapter.viewholder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.blankj.utilcode.util.ImageUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.util.QMUIDrawableHelper;
import com.tiocloud.chat.R;
import com.tiocloud.chat.TioApplication;
import com.tiocloud.chat.baseNewVersion.OssDataJsonBean;
import com.tiocloud.chat.baseNewVersion.utils2.HelperGlide;
import com.tiocloud.chat.baseNewVersion.view.BaseRVAdapter;
import com.tiocloud.chat.constant.TioConfig;
import com.tiocloud.chat.feature.session.common.SessionActivity;
import com.tiocloud.chat.feature.session.common.adapter.MsgAdapter;
import com.tiocloud.chat.feature.session.common.adapter.msg.TioMsg;
import com.tiocloud.chat.feature.session.common.adapter.model.TioMsgType;
import com.tiocloud.chat.feature.session.common.adapter.viewholder.base.MsgBaseViewHolder;
import com.tiocloud.chat.util.AESEncrypt;
import com.tiocloud.chat.util.AesUtil;
import com.tiocloud.chat.util.FileUtil;
import com.tiocloud.chat.util.FileUtils;
import com.watayouxiang.androidutils.engine.ImageAesFingerprinBean;
import com.watayouxiang.androidutils.tools.TioLogger;
import com.watayouxiang.androidutils.utils.UrlUtil;
import com.watayouxiang.androidutils.widget.TioToast;
import com.watayouxiang.httpclient.callback.TioFileCallback;
import com.watayouxiang.httpclient.preferences.HttpCache;
import com.watayouxiang.httpclient.preferences.HttpPreferences;
import com.watayouxiang.imclient.model.body.wx.msg.InnerMsgImage;
import com.tiocloud.chat.util.TioImageBrowser;
import com.watayouxiang.androidutils.widget.imageview.TioImageView;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static com.tiocloud.chat.util.AESEncrypt.getFileName;


/**
 * author : TaoWang
 * date : 2020/3/5
 * desc :
 */
public class MsgPictureViewHolder extends MsgBaseViewHolder {

    private TioImageView msgImageView;
    private String mOriginalImgUrl;
    private ProgressBar progressBar;
    private ImageView iv_chongxinjiazai;
    private String fingerprint;
    public MsgPictureViewHolder(MsgAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int contentResId() {
        return R.layout.tio_msg_item_picture;
    }

    @Override
    protected void inflateContent() {
        msgImageView = findViewById(R.id.msgImageView);
        progressBar = findViewById(R.id.progress);
        iv_chongxinjiazai=findViewById(R.id.iv_chongxinjiazai);
        iv_chongxinjiazai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOSSGetIP();

            }
        });
        findViewById(R.id.msgImageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWatchImageActivity(v);
            }
        });
        findViewById(R.id.msgImageView).setOnLongClickListener(onContentLongClick());
    }

    @Override
    protected View.OnLongClickListener onContentLongClick() {
         return view -> {
            Log.d("===changan==","=22=");
            showAttachView(view, null);
            return true;
        };
    }
    @Override
    protected void bindContent(BaseViewHolder holder) {
        InnerMsgImage data = (InnerMsgImage) getMessage().getContentObj();
        if (data == null) return;
        if (getMessage().isFault()){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(data.progress);
            progressBar.setMax(100);

        }else {
            progressBar.setVisibility(View.GONE);
            mOriginalImgUrl = data.url;
            fingerprint=data.fingerprint;

//            secretkey=data.secretkey;

//            msgImageView.load_tioMsgPic(data.coverurl, data.width, data.height);
//            HelperGlide.loadImg(msgImageView.getContext(),""+ HttpCache.TIO_RES_URL+data.url,msgImageView);
            try {
                Log.d("===????????????=url==",data.fingerprint+"=="+data.url);
                Log.d("===????????????=secretkey==",data.secretkey+"=="+data.secretkey);
                if(!TextUtils.isEmpty(data.fingerprint)){
                    //??????????????????????????????????????????
//                    OkGo.<File>get(HttpCache.TIO_RES_URL+data.coverurl)
//                            .tag(this)
//                            .execute(new TioFileCallback() {
//                                @Override
//                                public void onStart(com.lzy.okgo.request.base.Request<File, ? extends com.lzy.okgo.request.base.Request> request) {
//                                    TioLogger.d("======???????????????");
//                                }
//
//                                @Override
//                                public void onSuccess(Response<File> response) {
//                                    TioLogger.d("====????????????=="+response.body());
//                                    AESEncrypt.decryptFile(response.body().getParentFile(),FileUtils.bytePath,  getFileName(data.coverurl),data.fingerprint);
////
//                                    Glide.with(msgImageView.getContext()).load(FileUtils.bytePath+ getFileName(data.coverurl)).listener(new RequestListener<Drawable>() {
//                                        @Override
//                                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                            iv_chongxinjiazai.setVisibility(View.VISIBLE);
//                                            Log.d("===??????????????????222===","===");
//
//                                            return false;
//                                        }
//                                        @Override
//                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                            Log.d("===??????????????????===","===");
//                                            iv_chongxinjiazai.setVisibility(View.GONE);
//                                            return false;
//                                        }}).into(msgImageView);
//                                }
//
//                                @Override
//                                public void onError(Response<File> response) {
//                                    TioLogger.d("===????????????");
//                                }
//
//                                @Override
//                                public void downloadProgress(Progress progress) {
//                                    TioLogger.d("===???????????????" + progress);
//
//
//                                }
//                            });
                    Glide.with(msgImageView.getContext())
                            .downloadOnly()
                            .load(HttpCache.TIO_RES_URL+data.coverurl)
                            .listener(new RequestListener<File>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                    Log.d("===????????????===","===");
                                    iv_chongxinjiazai.setVisibility(View.VISIBLE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
//                                    Log.d("===????????????===",data.fingerprint+"==="+resource.getName()+"==="+resource.getAbsolutePath());
//                                byte[] bytes= FileUtils.File2byte(resource);
                                    try {
//                                    byte[] imagebyte2=  FileUtils.encryptByte(bytes);
//                                        byte [] encsome = AesUtil.decrypt(data.fingerprint, bytes);
                                       AESEncrypt.decryptFile(resource,FileUtils.bytePath, getFileName(data.coverurl),data.fingerprint);
                                        Glide.with(msgImageView.getContext()).load(FileUtils.bytePath+ getFileName(data.coverurl)).listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                iv_chongxinjiazai.setVisibility(View.VISIBLE);
                                                Log.d("===??????????????????222===","===");
                                                boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ getFileName(data.coverurl));

                                                return false;
                                            }
                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                               //?????????????????????????????????????????????
                                                boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ getFileName(data.coverurl));
//                                                Log.d("===??????????????????===",isRemove+"===");

                                                iv_chongxinjiazai.setVisibility(View.GONE);
                                                return false;
                                            }}).into(msgImageView);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    return false;
                                }
                            })
                            .preload();
                }else {

                    Glide.with(msgImageView.getContext()).load(HttpCache.TIO_RES_URL+data.coverurl).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.d("===??????????????????===",HttpCache.TIO_RES_URL+"===");
                            iv_chongxinjiazai.setVisibility(View.VISIBLE);

                            return false;
                        }
                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            iv_chongxinjiazai.setVisibility(View.GONE);
                            return false;
                        }}).into(msgImageView);
                }

//                Glide.with(msgImageView.getContext())
//                        .downloadOnly()
//                        .load(HttpCache.TIO_RES_URL+data.coverurl)
//                        .listener(new RequestListener<File>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//                               Log.d("===????????????===","===");
//                                iv_chongxinjiazai.setVisibility(View.VISIBLE);
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
////                                Log.d("===????????????===",resource.getName()+"==="+resource.getAbsolutePath());
//                                byte[] bytes= FileUtils.File2byte(resource);
//                                try {
//                                    byte[] imagebyte2=  FileUtils.encryptByte(bytes);
//                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
//                                    msgImageView.setImageBitmap(bitmap);
//                                    Glide.with(msgImageView.getContext()).load(imagebyte2).listener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
////                                    Log.d("===??????????????????===",HttpCache.TIO_RES_URL+"===");
//                                    iv_chongxinjiazai.setVisibility(View.VISIBLE);
//
//                                    return false;
//                                }
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    Log.d("===??????????????????===","===");
//                                    iv_chongxinjiazai.setVisibility(View.GONE);
//                                    return false;
//                                }}).into(msgImageView);
//
//
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//
//                                return false;
//                            }
//                        })
//                        .preload();


            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }

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
        //????????????????????????
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                Log.d("===getobj_progress:== " ,currentSize+" == total_size: " + totalSize);
            }
        });
        try {
            // ???????????????????????????????????????
            GetObjectResult getResult =oss.getObject(get);
            // ?????????????????????
            InputStream inputStream = getResult.getObjectContent();
            byte[] buffer = new byte[2048];
            int len;
            InputStreamReader inputStreamReader=  new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String  contentJson = "";
            while (true){
                String content=reader.readLine();
                if (content == null) break;
                contentJson=contentJson+content;
            }
            Log.d("===content222===","=="+contentJson);
            OssDataJsonBean jsonBean=new Gson().fromJson(contentJson,OssDataJsonBean.class);
            if(jsonBean!=null){
                if(jsonBean.getAPI_URLS()!=null&&jsonBean.getAPI_URLS().size()>0){
                    HttpPreferences.saveBaseUrl(jsonBean.getAPI_URLS().get(0));
                }
                if(jsonBean.getRES_URLS()!=null&&jsonBean.getRES_URLS().size()>0){
                    HttpPreferences.saveResUrl(jsonBean.getRES_URLS().get(0));
                    HttpCache.TIO_RES_URL=jsonBean.getRES_URLS().get(0);
                    if(!TextUtils.isEmpty(fingerprint)){
                        Glide.with(msgImageView.getContext())
                                .downloadOnly()
                                .load(HttpCache.TIO_RES_URL+mOriginalImgUrl)
                                .listener(new RequestListener<File>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                                        Log.d("===????????????===","===");
                                        iv_chongxinjiazai.setVisibility(View.VISIBLE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
//                                Log.d("===????????????===",resource.getName()+"==="+resource.getAbsolutePath());
//                                    byte[] bytes= FileUtils.File2byte(resource);
                                        try {
//                                        byte[] imagebyte2=  FileUtils.encryptByte(bytes);
//                                    Bitmap  bitmap= Bytes2Bimap(imagebyte2);
//                                    msgImageView.setImageBitmap(bitmap);

                                            AESEncrypt.decryptFile(resource,FileUtils.bytePath, getFileName(mOriginalImgUrl),fingerprint);
                                            Glide.with(msgImageView.getContext()).load(FileUtils.bytePath+ getFileName(mOriginalImgUrl)).listener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    iv_chongxinjiazai.setVisibility(View.VISIBLE);
                                                    boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ getFileName(mOriginalImgUrl));
                                                    Log.d("===??????????????????222===","===");

                                                    return false;
                                                }
                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                    //?????????????????????????????????????????????
                                                    boolean isRemove=AESEncrypt.deleteFile(FileUtils.bytePath+ getFileName(mOriginalImgUrl));
                                                    Log.d("===??????????????????===",isRemove+"===");
                                                    iv_chongxinjiazai.setVisibility(View.GONE);
                                                    return false;
                                                }}).into(msgImageView);



                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        return false;
                                    }
                                })
                                .preload();
                    }else {
                        Glide.with(msgImageView.getContext()).load(HttpCache.TIO_RES_URL+mOriginalImgUrl).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                Log.d("===??????????????????===",HttpCache.TIO_RES_URL+"===");
                                iv_chongxinjiazai.setVisibility(View.VISIBLE);

                                return false;
                            }
                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Log.d("===??????????????????=2==","===");
                                iv_chongxinjiazai.setVisibility(View.GONE);
                                return false;
                            }}).into(msgImageView);
                    }

                }
            }
//                HttpPreferences.saveResUrl("");

        } catch (ClientException e) {
            // ??????????????????????????????
            e.printStackTrace();
        } catch (ServiceException e) {
            // ????????????
            Log.e("RequestId", e.getRequestId());
            Log.e("ErrorCode", e.getErrorCode());
            Log.e("HostId", e.getHostId());
            Log.e("RawMessage", e.getRawMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected View.OnClickListener onContentClick() {

        return null;
    }

    /**
     * ?????????????????? - ??????????????????????????????
     */
    private  ArrayList<String> imgUrls=new ArrayList<>();
    private  List<ImageAesFingerprinBean> imgUrlsfingerprint=new ArrayList<>();

    private void openWatchImageActivity(View view) {
        imgUrlsfingerprint.clear();
        List<TioMsg> data = getAdapter().getData();
        int size;

        if (data == null || (size = data.size()) == 0) return;

        // ???????????? "????????????"
        ArrayList<TioMsg> imgMsgs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            TioMsg msg = data.get(i);
            if (msg.getMsgType() == TioMsgType.image) {
                Object contentObj = msg.getContentObj();
                if (contentObj instanceof InnerMsgImage) {
                    imgMsgs.add(msg);

                }
            }
        }

        // ?????? "????????????"
        // ???????????? "??????" ???????????????
//        imgUrls= new ArrayList<>(imgMsgs.size());
        int imgUrlsIndex = 0;
        int currImgPosition = 0;
        for (int i = 0, imgMsgSize = imgMsgs.size(); i < imgMsgSize; i++) {
            TioMsg msg = imgMsgs.get(i);
            if (msg.getId() != null && msg.getId().equals(getMessage().getId())) {
                currImgPosition = imgUrlsIndex;
            }
            InnerMsgImage imgMsg = (InnerMsgImage) msg.getContentObj();

            imgUrls.add(imgUrlsIndex++, imgMsg.url);
            ImageAesFingerprinBean fingerprinBean= new ImageAesFingerprinBean();
            fingerprinBean.setImgUrls(imgMsg.url+"");
            fingerprinBean.setFingerprint(imgMsg.fingerprint);
            fingerprinBean.setSecretkey(imgMsg.secretkey);
//            Log.d("===imgUrls===","=imgUrlsfingerprint==="+imgMsg.fingerprint+"=="+imgMsg.secretkey);

            imgUrlsfingerprint.add(fingerprinBean);

        }
        // ????????????
        if (mOriginalImgUrl != null) {
            TioImageBrowser.getInstance().showPic(view, currImgPosition,imgUrlsfingerprint, imgUrls.toArray(new String[]{}));

        }
    }
}
