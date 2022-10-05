package com.tiocloud.chat.feature.user.group.mvp;

import com.tiocloud.chat.feature.main.fragment.MainFoundFragment;
import com.tiocloud.chat.widget.TioRefreshView;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.model.response.MailListResp;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public interface GroupContract {

    interface View extends BaseView {
        TioActivity getTioActivity();

        @Nullable
        MainFoundFragment getMainGroupFragment();
    }

    abstract class Model extends BaseModel {
        public abstract void requestMailList(DataProxy<List<MailListResp.Group>> proxy);

        public abstract void setGroupNum(@Nullable MainFoundFragment fragment, int size);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(View view) {
            super(new GroupModel(), view, true);
        }

        public abstract void initRecyclerView(RecyclerView recyclerView);

        public abstract void initRefreshView(TioRefreshView refreshView);

        public abstract void load();
    }
}
