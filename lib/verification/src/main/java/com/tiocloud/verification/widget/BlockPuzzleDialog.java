package com.tiocloud.verification.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tiocloud.verification.R;
import com.tiocloud.verification.model.CaptchaCheckIt;
import com.tiocloud.verification.model.CaptchaGetIt;
import com.tiocloud.verification.model.Point;
import com.tiocloud.verification.network.BaseObserver;
import com.tiocloud.verification.network.RetrofitUtils;
import com.tiocloud.verification.network.RxHelper;
import com.tiocloud.verification.utils.AESUtil;
import com.tiocloud.verification.utils.ImageUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Date:2020/5/19
 * author:wuyan
 */
public class BlockPuzzleDialog extends Dialog {
    private String baseImageBase64;//背景图片
    private String slideImageBase64;//滑动图片
    private String token;
    private Context mContext;
    private TextView tvDelete;
    private ImageView tvRefresh;
    private DragImageView dragView;
    private Handler handler = new Handler();
    private String key;


    public BlockPuzzleDialog(@NonNull Context context) {
        super(context, R.style.dialog);
        this.mContext = context;
        setContentView(R.layout.vercode_dialog_block_puzzle);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        WindowManager windowManager = ((Activity) mContext).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        ViewGroup.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 9 / 10;
        getWindow().setAttributes((WindowManager.LayoutParams) lp);
        setCanceledOnTouchOutside(false);//点击外部Dialog不消失
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        loadCaptcha();

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCaptcha();
            }
        });
    }

    private void initView() {
        tvDelete = findViewById(R.id.tv_delete);
        tvRefresh = findViewById(R.id.tv_refresh);
        dragView = findViewById(R.id.dragView);

        Bitmap bitmap = ImageUtil.getBitmap(getContext(), R.drawable.vercode_bg_default);
        dragView.setUp(bitmap, bitmap);
        dragView.setSBUnMove(false);
    }

    private void loadCaptcha() {
        Map<String, Object> params = new HashMap<>();
        params.put("captchaType", "blockPuzzle");
        JSONObject jsonObject = new JSONObject(params);
        String jsonObjectString = jsonObject.toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObjectString);

        RetrofitUtils.getServerApi().getAsync(body).compose(RxHelper.observableIO2Main(mContext)).subscribe(new BaseObserver<CaptchaGetIt>(mContext, true) {
            @Override
            public void onSuccess(CaptchaGetIt data) {
                baseImageBase64 = data.getOriginalImageBase64();
                slideImageBase64 = data.getJigsawImageBase64();
                token = data.getToken();
                key = data.getSecretKey();
                dragView.setUp(ImageUtil.base64ToBitmap(baseImageBase64), ImageUtil.base64ToBitmap(slideImageBase64));
                dragView.setSBUnMove(true);
                initEvent();
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dragView.setSBUnMove(false);
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkCaptcha(double sliderXMoved) {
        Point point = new Point();
        point.setY(5.0);
        point.setX(sliderXMoved);
        String pointStr = new Gson().toJson(point);
        Map<String, Object> params = new HashMap<>();
        params.put("captchaType", "blockPuzzle");
        params.put("token", token);
        params.put("pointJson", AESUtil.encode(pointStr, key));
        JSONObject jsonObject = new JSONObject(params);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        RetrofitUtils.getServerApi().checkAsync(body).compose(RxHelper.observableIO2Main(mContext)).subscribe(new BaseObserver<CaptchaCheckIt>(mContext, false) {
            @Override
            public void onSuccess(CaptchaCheckIt data) {
                dragView.ok();
                loadCaptcha();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 1500);

                if (mOnResultsListener != null) {
                    String result = token + "---" + pointStr;
                    mOnResultsListener.onResultsClick(AESUtil.encode(result, key));
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                dragView.fail();
                //刷新验证码
                loadCaptcha();
            }
        });

    }

    private void initEvent() {
        dragView.setDragListenner(new DragImageView.DragListenner() {
            @Override
            public void onDrag(double position) {
                checkCaptcha(position);
            }
        });
    }

    private OnResultsListener mOnResultsListener;

    public interface OnResultsListener {
        void onResultsClick(String result);
    }

    public void setOnResultsListener(OnResultsListener mOnResultsListener) {
        this.mOnResultsListener = mOnResultsListener;
    }
}
