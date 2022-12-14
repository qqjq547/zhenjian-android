/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tiocloud.chat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.main.adapter.MainTabPagerAdapter;
import com.tiocloud.chat.widget.textview.TabUnreadTextView;
import com.watayouxiang.httpclient.preferences.HttpCache;

import org.jetbrains.annotations.NotNull;

// ????????????
public class PagerSlidingTabStrip extends HorizontalScrollView implements OnPageChangeListener {

    // attrs
    private int lastScrollX = 0;// ?????????????????? X
    private int scrollOffset = 52;// ???????????????(dp)

    private int tabPadding = 0;// tab???padding(dp)

    private int underlineColor = 0xFFD9D9D9;// ???????????????
    private int underlineHeight = 1;// ???????????????(px)

    private int indicatorColor = 0xFF4C94E8;// ????????????
    private int indicatorHeight = 3;// ????????????(dp)

    // paint
    private Paint rectPaint;

    // container
    private LinearLayout tabsContainer;
    private LinearLayout.LayoutParams tabViewLayoutParams;

    // viePager
    private ViewPager pager;
    private int tabCount;// vp???????????????
    private int currentPosition = 0;// ??????vp??????
    private float currentPositionOffset = 0f;// vp??????????????????

    private OnTabClickListener onTabClickListener = null;// ????????????
    private OnTabDoubleTapListener onTabDoubleTapListener = null;// ????????????

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        tabViewLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        // dimension
        DisplayMetrics dm = getResources().getDisplayMetrics();
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, underlineHeight, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);

        int[] ATTRS = new int[]{android.R.attr.textSize, android.R.attr.textColor};
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        a.recycle();

        // attrs
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_psts_indicatorColor, indicatorColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_psts_indicatorHeight, indicatorHeight);

        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_psts_underlineColor, underlineColor);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_psts_underlineHeight, underlineHeight);

        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_psts_tabPaddingLeftRight, tabPadding);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_psts_scrollOffset, scrollOffset);

        a.recycle();

        // paint
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);
    }

    // ========================================================================================
    // ?????????
    // ========================================================================================

    /**
     * ?????? ViewPager
     *
     * @param pager ViewPager
     */
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        this.pager.addOnPageChangeListener(this);

        notifyDataSetChanged();
    }

    /**
     * ????????????
     *
     * @param onTabClickListener ??????????????????
     */
    public void setOnTabClickListener(OnTabClickListener onTabClickListener) {
        this.onTabClickListener = onTabClickListener;
    }

    /**
     * ????????????
     *
     * @param onTabDoubleTapListener ??????????????????
     */
    public void setOnTabDoubleTapListener(OnTabDoubleTapListener onTabDoubleTapListener) {
        this.onTabDoubleTapListener = onTabDoubleTapListener;
    }

    /**
     * ??????tab
     *
     * @param index  position
     * @param unread ???????????????
     */
    public void updateTab(int index, int unread) {
        View child = tabsContainer.getChildAt(index);
        TabUnreadTextView tvUnread = child.findViewById(R.id.tv_unread);
        tvUnread.setUnread(unread);
    }

    // ====================================================================================
    // tab
    // ====================================================================================

    private PagerAdapter getPagerAdapter() {
        PagerAdapter adapter = pager.getAdapter();
        if (adapter == null)
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        return adapter;
    }

    private void notifyDataSetChanged() {
        tabsContainer.removeAllViews();

        tabCount = getPagerAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            addTab(i, createTabView(i));
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }

                currentPosition = pager.getCurrentItem();
                chooseTab(currentPosition);
                scrollToChild(currentPosition, 0);
            }
        });
    }

    // tab ??????
    private void chooseTab(int position) {
        int childCount = tabsContainer.getChildCount();
        View tabView;
        for (int i = 0; i < childCount; ++i) {
            tabView = tabsContainer.getChildAt(i);
            tabView.setSelected(i == position);
        }
    }

    // ?????? HorizontalScrollView
    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;

        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }

        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    private View createTabView(int position) {
        // ????????????
        PagerAdapter adapter = getPagerAdapter();
        String title = adapter.getPageTitle(position).toString();
        int iconId = 0;
        String[] selectorUrl = null;
        if (adapter instanceof MainTabPagerAdapter) {
            iconId = ((MainTabPagerAdapter) adapter).getImageResource(position);
            selectorUrl=((MainTabPagerAdapter) adapter).getImageUrls(position);
        }

        // ????????????
        View tabView = LayoutInflater.from(getContext()).inflate(R.layout.psts_tab_layout_main, null);
        // ??????
        TextView titleTV = tabView.findViewById(R.id.tab_title);
        titleTV.setText(title);
        // icon
        ImageView iconIV = tabView.findViewById(R.id.tab_icon);
        if (selectorUrl!=null){
            iconIV.setVisibility(VISIBLE);
            addSeletorFromNet(selectorUrl[0],selectorUrl[1],iconIV);
            Log.d("hjq","selectorUrl="+ GsonUtils.toJson(HttpCache.getResUrl(selectorUrl[0])));
        }else{
            if (iconId != 0) {
                iconIV.setVisibility(VISIBLE);
                iconIV.setImageResource(iconId);
            } else {
                iconIV.setVisibility(GONE);
            }
        }
        // ???????????????
        final TextView tvUnread = tabView.findViewById(R.id.tv_unread);
        tvUnread.setVisibility(View.GONE);

        return tabView;
    }

    private void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == position && onTabClickListener != null) {
                    onTabClickListener.onCurrentTabClicked(position);
                } else {
                    pager.setCurrentItem(position, false);
                }
            }
        });
        addTabDoubleTapListener(position, tab);
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, tabViewLayoutParams);
    }

    // ????????????
    private void addTabDoubleTapListener(final int position, View tab) {
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (onTabDoubleTapListener != null)
                    onTabDoubleTapListener.onCurrentTabDoubleTap(position);
                return true;
            }
        });

        tab.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    // ========================================================================================
    // Override
    // ========================================================================================

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        drawUnderline(canvas);
        drawIndicatorLine(canvas);
    }

    private void drawIndicatorLine(Canvas canvas) {
        if (indicatorHeight <= 0) return;

        rectPaint.setColor(indicatorColor);

        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        // ????????????vp??????????????????
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        int height = getHeight();
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);
    }

    private void drawUnderline(Canvas canvas) {
        if (underlineHeight <= 0) return;

        rectPaint.setColor(underlineColor);
        int height = getHeight();
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        currentPosition = position;
        currentPositionOffset = positionOffset;
        scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            scrollToChild(pager.getCurrentItem(), 0);
        }
    }

    @Override
    public void onPageSelected(int position) {
        chooseTab(position);
    }

    // ========================================================================================
    // ????????????
    // ========================================================================================

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // currentPosition = savedState.currentPosition;
        currentPosition = 0;
        requestLayout();
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    // ========================================================================================
    // class
    // ========================================================================================

    public interface OnTabClickListener {
        void onCurrentTabClicked(int position);
    }

    public interface OnTabDoubleTapListener {
        void onCurrentTabDoubleTap(int position);
    }
    private void addSeletorFromNet(final String normalPic, final String selectedPic, final ImageView imageView) {

        if(imageView == null || TextUtils.isEmpty(normalPic))
            return;
        final StateListDrawable drawable = new StateListDrawable();
        Glide.with(getContext())
                .asBitmap()
                .load(StringUtils.null2Length0(HttpCache.getResUrl(normalPic)))
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {

                        Drawable newDraw = new BitmapDrawable(bitmap);
                        drawable.addState(new int[]{
                                -android.R.attr.state_selected}, newDraw);

                        Glide.with(getContext())
                                .asBitmap()
                                .load(StringUtils.null2Length0(HttpCache.getResUrl(selectedPic)))
                                .into(new SimpleTarget<Bitmap>() {

                                    @Override
                                    public void onResourceReady(@NotNull Bitmap bitmap, Transition<? super Bitmap> transition) {

                                        Drawable newDraw = new BitmapDrawable(bitmap);
                                        drawable.addState(new int[]{
                                                android.R.attr.state_selected}, newDraw);
                                        imageView.setImageDrawable(drawable);
                                    }
                                });

                    }
                });
    }
}
