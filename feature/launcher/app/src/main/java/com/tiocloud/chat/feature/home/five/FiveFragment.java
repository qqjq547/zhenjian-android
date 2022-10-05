package com.tiocloud.chat.feature.home.five;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.home.five.mvp.FiveContract;
import com.tiocloud.chat.feature.home.five.mvp.FivePresenter;
import com.tiocloud.chat.feature.home.found.mvp.FoundContract;
import com.tiocloud.chat.feature.home.found.mvp.FoundPresenter;
import com.tiocloud.chat.feature.main.fragment.MainFiveFragment;
import com.tiocloud.chat.feature.main.fragment.MainFoundFragment;
import com.tiocloud.chat.widget.TioRefreshView;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.page.TioFragment;

/**
 * author : TaoWang
 * date : 2020-01-22
 * desc :
 */
public class FiveFragment extends TioFragment implements FiveContract.View {

    private RecyclerView recyclerView;
    private TioRefreshView refreshView;
    private FivePresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tio_group_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        presenter = new FivePresenter(this);
        presenter.initRefreshView(refreshView);
        presenter.initRecyclerView(recyclerView);
        presenter.load();
    }

    private void findViews() {
        recyclerView = findViewById(R.id.group_recycler_view);
        refreshView = findViewById(R.id.refresh_view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public TioActivity getTioActivity() {
        return (TioActivity) getActivity();
    }

    @Nullable
    @Override
    public MainFiveFragment getMainGroupFragment() {
        FragmentActivity activity = getActivity();
        if (activity instanceof TioActivity) {
            TioActivity tioActivity = (TioActivity) activity;
            return (MainFiveFragment) tioActivity.findFragmentByClass(MainFiveFragment.class);
        }
        return null;
    }

    public void onRefresh() {
        if (presenter != null) {
            presenter.load();
        }
    }
}
