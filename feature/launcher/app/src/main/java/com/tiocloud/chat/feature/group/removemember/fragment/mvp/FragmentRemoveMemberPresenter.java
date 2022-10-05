package com.tiocloud.chat.feature.group.removemember.fragment.mvp;

import android.view.View;
import android.widget.TextView;

import com.tiocloud.chat.feature.group.removemember.fragment.adapter.RemoveMemberAdapter;
import com.watayouxiang.androidutils.widget.dialog.oper.EasyOperDialog;
import com.watayouxiang.db.prefernces.TioDBPreferences;
import com.watayouxiang.httpclient.model.response.GroupUserListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.TioToast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class FragmentRemoveMemberPresenter extends FragmentRemoveMemberContract.Presenter {

    private int mPageNumber;
    private String mSearchKey;

    public FragmentRemoveMemberPresenter(FragmentRemoveMemberContract.View view) {
        super(new FragmentRemoveMemberModel(), view);
    }

    @Override
    public void init() {
        getView().initRecyclerView();
        load(1, null);
        initMenuBtn();
    }

    // ====================================================================================
    // 列表数据
    // ====================================================================================

    private void load(int pageNumber, String searchKey) {
        final RemoveMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;

        mPageNumber = pageNumber;
        mSearchKey = searchKey;

        // 获取群成员列表
        getModel().getGroupUserList(String.valueOf(pageNumber), getView().getGroupId(), searchKey,
                new BaseModel.DataProxy<GroupUserListResp>() {
                    @Override
                    public void onSuccess(GroupUserListResp groupUserList) {
                        // 移除本人
                        String uid = String.valueOf(TioDBPreferences.getCurrUid());
                        Iterator<GroupUserListResp.GroupMember> it = groupUserList.list.iterator();
                        while (it.hasNext()) {
                            if (String.valueOf(it.next().uid).equals(uid)) {
                                it.remove();
                                break;
                            }
                        }
                        if (groupUserList.firstPage) {
                            adapter.setNewData(groupUserList.list);
                        } else {
                            adapter.addData(groupUserList.list);
                        }
                        if (groupUserList.lastPage) {
                            adapter.loadMoreEnd();
                        } else {
                            adapter.loadMoreComplete();
                        }
                    }
                });
    }

    @Override
    public void loadMore() {
        load(++mPageNumber, mSearchKey);
    }

    @Override
    public void searchKey(String keyWord) {
        load(1, keyWord);
    }

    // ====================================================================================
    // 安装菜单
    // ====================================================================================

    private TextView tv_menuBtn;

    @Override
    public void installMenuBtn(TextView tv_menuBtn) {
        this.tv_menuBtn = tv_menuBtn;
    }

    private void initMenuBtn() {
        if (tv_menuBtn == null) return;
        tv_menuBtn.setEnabled(false);
        tv_menuBtn.setText("移除");

        tv_menuBtn.setOnClickListener(view -> showRemoveMemberDialog());

        final RemoveMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;
        adapter.setOnCheckedChangeListener(linkedList -> {
            tv_menuBtn.setEnabled(linkedList.size() > 0);
            tv_menuBtn.setText(linkedList.size() > 0 ? String.format(Locale.getDefault(), "移除(%d)", linkedList.size()) : "移除");
        });
    }

    // ====================================================================================
    // 删除成员
    // ====================================================================================

    private void showRemoveMemberDialog() {
        final RemoveMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;
        LinkedList<String> checkedIds = adapter.getCheckedIds();

        new EasyOperDialog.Builder(String.format(Locale.getDefault(), "确定删除已选的%d位成员吗?", checkedIds.size()))
                .setPositiveBtnTxt("删除")
                .setNegativeBtnTxt("取消")
                .setOnBtnListener(new EasyOperDialog.OnBtnListener() {
                    @Override
                    public void onClickPositive(View view, EasyOperDialog dialog) {
                        removeMember(dialog);
                    }

                    @Override
                    public void onClickNegative(View view, EasyOperDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .build()
                .show_unCancel(getView().getActivity());
    }

    private void removeMember(EasyOperDialog dialog) {
        final RemoveMemberAdapter adapter = getView().getListAdapter();
        if (adapter == null) return;
        LinkedList<String> checkedIds = adapter.getCheckedIds();
        String uidList = StringUtil.list2String(checkedIds);
        if (uidList == null) return;
        postRemoveMember(uidList, dialog);
    }

    private void postRemoveMember(String uidList, final EasyOperDialog dialog) {
        getModel().postKickGroup(uidList, getView().getGroupId(), new BaseModel.DataProxy<String>() {
            @Override
            public void onSuccess(String String) {
                getView().getActivity().finish();
                dialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }
        });
    }
}
