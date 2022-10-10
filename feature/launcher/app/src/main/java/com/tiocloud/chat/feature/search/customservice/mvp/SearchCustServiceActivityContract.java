package com.tiocloud.chat.feature.search.customservice.mvp;

import android.widget.EditText;
import android.widget.TextView;

import com.tiocloud.chat.feature.search.customservice.fragment.SearchCustServiceFragment;
import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;

/**
 * author : TaoWang
 * date : 2020-02-20
 * desc :
 */
public interface SearchCustServiceActivityContract {
    interface View extends BaseView {
        void finishPage();

        SearchCustServiceFragment addFragment(SearchCustServiceFragment fragment);

        void hideFragment(SearchCustServiceFragment fragment);

        void showFragment(SearchCustServiceFragment fragment);
    }

    abstract class Model extends BaseModel {
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view) {
            super(model, view);
        }

        public abstract void initCancelBtn(TextView cancelBtn);

        public abstract void initFragment(int containerId);

        public abstract void initEtInputView(EditText etInput);

        public abstract void showKeyBoard();
    }
}
