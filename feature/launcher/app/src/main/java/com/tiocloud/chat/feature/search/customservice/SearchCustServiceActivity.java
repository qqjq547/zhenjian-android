package com.tiocloud.chat.feature.search.customservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tiocloud.chat.R;
import com.tiocloud.chat.feature.search.customservice.fragment.SearchCustServiceFragment;
import com.tiocloud.chat.feature.search.customservice.mvp.SearchCustServiceActivityContract;
import com.tiocloud.chat.feature.search.customservice.mvp.SearchCustServiceActivityPresenter;
import com.watayouxiang.androidutils.page.TioActivity;
import com.watayouxiang.androidutils.widget.edittext.TioEditText;
import com.watayouxiang.androidutils.widget.titlebar.WtTitleBar;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public class SearchCustServiceActivity extends TioActivity implements SearchCustServiceActivityContract.View {

    private TioEditText et_input;

    private SearchCustServiceActivityPresenter presenter;
    private WtTitleBar titleBar;

    public static void start(Context context) {
        Intent starter = new Intent(context, SearchCustServiceActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tio_search_user_activity);
        findViews();
        initViews();
        presenter = new SearchCustServiceActivityPresenter(this);
        presenter.initFragment(R.id.frameLayout);
        presenter.initEtInputView(et_input);
        presenter.showKeyBoard();
    }

    private void initViews() {
        titleBar.setTitle("客服小组");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void findViews() {
        et_input = findViewById(R.id.et_input);
        titleBar = findViewById(R.id.titleBar);
    }

    @Override
    public void finishPage() {
        finish();
    }

    @Override
    public SearchCustServiceFragment addFragment(SearchCustServiceFragment fragment) {
        return super.addFragment(fragment);
    }

    @Override
    public void hideFragment(SearchCustServiceFragment fragment) {
        super.hideFragment(fragment);
    }

    @Override
    public void showFragment(SearchCustServiceFragment fragment) {
        super.showFragment(fragment);
    }
}
