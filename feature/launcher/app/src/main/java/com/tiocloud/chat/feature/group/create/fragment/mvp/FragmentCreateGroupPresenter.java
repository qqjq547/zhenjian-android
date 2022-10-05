package com.tiocloud.chat.feature.group.create.fragment.mvp;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tiocloud.chat.widget.ContactsCatalogView;
import com.tiocloud.chat.widget.dialog.tio.CreateGroupDialog;
import com.tiocloud.chat.feature.session.group.GroupSessionActivity;
import com.tiocloud.chat.feature.group.create.fragment.adapter.ExCreateGroupAdapter;
import com.watayouxiang.httpclient.model.response.CreateGroupResp;
import com.watayouxiang.httpclient.model.response.MailListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.tiocloud.chat.util.StringUtil;
import com.watayouxiang.androidutils.widget.TioToast;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * author : TaoWang
 * date : 2020/2/26
 * desc :
 */
public class FragmentCreateGroupPresenter extends FragmentCreateGroupContract.Presenter {

    private ExCreateGroupAdapter adapter;
    private TextView tvMenuBtn;

    public FragmentCreateGroupPresenter(FragmentCreateGroupContract.View view) {
        super(new FragmentCreateGroupModel(), view);
    }

    // ====================================================================================
    // ui
    // ====================================================================================

    @Override
    public void initRecyclerView(RecyclerView recyclerView, ContactsCatalogView contactsCatalogView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        adapter = new ExCreateGroupAdapter() {
            @Override
            protected void onCheckedItemChange(LinkedList<String> checkedIds) {
                super.onCheckedItemChange(checkedIds);
                FragmentCreateGroupPresenter.this.onCheckedItemChange(checkedIds);
            }
        };
        adapter.installCatalogView(contactsCatalogView, recyclerView);
        recyclerView.setAdapter(adapter);
    }

    private void onCheckedItemChange(LinkedList<String> checkedIds) {
        if (checkedIds == null) checkedIds = new LinkedList<>();
        if (tvMenuBtn == null) return;

        tvMenuBtn.setEnabled(checkedIds.size() != 0);
        if (checkedIds.size() != 0) {
            tvMenuBtn.setText(String.format(Locale.getDefault(), "确定(%d)", checkedIds.size()));
        } else {
            tvMenuBtn.setText("确定");
        }
    }

    @Override
    public void initMenuBtn(TextView tvMenuBtn) {
        this.tvMenuBtn = tvMenuBtn;
        if (tvMenuBtn != null) {
            tvMenuBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createGroup();
                }
            });
        }
    }

    // ====================================================================================
    // 搜索
    // ====================================================================================

    @Override
    public void search(final String keyWord) {
        if (adapter == null) return;
        getModel().getMailList(keyWord, new BaseModel.DataProxy<List<MailListResp.Friend>>() {
            @Override
            public void onSuccess(List<MailListResp.Friend> friends) {
                adapter.updateData(friends, keyWord);
            }
        });
    }

    // ====================================================================================
    // 建群
    // ====================================================================================

    @Override
    public void createGroup() {
        if (adapter == null) return;
        String uidArray = StringUtil.list2String(adapter.getCheckedIds());
        if (uidArray == null) return;
        showCreateGroupDialog(uidArray);
    }

    private void showCreateGroupDialog(final String uidArray) {
        new CreateGroupDialog(new CreateGroupDialog.OnBtnListener() {
            @Override
            public void onClickPositive(View view, String submitTxt, CreateGroupDialog dialog) {
                if (TextUtils.isEmpty(submitTxt))
                    submitTxt = null;
                postCreateGroup(submitTxt, uidArray, dialog);
            }
        }).show_unCancel(getView().getActivity());
    }

    private void postCreateGroup(String groupName, String uidArray, final CreateGroupDialog dialog) {
        getModel().postCreateGroup(groupName, uidArray, new BaseModel.DataProxy<CreateGroupResp>() {
            @Override
            public void onSuccess(CreateGroupResp resp) {
                // 跳到群组会话页
                GroupSessionActivity.active(getView().getActivity(), resp.getGroupId());
                // 关闭当前页
                getView().finishPage();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }
}
