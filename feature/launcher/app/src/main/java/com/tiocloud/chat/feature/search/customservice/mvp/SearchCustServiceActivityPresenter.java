package com.tiocloud.chat.feature.search.customservice.mvp;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tiocloud.chat.feature.search.customservice.fragment.SearchCustServiceFragment;
import com.tiocloud.chat.util.KeyboardUtil;
import com.watayouxiang.androidutils.listener.SimpleTextWatcher;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public class SearchCustServiceActivityPresenter extends SearchCustServiceActivityContract.Presenter {

    private SearchCustServiceFragment fragment;
    private EditText etInput;

    public SearchCustServiceActivityPresenter(SearchCustServiceActivityContract.View view) {
        super(new SearchCustServiceActivityModel(), view);
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
    public void initFragment(int containerId) {
        fragment = new SearchCustServiceFragment();
        fragment.setContainerId(containerId);
        fragment = getView().addFragment(fragment);
    }

    @Override
    public void initEtInputView(EditText etInput) {
        this.etInput = etInput;
        etInput.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                notifyInputTextChanged();
            }
        });

        notifyInputTextChanged();
    }

    @Override
    public void showKeyBoard() {
        if (etInput == null) return;
        KeyboardUtil.showSoftInput(etInput);
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
}
