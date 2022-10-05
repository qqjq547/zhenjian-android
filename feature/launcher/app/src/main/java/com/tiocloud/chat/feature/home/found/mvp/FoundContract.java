package com.tiocloud.chat.feature.home.found.mvp;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tiocloud.chat.widget.TioRefreshView;
import com.tiocloud.chat.feature.main.fragment.MainFoundFragment;
import com.watayouxiang.httpclient.model.request.FoundListResp;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.androidutils.page.TioActivity;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public interface FoundContract {

    interface View extends BaseView {
        TioActivity getTioActivity();

        @Nullable
        MainFoundFragment getMainGroupFragment();
    }

    abstract class Model extends BaseModel {
        public abstract void requestFoundList(DataProxy<FoundListResp> proxy);

        public abstract void setGroupNum(@Nullable MainFoundFragment fragment, int size);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(View view) {
            super(new FoundModel(), view, false);
        }

        public abstract void initRecyclerView(RecyclerView recyclerView);

        public abstract void initRefreshView(TioRefreshView refreshView);

        public abstract void load();
    }
}
