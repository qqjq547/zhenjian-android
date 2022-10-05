package com.watayouxiang.androidutils.widget.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ImageDecodeOptions;
import com.facebook.imagepipeline.common.ImageDecodeOptionsBuilder;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.io.File;
import java.io.IOException;

import static com.watayouxiang.httpclient.preferences.HttpCache.getResUrl;
import static com.watayouxiang.widgetlibrary.ContextUtils.getContext;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/8/7
 *     desc   : 基于 SimpleDraweeView 的图片控件
 *
 *     https://frescolib.org/docs/using-simpledraweeview.html
 * </pre>
 */
public class WtImageView extends SimpleDraweeView {

    public WtImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init(context, null);
    }

    public WtImageView(Context context) {
        super(context);
        init(context, null);
    }

    public WtImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WtImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WtImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        // 默认占位图
        setPlaceholderColorResId(R.color.black_26000000);
    }

    // ====================================================================================
    // 图片加载
    // ====================================================================================

    /**
     * 加载静态图
     * <p>
     * 如果是gif图，那么给它设置圆角就会失效。
     * 所以需要把动画变成静态图。
     */
    public void loadUrlStatic(@Nullable String url) {
        // 如果是gif图，那么设置圆角，就会失效。
        // GIF取第一帧，把动画变成静态图，再设置圆角即可。
        Uri uri = Uri.parse(StringUtils.null2Length0(getResUrl(url)));

        ImageDecodeOptions imageDecodeOptions = new ImageDecodeOptionsBuilder()
                .setForceStaticImage(true)
                .build();

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setImageDecodeOptions(imageDecodeOptions)
                .build();

        setController(Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setImageRequest(imageRequest)
                .build());



    }

    /**
     * 加载图片
     *
     * @param url           图片url
     * @param isGifAutoPlay 如果是动图，是否自动播放
     */
    public void loadUrl(@Nullable String url, final boolean isGifAutoPlay) {
        // gif图片，务必取消占位图，否则会闪烁
        getHierarchy().setPlaceholderImage(null);

        setController(Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setUri(StringUtils.null2Length0(getResUrl(url)))
                .setAutoPlayAnimations(isGifAutoPlay)
                .build());
    }

    public void loadUrl(@Nullable String url) {
        loadUrl(url, true);
    }

    /**
     * 加载图片
     *
     * @param drawableId    本地图片id
     * @param isGifAutoPlay 如果是动图，是否自动播放
     */
    public void loadDrawableId(@DrawableRes int drawableId, final boolean isGifAutoPlay) {
        // gif图片，务必取消占位图，否则会闪烁
        getHierarchy().setPlaceholderImage(null);

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(drawableId))
                .build();

        setController(Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setUri(uri)
                .setAutoPlayAnimations(isGifAutoPlay)
                .build());
    }

    public void loadDrawableId(@DrawableRes int drawableId) {
        loadDrawableId(drawableId, true);
    }

    public void loadUrlStatic_radius(@Nullable String url, float radius) {
        loadUrlStatic(url);
        setRadius(radius);
    }

    public void loadUrlStatic_asCircle_border(@Nullable String url, int color, float width) {
        loadUrlStatic(url);
        setRadiusAsCircle();
        setBorder(color, width);
    }

    // ====================================================================================
    // 设置圆角
    // ====================================================================================

    /**
     * 设置圆角
     *
     * @param radius 单位dp
     */
    public void setRadius(float radius) {
        getHierarchy().setRoundingParams(RoundingParams.fromCornersRadius(SizeUtils.dp2px(radius)));
    }

    /**
     * 设置圆形
     */
    public void setRadiusAsCircle() {
        getHierarchy().setRoundingParams(RoundingParams.asCircle());
    }

    // ====================================================================================
    // 占位图
    // ====================================================================================

    public void setPlaceholderColorResId(int colorResId) {
        getHierarchy().setPlaceholderImage(new ColorDrawable(getResources().getColor(colorResId)));
    }

    public void setPlaceholderColorId(int colorId) {
        getHierarchy().setPlaceholderImage(new ColorDrawable(colorId));
    }

    public void setPlaceholderColorString(String colorString) {
        getHierarchy().setPlaceholderImage(new ColorDrawable(Color.parseColor(colorString)));
    }

    public void setPlaceholderDrawable(@Nullable Drawable drawable) {
        getHierarchy().setPlaceholderImage(drawable);
    }

    // ====================================================================================
    // 其他
    // ====================================================================================

    /**
     * 是否正在播放动图
     *
     * @return 动图是否正在播放
     */
    public boolean isGifStart() {
        DraweeController controller = getController();
        if (controller != null) {
            Animatable animatable = controller.getAnimatable();
            if (animatable != null) {
                return animatable.isRunning();
            }
        }
        return false;
    }

    /**
     * 设置圆角边框
     *
     * @param borderColor 颜色
     * @param borderWidth 宽度 dp
     */
    public void setBorder(int borderColor, float borderWidth) {
        RoundingParams roundingParams = getHierarchy().getRoundingParams();
        if (roundingParams != null) {
            roundingParams.setBorder(borderColor, SizeUtils.dp2px(borderWidth));
            getHierarchy().setRoundingParams(roundingParams);
        }
    }

    /**
     * 设置 LayoutParams 的宽高
     *
     * @param width    宽
     * @param height   高
     * @param scaling  缩放比
     * @param maxWidth 最大宽度
     */
    public void setWidthHeight(int width, int height, @Nullable Float scaling, @Nullable Integer maxWidth) {
        // 设置缩放比例
        if (scaling != null) {
            width = (int) (width * scaling);
            height = (int) (height * scaling);
        }

        // 限制最大宽度
        if (maxWidth != null) {
            if (width > maxWidth) {
                height = (int) ((float) height / width * maxWidth);
                width = maxWidth;
            }
        }

        // 设置宽高
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        params.height = height;
        setLayoutParams(params);
    }

    /**
     * 如果需要处理url，则重新该方法
     *
     * @param url url
     * @return 新的url
     */
    protected String getResUrl(String url) {
        return url;
    }
}
