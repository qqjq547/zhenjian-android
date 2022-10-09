package com.tiocloud.chat.widget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.SpanUtils;
import com.tiocloud.chat.R;
import com.tiocloud.chat.databinding.TioHomeTitleBarBinding;
import com.tiocloud.chat.feature.search.curr.SearchActivity;
import com.tiocloud.chat.widget.popupwindow.HomePopupWindow;

/**
 * author : TaoWang
 * date : 2020-02-19
 * desc :
 */
public class HomeTitleBar extends RelativeLayout implements View.OnClickListener {

    private final TioHomeTitleBarBinding binding;
    private String title;

    public HomeTitleBar(Context context) {
        this(context, null);
    }

    public HomeTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HomeTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.tio_home_title_bar, this, true);
        binding.ivSearch.setOnClickListener(this);
        binding.ivMore.setOnClickListener(this);
        binding.IVWebhoutui.setOnClickListener(this);
        binding.IVSHUAXIN.setOnClickListener(this);
        binding.ivClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.ivMore) {
            if (rightClickListener == null) {
                showHomePopupWindow(v);
            } else {
                rightClickListener.onClick(v);
            }

        } else if (v == binding.ivSearch) {
            if (leftClickListener==null){
                SearchActivity.start(v.getContext());
            }else {
                leftClickListener.onClick(v);
            }
        }else if (v == binding.IVWebhoutui) {
            htClickListener.onClick(v);

        }else if (v == binding.IVSHUAXIN) {
            scClickListener.onClick(v);

        }else if (v == binding.ivClose) {
     closeClickListener.onClick(v);
        }

    }

    // ====================================================================================
    // window
    // ====================================================================================

    private HomePopupWindow homePopupWindow;

    private void showHomePopupWindow(View v) {
        if (homePopupWindow == null) {
            homePopupWindow = new HomePopupWindow(v);
        }
        homePopupWindow.show();
    }
    public void isShowWebBt(boolean isshow){
        if (isshow){
            binding.llWeb.setVisibility(VISIBLE);
            binding.ivMore.setVisibility(GONE);
        }else {
            binding.llWeb.setVisibility(GONE);
            binding.ivMore.setVisibility(VISIBLE);

        }
    }
    // ====================================================================================
    // public
    // ====================================================================================

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(@NonNull String title) {
        if (binding == null) return;

        this.title = title;
        SpanUtils.with(binding.tvTitle)
                .append(title).setFontSize((int) getResources().getDimension(R.dimen.sp_24))
                .create();
    }
    public void setWebTitle(@NonNull String title) {
        if (binding == null) return;
        this.title = title;
        binding.tvWebTitle.setText(title);
    }
    public View getWebTitleView() {
        return   binding.tvWebTitle;
    }
    public View getFriendBtnView() {
        return   binding.ivFriend;
    }
    public void setTitleCenter(@NonNull String title){
        if (binding == null) return;

        this.title = title;
        SpanUtils.with(binding.tvTitleCenter)
                .append(title).setFontSize((int) getResources().getDimension(R.dimen.sp_24))
                .create();
        binding.tvTitleCenter.setVisibility(VISIBLE);

    }

    public void setTitle(int titleId) {
        this.setTitle(getResources().getString(titleId));
    }

    /**
     * 追加标题信息
     *
     * @param append 追加信息，为空则复原标题
     */
    public void setAppendTitle(@Nullable String append) {
        if (binding == null) return;
        if (title == null) return;
        if (append == null){
            binding.tvSubtitle.setVisibility(GONE);
        }else {
            binding.tvSubtitle.setVisibility(VISIBLE);
            binding.tvSubtitle.setText(String.format("(%s)", append));
        }
//        if (append == null) {
//            SpanUtils.with(binding.tvTitle)
//                    .append(title).setFontSize((int) getResources().getDimension(R.dimen.sp_24))
//                    .create();
//        } else {
//            SpanUtils.with(binding.tvTitle)
//                    .append(title).setFontSize((int) getResources().getDimension(R.dimen.sp_24))
//                    .append("(").setFontSize((int) getResources().getDimension(R.dimen.sp_15)).setVerticalAlign(SpanUtils.ALIGN_CENTER)
//                    .append(append).setFontSize((int) getResources().getDimension(R.dimen.sp_17)).setVerticalAlign(SpanUtils.ALIGN_CENTER)
//                    .append(")").setFontSize((int) getResources().getDimension(R.dimen.sp_15)).setVerticalAlign(SpanUtils.ALIGN_CENTER)
//                    .create();
//        }
    }

    private OnClickListener leftClickListener = null;

    /**
     * 设置左侧图标的点击事件
     *
     * @param resId
     * @param clicListener
     */
    public void setLeftClicListener(@DrawableRes int resId, OnClickListener clicListener) {
        if (resId == 0) {
            binding.ivSearch.setVisibility(INVISIBLE);
        } else {
            binding.ivSearch.setVisibility(VISIBLE);
            binding.ivSearch.setImageResource(resId);
        }
        leftClickListener = clicListener;
    }

    private OnClickListener rightClickListener = null;

    /**
     * 设置右侧图标的点击事件
     *
     * @param resId
     * @param clicListener
     */
    public void setRightClicListener(@DrawableRes int resId, OnClickListener clicListener) {
        if (resId == 0) {
            binding.ivMore.setVisibility(INVISIBLE);
        } else {
            binding.ivMore.setVisibility(VISIBLE);
            binding.ivMore.setImageResource(resId);
        }
        rightClickListener = clicListener;
    }
    private OnClickListener scClickListener = null;

    public void setSXClicListener( OnClickListener clicListener) {

        scClickListener = clicListener;
    }
    private OnClickListener htClickListener = null;

    public void setHTClicListener( OnClickListener clicListener) {

        htClickListener = clicListener;
    }
    private OnClickListener closeClickListener = null;

    public void setCloselicListener( OnClickListener clicListener) {

        closeClickListener = clicListener;
    }

}
