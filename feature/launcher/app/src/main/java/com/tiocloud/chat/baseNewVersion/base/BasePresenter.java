package com.tiocloud.chat.baseNewVersion.base;

/**
 * P层的 基类
 */
public abstract class BasePresenter<T> {

	/**
	 * 绑定 view层
 	 */
	T view;
	public void onAttach(T view){this.view = view;}

	/**
	 * 解绑 view 层
 	 */
	public void onDetch(){
		if(view!=null){
			this.view = null;
		}
	}

	/**
	 * 获取view层
	 */
	public T getView(){
		return view;
	}
}
