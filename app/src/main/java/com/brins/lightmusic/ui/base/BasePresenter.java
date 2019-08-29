package com.brins.lightmusic.ui.base;

public interface BasePresenter<V> {

    void subscribe(V view);

    void unsubscribe();
}
