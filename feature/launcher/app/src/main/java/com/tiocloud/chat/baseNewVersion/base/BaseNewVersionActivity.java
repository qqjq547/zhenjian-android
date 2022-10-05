/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package com.tiocloud.chat.baseNewVersion.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.watayouxiang.androidutils.page.AppManager;

import butterknife.ButterKnife;


/**
 * TODO 基础Activity，通过继承可获取或使用 里面创建的 组件 和 方法;不能用于FragmentActivity
 * /滑动返回/显示与关闭进度弹窗方法/启动新Activity方法/快捷显示short toast方法/线程名列表/点击返回键事件/
 */
public abstract class BaseNewVersionActivity<T extends BaseView, P extends BasePresenter<T>> extends Activity implements BaseView {
    private static final String TAG = "BaseActivity";//用于打印日志（log）的类的标记
    protected BaseNewVersionActivity context = null;
    protected View view = null;
    private P presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        setContentView(bindLayout());
        ButterKnife.bind(this);
        presenter = bindPresenter();
        if (presenter != null) {
            presenter.onAttach((T) this);
        }
        initView();
    }

    /**
     * 绑定P层
     */
    protected abstract P bindPresenter();

    /**
     * TODO UI显示方法，必须在子类onCreate方法内setContentView后调用
     */
    public abstract void initView();

    /**
     * TODO 关联布局
     */
    public abstract int bindLayout();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.onDetch();
            presenter = null;
        }
        AppManager.getAppManager().finishActivity(this);
    }
}