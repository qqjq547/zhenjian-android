package com.watayouxiang.androidutils.widget.imageview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.watayouxiang.androidutils.utils.FileUtils;
import com.watayouxiang.httpclient.preferences.HttpCache;

import java.io.File;
import java.io.IOException;

/**
 * author : TaoWang
 * date : 2020-01-22
 * desc :
 * https://frescolib.org/docs/using-simpledraweeview.html
 */
public class TioImageView extends WtImageView {

    public TioImageView(Context context) {
        super(context);
    }

    public TioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected String getResUrl(String url) {
        return HttpCache.getResUrl(url);
    }

    /**
     * tio - 消息内页名片
     */
    public void load_tioMsgCard(@Nullable String url) {
        // 加载静态图片
        loadUrlStatic(url);
        // 设置圆角
        setRadius(4);
        // 设置边框
        setBorder(Color.WHITE, 1);
    }
    public void showGifPic(String url, final boolean isGifAutoPlay){
        loadUrl(url,isGifAutoPlay);

    }
    /**
     * tio - 消息内页图片
     */
    public void load_tioMsgPic(@Nullable String url, int width, int height) {
        // 加载图片
        loadUrlStatic(url);
        // 设置圆角
        setRadius(14);
        // 设置宽高
        setWidthHeight(width, height, 1.3f, SizeUtils.dp2px(160));
        // 设置边框
        setBorder(Color.WHITE, 1);
    }

    /**
     * tio - 头像
     */
    public void load_tioAvatar(@Nullable String url) {
        // 加载静态图片
        loadUrlStatic(url);

        // 圆角
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params.width != params.height) {
            return;
        }
//        int dpWH = SizeUtils.px2dp(params.width);
//        switch (dpWH) {
//            case 38:
//            case 42:
//            case 44:
//            case 48:
//            case 58:
//                setRadius(4);
//                break;
//            case 67:
//                setRadius(8);
//                break;
//            case 77:
//                setRadius(8);
//                setBorder(Color.WHITE, 4);
//                break;
//        }
    }

    /**
     * tio - 发现内logo
     * @param url
     */
    public void load_tioFoundPic(@Nullable String url){
        // 加载静态图片
        loadUrlStatic(url);
    }
}
