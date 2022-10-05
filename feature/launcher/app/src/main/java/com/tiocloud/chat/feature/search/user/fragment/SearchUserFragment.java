package com.tiocloud.chat.feature.search.user.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.tiocloud.chat.R;
import com.watayouxiang.androidutils.page.TioFragment;
import com.tiocloud.chat.feature.search.user.fragment.mvp.SearchUserFragmentContract;
import com.tiocloud.chat.feature.search.user.fragment.mvp.SearchUserFragmentPresenter;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public class SearchUserFragment extends TioFragment implements SearchUserFragmentContract.View {

    private SearchUserFragmentPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tio_search_user_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView rv_list = findViewById(R.id.rv_list);
        presenter = new SearchUserFragmentPresenter(this);
        presenter.initListView(rv_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    public void search(String keyWord) {
        if (presenter != null) {
            presenter.search(keyWord);
        }
    }
}
