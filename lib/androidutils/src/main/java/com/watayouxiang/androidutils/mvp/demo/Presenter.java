package com.watayouxiang.androidutils.mvp.demo;

public class Presenter extends Contract.Presenter {
    public Presenter(Contract.View view) {
        super(new Model(), view, false);
    }
}
