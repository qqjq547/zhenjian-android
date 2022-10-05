package com.tiocloud.chat.widget.emotion.emoji;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.tiocloud.chat.R;
import com.tiocloud.chat.widget.emotion.EmoticonSelectedListener;
import com.tiocloud.chat.widget.emotion.EmoticonViewPaperAdapter;
import com.tiocloud.chat.widget.emotion.EmotionType;

public class EmojiView {
    private final ViewPager emotionPager;
    private final LinearLayout pageNumberLayout;
    private final EmoticonSelectedListener listener;

    private EmoticonViewPaperAdapter viewPaperAdapter;

    public EmojiView(View rootView, EmoticonSelectedListener listener) {
        emotionPager = rootView.findViewById(R.id.scrPlugin);
        pageNumberLayout = rootView.findViewById(R.id.layout_scr_bottom);
        this.listener = listener;

        initEvents();
    }

    private void initEvents() {
        viewPaperAdapter = new EmoticonViewPaperAdapter(listener, emotionPager);
        emotionPager.setOffscreenPageLimit(1);
        emotionPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setIndicator(position, viewPaperAdapter.getCount());
            }
        });
        emotionPager.setAdapter(viewPaperAdapter);
    }

    private void setIndicator(int position, int pageCount) {
        int hasCount = pageNumberLayout.getChildCount();

        if (pageCount > hasCount) {// 不够
            for (int i = 0; i < pageCount - hasCount; i++) {
                ImageView img = new ImageView(pageNumberLayout.getContext());
                img.setBackgroundResource(R.drawable.view_pager_indicator_selector);
                pageNumberLayout.addView(img);
            }
        } else if (pageCount < hasCount) {// 太多
            pageNumberLayout.removeViews(pageCount, hasCount - pageCount);
        }

        for (int i = 0; i < pageCount; i++) {
            ImageView imgCur = (ImageView) pageNumberLayout.getChildAt(i);
            imgCur.setSelected(i == position);
        }
    }

    // ======================================================================================
    // public
    // ======================================================================================

    public void show() {
        if (viewPaperAdapter.setEmotionType(EmotionType.EMOJI)) {
            setIndicator(0, viewPaperAdapter.getCount());
        }
    }
}
