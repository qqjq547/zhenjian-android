package com.tiocloud.chat.feature.user.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.main.fragment.MainFoundFragment;
import com.tiocloud.chat.feature.user.group.mvp.GroupContract;
import com.tiocloud.chat.feature.user.group.mvp.GroupPresenter;
import com.tiocloud.chat.widget.TioRefreshView;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.page.TioFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author : TaoWang
 * date : 2020-01-22
 * desc :
 */
public class GroupFragment extends TioFragment implements GroupContract.View {

    private RecyclerView recyclerView;
    private TioRefreshView refreshView;
    private GroupPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tio_group_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findViews();
        presenter = new GroupPresenter(this);
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
    public MainFoundFragment getMainGroupFragment() {
        FragmentActivity activity = getActivity();
        if (activity instanceof TioActivity) {
            TioActivity tioActivity = (TioActivity) activity;
            return (MainFoundFragment) tioActivity.findFragmentByClass(MainFoundFragment.class);
        }
        return null;
    }

    public void onRefresh() {
        if (presenter != null) {
            presenter.load();
        }
    }
}
