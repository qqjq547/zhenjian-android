package com.tiocloud.chat.feature.home.five.mvp;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tiocloud.chat.feature.main.fragment.MainFiveFragment;
import com.tiocloud.chat.widget.TioRefreshView;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.httpclient.model.request.FoundListResp;

/**
 * author : TaoWang
 * date : 2020-02-18
 * desc :
 */
public interface FiveContract {

    interface View extends BaseView {
        TioActivity getTioActivity();

        @Nullable
        MainFiveFragment getMainGroupFragment();
    }

    abstract class Model extends BaseModel {
        public abstract void requestFoundList(DataProxy<FoundListResp> proxy);

        public abstract void setGroupNum(@Nullable MainFiveFragment fragment, int size);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(View view) {
            super(new FiveModel(), view, false);
        }

        public abstract void initRecyclerView(RecyclerView recyclerView);

        public abstract void initRefreshView(TioRefreshView refreshView);

        public abstract void load();
    }
}
