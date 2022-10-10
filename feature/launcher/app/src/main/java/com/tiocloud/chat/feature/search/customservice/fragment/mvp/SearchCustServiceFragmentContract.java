package com.tiocloud.chat.feature.search.customservice.fragment.mvp;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.watayouxiang.httpclient.model.response.CustServiceTeamListResp;
import com.watayouxiang.httpclient.model.response.UserSearchResp;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public interface SearchCustServiceFragmentContract {
    interface View extends BaseView {
        Activity getActivity();
    }

    abstract class Model extends BaseModel {
        public abstract void searchCustService(String keyWord, int pageNumber, DataProxy<CustServiceTeamListResp> proxy);
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view) {
            super(model, view);
        }

        public abstract void initListView(RecyclerView recyclerView);

        public abstract void search(String keyWord);
    }
}
