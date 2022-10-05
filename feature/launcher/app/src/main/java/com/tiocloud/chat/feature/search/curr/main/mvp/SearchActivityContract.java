package com.tiocloud.chat.feature.search.curr.main.mvp;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;
import com.tiocloud.chat.feature.search.curr.main.SearchFragment;

/**
 * author : TaoWang
 * date : 2020-02-13
 * desc :
 */
public interface SearchActivityContract {
    interface View extends BaseView {

        void finishPage();

        SearchFragment addFragment(SearchFragment fragment);

        void hideFragment(SearchFragment fragment);

        void showFragment(SearchFragment fragment);
    }

    abstract class Model extends BaseModel {
    }

    abstract class Presenter extends BasePresenter<Model, View> {

        public Presenter(View view) {
            super(new SearchActivityModel(), view);
        }

        public abstract void initCancelBtn(TextView cancelBtn);

        public abstract void showKeyBoard();

        public abstract void initFragment(int containerId);

        public abstract void initEtInputView(EditText etInput, ImageView ivClearText);
    }
}
