package com.tiocloud.chat.feature.forbidden;

import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.Utils;
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
 *     time   : 2021/01/05
 *     desc   : 左侧 列表类型的 Popup
 * </pre>
 */
public class LeftListPopup {

    private final int mPopWidth = QMUIDisplayHelper.dp2px(Utils.getApp(), 110);
    private final QMUIPopup mLeftListPopup;
    private OnPopupListener onPopupListener;

    /**
     * @param forbidden              被禁言了吗
     * @param groupMgr               群管理员吗
     * @param removeMemberPermission 有移出群聊权限吗
     */
    public LeftListPopup(Context context, @Nullable Boolean forbidden, @Nullable Boolean groupMgr, boolean removeMemberPermission) {
        HashMap<Integer, ItemEnum> posMap = new HashMap<>();
        List<String> data = new ArrayList<>();
        // 艾特
        data.add("@TA");
        posMap.put(data.size() - 1, ItemEnum.Ait);
        // 禁言
        if (forbidden != null) {
            data.add(forbidden ? "取消禁言" : "禁言");
            posMap.put(data.size() - 1, ItemEnum.Silent);
        }
        // 群管理员
        if (groupMgr != null) {
            data.add(groupMgr ? "取消管理员" : "设为管理员");
            posMap.put(data.size() - 1, ItemEnum.GroupMgr);
        }
        if (removeMemberPermission) {
            data.add("撤回全部消息");
            posMap.put(data.size() - 1, ItemEnum.RemoveMember_Chehui);
        }
        // 有移出群聊权限吗
        if (removeMemberPermission) {
            data.add("移出群聊");
            posMap.put(data.size() - 1, ItemEnum.RemoveMember);
        }

        mLeftListPopup = QMUIPopups.listPopup(context,
                mPopWidth,
                QMUIDisplayHelper.dp2px(context, 240),
                new ArrayAdapter<>(context, R.layout.tio_simple_list_item, data),
                (adapterView, view, position, id) -> {
                    ItemEnum itemEnum = posMap.get(position);
                    if (itemEnum == null) return;
                    switch (itemEnum) {
                        case Ait:
                            if (onPopupListener != null)
                                onPopupListener.onClickAitItem(LeftListPopup.this);
                            break;
                        case Silent:
                            if (forbidden == null) return;
                            if (onPopupListener != null)
                                onPopupListener.onClickSilentItem(LeftListPopup.this, forbidden);
                            break;
                        case GroupMgr:
                            if (groupMgr == null) return;
                            if (onPopupListener != null)
                                onPopupListener.onClickGroupMgrItem(LeftListPopup.this, groupMgr);
                            break;
                        case RemoveMember_Chehui:
                            if (onPopupListener != null)
                                onPopupListener.onClickRemoveMemberItem_CheHui(LeftListPopup.this);
                            break;
                        case RemoveMember:
                            if (onPopupListener != null)
                                onPopupListener.onClickRemoveMemberItem(LeftListPopup.this);
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
                });
    }

    public LeftListPopup setOnPopupListener(OnPopupListener onPopupListener) {
        this.onPopupListener = onPopupListener;
        return this;
    }

    public void show(View v) {
        mLeftListPopup
                .offsetX(mPopWidth / 2+50)
                .offsetYIfBottom(-v.getHeight() / 2)
                .offsetYIfTop(-v.getHeight() / 2)
                .show(v);
    }

    public void dismiss() {
        if (mLeftListPopup != null) {
            mLeftListPopup.dismiss();
        }
    }

    // ====================================================================================
    // inner class
    // ====================================================================================

    public interface OnPopupListener {
        void onClickAitItem(LeftListPopup popup);

        void onClickRemoveMemberItem(LeftListPopup popup);
        void onClickRemoveMemberItem_CheHui(LeftListPopup popup);

        void onClickSilentItem(LeftListPopup popup, boolean forbidden);

        void onClickGroupMgrItem(LeftListPopup popup, boolean groupMgr);

        void onDismiss();
    }

    public abstract static class OnSimplePopupListener implements OnPopupListener {
        @Override
        public void onDismiss() {

        }
    }

    public enum ItemEnum {
        Ait, Silent, GroupMgr, RemoveMember,RemoveMember_Chehui
    }
}
