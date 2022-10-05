package com.tiocloud.chat.feature.search.curr.main.mvp;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.watayouxiang.androidutils.listener.SimpleTextWatcher;
import com.tiocloud.chat.util.KeyboardUtil;
import com.tiocloud.chat.feature.search.curr.main.SearchFragment;

/**
 * author : TaoWang
 * date : 2020-02-13
 * desc :
 */
public class SearchActivityPresenter extends SearchActivityContract.Presenter {

    private SearchFragment fragment;
    private EditText etInput;

    public SearchActivityPresenter(SearchActivityContract.View view) {
        super(view);
    }

    @Override
    public void initCancelBtn(TextView cancelBtn) {
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().finishPage();
            }
        });
    }

    @Override
    public void showKeyBoard() {
        if (etInput == null) return;
        KeyboardUtil.showSoftInput(etInput);
    }

    @Override
    public void initFragment(int containerId) {
        fragment = new SearchFragment();
        fragment.setContainerId(containerId);
        fragment = getView().addFragment(fragment);
    }

    @Override
    public void initEtInputView(EditText etInput, ImageView ivClearText) {
        this.etInput = etInput;
        etInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                ivClearText.setVisibility(TextUtils.isEmpty(s) ? View.GONE : View.VISIBLE);
                notifyInputTextChanged();
            }
        });

        ivClearText.setVisibility(View.GONE);
        ivClearText.setOnClickListener(view -> etInput.setText(""));

        notifyInputTextChanged();
    }

    private void notifyInputTextChanged() {
        if (etInput == null) return;
        if (fragment == null) return;

        String keyWord = etInput.getText().toString();
        if (TextUtils.isEmpty(keyWord)) {
            getView().hideFragment(fragment);
        } else {
            fragment.search(keyWord);
            getView().showFragment(fragment);
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (etInput != null) {
            KeyboardUtil.hideSoftInput(etInput);
        }
    }
}
