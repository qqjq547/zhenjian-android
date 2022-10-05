package com.tiocloud.chat.mvp.upload;

import android.app.Activity;

import androidx.annotation.Nullable;

import com.watayouxiang.androidutils.mvp.BaseModel;
import com.watayouxiang.androidutils.mvp.BasePresenter;
import com.watayouxiang.androidutils.mvp.BaseView;

/**
 * author : TaoWang
 * date : 2020/3/9
 * desc :
 */
public interface UploadContract {
    interface View extends BaseView {
        @Nullable
        Activity getActivity();
    }

    abstract class Model extends BaseModel {
        public Model(boolean registerEvent) {
            super(registerEvent);
        }
    }

    abstract class Presenter extends BasePresenter<Model, View> {
        public Presenter(Model model, View view) {
            super(model, view);
        }

        public abstract void uploadImg(String chatlinkid, String filePath);
        public abstract void uploadImg2(boolean isVideo,String chatlinkid, String coverurl,String filePath,String fingerprint,int width,int height,String size,String duration,String VideoPath);
        public abstract void uploadVideo(String chatlinkid, String filePath);

        public abstract void uploadFile(String chatlinkid, String filePath);
    }
}
