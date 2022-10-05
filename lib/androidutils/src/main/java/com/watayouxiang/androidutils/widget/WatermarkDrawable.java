package com.watayouxiang.androidutils.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2/24/21
 *     desc   : 水印图
 * </pre>
 */
public class WatermarkDrawable extends Drawable {

    private final Paint paint = new Paint();
    private final float blockWidth;
    private final int blockHeight;

    // label
    private final List<String> labels;
    private final int labelSpacing;
    // canvas
    private final int canvasColor;
    private final int canvasAngle;
    // label block
    private final float blockHSpacing;
    private final int blockVSpacing;
    // rate
    private final float startYRate;
    private final float endYRate;
    private final float startXRate;
    private final float endXRate;

    private WatermarkDrawable(Builder builder) {
        // label
        this.labels = builder.labels;
        int labelSize = builder.labelSize;
        int labelColor = builder.labelColor;
        this.labelSpacing = builder.labelSpacing;
        Paint.Align labelAlign = builder.labelAlign;
        // canvas
        this.canvasColor = builder.canvasColor;
        this.canvasAngle = builder.canvasAngle;
        // label block
        this.blockHSpacing = builder.blockHSpacing;
        this.blockVSpacing = builder.blockVSpacing;
        // rate
        this.startYRate = builder.startYRate;
        this.endYRate = builder.endYRate;
        this.startXRate = builder.startXRate;
        this.endXRate = builder.endXRate;

        // 字体
        paint.setTypeface(Typeface.DEFAULT);
        // 对其方式
        paint.setTextAlign(labelAlign);
        // 文字颜色
        paint.setColor(labelColor);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 文字大小
        paint.setTextSize(labelSize);

        // 文字块宽度
        blockWidth = getBlockWidth(paint);
        // 文字块高度
        blockHeight = getBlockHeight(paint);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        int width = getBounds().right;
        int height = getBounds().bottom;

        canvas.drawColor(canvasColor);
        canvas.save();
        canvas.rotate(canvasAngle);

        // 计算水印块 y 坐标
        float startY = height * startYRate;
        float endY = height * endYRate;
        for (float positionY = startY, loopCount = 0; positionY <= endY; positionY += blockHeight + blockVSpacing, loopCount++) {

            // 计算水印块 x 坐标
            float startX = (width * startXRate) + ((loopCount % 2) * blockWidth);
            float endX = width * endXRate;
            for (float positionX = startX; positionX < endX; positionX += blockWidth + blockHSpacing) {

                // 写水印
                for (int i = 0, labelTotalHeight = 0; i < labels.size(); i++) {
                    String label = labels.get(i);
                    int labelHeight = getLabelHeight(paint, label);
                    if (i != 0) {
                        labelTotalHeight += labelHeight + labelSpacing;
                    }
                    canvas.drawText(label, positionX, positionY + labelTotalHeight, paint);
                }
            }
        }

        canvas.restore();
    }

    private float getBlockWidth(Paint paint) {
        String maxLengthLabel = "";
        for (String label : labels) {
            if (label.length() > maxLengthLabel.length()) {
                maxLengthLabel = label;
            }
        }
        return paint.measureText(maxLengthLabel);
    }

    private int getBlockHeight(Paint paint) {
        int labelHeight = 0;
        for (String label : labels) {
            labelHeight += getLabelHeight(paint, label);
        }
        return labelHeight;
    }

    private int getLabelHeight(Paint paint, String label) {
        Rect rect = new Rect();
        paint.getTextBounds(label, 0, label.length(), rect);
        // 文字的宽度
        int width = rect.width();
        // 文字的高度
        return rect.height();
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    public static int sp2px(final float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    // ====================================================================================
    // Builder
    // ====================================================================================

    public static class Builder {
        // label
        private final List<String> labels;// 水印列表，多行显示支持
        private int labelSize = sp2px(18);// 水印大小，单位sp
        private int labelColor = Color.parseColor("#E5F0FF");// 水印颜色
        private int labelSpacing = dp2px(15);// 水印间距
        private Paint.Align labelAlign = Paint.Align.LEFT;// 水印对齐方式
        // canvas
        private int canvasColor = Color.parseColor("#F8F8F8");// 画布颜色
        private int canvasAngle = -45;// 画布角度
        // label block
        private float blockHSpacing = dp2px(80);// 块水平间距
        private int blockVSpacing = dp2px(80);// 块垂直间距
        // rate
        private float startYRate = 0f;
        private float endYRate = 1.5f;
        private float startXRate = -1.5f;
        private float endXRate = 1;

        public Builder(List<String> labels) {
            this.labels = labels;
        }

        public Builder setCanvasColor(int canvasColor) {
            this.canvasColor = canvasColor;
            return this;
        }

        public Builder setCanvasAngle(int canvasAngle) {
            this.canvasAngle = canvasAngle;
            return this;
        }

        public Builder setLabelSize(int labelSize) {
            this.labelSize = labelSize;
            return this;
        }

        public Builder setLabelColor(int labelColor) {
            this.labelColor = labelColor;
            return this;
        }

        public Builder setLabelSpacing(int labelSpacing) {
            this.labelSpacing = labelSpacing;
            return this;
        }

        public Builder setBlockHSpacing(float blockHSpacing) {
            this.blockHSpacing = blockHSpacing;
            return this;
        }

        public Builder setBlockVSpacing(int blockVSpacing) {
            this.blockVSpacing = blockVSpacing;
            return this;
        }

        public Builder setStartYRate(float startYRate) {
            this.startYRate = startYRate;
            return this;
        }

        public Builder setEndYRate(float endYRate) {
            this.endYRate = endYRate;
            return this;
        }

        public Builder setStartXRate(float startXRate) {
            this.startXRate = startXRate;
            return this;
        }

        public Builder setEndXRate(float endXRate) {
            this.endXRate = endXRate;
            return this;
        }

        public Builder setLabelAlign(Paint.Align labelAlign) {
            this.labelAlign = labelAlign;
            return this;
        }

        public WatermarkDrawable build() {
            return new WatermarkDrawable(this);
        }
    }
}
