package com.tiocloud.chat.feature.home.chat.mvp;

import androidx.annotation.NonNull;

import com.watayouxiang.db.event.ChatListTableEvent;
import com.watayouxiang.httpclient.model.response.ChatListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.widget.TioToast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author : TaoWang
 * date : 2020-02-11
 * desc :
 */
public class ChatPresenter extends ChatContract.Presenter {

    public ChatPresenter(ChatContract.View view) {
        super(new ChatModel(), view, true);
    }

    @Override
    public void init() {
        getView().initUI();
        queryAllFromDB();
    }

    @Override
    public void getChatList(boolean forceHttpReq) {
        ChatContract.Model model = getModel();
        if (model == null) return;

        model.getChatList(new BaseModel.DataProxy<ChatListResp>() {
            @Override
            public void onSuccess(ChatListResp lists) {
                super.onSuccess(lists);
                ChatContract.View view = getView();
                if (view != null) {
                    view.onChatListRespSuccess(lists);
                }
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                TioToast.showShort(msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                ChatContract.View view = getView();
                if (view != null) {
                    view.onEndRefresh();
                }
            }
        });
    }

    @Override
    public void queryAllFromDB() {
        getModel().queryAllFromDB(new BaseModel.DataProxy<ChatListResp>() {
            @Override
            public void onSuccess(ChatListResp resp) {
                super.onSuccess(resp);
                if (resp != null && resp.size() != 0) {
                    // 数据库有数据，显示出来
                    ChatContract.View view = getView();
                    if (view != null) {
                        view.onChatListRespSuccess(resp);
                    }
                }
            }
        });
    }
    
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChatListTableEvent(@NonNull ChatListTableEvent event) {
        getView().onChatListTableEvent(event);
    }

}
