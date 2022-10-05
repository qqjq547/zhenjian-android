package com.tiocloud.chat.baseNewVersion.utils2;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.drawee.view.SimpleDraweeView;

public class HelperGlide {

    public static void loadHead(Context c, Object url, ImageView imageView) {
        Glide.with(c)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }
    public static void loadHeadSim(Context c, Object url, SimpleDraweeView imageView) {
        Glide.with(c)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);
    }
    public static void load(Context c, Object url, ImageView imageView) {
        Glide.with(c)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new FilletTransformation(50)))
                .into(imageView);
    }

    public static void loadRound(Context c, Object url, ImageView imageView, int r) {
        Glide.with(c)
                .load(url)
//                .placeholder(R.mipmap.image_error)
                .apply(RequestOptions.bitmapTransform(new FilletTransformation(r)))
                .into(imageView);
    }
    public static void loadNoErrorRound(Context c, Object url, ImageView imageView, int r) {
        Glide.with(c)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new FilletTransformation(r)))
                .into(imageView);
    }
    public static void loadNoErrorImage(Context c, Object url, ImageView imageView) {
        Glide.with(c)
                .load(url)
                .into(imageView);
    }
    public static void loadImg(Context c, Object url, ImageView imageView) {
        Glide.with(c)
                .load(url)
//                .placeholder(R.mipmap.image_error)
                .into(imageView);
    }


}
