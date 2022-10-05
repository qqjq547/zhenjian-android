package com.watayouxiang.androidutils.widget.listcell;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.StringUtils;
import com.watayouxiang.androidutils.R;
import com.watayouxiang.androidutils.databinding.AndroidutilsListCellBinding;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/03/05
 *     desc   : 列表单元
 * </pre>
 */
public class ListCellView extends RelativeLayout {

    private final AndroidutilsListCellBinding binding;
    // 标题
    private final String tvTitle_text;
    private final Drawable tvTitle_drawableLeft;
    private final int tvTitle_drawablePadding = SizeUtils.dp2px(4);
    // 副标题
    private final String tvSubTitle_text;
    // 分割线
    private final boolean vLine_hide;
    private float vLine_marginLeft = SizeUtils.dp2px(15);
    // 右侧箭头
    private final Drawable ivArrowRight_drawable = ResourceUtils.getDrawable(R.drawable.androidutils_ic_forward);

    public ListCellView(Context context) {
        this(context, null);
    }

    public ListCellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListCellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TypedArray
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListCellView);

        // 标题
        tvTitle_text = a.getString(R.styleable.ListCellView_lcv_tvTitle_text);
        tvTitle_drawableLeft = a.getDrawable(R.styleable.ListCellView_lcv_tvTitle_drawableLeft);
        // 副标题
        tvSubTitle_text = a.getString(R.styleable.ListCellView_lcv_tvSubTitle_text);
        // 分割线
        vLine_hide = a.getBoolean(R.styleable.ListCellView_lcv_vLine_hide, false);
        vLine_marginLeft = a.getDimension(R.styleable.ListCellView_lcv_vLine_marginLeft, vLine_marginLeft);

        a.recycle();

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.androidutils_list_cell, this, true);
        initUI();
    }

    private void initUI() {
        // 标题
        binding.tvTitle.setText(StringUtils.null2Length0(tvTitle_text));
        if (tvTitle_drawableLeft != null) {
            tvTitle_drawableLeft.setBounds(0, 0, tvTitle_drawableLeft.getMinimumWidth(), tvTitle_drawableLeft.getMinimumHeight());
            binding.tvTitle.setCompoundDrawables(tvTitle_drawableLeft, null, null, null);
            binding.tvTitle.setCompoundDrawablePadding(tvTitle_drawablePadding);
        } else {
            binding.tvTitle.setCompoundDrawables(null, null, null, null);
            binding.tvTitle.setCompoundDrawablePadding(0);
        }
        // 副标题
        binding.tvSubTitle.setText(StringUtils.null2Length0(tvSubTitle_text));
        // 分割线
        if (vLine_hide) {
            binding.vLineBottom.setVisibility(GONE);
        } else {
            binding.vLineBottom.setVisibility(VISIBLE);
            MarginLayoutParams params = (MarginLayoutParams) binding.vLineBottom.getLayoutParams();
            params.setMargins((int) vLine_marginLeft, 0, 0, 0);
        }
        // 右侧箭头
        binding.ivArrowRight.setImageDrawable(ivArrowRight_drawable);
    }

}
