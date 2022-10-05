package com.tiocloud.chat.widget.textview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ResourceUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.tiocloud.chat.R;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2020/07/14
 *     desc   :
 * </pre>
 */
public class ListUnreadTextView extends AppCompatTextView {
    public ListUnreadTextView(Context context) {
        this(context, null);
    }

    public ListUnreadTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListUnreadTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackground(ResourceUtils.getDrawable(R.drawable.bg_unread_msg));
        setGravity(Gravity.CENTER);
        setIncludeFontPadding(false);
        setPadding(SizeUtils.dp2px(2), SizeUtils.dp2px(0), SizeUtils.dp2px(2), SizeUtils.dp2px(0));
        setSingleLine(true);
        setTextColor(getResources().getColor(R.color.white));
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        setMinWidth(SizeUtils.dp2px(16));
        setMinHeight(SizeUtils.dp2px(16));
    }

    public void setUnread(int unread) {
        setUnread(unread, false, false);
    }

    public void setUnread(int unread, boolean emptyIsInvisible, boolean isDND) {
        if (unread > 0) {
            setVisibility(View.VISIBLE);
            if (unread >= 100) {
                setText("⋯");
            } else {
                setText(String.valueOf(unread));
            }
        } /*else if (unread < 0){
            setVisibility(View.VISIBLE);
            setText(" ");
        }*/else {
            setVisibility(emptyIsInvisible ? INVISIBLE : GONE);
        }
//        if (isDND) {
//            setVisibility(View.VISIBLE);
//            setBackground(null);
//            setText("");
//
//            Drawable drawable;
//            if (unread > 0) {
//                drawable = ResourceUtils.getDrawable(R.drawable.session_ic_dnd_reddots);
//            } else {
//                drawable = ResourceUtils.getDrawable(R.drawable.session_ic_dnd);
//            }
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            setCompoundDrawables(drawable, null, null, null);
//
//        } else {
//            setCompoundDrawables(null, null, null, null);
//            setBackground(ResourceUtils.getDrawable(R.drawable.bg_unread_msg));
//            if (unread > 0) {
//                setVisibility(View.VISIBLE);
//                setText(unread >= 100 ? "⋯" : String.valueOf(unread));
//            } else {
//                setVisibility(emptyIsInvisible ? INVISIBLE : GONE);
//            }
//        }
    }
}
