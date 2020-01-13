package com.brins.lightmusic.ui.base;

public interface BasePresenter<V extends BaseView> {

    void subscribe(V view);

    void unsubscribe();
}
