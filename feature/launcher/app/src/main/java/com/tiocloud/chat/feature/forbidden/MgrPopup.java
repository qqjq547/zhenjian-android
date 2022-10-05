package com.tiocloud.chat.feature.forbidden;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.tiocloud.chat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
 *     author : TaoWang
 *     e-mail : watayouxiang@qq.com
 *     time   : 2021/01/07
 *     desc   : 管理 Popup
 * </pre>
 */
public class MgrPopup {

    private final QMUIPopup mLeftListPopup;
    private OnPopupListener onPopupListener;
    private View mAnchor;

    /**
     * @param forbidden 被禁言了吗
     * @param groupMgr  群管理员吗
     */
    public MgrPopup(Context context, @Nullable Boolean forbidden, @Nullable Boolean groupMgr) {
        HashMap<Integer, ItemEnum> posMap = new HashMap<>();
        List<String> data = new ArrayList<>();
        if (forbidden != null) {
            data.add(forbidden ? "取消禁言" : "禁言");
            posMap.put(data.size() - 1, ItemEnum.Silent);
        }
        if (groupMgr != null) {
            data.add(groupMgr ? "取消管理员" : "设为管理员");
            posMap.put(data.size() - 1, ItemEnum.GroupMgr);
        }

        mLeftListPopup = QMUIPopups.listPopup(context,
                QMUIDisplayHelper.dp2px(context, 110),
                QMUIDisplayHelper.dp2px(context, 200),
                new ArrayAdapter<>(context, R.layout.tio_simple_list_item, data),
                (adapterView, view, position, id) -> {
                    ItemEnum itemEnum = posMap.get(position);
                    if (itemEnum == null) return;
                    switch (itemEnum) {
                        case Silent:
                            if (forbidden == null) return;
                            if (onPopupListener != null)
                                onPopupListener.onClickSilentItem(MgrPopup.this, forbidden);
                            break;
                        case GroupMgr:
                            if (groupMgr == null) return;
                            if (onPopupListener != null)
                                onPopupListener.onClickGroupMgrItem(MgrPopup.this, groupMgr);
                            break;
                    }
                })
                // 配置
                .radius(QMUIDisplayHelper.dp2px(context, 4))
                .animStyle(QMUIPopup.ANIM_GROW_FROM_CENTER)
                .preferredDirection(QMUIPopup.DIRECTION_BOTTOM)
                .shadow(true)
                .arrow(false)
                .skinManager(QMUISkinManager.defaultInstance(context))
                .onDismiss(() -> {
                    if (onPopupListener != null) {
                        onPopupListener.onDismiss();
                    }
                    if (mAnchor != null) cancelAnchorBg(mAnchor);
                });
    }

    public MgrPopup setOnPopupListener(OnPopupListener onPopupListener) {
        this.onPopupListener = onPopupListener;
        return this;
    }

    public void show(View v) {
        mAnchor = v;
        mLeftListPopup
                .offsetYIfBottom(-v.getHeight() / 2)
                .offsetYIfTop(-v.getHeight() / 2)
                .show(v);
        if (mAnchor != null) setAnchorBg(mAnchor);
    }

    public void dismiss() {
        if (mLeftListPopup != null) {
            mLeftListPopup.dismiss();
        }
    }

    // ====================================================================================
    // 背景
    // ====================================================================================

    private Drawable originalBackground;

    private void setAnchorBg(View view) {
        originalBackground = view.getBackground();
        view.setBackground(new ColorDrawable(view.getResources().getColor(R.color.gray_e6e6e6)));
    }

    private void cancelAnchorBg(View view) {
        view.setBackground(originalBackground);
    }

    // ====================================================================================
    // inner class
    // ====================================================================================

    public interface OnPopupListener {
        void onClickSilentItem(MgrPopup popup, boolean forbidden);

        void onClickGroupMgrItem(MgrPopup popup, boolean groupMgr);

        void onDismiss();
    }

    public abstract static class OnSimplePopupListener implements OnPopupListener {
        @Override
        public void onDismiss() {

        }
    }

    public enum ItemEnum {
        Silent, GroupMgr
    }
}
