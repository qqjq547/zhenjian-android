package com.tiocloud.chat.feature.home.friend.mvp;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.tiocloud.chat.R;
import com.tiocloud.chat.baseNewVersion.MessageUpdataEvent;
import com.tiocloud.chat.event.UpdateFriendApplyNumEvent;
import com.tiocloud.chat.databinding.TioFriendFragmentBinding;
import com.tiocloud.chat.feature.home.chat.ChatFragment;
import com.tiocloud.chat.feature.home.friend.adapter.ContactAdapter;
import com.tiocloud.chat.feature.home.friend.adapter.model.IContact;
import com.tiocloud.chat.feature.home.friend.adapter.model.IData;
import com.tiocloud.chat.feature.home.friend.adapter.model.IItem;
import com.tiocloud.chat.feature.home.friend.adapter.model.item.ContactItem;
import com.tiocloud.chat.feature.home.friend.adapter.model.item.FuncItem;
import com.tiocloud.chat.feature.home.friend.task.maillist.MailListTaskProxy;
import com.tiocloud.chat.feature.main.MainActivity;
import com.tiocloud.chat.feature.main.model.MainTab;
import com.tiocloud.chat.feature.user.applylist.ApplyListActivity;
import com.tiocloud.chat.feature.user.detail.UserDetailActivity;
import com.tiocloud.chat.feature.user.group.GroupListActivity;
import com.watayouxiang.androidutils.widget.TioToast;
import com.tiocloud.chat.widget.popupwindow.FriendOpWindow;
import com.watayouxiang.httpclient.callback.TioCallback;
import com.watayouxiang.imclient.model.body.wx.WxUserSysNtf;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-10
 * desc :
 */
public class FriendPresenter extends FriendContract.Presenter {

    private final TioFriendFragmentBinding binding;
    private ContactAdapter adapter;

    // ====================================================================================
    // eventBus
    // ====================================================================================

    public FriendPresenter(FriendContract.View view, TioFriendFragmentBinding binding) {
        super(view);
        this.binding = binding;
    }

    // ??????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserSysNtf(WxUserSysNtf ntf) {
        switch (ntf.code) {
            case 30:// ??????????????????
            case 31:// ??????????????????-??????
            case 32:// ??????????????????-??????
            case 33:// ??????????????????-????????????
            case 41://??????????????? ????????????????????????????????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????? ??????????????????????????????????????????
//                Log.d("===??????????????????===","====");
                if(ntf.code==41){
                    EventBus.getDefault().post(new MessageUpdataEvent());
                }
                load();
                break;

        }
    }

    // ?????????????????????????????????
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWxUserSysNtf(UpdateFriendApplyNumEvent event) {
        requestApplyNum();
    }

    // ====================================================================================
    // init
    // ====================================================================================

    @Override
    public void initRefreshView() {
        binding.refreshView.setEnabled(true);
        binding.refreshView.setOnRefreshListener(this::load);
    }

    @Override
    public void initListView() {
        adapter = new ContactAdapter(getView().getActivity());
        adapter.installCatalogView(binding.contactsCatalogView, binding.contactListView);
        binding.contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IItem item = adapter.getItem(position);
                if (item == null) return;

                switch (item.getType()) {
                    case IItem.Type.FUNC:
                        gotoFuncActivity((FuncItem) item);
                        break;
                    case IItem.Type.CONTACT:
                        ContactItem contactItem = (ContactItem) item;
                        IContact contact = contactItem.getContact();
                        UserDetailActivity.start(getView().getActivity(), contact.getId());
                        break;
                }
            }
        });
        binding.contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                IItem item = adapter.getItem(position);
                if (item == null) return false;

                if (item.getType() == IItem.Type.CONTACT) {
                    ContactItem contactItem = (ContactItem) item;
                    IContact contact = contactItem.getContact();
                    // ????????????????????????
                    showFriendOpWindow(view, contact);
                }
                return true;
            }

            private void showFriendOpWindow(View view, IContact contact) {
                new FriendOpWindow(view) {
                    @Override
                    public void onDelFriendOk(IContact contact) {
                        super.onDelFriendOk(contact);
                        load();
                    }
                }.show(contact);
            }
        });
        binding.contactListView.setAdapter(adapter);
    }

    // ====================================================================================
    // data
    // ====================================================================================

    @Override
    public void load() {
        requestContacts(data -> {
            // ????????????
            if (binding != null) {
                binding.refreshView.setRefreshing(false);
            }
            // ?????????????????????
            requestApplyNum();
        });
    }

    /**
     * ??????????????????????????????
     *
     * @param funcItem
     */
    private void gotoFuncActivity(FuncItem funcItem) {
        if (funcItem.imgResId == R.drawable.ic_new_friend) {
            ApplyListActivity.start(getView().getActivity());
        } else {
            GroupListActivity.start(getView().getActivity());
        }
    }


    /**
     * ?????????????????????
     */
    private void requestContacts(MailListTaskProxy proxy) {
        // ?????????
        IData iData = new IData();
        iData.add(new FuncItem());

        //????????????
        FuncItem funcGroupItem = new FuncItem();
        funcGroupItem.name = "??????";
        funcGroupItem.imgResId = R.drawable.ic_newly_friend;
        funcGroupItem.isLast = true;
        iData.add(funcGroupItem);

        // ???????????????
        getModel().requestMailList(iData, taskData -> {
            if (taskData.ok) {
                if (adapter != null) {
                    adapter.setData(taskData.data);
                }
            } else {
                TioToast.showShort(taskData.msg);
            }
            if (proxy != null) {
                proxy.onTaskDone(taskData);
            }
        });
    }

    /**
     * ?????????????????????
     */
    private void requestApplyNum() {
        getModel().requestApplyData(new TioCallback<Integer>() {
            @Override
            public void onTioSuccess(Integer integer) {
                // ?????? ??????-??????tab ??????
                updateFriendRedDot(integer);
                // ?????? - ?????? - ???????????????
                if (adapter != null) {
                    adapter.updateFriendApplyNum(integer);
                }
            }

            @Override
            public void onTioError(String msg) {
                TioToast.showShort(msg);
            }
        });
    }

    /**
     * ?????? ??????-??????tab ??????
     */
    private void updateFriendRedDot(int applyCount) {
        Activity activity = getView().getActivity();
        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.updateApplyUnReadCount(applyCount);
        }
    }
}
